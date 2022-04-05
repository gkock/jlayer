package org.jlayer.util;

import java.util.List;
import java.util.ArrayList;

import org.jlayer.model.Relation;
import org.jlayer.model.Layer;
import org.jlayer.model.VectorLayer;

import static org.jlayer.util.ForkJoinUtil.getX1Slices;

import org.jlayer.util.ForkJoinUtil.ActionItem;
import org.jlayer.util.ForkJoinUtil.Tuple;

/**
 * This class provides the actual implementation of 
 * <strong>VectorLayer's</strong> method <strong>connect()</strong>.
 * <p>
 * A flag controlling, whether the implementation uses
 * the <strong>Fork/Join Framework</strong>,
 * is set or get, respectively, by:
 * </p>
 * <ul>
 *   <li> {@link #setConnectForkJoinFlag(boolean flag)} 
 *   <li> {@link #getConnectForkJoinFlag()}
 * </ul>
 * <p>
* The abstract method items {@link #newObject()} and {@link #newArray(int)} 
 * return an object or an array of objects of the element type under consideration.
 * </p>
 * 
 * @see ForkJoinUtil
 * @see AssociateUtil
 * @see MethodUtil
 * 
 * @author Gerd Kock
 *
 * @param <T> the element type under consideration
 */
public abstract class ConnectUtil<T> {
	
	// abstract items
	abstract protected T newObject();
	abstract protected T[] newArray(int len);
	
	// ForkJoinFlags
	static private boolean connectForkJoinFlag = true;
	static public void setConnectForkJoinFlag(boolean flag) { connectForkJoinFlag = flag; }
	static public boolean getConnectForkJoinFlag() { return connectForkJoinFlag; }
	
	//////////////////////////////////////////////////////////////////////////////
	//	ACTION ITEMS					    									//
	//////////////////////////////////////////////////////////////////////////////
	
	// classes D1D1C, D1D2C, D1D3C for CONNECTING (D1, *)
	
	private abstract class D1D1C extends ActionItem {
		@Override
		abstract protected void compute();
		VectorLayer.D1<?,T> xLayer;
		Layer.D1<?,T> yLayer;
		Relation rel;
		List<int[]> ixList;
	}
	
	private abstract class D1D2C extends ActionItem {
		@Override
		abstract protected void compute();
		VectorLayer.D1<?,T> xLayer;
		Layer.D2<?,T> yLayer;
		Relation rel;
		List<int[]> ixList;
	}
	
	private abstract class D1D3C extends ActionItem {
		@Override
		abstract protected void compute();
		VectorLayer.D1<?,T> xLayer;
		Layer.D3<?,T> yLayer;
		Relation rel;
		List<int[]> ixList;
	}
	
	// classes D2D1C, D2D2C, D2D3C for CONNECTING (D2, *)
	
	private abstract class D2D1C extends ActionItem {
		@Override
		abstract protected void compute();
		VectorLayer.D2<?,T> xLayer;
		Layer.D1<?,T> yLayer;
		Relation rel;
		List<int[]> ixList;
	}
	
	private abstract class D2D2C extends ActionItem {
		@Override
		abstract protected void compute();
		VectorLayer.D2<?,T> xLayer;
		Layer.D2<?,T> yLayer;
		Relation rel;
		List<int[]> ixList;
	}
	
	private abstract class D2D3C extends ActionItem {
		@Override
		abstract protected void compute();
		VectorLayer.D2<?,T> xLayer;
		Layer.D3<?,T> yLayer;
		Relation rel;
		List<int[]> ixList;
	}
	
	// classes D3D1C, D3D2C, D3D3C for CONNECTING (D3, *)
	
	private abstract class D3D1C extends ActionItem {
		@Override
		abstract protected void compute();
		VectorLayer.D3<?,T> xLayer;
		Layer.D1<?,T> yLayer;
		Relation rel;
		List<int[]> ixList;
	}
	
	private abstract class D3D2C extends ActionItem {
		@Override
		abstract protected void compute();
		VectorLayer.D3<?,T> xLayer;
		Layer.D2<?,T> yLayer;
		Relation rel;
		List<int[]> ixList;
	}
	
	private abstract class D3D3C extends ActionItem {
		@Override
		abstract protected void compute();
		VectorLayer.D3<?,T> xLayer;
		Layer.D3<?,T> yLayer;
		Relation rel;
		List<int[]> ixList;
	}	
	
	//////////////////////////////////////////////////////////////////////////////
	// 						SPLITTING AN INDEX LIST 							//
	//////////////////////////////////////////////////////////////////////////////
	
	private static List<List<int[]>> splitIndexList(List<int[]> ixList) {
		Tuple[] slices = getX1Slices(ixList.size());
		List<List<int[]>> listIxList = new ArrayList<List<int[]>>();
		for (int i = 0; i < slices.length; i++) {
			listIxList.add(ixList.subList(slices[i].low, slices[i].high));
		}
		return listIxList;
	}
	
	//////////////////////////////////////////////////////////////////////////////
	// 						CONNECTING LINK VECTORS 							//
	//////////////////////////////////////////////////////////////////////////////
	
	// CONNECTING (D1, D1)
	
	void connect(VectorLayer.D1<?,T> xLayer, Layer.D1<?,T> yLayer, Relation rel){
		if (!getConnectForkJoinFlag()) {
			connect0(xLayer, yLayer, rel);
		} else {
			connect1(xLayer, yLayer, rel);
		}
	}
	
	private void connect0(VectorLayer.D1<?,T> xLayer, Layer.D1<?,T> yLayer, Relation rel){
		
		for (LayerIndex.D1 xGen = new LayerIndex.D1(xLayer); xGen.hasNext(); xGen.next()){
			int x = xGen.getX1();
			int[] ix = new int[]{x};
		 	T[] newArr = getLinkVector(ix, yLayer, rel);
		 	xLayer.set(x, newArr);
		}
		
	}
	
	private void connect1(VectorLayer.D1<?,T> xLayer, Layer.D1<?,T> yLayer, Relation rel){
		
		class ThisActionItem extends D1D1C {
			ThisActionItem(VectorLayer.D1<?,T> xLayer, Layer.D1<?,T> yLayer, Relation rel,
					       List<int[]> ixList) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = ixList;
			}
			@Override
			protected void compute() {
				for (int[] ix : ixList) {
					int x1 = ix[0];
					T[] newArr = getLinkVector(ix, yLayer, rel);
					synchronized(xLayer) {
						xLayer.set(x1, newArr); 
					}
				}
			}
		}
		
		List<int[]> ixList = new ArrayList<int[]>();
		for (LayerIndex.D1 xGen = new LayerIndex.D1(xLayer); xGen.hasNext(); xGen.next()){
			ixList.add(xGen.getIX());
		}
		
		List<List<int[]>> indexListList = splitIndexList(ixList);
		List<ActionItem>  actionList = new ArrayList<ActionItem>();	
		for (List<int[]> ixList2 : indexListList) {
			actionList.add( new ThisActionItem(xLayer, yLayer, rel, ixList2));
		}
		ForkJoinUtil.LayerAction thisAction = new ForkJoinUtil.LayerAction(actionList);
		ForkJoinUtil.invoke(thisAction);		
		
	}
	
	// CONNECTING (D1, D2)
	
	void connect(VectorLayer.D1<?,T> xLayer, Layer.D2<?,T> yLayer, Relation rel){
		if (!getConnectForkJoinFlag()) {
			connect0(xLayer, yLayer, rel);
		} else {
			connect1(xLayer, yLayer, rel);
		}
	}
	
	private void connect0(VectorLayer.D1<?,T> xLayer, Layer.D2<?,T> yLayer, Relation rel){
		
		for (LayerIndex.D1 xGen = new LayerIndex.D1(xLayer); xGen.hasNext(); xGen.next()){
			int x = xGen.getX1();
			int[] ix = new int[]{x};
		 	T[] newArr = getLinkVector(ix, yLayer, rel);
		 	xLayer.set(x, newArr);
		}
	}
	
	private void connect1(VectorLayer.D1<?,T> xLayer, Layer.D2<?,T> yLayer, Relation rel){
		
		class ThisActionItem extends D1D2C {
			ThisActionItem(VectorLayer.D1<?,T> xLayer, Layer.D2<?,T> yLayer, Relation rel,
					       List<int[]> ixList) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = ixList;
			}
			@Override
			protected void compute() {
				for (int[] ix : ixList) {
					int x1 = ix[0];
					T[] newArr = getLinkVector(ix, yLayer, rel);
					synchronized(xLayer) {
						xLayer.set(x1, newArr); 
					}
				}
			}
		}
		
		List<int[]> ixList = new ArrayList<int[]>();
		for (LayerIndex.D1 xGen = new LayerIndex.D1(xLayer); xGen.hasNext(); xGen.next()){
			ixList.add(xGen.getIX());
		}
		
		List<List<int[]>> indexListList = splitIndexList(ixList);
		List<ActionItem>  actionList = new ArrayList<ActionItem>();	
		for (List<int[]> ixList2 : indexListList) {
			actionList.add( new ThisActionItem(xLayer, yLayer, rel, ixList2));
		}
		ForkJoinUtil.LayerAction thisAction = new ForkJoinUtil.LayerAction(actionList);
		ForkJoinUtil.invoke(thisAction);		
	}
	
	// CONNECTING (D1, D3)
	
	void connect(VectorLayer.D1<?,T> xLayer, Layer.D3<?,T> yLayer, Relation rel){
		if (!getConnectForkJoinFlag()) {
			connect0(xLayer, yLayer, rel);
		} else {
			connect1(xLayer, yLayer, rel);
		}
	}
	
	private void connect0(VectorLayer.D1<?,T> xLayer, Layer.D3<?,T> yLayer, Relation rel){
		
		for (LayerIndex.D1 xGen = new LayerIndex.D1(xLayer); xGen.hasNext(); xGen.next()){
			int x = xGen.getX1();
			int[] ix = new int[]{x};
		 	T[] newArr = getLinkVector(ix, yLayer, rel);
		 	xLayer.set(x, newArr);
		}
		
	}
	
	private void connect1(VectorLayer.D1<?,T> xLayer, Layer.D3<?,T> yLayer, Relation rel){
		
		class ThisActionItem extends D1D3C {
			ThisActionItem(VectorLayer.D1<?,T> xLayer, Layer.D3<?,T> yLayer, Relation rel,
					       List<int[]> ixList) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = ixList;
			}
			@Override
			protected void compute() {
				for (int[] ix : ixList) {
					int x1 = ix[0];
					T[] newArr = getLinkVector(ix, yLayer, rel);
					synchronized(xLayer) {
						xLayer.set(x1, newArr); 
					}
				}
			}
		}
		
		List<int[]> ixList = new ArrayList<int[]>();
		for (LayerIndex.D1 xGen = new LayerIndex.D1(xLayer); xGen.hasNext(); xGen.next()){
			ixList.add(xGen.getIX());
		}
		
		List<List<int[]>> indexListList = splitIndexList(ixList);
		List<ActionItem>  actionList = new ArrayList<ActionItem>();	
		for (List<int[]> ixList2 : indexListList) {
			actionList.add( new ThisActionItem(xLayer, yLayer, rel, ixList2));
		}
		ForkJoinUtil.LayerAction thisAction = new ForkJoinUtil.LayerAction(actionList);
		ForkJoinUtil.invoke(thisAction);		
		
	}
	
	// CONNECTING (D2, D1)
	
	void connect(VectorLayer.D2<?,T> xLayer, Layer.D1<?,T> yLayer, Relation rel){
		if (!getConnectForkJoinFlag()) {
			connect0(xLayer, yLayer, rel);
		} else {
			connect1(xLayer, yLayer, rel);
		}
	}
	
	private void connect0(VectorLayer.D2<?,T> xLayer, Layer.D1<?,T> yLayer, Relation rel){
		
		for (LayerIndex.D2 xGen = new LayerIndex.D2(xLayer); xGen.hasNext(); xGen.next()){
			int x1 = xGen.getX1();
			int x2 = xGen.getX2();
			int[] ix = new int[]{x1, x2};
		 	T[] newArr = getLinkVector(ix, yLayer, rel);
		 	xLayer.set(x1, x2, newArr);
		}
		
	}
	
	private void connect1(VectorLayer.D2<?,T> xLayer, Layer.D1<?,T> yLayer, Relation rel){
		
		class ThisActionItem extends D2D1C {
			ThisActionItem(VectorLayer.D2<?,T> xLayer, Layer.D1<?,T> yLayer, Relation rel,
					       List<int[]> ixList) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = ixList;
			}
			@Override
			protected void compute() {
				for (int[] ix : ixList) {
					int x1 = ix[0];
					int x2 = ix[1];
					T[] newArr = getLinkVector(ix, yLayer, rel);
					synchronized(xLayer) {
						xLayer.set(x1, x2, newArr); 
					}
				}
			}
		}
		
		List<int[]> ixList = new ArrayList<int[]>();
		for (LayerIndex.D2 xGen = new LayerIndex.D2(xLayer); xGen.hasNext(); xGen.next()){
			ixList.add(xGen.getIX());
		}
		
		List<List<int[]>> indexListList = splitIndexList(ixList);
		List<ActionItem>  actionList = new ArrayList<ActionItem>();	
		for (List<int[]> ixList2 : indexListList) {
			actionList.add( new ThisActionItem(xLayer, yLayer, rel, ixList2));
		}
		ForkJoinUtil.LayerAction thisAction = new ForkJoinUtil.LayerAction(actionList);
		ForkJoinUtil.invoke(thisAction);		

	}
	
	// CONNECTING (D2, D2)
	
	void connect(VectorLayer.D2<?,T> xLayer, Layer.D2<?,T> yLayer, Relation rel){
		if (!getConnectForkJoinFlag()) {
			connect0(xLayer, yLayer, rel);
		} else {
			connect1(xLayer, yLayer, rel);
		}
	}
	
	private void connect0(VectorLayer.D2<?,T> xLayer, Layer.D2<?,T> yLayer, Relation rel){
		for (LayerIndex.D2 xGen = new LayerIndex.D2(xLayer); xGen.hasNext(); xGen.next()){
			int[] ix = xGen.getIX();
			int x1 = ix[0];
			int x2 = ix[1];
		 	T[] newArr = getLinkVector(ix, yLayer, rel);
		 	xLayer.set(x1, x2, newArr);
		}
	}
	
	private void connect1(VectorLayer.D2<?,T> xLayer, Layer.D2<?,T> yLayer, Relation rel){
		
		class ThisActionItem extends D2D2C {
			ThisActionItem(VectorLayer.D2<?,T> xLayer, Layer.D2<?,T> yLayer, Relation rel,
					       List<int[]> ixList) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = ixList;
			}
			@Override
			protected void compute() {
				for (int[] ix : ixList) {
					int x1 = ix[0];
					int x2 = ix[1];
					T[] newArr = getLinkVector(ix, yLayer, rel);
					synchronized(xLayer) {
						xLayer.set(x1, x2, newArr); 
					}
				}
			}
		}
		
		List<int[]> ixList = new ArrayList<int[]>();
		for (LayerIndex.D2 xGen = new LayerIndex.D2(xLayer); xGen.hasNext(); xGen.next()){
			ixList.add(xGen.getIX());
		}
		
		List<List<int[]>> indexListList = splitIndexList(ixList);
		List<ActionItem>  actionList = new ArrayList<ActionItem>();	
		for (List<int[]> ixList2 : indexListList) {
			actionList.add( new ThisActionItem(xLayer, yLayer, rel, ixList2));
		}
		ForkJoinUtil.LayerAction thisAction = new ForkJoinUtil.LayerAction(actionList);
		ForkJoinUtil.invoke(thisAction);
	}
	
	// CONNECTING (D2, D3)
	
	void connect(VectorLayer.D2<?,T> xLayer, Layer.D3<?,T> yLayer, Relation rel){
		if (!getConnectForkJoinFlag()) {
			connect0(xLayer, yLayer, rel);
		} else {
			connect1(xLayer, yLayer, rel);
		}
	}
	
	private void connect0(VectorLayer.D2<?,T> xLayer, Layer.D3<?,T> yLayer, Relation rel){
		
		for (LayerIndex.D2 xGen = new LayerIndex.D2(xLayer); xGen.hasNext(); xGen.next()){
			int x1 = xGen.getX1();
			int x2 = xGen.getX2();
			int[] ix = new int[]{x1, x2};
		 	T[] newArr = getLinkVector(ix, yLayer, rel);
		 	xLayer.set(x1, x2, newArr);
		}
		
	}
	
	private void connect1(VectorLayer.D2<?,T> xLayer, Layer.D3<?,T> yLayer, Relation rel){
		
		class ThisActionItem extends D2D3C {
			ThisActionItem(VectorLayer.D2<?,T> xLayer, Layer.D3<?,T> yLayer, Relation rel,
					       List<int[]> ixList) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = ixList;
			}
			@Override
			protected void compute() {
				for (int[] ix : ixList) {
					int x1 = ix[0];
					int x2 = ix[1];
					T[] newArr = getLinkVector(ix, yLayer, rel);
					synchronized(xLayer) {
						xLayer.set(x1, x2, newArr); 
					}
				}
			}
		}
		
		List<int[]> ixList = new ArrayList<int[]>();
		for (LayerIndex.D2 xGen = new LayerIndex.D2(xLayer); xGen.hasNext(); xGen.next()){
			ixList.add(xGen.getIX());
		}
		
		List<List<int[]>> indexListList = splitIndexList(ixList);
		List<ActionItem>  actionList = new ArrayList<ActionItem>();	
		for (List<int[]> ixList2 : indexListList) {
			actionList.add( new ThisActionItem(xLayer, yLayer, rel, ixList2));
		}
		ForkJoinUtil.LayerAction thisAction = new ForkJoinUtil.LayerAction(actionList);
		ForkJoinUtil.invoke(thisAction);
		
	}
	
	// CONNECTING (D3, D1)
	
	void connect(VectorLayer.D3<?,T> xLayer, Layer.D1<?,T> yLayer, Relation rel){
		if (!getConnectForkJoinFlag()) {
			connect0(xLayer, yLayer, rel);
		} else {
			connect1(xLayer, yLayer, rel);
		}
	}
	
	private void connect0(VectorLayer.D3<?,T> xLayer, Layer.D1<?,T> yLayer, Relation rel){
		
		for (LayerIndex.D3 xGen = new LayerIndex.D3(xLayer); xGen.hasNext(); xGen.next()){
			int x1 = xGen.getX1();
			int x2 = xGen.getX2();
			int x3 = xGen.getX3();
			int[] ix = new int[]{x1, x2, x3};
		 	T[] newArr = getLinkVector(ix, yLayer, rel);
		 	xLayer.set(x1, x2, x3, newArr);
		}
		
	}
	
	private void connect1(VectorLayer.D3<?,T> xLayer, Layer.D1<?,T> yLayer, Relation rel){
		
		class ThisActionItem extends D3D1C {
			ThisActionItem(VectorLayer.D3<?,T> xLayer, Layer.D1<?,T> yLayer, Relation rel,
					       List<int[]> ixList) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = ixList;
			}
			@Override
			protected void compute() {
				for (int[] ix : ixList) {
					int x1 = ix[0];
					int x2 = ix[1];
					int x3 = ix[2];
					T[] newArr = getLinkVector(ix, yLayer, rel);
					synchronized(xLayer) {
						xLayer.set(x1, x2, x3, newArr); 
					}
				}
			}
		}
		
		List<int[]> ixList = new ArrayList<int[]>();
		for (LayerIndex.D3 xGen = new LayerIndex.D3(xLayer); xGen.hasNext(); xGen.next()){
			ixList.add(xGen.getIX());
		}
		List<List<int[]>> indexListList = splitIndexList(ixList);
		
		List<ActionItem>  actionList = new ArrayList<ActionItem>();	
		for (List<int[]> ixList2 : indexListList) {
			actionList.add( new ThisActionItem(xLayer, yLayer, rel, ixList2));
		}
		ForkJoinUtil.LayerAction thisAction = new ForkJoinUtil.LayerAction(actionList);
		
		ForkJoinUtil.invoke(thisAction);
		
	}
	
	// CONNECTING (D3, D2)
	
	void connect(VectorLayer.D3<?,T> xLayer, Layer.D2<?,T> yLayer, Relation rel){
		if (!getConnectForkJoinFlag()) {
			connect0(xLayer, yLayer, rel);
		} else {
			connect1(xLayer, yLayer, rel);
		}
	}
	
	private void connect0(VectorLayer.D3<?,T> xLayer, Layer.D2<?,T> yLayer, Relation rel){
		
		for (LayerIndex.D3 xGen = new LayerIndex.D3(xLayer); xGen.hasNext(); xGen.next()){
			int x1 = xGen.getX1();
			int x2 = xGen.getX2();
			int x3 = xGen.getX3();
			int[] ix = new int[]{x1, x2, x3};
		 	T[] newArr = getLinkVector(ix, yLayer, rel);
		 	xLayer.set(x1, x2, x3, newArr);
		}
		
	}
	
	private void connect1(VectorLayer.D3<?,T> xLayer, Layer.D2<?,T> yLayer, Relation rel){
		
		class ThisActionItem extends D3D2C {
			ThisActionItem(VectorLayer.D3<?,T> xLayer, Layer.D2<?,T> yLayer, Relation rel,
					       List<int[]> ixList) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = ixList;
			}
			@Override
			protected void compute() {
				for (int[] ix : ixList) {
					int x1 = ix[0];
					int x2 = ix[1];
					int x3 = ix[2];
					T[] newArr = getLinkVector(ix, yLayer, rel);
					synchronized(xLayer) {
						xLayer.set(x1, x2, x3, newArr); 
					}
				}
			}
		}
		
		List<int[]> ixList = new ArrayList<int[]>();
		for (LayerIndex.D3 xGen = new LayerIndex.D3(xLayer); xGen.hasNext(); xGen.next()){
			ixList.add(xGen.getIX());
		}
		List<List<int[]>> indexListList = splitIndexList(ixList);
		
		List<ActionItem>  actionList = new ArrayList<ActionItem>();	
		for (List<int[]> ixList2 : indexListList) {
			actionList.add( new ThisActionItem(xLayer, yLayer, rel, ixList2));
		}
		ForkJoinUtil.LayerAction thisAction = new ForkJoinUtil.LayerAction(actionList);
		
		ForkJoinUtil.invoke(thisAction);
		
	}
	
	// CONNECTING (D3, D3)
	
	void connect(VectorLayer.D3<?,T> xLayer, Layer.D3<?,T> yLayer, Relation rel){
		if (!getConnectForkJoinFlag()) {
			connect0(xLayer, yLayer, rel);
		} else {
			connect1(xLayer, yLayer, rel);
		}
	}
	
	private void connect0(VectorLayer.D3<?,T> xLayer, Layer.D3<?,T> yLayer, Relation rel){
		
		for (LayerIndex.D3 xGen = new LayerIndex.D3(xLayer); xGen.hasNext(); xGen.next()){
			int x1 = xGen.getX1();
			int x2 = xGen.getX2();
			int x3 = xGen.getX3();
			int[] ix = new int[]{x1, x2, x3};
		 	T[] newArr = getLinkVector(ix, yLayer, rel);
		 	xLayer.set(x1, x2, x3, newArr);
		}
		
	}
	
	private void connect1(VectorLayer.D3<?,T> xLayer, Layer.D3<?,T> yLayer, Relation rel){
		
		class ThisActionItem extends D3D3C {
			ThisActionItem(VectorLayer.D3<?,T> xLayer, Layer.D3<?,T> yLayer, Relation rel,
					       List<int[]> ixList) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = ixList;
			}
			@Override
			protected void compute() {
				for (int[] ix : ixList) {
					int x1 = ix[0];
					int x2 = ix[1];
					int x3 = ix[2];
					T[] newArr = getLinkVector(ix, yLayer, rel);
					synchronized(xLayer) {
						xLayer.set(x1, x2, x3, newArr); 
					}
				}
			}
		}
		
		List<int[]> ixList = new ArrayList<int[]>();
		for (LayerIndex.D3 xGen = new LayerIndex.D3(xLayer); xGen.hasNext(); xGen.next()){
			ixList.add(xGen.getIX());
		}
		List<List<int[]>> indexListList = splitIndexList(ixList);
		
		List<ActionItem>  actionList = new ArrayList<ActionItem>();	
		for (List<int[]> ixList2 : indexListList) {
			actionList.add( new ThisActionItem(xLayer, yLayer, rel, ixList2));
		}
		ForkJoinUtil.LayerAction thisAction = new ForkJoinUtil.LayerAction(actionList);
		
		ForkJoinUtil.invoke(thisAction);
		
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	GETTING LINK VECTORS			    									//
	//////////////////////////////////////////////////////////////////////////////
	
	private T[] getLinkVector(int[] ix, Layer.D1<?,T> yLayer, Relation rel){

		int len = 0;
		for (LayerIndex.D1 yGen = new LayerIndex.D1(yLayer); yGen.hasNext(); yGen.next()){
	 		if (rel.contains(ix, yGen.getIX())) len++;
	 	}
		T[] newArr = newArray(len);
		int cnt = 0;
	 	for (LayerIndex.D1 yGen = new LayerIndex.D1(yLayer); yGen.hasNext(); yGen.next()){
   		 	if (rel.contains(ix, yGen.getIX())) {
	   		 	int y1 = yGen.getX1();		
   		 		synchronized(yLayer) {
	   		 		if (yLayer.get(y1) == null){
		 				yLayer.set(y1, newObject());
		 			} 
   		 		}
	 			newArr[cnt++] = yLayer.get(y1);
	 		}
	 	}
	 	return newArr;

	}

	private T[] getLinkVector(int[] ix, Layer.D2<?,T> yLayer, Relation rel){

		int len = 0;
		for (LayerIndex.D2 yGen = new LayerIndex.D2(yLayer); yGen.hasNext(); yGen.next()){
	 		if (rel.contains(ix, yGen.getIX())) len++;
	 	}
		T[] newArr = newArray(len);
		int cnt = 0;
	 	for (LayerIndex.D2 yGen = new LayerIndex.D2(yLayer); yGen.hasNext(); yGen.next()){
   		 	if (rel.contains(ix, yGen.getIX())) {
	   		 	int y1 = yGen.getX1();	
				int y2 = yGen.getX2();		
   		 		synchronized(yLayer) {
	   		 		if (yLayer.get(y1, y2) == null){
		 				yLayer.set(y1, y2, newObject());
		 			} 
   		 		}
	 			newArr[cnt++] = yLayer.get(y1, y2);
	 		}
	 	}
	 	return newArr;

	}
	
	private T[] getLinkVector(int[] ix, Layer.D3<?,T> yLayer, Relation rel){

		int len = 0;
		for (LayerIndex.D3 yGen = new LayerIndex.D3(yLayer); yGen.hasNext(); yGen.next()){
	 		if (rel.contains(ix, yGen.getIX())) len++;
	 	}
		T[] newArr = newArray(len);
		int cnt = 0;
	 	for (LayerIndex.D3 yGen = new LayerIndex.D3(yLayer); yGen.hasNext(); yGen.next()){
   		 	if (rel.contains(ix, yGen.getIX())) {
	   		 	int y1 = yGen.getX1();	
				int y2 = yGen.getX2();	
				int y3 = yGen.getX3();	
   		 		synchronized(yLayer) {
	   		 		if (yLayer.get(y1, y2, y3) == null){
		 				yLayer.set(y1, y2, y3, newObject());
		 			} 
   		 		}
	 			newArr[cnt++] = yLayer.get(y1, y2, y3);
	 		}
	 	}
	 	return newArr;

	}

}

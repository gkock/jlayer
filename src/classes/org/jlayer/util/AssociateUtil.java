package org.jlayer.util;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.jlayer.model.Relation;
import org.jlayer.model.VectorLayer;

import static org.jlayer.util.ForkJoinUtil.getX1Slices;
import static org.jlayer.util.LayerIndex.generateLayerIndexList;

import org.jlayer.util.ForkJoinUtil.ActionItem;
import org.jlayer.util.ForkJoinUtil.Tuple;
import org.jlayer.util.IndexTools.Transpose;

/**
 * This class provides the actual implementation of 
 * <strong>VectorLayer's</strong> method <strong>associate()</strong>.
 * <p>
 * A flag controlling, whether the implementation uses
 * the <strong>Fork/Join Framework</strong>,
 * is set or get, respectively, by:
 * </p>
 * <ul>
 *   <li> {@link #setAssociateForkJoinFlag(boolean flag)} 
 *   <li> {@link #getAssociateForkJoinFlag()} 
 * </ul>
 * <p>
* The abstract method items {@link #newObject()} and {@link #newArray(int)} 
 * return an object or an array of objects of the element type under consideration.
 * </p>
 * 
 * @see ForkJoinUtil
 * @see ConnectUtil
 * @see MethodUtil
 * 
 * @author Gerd Kock
 *
 * @param <T> the element type under consideration
 */
public abstract class AssociateUtil<T> {
	
	// abstract items
	abstract protected T newObject();
	abstract protected T[] newArray(int len);
	
	// ForkJoinFlags
	static private boolean associateForkJoinFlag = true;
	static public void setAssociateForkJoinFlag(boolean flag) { associateForkJoinFlag = flag; }
	static public boolean getAssociateForkJoinFlag() { return associateForkJoinFlag; }
	
	//////////////////////////////////////////////////////////////////////////////
	//	ACTION ITEMS					    									//
	//////////////////////////////////////////////////////////////////////////////
	
	// classes D1D1, D1D2, D1D3 for associating (D1, *)
	
	private abstract class D1D1 extends ActionItem {
		@Override
		abstract protected void compute();
		VectorLayer.D1<?,T> xLayer;
		VectorLayer.D1<?,T> yLayer;
		Relation rel;
		List<LayerIndex.D1> ixList;
		List<LayerIndex.D1> iyList;
		Map<LayerIndex.D1, List<LayerIndex.D1>> xMap;
		int[] yLen;
	}
	
	private abstract class D1D2 extends ActionItem {
		@Override
		abstract protected void compute();
		VectorLayer.D1<?,T> xLayer;
		VectorLayer.D2<?,T> yLayer;
		Relation rel;
		List<LayerIndex.D1> ixList;
		List<LayerIndex.D2> iyList;
		Map<LayerIndex.D1, List<LayerIndex.D2>> xMap;
		int[][] yLen;
	}
	
	private abstract class D1D3 extends ActionItem {
		@Override
		abstract protected void compute();
		VectorLayer.D1<?,T> xLayer;
		VectorLayer.D3<?,T> yLayer;
		Relation rel;
		List<LayerIndex.D1> ixList;
		List<LayerIndex.D3> iyList;
		Map<LayerIndex.D1, List<LayerIndex.D3>> xMap;
		int[][][] yLen;
	}
	
	// classes D2D2, D2D3 for associating (D2, *)
	
	private abstract class D2D2 extends ActionItem {
		@Override
		abstract protected void compute();
		VectorLayer.D2<?,T> xLayer;
		VectorLayer.D2<?,T> yLayer;
		Relation rel;
		List<LayerIndex.D2> ixList;
		List<LayerIndex.D2> iyList;
		Map<LayerIndex.D2, List<LayerIndex.D2>> xMap;
		int[][] yLen;
	}
	
	private abstract class D2D3 extends ActionItem {
		@Override
		abstract protected void compute();
		VectorLayer.D2<?,T> xLayer;
		VectorLayer.D3<?,T> yLayer;
		Relation rel;
		List<LayerIndex.D2> ixList;
		List<LayerIndex.D3> iyList;
		Map<LayerIndex.D2, List<LayerIndex.D3>> xMap;
		int[][][] yLen;
	}
	
	// class D3D3 for associating (D3, D3)
	
	private abstract class D3D3 extends ActionItem {
		@Override
		abstract protected void compute();
		VectorLayer.D3<?,T> xLayer;
		VectorLayer.D3<?,T> yLayer;
		Relation rel;
		List<LayerIndex.D3> ixList;
		List<LayerIndex.D3> iyList;
		Map<LayerIndex.D3, List<LayerIndex.D3>> xMap;
		int[][][] yLen;
	}
	
	//////////////////////////////////////////////////////////////////////////////
	// 						SPLITTING AN INDEX LISTS 							//
	//////////////////////////////////////////////////////////////////////////////
	
	private static List<List<LayerIndex.D1>> splitIndexD1List(List<LayerIndex.D1> ixList) {
		Tuple[] slices = getX1Slices(ixList.size());
		List<List<LayerIndex.D1>> listIxList = new ArrayList<List<LayerIndex.D1>>();
		for (int i = 0; i < slices.length; i++) {
			listIxList.add(ixList.subList(slices[i].low, slices[i].high));
		}
		return listIxList;
	}
	
	private static List<List<LayerIndex.D2>> splitIndexD2List(List<LayerIndex.D2> ixList) {
		Tuple[] slices = getX1Slices(ixList.size());
		List<List<LayerIndex.D2>> listIxList = new ArrayList<List<LayerIndex.D2>>();
		for (int i = 0; i < slices.length; i++) {
			listIxList.add(ixList.subList(slices[i].low, slices[i].high));
		}
		return listIxList;
	}
	
	private static List<List<LayerIndex.D3>> splitIndexD3List(List<LayerIndex.D3> ixList) {
		Tuple[] slices = getX1Slices(ixList.size());
		List<List<LayerIndex.D3>> listIxList = new ArrayList<List<LayerIndex.D3>>();
		for (int i = 0; i < slices.length; i++) {
			listIxList.add(ixList.subList(slices[i].low, slices[i].high));
		}
		return listIxList;
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	ASSOCIATING LINK VECTORS 												//
	//////////////////////////////////////////////////////////////////////////////
	
	// ASSOCIATING (D1, D1)
	
	void associate(VectorLayer.D1<?,T> xLayer, VectorLayer.D1<?,T> yLayer, Relation rel){
		if (!getAssociateForkJoinFlag()) {
			associate0(xLayer, yLayer, rel);
		} else {
			associate1(xLayer, yLayer, rel);
		}
	}
	
	private void associate0(VectorLayer.D1<?,T> xLayer, VectorLayer.D1<?,T> yLayer, Relation rel){
		
		Map<LayerIndex.D1, List<LayerIndex.D1>> xMap = new HashMap<LayerIndex.D1, List<LayerIndex.D1>>();
		int[] yLen = (new LayerIndex.D1(yLayer)).getFrame();
		
		for (LayerIndex.D1 xGen = new LayerIndex.D1(xLayer); xGen.hasNext(); xGen.next()){
			setXLink(xGen, xMap, yLen, xLayer, yLayer, rel);
		}
		for (LayerIndex.D1 yGen = new LayerIndex.D1(yLayer); yGen.hasNext(); yGen.next()){
			setYLink(yGen, yLen, xMap, xLayer, yLayer, rel);
		}
		
	}
	
	private void associate1(VectorLayer.D1<?,T> xLayer, VectorLayer.D1<?,T> yLayer, Relation rel){
		
		class XLayerActionItem extends D1D1 {
			XLayerActionItem(VectorLayer.D1<?,T> xLayer, VectorLayer.D1<?,T> yLayer, Relation rel, List<LayerIndex.D1> ixList,
			Map<LayerIndex.D1, List<LayerIndex.D1>> xMap, int[] yLen) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = ixList;
				this.iyList = null;
				this.xMap = xMap;
				this.yLen = yLen;
			}
			@Override
			protected void compute() {
				for (LayerIndex.D1 ix : ixList) {
					setXLink(ix, xMap, yLen, xLayer, yLayer, rel);
				}
			}
		}
		class YLayerActionItem extends D1D1 {		
			YLayerActionItem(VectorLayer.D1<?,T> xLayer, VectorLayer.D1<?,T> yLayer, Relation rel, List<LayerIndex.D1> iyList,
			Map<LayerIndex.D1, List<LayerIndex.D1>> xMap, int[] yLen) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = null;
				this.iyList = iyList;
				this.xMap = xMap;
				this.yLen = yLen;
			}
			@Override
			protected void compute() {
				for (LayerIndex.D1 iy : iyList) {
					setYLink(iy, yLen, xMap, xLayer, yLayer, rel);
				}
			}
		}
		
		Map<LayerIndex.D1, List<LayerIndex.D1>> xMap = new HashMap<LayerIndex.D1, List<LayerIndex.D1>>();
		int[] yLen = (new LayerIndex.D1(yLayer)).getFrame();
		
		List<LayerIndex.D1>       ixList = generateLayerIndexList(xLayer);
		List<List<LayerIndex.D1>> ixListList = splitIndexD1List(ixList);
		List<ActionItem>          xActionList = new ArrayList<ActionItem>();	
		for (List<LayerIndex.D1> ixList2 : ixListList) {
			xActionList.add( new XLayerActionItem(xLayer, yLayer, rel, ixList2, xMap, yLen));
		}
		ForkJoinUtil.LayerAction xAction = new ForkJoinUtil.LayerAction(xActionList);
		
		List<LayerIndex.D1>       iyList = generateLayerIndexList(yLayer);
		List<List<LayerIndex.D1>> iyListList = splitIndexD1List(iyList);
		List<ActionItem>          yActionList = new ArrayList<ActionItem>();	
		for (List<LayerIndex.D1>  iyList2 : iyListList) {
			yActionList.add( new YLayerActionItem(xLayer, yLayer, rel, iyList2, xMap, yLen));
		}
		ForkJoinUtil.LayerAction yAction = new ForkJoinUtil.LayerAction(yActionList);
		
		ForkJoinUtil.invoke(xAction);
		ForkJoinUtil.invoke(yAction);	
		
	}
	
	// ASSOCIATING (D1, D2)
	
	void associate(VectorLayer.D1<?,T> xLayer, VectorLayer.D2<?,T> yLayer, Relation rel){
		if (!getAssociateForkJoinFlag()) {
			associate0(xLayer, yLayer, rel);
		} else {
			associate1(xLayer, yLayer, rel);
		}
	}
	
	private void associate0(VectorLayer.D1<?,T> xLayer, VectorLayer.D2<?,T> yLayer, Relation rel){
		
		Map<LayerIndex.D1, List<LayerIndex.D2>> xMap = new HashMap<LayerIndex.D1, List<LayerIndex.D2>>();
		int[][] yLen = (new LayerIndex.D2(yLayer)).getFrame();
		
		for (LayerIndex.D1 xGen = new LayerIndex.D1(xLayer); xGen.hasNext(); xGen.next()){
			setXLink(xGen, xMap, yLen, xLayer, yLayer, rel);
		}
		for (LayerIndex.D2 yGen = new LayerIndex.D2(yLayer); yGen.hasNext(); yGen.next()){
			setYLink(yGen, yLen, xMap, xLayer, yLayer, rel);
		}
		
	}
	
	private void associate1(VectorLayer.D1<?,T> xLayer, VectorLayer.D2<?,T> yLayer, Relation rel){
		
		class XLayerActionItem extends D1D2 {
			XLayerActionItem(VectorLayer.D1<?,T> xLayer, VectorLayer.D2<?,T> yLayer, Relation rel, List<LayerIndex.D1> ixList,
			Map<LayerIndex.D1, List<LayerIndex.D2>> xMap, int[][] yLen) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = ixList;
				this.iyList = null;
				this.xMap = xMap;
				this.yLen = yLen;
			}
			@Override
			protected void compute() {
				for (LayerIndex.D1 ix : ixList) {
					setXLink(ix, xMap, yLen, xLayer, yLayer, rel);
				}
			}
		}
		class YLayerActionItem extends D1D2 {		
			YLayerActionItem(VectorLayer.D1<?,T> xLayer, VectorLayer.D2<?,T> yLayer, Relation rel, List<LayerIndex.D2> iyList,
			Map<LayerIndex.D1, List<LayerIndex.D2>> xMap, int[][] yLen) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = null;
				this.iyList = iyList;
				this.xMap = xMap;
				this.yLen = yLen;
			}
			@Override
			protected void compute() {
				for (LayerIndex.D2 iy : iyList) {
					setYLink(iy, yLen, xMap, xLayer, yLayer, rel);
				}
			}
		}
		
		Map<LayerIndex.D1, List<LayerIndex.D2>> xMap = new HashMap<LayerIndex.D1, List<LayerIndex.D2>>();
		int[][] yLen = (new LayerIndex.D2(yLayer)).getFrame();
		
		List<LayerIndex.D1>       ixList = generateLayerIndexList(xLayer);
		List<List<LayerIndex.D1>> ixListList = splitIndexD1List(ixList);
		List<ActionItem>          xActionList = new ArrayList<ActionItem>();	
		for (List<LayerIndex.D1> ixList2 : ixListList) {
			xActionList.add( new XLayerActionItem(xLayer, yLayer, rel, ixList2, xMap, yLen));
		}
		ForkJoinUtil.LayerAction xAction = new ForkJoinUtil.LayerAction(xActionList);
		
		List<LayerIndex.D2>       iyList = generateLayerIndexList(yLayer);
		List<List<LayerIndex.D2>> iyListList = splitIndexD2List(iyList);
		List<ActionItem>          yActionList = new ArrayList<ActionItem>();	
		for (List<LayerIndex.D2>  iyList2 : iyListList) {
			yActionList.add( new YLayerActionItem(xLayer, yLayer, rel, iyList2, xMap, yLen));
		}
		ForkJoinUtil.LayerAction yAction = new ForkJoinUtil.LayerAction(yActionList);
		
		ForkJoinUtil.invoke(xAction);
		ForkJoinUtil.invoke(yAction);	
		
	}
	
	// ASSOCIATING (D1, D3)
	
	void associate(VectorLayer.D1<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel){
		if (!getAssociateForkJoinFlag()) {
			associate0(xLayer, yLayer, rel);
		} else {
			associate1(xLayer, yLayer, rel);
		}
	}
	
	private void associate0(VectorLayer.D1<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel){

		Map<LayerIndex.D1, List<LayerIndex.D3>> xMap = new HashMap<LayerIndex.D1, List<LayerIndex.D3>>();
		int[][][] yLen = (new LayerIndex.D3(yLayer)).getFrame();
		
		for (LayerIndex.D1 xGen = new LayerIndex.D1(xLayer); xGen.hasNext(); xGen.next()){
			setXLink(xGen, xMap, yLen, xLayer, yLayer, rel);
		}
		for (LayerIndex.D3 yGen = new LayerIndex.D3(yLayer); yGen.hasNext(); yGen.next()){
			setYLink(yGen, yLen, xMap, xLayer, yLayer, rel);
		}
		
	}
	
	private void associate1(VectorLayer.D1<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel){
		
		class XLayerActionItem extends D1D3 {
			XLayerActionItem(VectorLayer.D1<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel, List<LayerIndex.D1> ixList,
			Map<LayerIndex.D1, List<LayerIndex.D3>> xMap, int[][][] yLen) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = ixList;
				this.iyList = null;
				this.xMap = xMap;
				this.yLen = yLen;
			}
			@Override
			protected void compute() {
				for (LayerIndex.D1 ix : ixList) {
					setXLink(ix, xMap, yLen, xLayer, yLayer, rel);
				}
			}
		}
		class YLayerActionItem extends D1D3 {		
			YLayerActionItem(VectorLayer.D1<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel, List<LayerIndex.D3> iyList,
			Map<LayerIndex.D1, List<LayerIndex.D3>> xMap, int[][][] yLen) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = null;
				this.iyList = iyList;
				this.xMap = xMap;
				this.yLen = yLen;
			}
			@Override
			protected void compute() {
				for (LayerIndex.D3 iy : iyList) {
					setYLink(iy, yLen, xMap, xLayer, yLayer, rel);
				}
			}
		}
		
		Map<LayerIndex.D1, List<LayerIndex.D3>> xMap = new HashMap<LayerIndex.D1, List<LayerIndex.D3>>();
		int[][][] yLen = (new LayerIndex.D3(yLayer)).getFrame();
		
		List<LayerIndex.D1>       ixList = generateLayerIndexList(xLayer);
		List<List<LayerIndex.D1>> ixListList = splitIndexD1List(ixList);
		List<ActionItem>          xActionList = new ArrayList<ActionItem>();	
		for (List<LayerIndex.D1> ixList2 : ixListList) {
			xActionList.add( new XLayerActionItem(xLayer, yLayer, rel, ixList2, xMap, yLen));
		}
		ForkJoinUtil.LayerAction xAction = new ForkJoinUtil.LayerAction(xActionList);
		
		List<LayerIndex.D3>       iyList = generateLayerIndexList(yLayer);
		List<List<LayerIndex.D3>> iyListList = splitIndexD3List(iyList);
		List<ActionItem>          yActionList = new ArrayList<ActionItem>();	
		for (List<LayerIndex.D3>  iyList2 : iyListList) {
			yActionList.add( new YLayerActionItem(xLayer, yLayer, rel, iyList2, xMap, yLen));
		}
		ForkJoinUtil.LayerAction yAction = new ForkJoinUtil.LayerAction(yActionList);
		
		ForkJoinUtil.invoke(xAction);
		ForkJoinUtil.invoke(yAction);	
		
	}
	
	// ASSOCIATING (D2, D1)
	
	void associate(VectorLayer.D2<?,T> xLayer, VectorLayer.D1<?,T> yLayer, Relation rel){
		if (!getAssociateForkJoinFlag()) {
			associate0(yLayer, xLayer, new Transpose(rel));
		} else {
			associate1(yLayer, xLayer, new Transpose(rel));
		}
	}
	
	// ASSOCIATING (D2, D2)
	
	void associate(VectorLayer.D2<?,T> xLayer, VectorLayer.D2<?,T> yLayer, Relation rel){
		if (!getAssociateForkJoinFlag()) {
			associate0(xLayer, yLayer, rel);
		} else {
			associate1(xLayer, yLayer, rel);
		}
	}
	
	private void associate0(VectorLayer.D2<?,T> xLayer, VectorLayer.D2<?,T> yLayer, Relation rel){
		
		Map<LayerIndex.D2, List<LayerIndex.D2>> xMap = new HashMap<LayerIndex.D2, List<LayerIndex.D2>>();
		int[][] yLen = (new LayerIndex.D2(yLayer)).getFrame();
		
		for (LayerIndex.D2 xGen = new LayerIndex.D2(xLayer); xGen.hasNext(); xGen.next()){
			setXLink(xGen, xMap, yLen, xLayer, yLayer, rel);
		}
		for (LayerIndex.D2 yGen = new LayerIndex.D2(yLayer); yGen.hasNext(); yGen.next()){
			setYLink(yGen, yLen, xMap, xLayer, yLayer, rel);
		}
		
	}
	
	private void associate1(VectorLayer.D2<?,T> xLayer, VectorLayer.D2<?,T> yLayer, Relation rel){
		
		class XLayerActionItem extends D2D2 {
			XLayerActionItem(VectorLayer.D2<?,T> xLayer, VectorLayer.D2<?,T> yLayer, Relation rel, List<LayerIndex.D2> ixList,
			Map<LayerIndex.D2, List<LayerIndex.D2>> xMap, int[][] yLen) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = ixList;
				this.iyList = null;
				this.xMap = xMap;
				this.yLen = yLen;
			}
			@Override
			protected void compute() {
				for (LayerIndex.D2 ix : ixList) {
					setXLink(ix, xMap, yLen, xLayer, yLayer, rel);
				}
			}
		}
		class YLayerActionItem extends D2D2 {		
			YLayerActionItem(VectorLayer.D2<?,T> xLayer, VectorLayer.D2<?,T> yLayer, Relation rel, List<LayerIndex.D2> iyList,
			Map<LayerIndex.D2, List<LayerIndex.D2>> xMap, int[][] yLen) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = null;
				this.iyList = iyList;
				this.xMap = xMap;
				this.yLen = yLen;
			}
			@Override
			protected void compute() {
				for (LayerIndex.D2 iy : iyList) {
					setYLink(iy, yLen, xMap, xLayer, yLayer, rel);
				}
			}
		}
		
		Map<LayerIndex.D2, List<LayerIndex.D2>> xMap = new HashMap<LayerIndex.D2, List<LayerIndex.D2>>();
		int[][] yLen = (new LayerIndex.D2(yLayer)).getFrame();
		
		List<LayerIndex.D2>       ixList = generateLayerIndexList(xLayer);
		List<List<LayerIndex.D2>> ixListList = splitIndexD2List(ixList);
		List<ActionItem>          xActionList = new ArrayList<ActionItem>();	
		for (List<LayerIndex.D2> ixList2 : ixListList) {
			xActionList.add( new XLayerActionItem(xLayer, yLayer, rel, ixList2, xMap, yLen));
		}
		ForkJoinUtil.LayerAction xAction = new ForkJoinUtil.LayerAction(xActionList);
		
		List<LayerIndex.D2> iyList = generateLayerIndexList(yLayer);
		
		List<List<LayerIndex.D2>> iyListList = splitIndexD2List(iyList);
		List<ActionItem>          yActionList = new ArrayList<ActionItem>();	
		for (List<LayerIndex.D2>  iyList2 : iyListList) {
			yActionList.add( new YLayerActionItem(xLayer, yLayer, rel, iyList2, xMap, yLen));
		}
		ForkJoinUtil.LayerAction yAction = new ForkJoinUtil.LayerAction(yActionList);
		
		ForkJoinUtil.invoke(xAction);
		ForkJoinUtil.invoke(yAction);	
		
	}
	
	// ASSOCIATING (D2, D3)
	
	void associate(VectorLayer.D2<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel){
		if (!getAssociateForkJoinFlag()) {
			associate0(xLayer, yLayer, rel);
		} else {
			associate1(xLayer, yLayer, rel);
		}
	}
	
	private void associate0(VectorLayer.D2<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel){

		Map<LayerIndex.D2, List<LayerIndex.D3>> xMap = new HashMap<LayerIndex.D2, List<LayerIndex.D3>>();
		int[][][] yLen = (new LayerIndex.D3(yLayer)).getFrame();
		
		for (LayerIndex.D2 xGen = new LayerIndex.D2(xLayer); xGen.hasNext(); xGen.next()){
			setXLink(xGen, xMap, yLen, xLayer, yLayer, rel);
		}
		for (LayerIndex.D3 yGen = new LayerIndex.D3(yLayer); yGen.hasNext(); yGen.next()){
			setYLink(yGen, yLen, xMap, xLayer, yLayer, rel);
		}
		
	}
	
	private void associate1(VectorLayer.D2<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel){
		
		class XLayerActionItem extends D2D3 {
			XLayerActionItem(VectorLayer.D2<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel, List<LayerIndex.D2> ixList,
			Map<LayerIndex.D2, List<LayerIndex.D3>> xMap, int[][][] yLen) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = ixList;
				this.iyList = null;
				this.xMap = xMap;
				this.yLen = yLen;
			}
			@Override
			protected void compute() {
				for (LayerIndex.D2 ix : ixList) {
					setXLink(ix, xMap, yLen, xLayer, yLayer, rel);
				}
			}
		}	
		class YLayerActionItem extends D2D3 {		
			YLayerActionItem(VectorLayer.D2<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel, List<LayerIndex.D3> iyList,
			Map<LayerIndex.D2, List<LayerIndex.D3>> xMap, int[][][] yLen) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = null;
				this.iyList = iyList;
				this.xMap = xMap;
				this.yLen = yLen;
			}
			@Override
			protected void compute() {
				for (LayerIndex.D3 iy : iyList) {
					setYLink(iy, yLen, xMap, xLayer, yLayer, rel);
				}
			}
		}
		
		Map<LayerIndex.D2, List<LayerIndex.D3>> xMap = new HashMap<LayerIndex.D2, List<LayerIndex.D3>>();
		int[][][] yLen = (new LayerIndex.D3(yLayer)).getFrame();
		
		List<LayerIndex.D2>       ixList = generateLayerIndexList(xLayer);
		List<List<LayerIndex.D2>> ixListList = splitIndexD2List(ixList);
		List<ActionItem>          xActionList = new ArrayList<ActionItem>();	
		for (List<LayerIndex.D2> ixList2 : ixListList) {
			xActionList.add( new XLayerActionItem(xLayer, yLayer, rel, ixList2, xMap, yLen));
		}
		ForkJoinUtil.LayerAction xAction = new ForkJoinUtil.LayerAction(xActionList);
		
		List<LayerIndex.D3>       iyList = generateLayerIndexList(yLayer);
		List<List<LayerIndex.D3>> iyListList = splitIndexD3List(iyList);
		List<ActionItem>          yActionList = new ArrayList<ActionItem>();	
		for (List<LayerIndex.D3>  iyList2 : iyListList) {
			yActionList.add( new YLayerActionItem(xLayer, yLayer, rel, iyList2, xMap, yLen));
		}
		ForkJoinUtil.LayerAction yAction = new ForkJoinUtil.LayerAction(yActionList);
		
		ForkJoinUtil.invoke(xAction);
		ForkJoinUtil.invoke(yAction);	
		
	}
	
	// ASSOCIATING (D3, D1)
	
	void associate(VectorLayer.D3<?,T> xLayer, VectorLayer.D1<?,T> yLayer, Relation rel){
		if (!getAssociateForkJoinFlag()) {
			associate0(yLayer, xLayer, new Transpose(rel));
		} else {
			associate1(yLayer, xLayer, new Transpose(rel));
		}
	}
	
	// ASSOCIATING (D3, D2)

	void associate(VectorLayer.D3<?,T> xLayer, VectorLayer.D2<?,T> yLayer, Relation rel){
		if (!getAssociateForkJoinFlag()) {
			associate0(yLayer, xLayer, new Transpose(rel));
		} else {
			associate1(yLayer, xLayer, new Transpose(rel));
		}
	}
	
	// ASSOCIATING (D3, D3)
	
	void associate(VectorLayer.D3<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel){
		if (!getAssociateForkJoinFlag()) {
			associate0(xLayer, yLayer, rel);
		} else {
			associate1(xLayer, yLayer, rel);
		}
	}
	
	private void associate0(VectorLayer.D3<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel){
		
		Map<LayerIndex.D3, List<LayerIndex.D3>> xMap = new HashMap<LayerIndex.D3, List<LayerIndex.D3>>();
		int[][][] yLen = (new LayerIndex.D3(yLayer)).getFrame();
		
		for (LayerIndex.D3 xGen = new LayerIndex.D3(xLayer); xGen.hasNext(); xGen.next()){
			setXLink(xGen, xMap, yLen, xLayer, yLayer, rel);
		}
		for (LayerIndex.D3 yGen = new LayerIndex.D3(yLayer); yGen.hasNext(); yGen.next()){
			setYLink(yGen, yLen, xMap, xLayer, yLayer, rel);
		}
		
	}
	
	private void associate1(VectorLayer.D3<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel){

		class XLayerActionItem extends D3D3 {
			XLayerActionItem(VectorLayer.D3<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel, List<LayerIndex.D3> ixList,
			Map<LayerIndex.D3, List<LayerIndex.D3>> xMap, int[][][] yLen) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = ixList;
				this.iyList = null;
				this.xMap = xMap;
				this.yLen = yLen;
			}
			@Override
			protected void compute() {
				for (LayerIndex.D3 ix : ixList) {
					setXLink(ix, xMap, yLen, xLayer, yLayer, rel);
				}
			}
		}
		class YLayerActionItem extends D3D3 {		
			YLayerActionItem(VectorLayer.D3<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel, List<LayerIndex.D3> iyList,
			Map<LayerIndex.D3, List<LayerIndex.D3>> xMap, int[][][] yLen) {
				this.xLayer = xLayer;
				this.yLayer = yLayer;
				this.rel = rel;
				this.ixList = null;
				this.iyList = iyList;
				this.xMap = xMap;
				this.yLen = yLen;
			}
			@Override
			protected void compute() {
				for (LayerIndex.D3 iy : iyList) {
					setYLink(iy, yLen, xMap, xLayer, yLayer, rel);
				}
			}
		}
		
		Map<LayerIndex.D3, List<LayerIndex.D3>> xMap = new HashMap<LayerIndex.D3, List<LayerIndex.D3>>();
		int[][][] yLen = (new LayerIndex.D3(yLayer)).getFrame();
		
		List<LayerIndex.D3>       ixList = generateLayerIndexList(xLayer);
		List<List<LayerIndex.D3>> ixListList = splitIndexD3List(ixList);
		List<ActionItem>          xActionList = new ArrayList<ActionItem>();	
		for (List<LayerIndex.D3> ixList2 : ixListList) {
			xActionList.add( new XLayerActionItem(xLayer, yLayer, rel, ixList2, xMap, yLen));
		}
		ForkJoinUtil.LayerAction xAction = new ForkJoinUtil.LayerAction(xActionList);
		
		List<LayerIndex.D3>       iyList = generateLayerIndexList(yLayer);
		List<List<LayerIndex.D3>> iyListList = splitIndexD3List(iyList);
		List<ActionItem>          yActionList = new ArrayList<ActionItem>();	
		for (List<LayerIndex.D3>  iyList2 : iyListList) {
			yActionList.add( new YLayerActionItem(xLayer, yLayer, rel, iyList2, xMap, yLen));
		}
		ForkJoinUtil.LayerAction yAction = new ForkJoinUtil.LayerAction(yActionList);
		
		ForkJoinUtil.invoke(xAction);
		ForkJoinUtil.invoke(yAction);	
		
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	CREATING LINK OBJECTS			    									//
	//////////////////////////////////////////////////////////////////////////////
	
	private void createLinkObjects(T[] xArr) {
		for (int i = 0; i < xArr.length; i++) {
			xArr[i] = newObject();
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	SET X-LINK (D1, D1)       		    									//
	//////////////////////////////////////////////////////////////////////////////
	
	private void setXLink(LayerIndex.D1 xGen, Map<LayerIndex.D1, List<LayerIndex.D1>> xMap, int[] yLen,
                          VectorLayer.D1<?,T> xLayer, VectorLayer.D1<?,T> yLayer, Relation rel) {

		int xLen = 0;
		for (LayerIndex.D1 yGen = new LayerIndex.D1(yLayer); yGen.hasNext(); yGen.next()){
			if (rel.contains(xGen.getIX(), yGen.getIX())) {
				int y1 = yGen.getX1();
					xLen++; 
					
					synchronized(yLen) {
						yLen[y1]++;
					}
					
					synchronized(xMap) {
						if ( xMap.isEmpty() || !xMap.containsKey(xGen) ){
							List<LayerIndex.D1> tmpList = new ArrayList<LayerIndex.D1>();
							tmpList.add(yGen.clone());
							xMap.put(xGen.clone(), tmpList);
						} else {
							List<LayerIndex.D1> tmpList = xMap.get(xGen);
							tmpList.add(yGen.clone());
						}
					}
					
			}
		}
		T[] newArr = newArray(xLen);
		createLinkObjects(newArr);
		xLayer.set(xGen.getX1(), newArr);
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	SET X-LINK (D1, D2)       		    									//
	//////////////////////////////////////////////////////////////////////////////
	
	private void setXLink(LayerIndex.D1 xGen, Map<LayerIndex.D1, List<LayerIndex.D2>> xMap, int[][] yLen,
                          VectorLayer.D1<?,T> xLayer, VectorLayer.D2<?,T> yLayer, Relation rel) {

		int xLen = 0;
		for (LayerIndex.D2 yGen = new LayerIndex.D2(yLayer); yGen.hasNext(); yGen.next()){
			if (rel.contains(xGen.getIX(), yGen.getIX())) {
				int y1 = yGen.getX1(), y2 = yGen.getX2();
					xLen++; 
					
					synchronized(yLen[y1]) {
						yLen[y1][y2]++;
					}
					
					synchronized(xMap) {
						if ( xMap.isEmpty() || !xMap.containsKey(xGen) ){
							List<LayerIndex.D2> tmpList = new ArrayList<LayerIndex.D2>();
							tmpList.add(yGen.clone());
							xMap.put(xGen.clone(), tmpList);
						} else {
							List<LayerIndex.D2> tmpList = xMap.get(xGen);
							tmpList.add(yGen.clone());
						}
					}
					
			}
		}
		T[] newArr = newArray(xLen);
		createLinkObjects(newArr);
		xLayer.set(xGen.getX1(), newArr);
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	SET X-LINK (D1, D3)       		    									//
	//////////////////////////////////////////////////////////////////////////////
	
	private void setXLink(LayerIndex.D1 xGen, Map<LayerIndex.D1, List<LayerIndex.D3>> xMap, int[][][] yLen,
                          VectorLayer.D1<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel) {

		int xLen = 0;
		for (LayerIndex.D3 yGen = new LayerIndex.D3(yLayer); yGen.hasNext(); yGen.next()){
			if (rel.contains(xGen.getIX(), yGen.getIX())) {
				int y1 = yGen.getX1(), y2 = yGen.getX2(), y3 = yGen.getX3();
					xLen++; 
					
					synchronized(yLen[y1][y2]) {
						yLen[y1][y2][y3]++;
					}
					
					synchronized(xMap) {
						if ( xMap.isEmpty() || !xMap.containsKey(xGen) ){
							List<LayerIndex.D3> tmpList = new ArrayList<LayerIndex.D3>();
							tmpList.add(yGen.clone());
							xMap.put(xGen.clone(), tmpList);
						} else {
							List<LayerIndex.D3> tmpList = xMap.get(xGen);
							tmpList.add(yGen.clone());
						}
					}
					
			}
		}
		T[] newArr = newArray(xLen);
		createLinkObjects(newArr);
		xLayer.set(xGen.getX1(), newArr);
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	SET X-LINK (D2, D2)    			    									//
	//////////////////////////////////////////////////////////////////////////////
	
	private void setXLink(LayerIndex.D2 xGen, Map<LayerIndex.D2, List<LayerIndex.D2>> xMap, int[][] yLen,
			              VectorLayer.D2<?,T> xLayer, VectorLayer.D2<?,T> yLayer, Relation rel) {

		int xLen = 0;
		for (LayerIndex.D2 yGen = new LayerIndex.D2(yLayer); yGen.hasNext(); yGen.next()){
			if (rel.contains(xGen.getIX(), yGen.getIX())) {
				int y1 = yGen.getX1(), y2 = yGen.getX2();
					xLen++; 
					
					synchronized(yLen[y1]) {
						yLen[y1][y2]++;
					}
					
					synchronized(xMap) {
						if ( xMap.isEmpty() || !xMap.containsKey(xGen) ){
							List<LayerIndex.D2> tmpList = new ArrayList<LayerIndex.D2>();
							tmpList.add(yGen.clone());
							xMap.put(xGen.clone(), tmpList);
						} else {
							List<LayerIndex.D2> tmpList = xMap.get(xGen);
							tmpList.add(yGen.clone());
						}
					}
					
			}
		}
		T[] newArr = newArray(xLen);
		createLinkObjects(newArr);
		xLayer.set(xGen.getX1(), xGen.getX2(), newArr);
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	SET X-LINK (D2, D3)       		    									//
	//////////////////////////////////////////////////////////////////////////////
	
	private void setXLink(LayerIndex.D2 xGen, Map<LayerIndex.D2, List<LayerIndex.D3>> xMap, int[][][] yLen,
                          VectorLayer.D2<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel) {

		int xLen = 0;
		for (LayerIndex.D3 yGen = new LayerIndex.D3(yLayer); yGen.hasNext(); yGen.next()){
			if (rel.contains(xGen.getIX(), yGen.getIX())) {
				int y1 = yGen.getX1(), y2 = yGen.getX2(), y3 = yGen.getX3();
					xLen++; 
					
					synchronized(yLen[y1][y2]) {
						yLen[y1][y2][y3]++;
					}
					
					synchronized(xMap) {
						if ( xMap.isEmpty() || !xMap.containsKey(xGen) ){
							List<LayerIndex.D3> tmpList = new ArrayList<LayerIndex.D3>();
							tmpList.add(yGen.clone());
							xMap.put(xGen.clone(), tmpList);
						} else {
							List<LayerIndex.D3> tmpList = xMap.get(xGen);
							tmpList.add(yGen.clone());
						}
					}
					
			}
		}
		T[] newArr = newArray(xLen);
		createLinkObjects(newArr);
		xLayer.set(xGen.getX1(), xGen.getX2(), newArr);
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	SET X-LINK (D3, D3)       		    									//
	//////////////////////////////////////////////////////////////////////////////
	
	private void setXLink(LayerIndex.D3 xGen, Map<LayerIndex.D3, List<LayerIndex.D3>> xMap, int[][][] yLen,
                          VectorLayer.D3<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel) {

		int xLen = 0;
		for (LayerIndex.D3 yGen = new LayerIndex.D3(yLayer); yGen.hasNext(); yGen.next()){
			if (rel.contains(xGen.getIX(), yGen.getIX())) {
				int y1 = yGen.getX1(), y2 = yGen.getX2(), y3 = yGen.getX3();
					xLen++; 
					
					synchronized(yLen[y1][y2]) {
						yLen[y1][y2][y3]++;
					}
					
					synchronized(xMap) {
						if ( xMap.isEmpty() || !xMap.containsKey(xGen) ){
							List<LayerIndex.D3> tmpList = new ArrayList<LayerIndex.D3>();
							tmpList.add(yGen.clone());
							xMap.put(xGen.clone(), tmpList);
						} else {
							List<LayerIndex.D3> tmpList = xMap.get(xGen);
							tmpList.add(yGen.clone());
						}
					}
					
			}
		}
		T[] newArr = newArray(xLen);
		createLinkObjects(newArr);
		xLayer.set(xGen.getX1(), xGen.getX2(), xGen.getX3(), newArr);
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	SET Y-LINK (D1, D1)      		    	 								//
	//////////////////////////////////////////////////////////////////////////////
	
	private void setYLink(LayerIndex.D1 yGen, int[] yLen, Map<LayerIndex.D1, List<LayerIndex.D1>> xMap,
                          VectorLayer.D1<?,T> xLayer, VectorLayer.D1<?,T> yLayer, Relation rel) {

		int y1 = yGen.getX1();
		T[] newArr = newArray(yLen[y1]);
		yLayer.set(y1, newArr);
		
		int yIndex = 0;
		for (LayerIndex.D1 xGen = new LayerIndex.D1(xLayer); xGen.hasNext(); xGen.next()){
			if (rel.contains(xGen.getIX(), yGen.getIX())) {
				int x1 = xGen.getX1();
				int index = xMap.get(xGen).indexOf(yGen);
				
				synchronized(yLayer) {
					yLayer.get(y1)[yIndex++] = xLayer.get(x1)[index];
				}
				
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	SET Y-LINK (D1, D2)      		    	 								//
	//////////////////////////////////////////////////////////////////////////////
	
	private void setYLink(LayerIndex.D2 yGen, int[][] yLen, Map<LayerIndex.D1, List<LayerIndex.D2>> xMap,
                          VectorLayer.D1<?,T> xLayer, VectorLayer.D2<?,T> yLayer, Relation rel) {

		int y1 = yGen.getX1(), y2 = yGen.getX2();
		T[] newArr = newArray(yLen[y1][y2]);
		yLayer.set(y1, y2, newArr);
		
		int yIndex = 0;
		for (LayerIndex.D1 xGen = new LayerIndex.D1(xLayer); xGen.hasNext(); xGen.next()){
			if (rel.contains(xGen.getIX(), yGen.getIX())) {
				int x1 = xGen.getX1();
				int index = xMap.get(xGen).indexOf(yGen);
				
				synchronized(yLayer) {
					yLayer.get(y1, y2)[yIndex++] = xLayer.get(x1)[index];
				}
				
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	SET Y-LINK (D1, D3)      		    	 								//
	//////////////////////////////////////////////////////////////////////////////
	
	private void setYLink(LayerIndex.D3 yGen, int[][][] yLen, Map<LayerIndex.D1, List<LayerIndex.D3>> xMap,
                          VectorLayer.D1<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel) {

		int y1 = yGen.getX1(), y2 = yGen.getX2(), y3 = yGen.getX3();
		T[] newArr = newArray(yLen[y1][y2][y3]);
		yLayer.set(y1, y2, y3, newArr);
		
		int yIndex = 0;
		for (LayerIndex.D1 xGen = new LayerIndex.D1(xLayer); xGen.hasNext(); xGen.next()){
			if (rel.contains(xGen.getIX(), yGen.getIX())) {
				int x1 = xGen.getX1();
				int index = xMap.get(xGen).indexOf(yGen);
				
				synchronized(yLayer) {
					yLayer.get(y1, y2, y3)[yIndex++] = xLayer.get(x1)[index];
				}
				
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	SET Y-LINK (D2, D2)      		    	 								//
	//////////////////////////////////////////////////////////////////////////////
	
	private void setYLink(LayerIndex.D2 yGen, int[][] yLen, Map<LayerIndex.D2, List<LayerIndex.D2>> xMap,
                          VectorLayer.D2<?,T> xLayer, VectorLayer.D2<?,T> yLayer, Relation rel) {

		int y1 = yGen.getX1(), y2 = yGen.getX2();
		T[] newArr = newArray(yLen[y1][y2]);
		yLayer.set(y1, y2, newArr);
		
		int yIndex = 0;
		for (LayerIndex.D2 xGen = new LayerIndex.D2(xLayer); xGen.hasNext(); xGen.next()){
			if (rel.contains(xGen.getIX(), yGen.getIX())) {
				int x1 = xGen.getX1(), x2 = xGen.getX2();
				int index = xMap.get(xGen).indexOf(yGen);
				
				synchronized(yLayer) {
					yLayer.get(y1, y2)[yIndex++] = xLayer.get(x1, x2)[index];
				}
				
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	SET Y-LINK (D2, D3)      		    	 								//
	//////////////////////////////////////////////////////////////////////////////
	
	private void setYLink(LayerIndex.D3 yGen, int[][][] yLen, Map<LayerIndex.D2, List<LayerIndex.D3>> xMap,
                          VectorLayer.D2<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel) {

		int y1 = yGen.getX1(), y2 = yGen.getX2(), y3 = yGen.getX3();
		T[] newArr = newArray(yLen[y1][y2][y3]);
		yLayer.set(y1, y2, y3, newArr);
		
		int yIndex = 0;
		for (LayerIndex.D2 xGen = new LayerIndex.D2(xLayer); xGen.hasNext(); xGen.next()){
			if (rel.contains(xGen.getIX(), yGen.getIX())) {
				int x1 = xGen.getX1(), x2 = xGen.getX2();
				int index = xMap.get(xGen).indexOf(yGen);
				
				synchronized(yLayer) {
					yLayer.get(y1, y2, y3)[yIndex++] = xLayer.get(x1, x2)[index];
				}
				
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	SET Y-LINK (D3, D3)      		    	 								//
	//////////////////////////////////////////////////////////////////////////////
	
	private void setYLink(LayerIndex.D3 yGen, int[][][] yLen, Map<LayerIndex.D3, List<LayerIndex.D3>> xMap,
                          VectorLayer.D3<?,T> xLayer, VectorLayer.D3<?,T> yLayer, Relation rel) {

		int y1 = yGen.getX1(), y2 = yGen.getX2(), y3 = yGen.getX3();
		T[] newArr = newArray(yLen[y1][y2][y3]);
		yLayer.set(y1, y2, y3, newArr);
		
		int yIndex = 0;
		for (LayerIndex.D3 xGen = new LayerIndex.D3(xLayer); xGen.hasNext(); xGen.next()){
			if (rel.contains(xGen.getIX(), yGen.getIX())) {
				int x1 = xGen.getX1(), x2 = xGen.getX2(), x3 = xGen.getX3();
				int index = xMap.get(xGen).indexOf(yGen);
				
				synchronized(yLayer) {
					yLayer.get(y1, y2, y3)[yIndex++] = xLayer.get(x1, x2, x3)[index];
				}
				
			}
		}
	}

}

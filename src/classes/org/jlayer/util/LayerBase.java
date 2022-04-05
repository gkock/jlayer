package org.jlayer.util;

import org.jlayer.model.BasedLayer;

public class LayerBase<U> extends LayerCore<U,U> implements BasedLayer<U> {
		
	// class LayerBase.D1
	public static class D1<U> extends LayerCore.D1<U,U> implements BasedLayer.D1<U> {
		
		private U[] arrayD1;
		
		// constructors
		public D1(BasedLayer.D1<U> layerD1){ 
			this.arrayD1 = layerD1.getBase();
		}
		public D1(U[] arrayD1){ 
			this.arrayD1 = arrayD1;
		}
		
		// base()
		@Override
		public U[] getBase() {
			return arrayD1;
		}
		
		// length(), get(), set(), isAdapter(), getUnit(), setUnit()
		@Override
		public int length(){
			return this.arrayD1.length;
		}
		@Override
		public U get(int x1){
			return arrayD1[x1];
		}
		@Override
		public void set(int x1, U elem){
			arrayD1[x1] = elem;
		}
		@Override
		public boolean isAdapter(){ return false; }
		@Override
		public U getUnit(int x1){
			return get(x1);
		}
		@Override
		public void setUnit(int x1, U unit){
			set(x1, unit);
		}
		
	}
	
	// class LayerBase.D2
	public static class D2<U> extends LayerCore.D2<U,U> implements BasedLayer.D2<U> {
		
		private U[][] arrayD2;
		
		// constructors
		public D2(BasedLayer.D2<U> layerD2){ 
			this.arrayD2 = layerD2.getBase();
		}
		public D2(U[][] arrayD2){ 
			this.arrayD2 = arrayD2;
		}
		
		// base()
		@Override
		public U[][] getBase() {
			return arrayD2;
		}
		
		// length(), get(), set(), isAdapter(), getUnit(), setUnit()
		@Override
		public int length(){
			return this.arrayD2.length;
		}
		@Override
		public int length(int x1){
			return arrayD2[x1].length;
		}
		@Override
		public U get(int x1, int x2){
			return arrayD2[x1][x2];
		}
		@Override
		public void set(int x1, int x2, U elem){
			arrayD2[x1][x2] = elem;
		}
		@Override
		public boolean isAdapter(){ return false; }
		@Override
		public U getUnit(int x1, int x2){
			return get(x1, x2);
		}
		@Override
		public void setUnit(int x1, int x2, U unit){
			set(x1, x2, unit);
		}

	}
	
	// class LayerBase.D3
	public static class D3<U> extends LayerCore.D3<U,U> implements BasedLayer.D3<U> {
		
		private U[][][] arrayD3;
		
		// constructors
		public D3(BasedLayer.D3<U> layerD3){ 
			this.arrayD3 = layerD3.getBase();
		}
		public D3(U[][][] arrayD3){ 
			this.arrayD3 = arrayD3;
		}
		
		// base()
		@Override
		public U[][][] getBase() {
			return arrayD3;
		}
		
		// length(), get(), set(), isAdapter(), getUnit(), setUnit()
		@Override
		public int length(){
			return this.arrayD3.length;
		}
		@Override
		public int length(int x1){
			return arrayD3[x1].length;
		}
		@Override
		public int length(int x1, int x2){
			return arrayD3[x1][x2].length;
		}
		@Override
		public U get(int x1, int x2, int x3){
			return arrayD3[x1][x2][x3];
		}
		@Override
		public void set(int x1, int x2, int x3, U elem){
			arrayD3[x1][x2][x3] = elem;
		}
		@Override
		public boolean isAdapter(){ return false; }
		@Override
		public U getUnit(int x1, int x2, int x3){
			return get(x1, x2, x3);
		}
		@Override
		public void setUnit(int x1, int x2, int x3, U unit){
			set(x1, x2, x3, unit);
		}
		
	}
	
// class LayerBase
	
	protected D1<U> layerD1;
	protected D2<U> layerD2;
	protected D3<U> layerD3;
	
	protected int dimensionality;
	
	// constructors
	
	public LayerBase(U[] arrayD1){ 
		this.layerD1 = new D1<U>(arrayD1);
		this.dimensionality = 1;
	}
	public LayerBase(U[][] arrayD2){ 
		this.layerD2 = new D2<U>(arrayD2);
		this.dimensionality = 2;
	}
	public LayerBase(U[][][] arrayD3){ 
		this.layerD3 = new D3<U>(arrayD3);
		this.dimensionality = 3;
	}
	
	public LayerBase(BasedLayer<U> layer){ 
		switch (layer.dims()) {
			case 1:
				this.layerD1 = new D1<U>(layer.getD1().getBase());
				this.dimensionality = 1;
				break;
			case 2:
				this.layerD2 = new D2<U>(layer.getD2().getBase());
				this.dimensionality = 2;
				break;
			case 3:
				this.layerD3 = new D3<U>(layer.getD3().getBase());
				this.dimensionality = 3;
				break;
			default: throw new JLayerException();
		}
	}
	public LayerBase(BasedLayer.D1<U> layerD1){ 
		this.layerD1 = new D1<U>(layerD1.getBase());
		this.dimensionality = 1;
	}
	public LayerBase(BasedLayer.D2<U> layerD2){ 
		this.layerD2 = new D2<U>(layerD2.getBase());
		this.dimensionality = 2;
	}
	public LayerBase(BasedLayer.D3<U> layerD3){ 
		this.layerD3 = new D3<U>(layerD3.getBase());
		this.dimensionality = 3;
	}
	
	// getD1(), getD2(), getD3()
	
	@Override
	public D1<U> getD1() {
		if (this.dimensionality != 1) return null;
		return layerD1;
	}
	@Override
	public D2<U> getD2() {
		if (this.dimensionality != 2) return null;
		return layerD2;
	}
	@Override
	public D3<U> getD3() {
		if (this.dimensionality != 3) return null;
		return layerD3;
	}
	
	// dims() and length()
	
	@Override
	public int dims(){
		return this.dimensionality;
	}
	
	@Override
	public int length() {
		switch (this.dimensionality) {
			case 1:	 return layerD1.length();
			case 2:	 return layerD2.length();
			case 3:	 return layerD3.length();
			default: throw new IndexOutOfBoundsException("class LayerBase<U>.length()int"); 
		}
	}
	@Override
	public int length(int... ix){
		if (ix.length >= this.dimensionality) { 
			throw new IndexOutOfBoundsException("class LayerBase<U>.length(int...)int"); 
		}
		switch (ix.length) {
			case 0:	 return length();
			case 1:	 if (this.dimensionality == 2) {
							return layerD2.length(ix[0]);
						} else { // (this.dimensionality == 3)
							return layerD3.length(ix[0]);
						}
			case 2:	 return layerD3.length(ix[0], ix[1]);
			default: throw new IndexOutOfBoundsException("class LayerBase<U>.length(int...)int"); 
		}
	}
	
	// get() and set()
	
	@Override
	public U get(int... ix){ 
		if (ix.length != dims()) { 
			throw new IndexOutOfBoundsException("class LayerBase<U>.get(int...)U"); 
		}
		switch (this.dimensionality) {
			case 1:	 return layerD1.get(ix[0]);
			case 2:	 return layerD2.get(ix[0], ix[1]);
			case 3:	 return layerD3.get(ix[0], ix[1], ix[2]);
			default: throw new IndexOutOfBoundsException("class LayerBase<U>.get(int...)U"); 
		}
	}
	
	@Override
	public void set(int[] ix, U elem){ 
		if (ix.length != dims()) { 
			throw new IndexOutOfBoundsException("class LayerBase<U>.set(int[],U)void"); 
		}
		switch (this.dimensionality) {
			case 1: layerD1.set(ix[0], elem); break;
			case 2: layerD2.set(ix[0], ix[1], elem); break;
			case 3: layerD3.set(ix[0], ix[1], ix[2], elem); break;
			default: throw new IndexOutOfBoundsException("class LayerBase<U>.set(int[],U)void"); 
		}
	}
	
	@Override
	public void set(int x1, U elem){ 
		if (dims() != 1) { 
			throw new IndexOutOfBoundsException("class LayerBase<U>.set(int,U)void"); 
		}
		layerD1.set(x1, elem);
	}
	
	@Override
	public void set(int x1, int x2, U elem){ 
		if (dims() != 2) { 
			throw new IndexOutOfBoundsException("class LayerBase<U>.set(int,int,U)void"); 
		}
		layerD2.set(x1, x2, elem);
	}
	
	@Override
	public void set(int x1, int x2, int x3, U elem){ 
		if (dims() != 3) { 
			throw new IndexOutOfBoundsException("class LayerBase<U>.set(int,int,U)void"); 
		}
		layerD3.set(x1, x2, x3, elem);
	}
	
	// isAdapter()
	@Override
	public boolean isAdapter(){ return false; }
	
	// getUnit() and setUnit()
	
	@Override
	public U getUnit(int... ix){ 
		return get(ix);
	}
	
	@Override
	public void setUnit(int[] ix, U unit){ 
		set(ix, unit);
	}
	
	@Override
	public void setUnit(int x1, U unit){ 
		set(x1, unit);
	}
	
	@Override
	public void setUnit(int x1, int x2, U unit){ 
		set(x1, x2, unit);
	}
	
	@Override
	public void setUnit(int x1, int x2, int x3, U unit){ 
		set(x1, x2, x3, unit);
	}
	
}

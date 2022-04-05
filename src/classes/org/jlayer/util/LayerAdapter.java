package org.jlayer.util;

import org.jlayer.model.Layer;

abstract public class LayerAdapter<U,T> extends LayerCore<U,T> implements Layer<U,T> {
	
	// class LayerAdapter.D1
	abstract public static class D1<U,T> extends LayerCore.D1<U,T> implements Layer.D1<U,T> {
		@Override
		public boolean isAdapter(){ return true; }
	}

	
	// class LayerAdapter.D2
	abstract public static class D2<U,T> extends LayerCore.D2<U,T> implements Layer.D2<U,T> {
		@Override
		public boolean isAdapter(){ return true; }
	}
	
	// class LayerAdapter.D3
	abstract public static class D3<U,T> extends LayerCore.D3<U,T> implements Layer.D3<U,T> {
		@Override
		public boolean isAdapter(){ return true; }
	}
	
// class LayerAdapter
	
	@Override
	public boolean isAdapter(){ return true; }
	
	// getD1(), getD2(), getD3()
	
	@Override
	public D1<U,T> getD1() {
		if (this.dims() != 1) return null;
		return new D1<U,T>(){
			@Override
			public int length() { return LayerAdapter.this.length(); }
			@Override
			public T get(int x1) { return LayerAdapter.this.get(x1); }
			@Override
			public void set(int x1, T elem ){ LayerAdapter.this.set(x1, elem); }
			@Override
			public U getUnit(int x1) { return LayerAdapter.this.getUnit(x1); }
			@Override
			public void setUnit(int x1, U unit ){ LayerAdapter.this.setUnit(x1, unit); }
		};
  	 }
	
	@Override
	public D2<U,T> getD2() {
  		if (this.dims() != 2) return null;
  		return new D2<U,T>(){
			@Override
			public int length() { return LayerAdapter.this.length(); }
			@Override
			public int length(int x1) { return LayerAdapter.this.length(x1); }
			@Override
			public T get(int x1, int x2) { return LayerAdapter.this.get(x1, x2); }
			@Override
			public void set(int x1, int x2, T elem ){ LayerAdapter.this.set(x1, x2, elem); }
			@Override
			public U getUnit(int x1, int x2) { return LayerAdapter.this.getUnit(x1, x2); }
			@Override
			public void setUnit(int x1, int x2, U unit ){ LayerAdapter.this.setUnit(x1, x2, unit); }
		};
  	 }
  	 @Override
	public D3<U,T> getD3() {
  		if (this.dims() != 3) return null;
  		return new D3<U,T>(){
			@Override
			public int length() { return LayerAdapter.this.length(); }
			@Override
			public int length(int x1) { return LayerAdapter.this.length(x1); }
			@Override
			public int length(int x1, int x2) { return LayerAdapter.this.length(x1, x2); }
			@Override
			public T get(int x1, int x2, int x3) { return LayerAdapter.this.get(x1, x2, x3); }
			@Override
			public void set(int x1, int x2, int x3, T elem ){ LayerAdapter.this.set(x1, x2, x3, elem); }
			@Override
			public U getUnit(int x1, int x2, int x3) { return LayerAdapter.this.getUnit(x1, x2, x3); }
			@Override
			public void setUnit(int x1, int x2, int x3, U unit ){ LayerAdapter.this.setUnit(x1, x2, x3, unit); }
		};
  	  }

}

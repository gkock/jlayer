package org.jlayer.model;

public interface BasedLayer<T> extends Layer<T,T> {
	
	// interface BasedLayer.D1
	interface D1<T> extends Layer.D1<T,T> {
		T[] getBase();
    }
	
	// interface BasedLayer.D2
	interface D2<T> extends Layer.D2<T,T> {
		T[][] getBase();
	}
	
	// interface BasedLayer.D3
	interface D3<T> extends Layer.D3<T,T> {
		T[][][] getBase();
	}
	
// interface BasedLayer
	@Override
	D1<T> getD1();
	@Override
	D2<T> getD2();
	@Override
	D3<T> getD3();
	
}

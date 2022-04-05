package org.jlayer.model;

public interface BasedVectorLayer<T> extends Layer<T[],T[]>, BasedLayer<T[]>, VectorLayer<T[],T> {
	
	// interface BasedVectorLayer.D1
	interface D1<T> extends Layer.D1<T[],T[]>, BasedLayer.D1<T[]>, VectorLayer.D1<T[],T> {
		
    }
	
	// interface BasedVectorLayer.D2
	interface D2<T> extends Layer.D2<T[],T[]>, BasedLayer.D2<T[]>, VectorLayer.D2<T[],T> {
		
	}
	
	// interface BasedVectorLayer.D3
	interface D3<T> extends Layer.D3<T[],T[]>, BasedLayer.D3<T[]>, VectorLayer.D3<T[],T> {
		
	}

// interface BasedVectorLayer
	@Override
	D1<T> getD1();
	@Override
	D2<T> getD2();
	@Override
	D3<T> getD3();
	
}

package org.jlayer.model;

public interface VectorLayer<U,T> extends Layer<U,T[]> {
	
	// interface VectorLayer.D1
	interface D1<U,T> extends Layer.D1<U,T[]>{
		
		// connecting layers
		void connect(Layer<?,T> layer, Relation rel);
		void connect(Layer.D1<?,T> layer, Relation rel);
		void connect(Layer.D2<?,T> layer, Relation rel);
		void connect(Layer.D3<?,T> layer, Relation rel);
		
		// associating vector layers
		void associate(VectorLayer<?,T> layer, Relation rel);
		void associate(VectorLayer.D1<?,T> layer, Relation rel);
		void associate(VectorLayer.D2<?,T> layer, Relation rel);
		void associate(VectorLayer.D3<?,T> layer, Relation rel);
		
    }
	
	// interface VectorLayer.D2
	interface D2<U,T> extends Layer.D2<U,T[]> {
		
		// connecting layers
		void connect(Layer<?,T> layer, Relation rel);
		void connect(Layer.D1<?,T> layer, Relation rel);
		void connect(Layer.D2<?,T> layer, Relation rel);
		void connect(Layer.D3<?,T> layer, Relation rel);
		
		// associating vector layers
		void associate(VectorLayer<?,T> layer, Relation rel);
		void associate(VectorLayer.D1<?,T> layer, Relation rel);
		void associate(VectorLayer.D2<?,T> layer, Relation rel);
		void associate(VectorLayer.D3<?,T> layer, Relation rel);
		
	}
		
	// interface VectorLayer.D3
	interface D3<U,T> extends Layer.D3<U,T[]> {
		
		// connecting layers
		void connect(Layer<?,T> layer, Relation rel);
		void connect(Layer.D1<?,T> layer, Relation rel);
		void connect(Layer.D2<?,T> layer, Relation rel);
		void connect(Layer.D3<?,T> layer, Relation rel);
		
		// associating vector layers
		void associate(VectorLayer<?,T> layer, Relation rel);
		void associate(VectorLayer.D1<?,T> layer, Relation rel);
		void associate(VectorLayer.D2<?,T> layer, Relation rel);
		void associate(VectorLayer.D3<?,T> layer, Relation rel);
		
	}
		
// interface VectorLayer
	
	// connecting layers
	void connect(Layer<?,T> layer, Relation rel);
	void connect(Layer.D1<?,T> layer, Relation rel);
	void connect(Layer.D2<?,T> layer, Relation rel);
	void connect(Layer.D3<?,T> layer, Relation rel);
	
	// associating vector layers
	void associate(VectorLayer<?,T> layer, Relation rel);
	void associate(VectorLayer.D1<?,T> layer, Relation rel);
	void associate(VectorLayer.D2<?,T> layer, Relation rel);
	void associate(VectorLayer.D3<?,T> layer, Relation rel);
	
	// getting dimensioned layers
	@Override
	D1<U,T> getD1();
	@Override
	D2<U,T> getD2();
	@Override
	D3<U,T> getD3();
	
}

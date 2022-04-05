package org.jlayer.util;

import org.jlayer.model.*;

public abstract class VectorLayerBase<T> extends LayerBase<T[]> implements VectorLayer<T[],T>, BasedVectorLayer<T> {
	
	private static void assertUnequality(Object layer1, Object layer2) {
		if (layer1.equals(layer2)) {
			throw new JLayerException("runtime exception raised by org.jlayer: identical layer must not be associated");
		}
	}
	
	// class VectorLayerBase.D1
	public abstract static class D1<T> extends LayerBase.D1<T[]> implements VectorLayer.D1<T[],T>, BasedVectorLayer.D1<T> {
		
		// reference item for checking equality
		VectorLayerBase<T> referenceItem = null;
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean equals(Object obj) {
			if (obj instanceof D1<?>) {
				return D1.this.refEquals((D1<T>)obj);
			} else {
				return super.equals(obj);
			}
		}
		public boolean refEquals(D1<T> obj){
			if ( (this.referenceItem != null) && (obj.referenceItem != null) ) {
				return this.referenceItem.equals(obj.referenceItem);
			} else if (this.referenceItem != null) {
				return this.referenceItem.equals(obj );
			} else if (obj.referenceItem != null) {
				return obj.referenceItem.equals(this);
			} else {
				return super.equals(obj);
			}
		}
		
		// abstract items
		abstract protected T newObject();
		abstract protected T[] newArray(int len);
		
		// constructors
		public D1(BasedLayer.D1<T[]> layerD1){ 
			super(layerD1);
		}
		public D1(T[][] arrayD1){ 
			super(arrayD1);
		}
		
		// connect utilities
		ConnectUtil<T> connectUtils = new ConnectUtil<T>(){
			@Override
			protected T newObject(){ return D1.this.newObject(); }
			@Override
			protected T[] newArray(int len){ return D1.this.newArray(len); }
		};
		
		// associate utilities
		AssociateUtil<T> associateUtils = new AssociateUtil<T>(){
			@Override
			protected T newObject(){ return D1.this.newObject(); }
			@Override
			protected T[] newArray(int len){ return D1.this.newArray(len); }
		};
		
		// connecting layers
		@Override
		public void connect(Layer<?,T> layer, Relation rel){
			switch (layer.dims()) {
				case 1:  connect(layer.getD1(), rel); break;
				case 2:  connect(layer.getD2(), rel); break;
				case 3:  connect(layer.getD3(), rel); break;
				default: throw new JLayerException();
			}	
		}
		
		@Override
		public void connect(Layer.D1<?,T> layer, Relation rel){
			connectUtils.connect(this, layer, rel);
		}
		
		@Override
		public void connect(Layer.D2<?,T> layer, Relation rel){
			connectUtils.connect(this, layer, rel);
		}
		
		@Override
		public void connect(Layer.D3<?,T> layer, Relation rel){
			connectUtils.connect(this, layer, rel);
		}
		
		// associating vector layers
		@Override
		public void associate(VectorLayer<?,T> layer, Relation rel){
			switch (layer.dims()) {
				case 1:  associate(layer.getD1(), rel); break;
				case 2:  associate(layer.getD2(), rel); break;
				case 3:  associate(layer.getD3(), rel); break;
				default: throw new JLayerException();
			}	
		}
		
		@Override
		public void associate(VectorLayer.D1<?,T> layer, Relation rel){
			assertUnequality(this, layer);
			associateUtils.associate(this, layer, rel);
		}
		
		@Override
		public void associate(VectorLayer.D2<?,T> layer, Relation rel){
			assertUnequality(this, layer);
			associateUtils.associate(this, layer, rel);
		}
		
		@Override
		public void associate(VectorLayer.D3<?,T> layer, Relation rel){
			assertUnequality(this, layer);
			associateUtils.associate(this, layer, rel);
		}
		
	}
	
	
	// class VectorLayerBase.D2
	public abstract static class D2<T> extends LayerBase.D2<T[]> implements VectorLayer.D2<T[],T>, BasedVectorLayer.D2<T> {
		
		// reference item for checking equality
		VectorLayerBase<T> referenceItem = null;
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean equals(Object obj) {
			if (obj instanceof D2<?>) {
				return D2.this.refEquals((D2<T>)obj);
			} else {
				return super.equals(obj);
			}
		}
		public boolean refEquals(D2<T> obj){
			if ( (this.referenceItem != null) && (obj.referenceItem != null) ) {
				return this.referenceItem.equals(obj.referenceItem);
			} else if (this.referenceItem != null) {
				return this.referenceItem.equals(obj );
			} else if (obj.referenceItem != null) {
				return obj.referenceItem.equals(this);
			} else {
				return super.equals(obj);
			}
		}
		
		// abstract items
		abstract protected T newObject();
		abstract protected T[] newArray(int len);
		
		// constructors
		public D2(BasedLayer.D2<T[]> layerD2){ 
			super(layerD2);
		}
		public D2(T[][][] arrayD2){ 
			super(arrayD2);
		}
		
		// connect utilities
		ConnectUtil<T> connectUtils = new ConnectUtil<T>(){
			@Override
			protected T newObject(){ return D2.this.newObject(); }
			@Override
			protected T[] newArray(int len){ return D2.this.newArray(len); }
		};
		
		// associate utilities
		AssociateUtil<T> associateUtils = new AssociateUtil<T>(){
			@Override
			protected T newObject(){ return D2.this.newObject(); }
			@Override
			protected T[] newArray(int len){ return D2.this.newArray(len); }
		};
		
		// connecting layers
		@Override
		public void connect(Layer<?,T> layer, Relation rel){
			switch (layer.dims()) {
				case 1:  connect(layer.getD1(), rel); break;
				case 2:  connect(layer.getD2(), rel); break;
				case 3:  connect(layer.getD3(), rel); break;
				default: throw new JLayerException();
			}	
		}

		@Override
		public void connect(Layer.D1<?,T> layer, Relation rel){
			connectUtils.connect(this, layer, rel);
       	}
		
		@Override
		public void connect(Layer.D2<?,T> layer, Relation rel){
			connectUtils.connect(this, layer, rel);
		}
		
		@Override
		public void connect(Layer.D3<?,T> layer, Relation rel){
			connectUtils.connect(this, layer, rel);
		}

		// associating vector layers
		@Override
		public void associate(VectorLayer<?,T> layer, Relation rel){
			switch (layer.dims()) {
				case 1:  associate(layer.getD1(), rel); break;
				case 2:  associate(layer.getD2(), rel); break;
				case 3:  associate(layer.getD3(), rel); break;
				default: throw new JLayerException();
			}	
		}
		
		@Override
		public void associate(VectorLayer.D1<?,T> layer, Relation rel){
			assertUnequality(this, layer);
			associateUtils.associate(this, layer, rel);
		}
		
		@Override
		public void associate(VectorLayer.D2<?,T> layer, Relation rel){
			assertUnequality(this, layer);
			associateUtils.associate(this, layer, rel);
		}
		
		@Override
		public void associate(VectorLayer.D3<?,T> layer, Relation rel){
			assertUnequality(this, layer);
			associateUtils.associate(this, layer, rel);
		}
		
	}
	
	// class VectorLayerBase.D3
	public abstract static class D3<T> extends LayerBase.D3<T[]> implements VectorLayer.D3<T[],T>, BasedVectorLayer.D3<T> {
		
		// reference item for checking equality
		VectorLayerBase<T> referenceItem = null;
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean equals(Object obj) {
			if (obj instanceof D3<?>) {
				return D3.this.refEquals((D3<T>)obj);
			} else {
				return super.equals(obj);
			}
		}
		public boolean refEquals(D3<T> obj){
			if ( (this.referenceItem != null) && (obj.referenceItem != null) ) {
				return this.referenceItem.equals(obj.referenceItem);
			} else if (this.referenceItem != null) {
				return this.referenceItem.equals(obj );
			} else if (obj.referenceItem != null) {
				return obj.referenceItem.equals(this);
			} else {
				return super.equals(obj);
			}
		}
		
		// abstract items
		abstract protected T newObject();
		abstract protected T[] newArray(int len);
		
		// constructors
		public D3(BasedLayer.D3<T[]> layerD3){ 
			super(layerD3);
		}
		public D3(T[][][][] arrayD3){ 
			super(arrayD3);
		}
		
		// connect utilities
		ConnectUtil<T> connectUtils = new ConnectUtil<T>(){
			@Override
			protected T newObject(){ return D3.this.newObject(); }
			@Override
			protected T[] newArray(int len){ return D3.this.newArray(len); }
		};
		
		// associate utilities
		AssociateUtil<T> associateUtils = new AssociateUtil<T>(){
			@Override
			protected T newObject(){ return D3.this.newObject(); }
			@Override
			protected T[] newArray(int len){ return D3.this.newArray(len); }
		};
		
		// connecting layers
		@Override
		public void connect(Layer<?,T> layer, Relation rel){
			switch (layer.dims()) {
				case 1:  connect(layer.getD1(), rel); break;
				case 2:  connect(layer.getD2(), rel); break;
				case 3:  connect(layer.getD3(), rel); break;
				default: throw new JLayerException();
			}	
		}

		@Override
		public void connect(Layer.D1<?,T> layer, Relation rel){
			connectUtils.connect(this, layer, rel);
       	}
		
		@Override
		public void connect(Layer.D2<?,T> layer, Relation rel){
			connectUtils.connect(this, layer, rel);
		}
		
		@Override
		public void connect(Layer.D3<?,T> layer, Relation rel){
			connectUtils.connect(this, layer, rel);
		}
		
		// associating vector layers
		@Override
		public void associate(VectorLayer<?,T> layer, Relation rel){
			switch (layer.dims()) {
				case 1:  associate(layer.getD1(), rel); break;
				case 2:  associate(layer.getD2(), rel); break;
				case 3:  associate(layer.getD3(), rel); break;
				default: throw new JLayerException();
			}	
		}
		
		@Override
		public void associate(VectorLayer.D1<?,T> layer, Relation rel){
			assertUnequality(this, layer);
			associateUtils.associate(this, layer, rel);
		}
		
		@Override
		public void associate(VectorLayer.D2<?,T> layer, Relation rel){
			assertUnequality(this, layer);
			associateUtils.associate(this, layer, rel);
		}
		
		@Override
		public void associate(VectorLayer.D3<?,T> layer, Relation rel){
			assertUnequality(this, layer);
			associateUtils.associate(this, layer, rel);
		}
		
	}
	
// class VectorLayerBase
	
	// abstract items
	abstract protected T newObject();
	abstract protected T[] newArray(int len);
	
	// constructors
	
	public VectorLayerBase(T[][] arrayD1){ 
		super(arrayD1);
	}
	public VectorLayerBase(T[][][] arrayD2){ 
		super(arrayD2);
	}
	public VectorLayerBase(T[][][][] arrayD3){ 
		super(arrayD3);
	}
	
	public VectorLayerBase(BasedLayer.D1<T[]> layerD1){ 
		super(layerD1);
	}
	public VectorLayerBase(BasedLayer.D2<T[]> layerD2){ 
		super(layerD2);
	}
	public VectorLayerBase(BasedLayer.D3<T[]> layerD3){ 
		super(layerD3);
	}
	
	// connecting layers
	@Override
	public void connect(Layer<?,T> layer, Relation rel) { 
		switch (this.dimensionality) {
			case 1:  getD1().connect(layer, rel); break;
			case 2:  getD2().connect(layer, rel); break;
			case 3:  getD3().connect(layer, rel); break;
			default: throw new JLayerException();
		}
	}
	@Override
	public void connect(Layer.D1<?,T> layer, Relation rel) {
		switch (this.dimensionality) {
			case 1:  getD1().connect(layer, rel); break;
			case 2:  getD2().connect(layer, rel); break;
			case 3:  getD3().connect(layer, rel); break;
			default: throw new JLayerException();
		}
	}
	@Override
	public void connect(Layer.D2<?,T> layer, Relation rel) { 
		switch (this.dimensionality) {
			case 1:  getD1().connect(layer, rel); break;
			case 2:  getD2().connect(layer, rel); break;
			case 3:  getD3().connect(layer, rel); break;
			default: throw new JLayerException();
		}
	}
	@Override
	public void connect(Layer.D3<?,T> layer, Relation rel) { 
		switch (this.dimensionality) {
			case 1:  getD1().connect(layer, rel); break;
			case 2:  getD2().connect(layer, rel); break;
			case 3:  getD3().connect(layer, rel); break;
			default: throw new JLayerException();
		}
	}
	
	// associating layers
	@Override
	public void associate(VectorLayer<?,T> layer, Relation rel) { 
		switch (this.dimensionality) {
			case 1:  getD1().associate(layer, rel); break;
			case 2:  getD2().associate(layer, rel); break;
			case 3:  getD3().associate(layer, rel); break;
			default: throw new JLayerException();
		}
	}
	@Override
	public void associate(VectorLayer.D1<?,T> layer, Relation rel) { 
		switch (this.dimensionality) {
			case 1:  getD1().associate(layer, rel); break;
			case 2:  getD2().associate(layer, rel); break;
			case 3:  getD3().associate(layer, rel); break;
			default: throw new JLayerException();
		}		
	}
	@Override
	public void associate(VectorLayer.D2<?,T> layer, Relation rel) { 
		switch (this.dimensionality) {
			case 1:  getD1().associate(layer, rel); break;
			case 2:  getD2().associate(layer, rel); break;
			case 3:  getD3().associate(layer, rel); break;
			default: throw new JLayerException();
		}		
	}
	@Override
	public void associate(VectorLayer.D3<?,T> layer, Relation rel) { 
		switch (this.dimensionality) {
			case 1:  getD1().associate(layer, rel); break;
			case 2:  getD2().associate(layer, rel); break;
			case 3:  getD3().associate(layer, rel); break;
			default: throw new JLayerException();
		}		
	}
	
	// getting dimensioned layers
	@Override
	public D1<T> getD1(){
		if (this.dimensionality != 1) return null;
		D1<T> obj = new D1<T>(super.getD1().getBase()){
			@Override
			protected T newObject(){ return VectorLayerBase.this.newObject(); }
			@Override
			protected T[] newArray(int len) { return VectorLayerBase.this.newArray(len);}
		};
		obj.referenceItem = VectorLayerBase.this;
		return obj;
	}
	@Override
	public D2<T> getD2(){
		if (this.dimensionality != 2) return null;
		D2<T> obj = new D2<T>(super.getD2().getBase()){
			@Override
			protected T newObject(){ return VectorLayerBase.this.newObject(); }
			@Override
			protected T[] newArray(int len) { return VectorLayerBase.this.newArray(len);}
		};
		obj.referenceItem = VectorLayerBase.this;
		return obj;
	}
	@Override
	public D3<T> getD3(){
		if (this.dimensionality != 3) return null;
		D3<T> obj = new D3<T>(super.getD3().getBase()){
			@Override
			protected T newObject(){ return VectorLayerBase.this.newObject(); }
			@Override
			protected T[] newArray(int len) { return VectorLayerBase.this.newArray(len);}
		};
		obj.referenceItem = VectorLayerBase.this;
		return obj;
	}

}

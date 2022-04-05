package org.jlayer.util;

import java.util.Iterator;

import org.jlayer.model.VectorLayer;
import org.jlayer.model.Layer;
import org.jlayer.model.Relation;

public abstract class VectorLayerAdapter<U,T> extends LayerAdapter<U,T[]> implements VectorLayer<U,T> {
	
	private static void assertUnequality(Object layer1, Object layer2) {
		if (layer1.equals(layer2)) {
			throw new JLayerException("runtime exception raised by org.jlayer: identical layer must not be associated");
		}
	}
	
	// class VectorLayerAdapter.D1
	public abstract static class D1<U,T> extends LayerAdapter.D1<U,T[]> implements VectorLayer.D1<U,T> {
		
		// reference item for checking equality
		VectorLayerAdapter<U,T> referenceItem = null;
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean equals(Object obj) {
			if (obj instanceof D1<?,?>) {
				return D1.this.refEquals((D1<?,T>)obj);
			} else {
				return super.equals(obj);
			}
		}
		public boolean refEquals(D1<?,T> obj){
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
	
	
	// class VectorLayerAdapter.D2
	public abstract static class D2<U,T> extends LayerAdapter.D2<U,T[]> implements VectorLayer.D2<U,T> {
		
		// reference item for checking equality
		VectorLayerAdapter<U,T> referenceItem = null;
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean equals(Object obj) {
			if (obj instanceof D2<?,?>) {
				return D2.this.refEquals((D2<?,T>)obj);
			} else {
				return super.equals(obj);
			}
		}
		public boolean refEquals(D2<?,T> obj){
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
	
	// class VectorLayerAdapter.D3
	public abstract static class D3<U,T> extends LayerAdapter.D3<U,T[]> implements VectorLayer.D3<U,T> {
		
		// reference item for checking equality
		VectorLayerAdapter<U,T> referenceItem = null;
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean equals(Object obj) {
			if (obj instanceof D3<?,?>) {
				return D3.this.refEquals((D3<?,T>)obj);
			} else {
				return super.equals(obj);
			}
		}
		public boolean refEquals(D3<?,T> obj){
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
	
// class VectorLayerAdapter
	
	// abstract items
	abstract protected T newObject();
	abstract protected T[] newArray(int len);
	
	// connecting layers
	@Override
	public void connect(Layer<?,T> layer, Relation rel) { 
		switch (this.dims()) {
			case 1:  getD1().connect(layer, rel); break;
			case 2:  getD2().connect(layer, rel); break;
			case 3:  getD3().connect(layer, rel); break;
			default: throw new JLayerException();
		}
	}
	@Override
	public void connect(Layer.D1<?,T> layer, Relation rel) { 
		switch (this.dims()) {
			case 1:  getD1().connect(layer, rel); break;
			case 2:  getD2().connect(layer, rel); break;
			case 3:  getD3().connect(layer, rel); break;
			default: throw new JLayerException();
		}
	}
	@Override
	public void connect(Layer.D2<?,T> layer, Relation rel) { 
		switch (this.dims()) {
			case 1:  getD1().connect(layer, rel); break;
			case 2:  getD2().connect(layer, rel); break;
			case 3:  getD3().connect(layer, rel); break;
			default: throw new JLayerException();
		}
	}
	@Override
	public void connect(Layer.D3<?,T> layer, Relation rel) { 
		switch (this.dims()) {
			case 1:  getD1().connect(layer, rel); break;
			case 2:  getD2().connect(layer, rel); break;
			case 3:  getD3().connect(layer, rel); break;
			default: throw new JLayerException();
		}
	}
	
	// associating vector layers
	@Override
	public void associate(VectorLayer<?,T> layer, Relation rel) {
		switch (this.dims()) {
			case 1:	getD1().associate(layer, rel); break;
			case 2: getD2().associate(layer, rel); break;
			case 3: getD3().associate(layer, rel); break;
			default: throw new JLayerException();
		}	
	}
	@Override
	public void associate(VectorLayer.D1<?,T> layer, Relation rel) { 
		switch (this.dims()) {
			case 1:  getD1().associate(layer, rel); break;
			case 2:  getD2().associate(layer, rel); break;
			case 3:  getD3().associate(layer, rel); break;
			default: throw new JLayerException();
		}	
	}
	@Override
	public void associate(VectorLayer.D2<?,T> layer, Relation rel) { 
		switch (this.dims()) {
			case 1:	getD1().associate(layer, rel); break;
			case 2: getD2().associate(layer, rel); break;
			case 3: getD3().associate(layer, rel); break;
			default: throw new JLayerException();
		}	
	}
	@Override
	public void associate(VectorLayer.D3<?,T> layer, Relation rel) { 
		switch (this.dims()) {
			case 1:	getD1().associate(layer, rel); break;
			case 2: getD2().associate(layer, rel); break;
			case 3: getD3().associate(layer, rel); break;
			default: throw new JLayerException();
		}	
	}
		
	// getting dimensioned layers
	@Override
	public D1<U,T>  getD1(){
		if (this.dims() != 1) return null;
		D1<U,T> obj = new D1<U,T>(){
			@Override
			public int length() { return VectorLayerAdapter.this.length(); }
			@Override
			public T[] get(int x1) { return VectorLayerAdapter.this.get(x1); }
			@Override
			public void set(int x1, T[] elem ){ VectorLayerAdapter.this.set(x1, elem); }
			@Override
			public U getUnit(int x1) { return VectorLayerAdapter.this.getUnit(x1); }
			@Override
			public void setUnit(int x1, U unit ){ VectorLayerAdapter.this.setUnit(x1, unit); }
			@Override
			public Iterator<T[]> iterator() { return VectorLayerAdapter.this.iterator(); }
			@Override
			protected T newObject(){ return VectorLayerAdapter.this.newObject(); }
			@Override
			protected T[] newArray(int len) { return VectorLayerAdapter.this.newArray(len);}
		};
		obj.referenceItem = VectorLayerAdapter.this;
		return obj;
	}
	@Override
	public D2<U,T>  getD2(){
		if (this.dims() != 2) return null;
		D2<U,T> obj = new D2<U,T>(){
			@Override
			public int length() { return VectorLayerAdapter.this.length(); }
			@Override
			public int length(int x1) { return VectorLayerAdapter.this.length(x1); }
			@Override
			public T[] get(int x1, int x2) { return VectorLayerAdapter.this.get(x1, x2); }
			@Override
			public void set(int x1, int x2, T[] elem ){ VectorLayerAdapter.this.set(x1, x2, elem); }
			@Override
			public U getUnit(int x1, int x2) { return VectorLayerAdapter.this.getUnit(x1, x2); }
			@Override
			public void setUnit(int x1, int x2, U unit){ VectorLayerAdapter.this.setUnit(x1, x2, unit); }
			@Override
			public Iterator<T[]> iterator() { return VectorLayerAdapter.this.iterator(); }
			@Override
			protected T newObject(){ return VectorLayerAdapter.this.newObject(); }
			@Override
			protected T[] newArray(int len) { return VectorLayerAdapter.this.newArray(len);}
		};
		obj.referenceItem = VectorLayerAdapter.this;
		return obj;
	}
	@Override
	public D3<U,T>  getD3(){
		if (this.dims() != 3) return null;
		D3<U,T> obj = new D3<U,T>(){
			@Override
			public int length() { return VectorLayerAdapter.this.length(); }
			@Override
			public int length(int x1) { return VectorLayerAdapter.this.length(x1); }
			@Override
			public int length(int x1, int x2) { return VectorLayerAdapter.this.length(x1, x2); }
			@Override
			public T[] get(int x1, int x2, int x3) { return VectorLayerAdapter.this.get(x1, x2, x3); }
			@Override
			public void set(int x1, int x2, int x3, T[] elem ){ VectorLayerAdapter.this.set(x1, x2, x3, elem); }
			@Override
			public U getUnit(int x1, int x2, int x3) { return VectorLayerAdapter.this.getUnit(x1, x2, x3); }
			@Override
			public void setUnit(int x1, int x2, int x3, U unit){ VectorLayerAdapter.this.setUnit(x1, x2, x3, unit); }
			@Override
			public Iterator<T[]> iterator() { return VectorLayerAdapter.this.iterator(); }
			@Override
			protected T newObject(){ return VectorLayerAdapter.this.newObject(); }
			@Override
			protected T[] newArray(int len) { return VectorLayerAdapter.this.newArray(len);}
		};
		obj.referenceItem = VectorLayerAdapter.this;
		return obj;
	}

}

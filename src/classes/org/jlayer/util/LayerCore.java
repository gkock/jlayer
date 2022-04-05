package org.jlayer.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jlayer.model.Layer;
import org.jlayer.model.XIterator;

abstract public class LayerCore<U,T> implements Layer<U,T> {
		
	// class LayerCore.D1
	abstract public static class D1<U,T> implements Layer.D1<U,T> {
		
		// class LayerCore.D1.DIterator
		private class DIterator extends IndexCore.D1 implements Iterator<T> {
			
			int x1ubp = D1.this.length();
			
			// abstract items
			@Override
			protected int getLength() {
				return x1ubp;
			}
			@Override
			protected boolean validIndex(int x1) {
				return (D1.this.getUnit(x1) != null) && (D1.this.get(x1) != null);
			}
			
			DIterator(){
				if ( !setNextX1(0) ) {
					x1 = x1ubp;
				} 
			}
			
			@Override
			public boolean hasNext(){
				return (x1 < x1ubp);
			}
			@Override
			public T next() {
				if (!hasNext()){
					throw new NoSuchElementException("class LayerCore.D1<U,T>.DIterator.next()U");
				}
				T retVal = D1.this.get(x1);
				if ( !setNextX1(x1 + 1) ) {
					x1 = x1ubp;
				} 
				return retVal;
			}
			@Override
			public void remove(){
				throw new UnsupportedOperationException("class LayerCore.D1<U,T>.DIterator.remove()void");
			}
			
		}
		@Override
		public Iterator<T> iterator(){ return new DIterator();	}
		
		// class LayerCore.D1.DXIterator
		private class DXIterator extends DIterator implements XIterator.D1<T>{
			public DXIterator() { 
				super();
			}
			public DXIterator(int x1low, int x1upb) {
				this.x1 = x1low;
				this.x1ubp = x1upb;
				if ( !setNextX1(x1low) ) {
					x1 = x1ubp;
				} 
			}
			@Override
			public int getX1() { return x1;	}
		}
		@Override
		public XIterator.D1<T> xIterator(){ return new DXIterator(); }
		@Override
		public XIterator.D1<T> xIterator(int x1low, int x1upb){ 
			if ( (x1low < 0) || (x1low > x1upb) || (x1upb > D1.this.length())) {
				throw new JLayerException();
			}
			return new DXIterator(x1low, x1upb); 
		}
		
	}
	
	// class LayerCore.D2
	abstract public static class D2<U,T> implements Layer.D2<U,T> {
		
		// class LayerCore.D2.DIterator
		private class DIterator extends IndexCore.D2 implements Iterator<T> {
			
			int x1ubp = D2.this.length();
			
			// abstract items
			@Override
			protected int getLength() {
				return x1ubp;
			}
			@Override
			protected int getLength(int x1) {
				return D2.this.length(x1);
			}
			@Override
			protected boolean validIndex(int x1, int x2) {
				return (D2.this.getUnit(x1, x2) != null) && (D2.this.get(x1, x2) != null);
			}
			
			DIterator(){ 
				if ( !setNextX2(0) && !setNextX12(0) ) {
					x1 = x1ubp;
				}
			}
			
			@Override
			public boolean hasNext(){
				return (x1 <x1ubp);
			}
			@Override
			public T next() {
				if (!hasNext()){
					throw new NoSuchElementException("class LayerCore.D2<U,T>.DIterator.next()U");
				}
				T retVal = D2.this.get(x1, x2);
				if ( !setNextX2(x2 + 1) && !setNextX12(x1 + 1) ) {
					x1 = x1ubp;
				} 
				return retVal;
			}
			@Override
			public void remove(){
				throw new UnsupportedOperationException("class LayerCore.D2<U,T>.DIterator.remove()void");
			}

		}
		@Override
		public Iterator<T> iterator(){ return new DIterator();	}
		
		// class LayerCore.D2.DXIterator
		private class DXIterator extends DIterator implements XIterator.D2<T>{
			public DXIterator() { 
				super();
			}
			public DXIterator(int x1low, int x1upb) {
				this.x1 = x1low;
				this.x1ubp = x1upb;
				if ( !setNextX12(x1low) ) {
					x1 = x1ubp;
				}
			}
			@Override
			public int getX1() { return x1;	}
			@Override
			public int getX2() { return x2;	}
		}
		@Override
		public XIterator.D2<T> xIterator(){ return new DXIterator(); }
		@Override
		public XIterator.D2<T> xIterator(int x1low, int x1upb){ 
			if ( (x1low < 0) || (x1low > x1upb) || (x1upb > D2.this.length())) {
				throw new JLayerException();
			}
			return new DXIterator(x1low, x1upb); 
		}

	}
	
	// class LayerCore.D3
	abstract public static class D3<U,T> implements Layer.D3<U,T> {
		
		// class LayerCore.D3.DIterator
		private class DIterator extends IndexCore.D3 implements Iterator<T> {
			
			int x1upb = D3.this.length();
			
			// abstract items
			@Override
			protected int getLength() {
				return x1upb;
			}
			@Override
			protected int getLength(int x1) {
				return  D3.this.length(x1);
			}
			@Override
			protected int getLength(int x1, int x2) {
				return D3.this.length(x1 , x2);
			}
			@Override
			protected boolean validIndex(int x1, int x2, int x3) {
				return (D3.this.getUnit(x1, x2, x3) != null) && (D3.this.get(x1, x2, x3) != null);
			}
			
			DIterator(){ 
				if ( !setNextX3(0) && !setNextX23(0) && !setNextX123(0) ) {
					x1 = x1upb;
				}
			}
			
			@Override
			public boolean hasNext(){
				return (x1 < x1upb);
			}
			@Override
			public T next() {
				if (!hasNext()){
					throw new NoSuchElementException("class LayerCore.D3<U,T>.DIterator.next()U");
				}
				T retVal = D3.this.get(x1, x2, x3);
				if ( !setNextX3(x3 + 1) && !setNextX23(x2 + 1) && !setNextX123(x1 + 1) ) {
					x1 = x1upb;
				} 
				return retVal;
			}
			@Override
			public void remove(){
				throw new UnsupportedOperationException("class LayerCore.D3<U,T>.DIterator.remove()void");
			}

		}
		@Override
		public Iterator<T> iterator(){ return new DIterator();	}
		
		// class LayerCore.D3.DXIterator
		private class DXIterator extends DIterator implements XIterator.D3<T>{
			public DXIterator() { 
				super();
			}
			public DXIterator(int x1low, int x1upb) {
				this.x1 = x1low;
				this.x1upb = x1upb;
				if ( !setNextX123(x1low) ) {
					x1 = x1upb;
				}
			}
			@Override
			public int getX1() { return x1;	}
			@Override
			public int getX2() { return x2;	}
			@Override
			public int getX3() { return x3;	}
		}
		@Override
		public XIterator.D3<T> xIterator(){ return new DXIterator(); }
		@Override
		public XIterator.D3<T> xIterator(int x1low, int x1upb){ 
			if ( (x1low < 0) || (x1low > x1upb) || (x1upb > D3.this.length())) {
				throw new JLayerException();
			}
			return new DXIterator(x1low, x1upb); 
		}
		
	}
	
// class LayerCore
	
	// iterator()
	@Override
	public Iterator<T> iterator(){
		switch (this.dims()) {
			case 1:  return getD1().iterator();
			case 2:  return getD2().iterator();
			case 3:  return getD3().iterator();
			default: throw new JLayerException();
		}
	}
	
	// class LayerCore.DXIterator
	private class DXIterator implements XIterator<T>{
		
		XIterator.D1<T> xIterD1;
		XIterator.D2<T> xIterD2;
		XIterator.D3<T> xIterD3;
		int dim;
		
		DXIterator(XIterator.D1<T> xIterD1) {
			this.xIterD1 = xIterD1;
			this.dim = 1;
		}
		DXIterator(XIterator.D2<T> xIterD2) {
			this.xIterD2 = xIterD2;
			this.dim = 2;
		}
		DXIterator(XIterator.D3<T> xIterD3) {
			this.xIterD3 = xIterD3;
			this.dim = 3;
		}
		
		@Override
		public boolean hasNext(){
			switch (dim) {
				case 1: return xIterD1.hasNext();
				case 2: return xIterD2.hasNext();
				case 3: return xIterD3.hasNext();
				default: throw new JLayerException();
			}
		}
		@Override
		public T next() {
			switch (dim) {
				case 1: return xIterD1.next();
				case 2: return xIterD2.next();
				case 3: return xIterD3.next();
				default: throw new JLayerException();
			}
		}
		@Override
		public void remove(){
			switch (dim) {
				case 1: xIterD1.remove(); break;
				case 2: xIterD2.remove(); break;
				case 3: xIterD3.remove(); break;
				default: throw new JLayerException();
			}		
		}
		@Override
		public int[] getXX() {
			int[] xx;
			switch (dim) {
				case 1: xx = new int[]{xIterD1.getX1()}; break;
				case 2: xx = new int[]{xIterD2.getX1(), xIterD2.getX2()}; break;
				case 3: xx = new int[]{xIterD3.getX1(), xIterD3.getX2(), xIterD3.getX3()}; break;
				default: throw new JLayerException();
			}
			return xx;
		}
	}
	
	// xIterator()
	@Override
	public XIterator<T> xIterator(){
		switch (this.dims()) {
			case 1:  return new DXIterator(getD1().xIterator());
			case 2:  return new DXIterator(getD2().xIterator());
			case 3:  return new DXIterator(getD3().xIterator());
			default: throw new JLayerException();
		}
	}
	@Override
	public XIterator<T> xIterator(int x1low, int x1ubp){
		switch (this.dims()) {
			case 1:  return new DXIterator(getD1().xIterator(x1low, x1ubp));
			case 2:  return new DXIterator(getD2().xIterator(x1low, x1ubp));
			case 3:  return new DXIterator(getD3().xIterator(x1low, x1ubp));
			default: throw new JLayerException();
		}
	}
	
}

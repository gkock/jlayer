package org.jlayer.util;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.jlayer.model.Layer;

/**
*
* The inner classes can be used as generators for layer indices, 
* and if the elements are cloned, they can be used as elements of collections.
* @author Gerd Kock
*
*/
class LayerIndex {
	
	// class LayerIndex.D1
	static class D1 extends IndexCore.D1 implements Comparable<LayerIndex.D1> {
		
		// abstract items
		@Override
		protected int getLength(){ 
			return layerD1.length();
		}
		@Override
		protected boolean validIndex(int x1) {
			return (!layerD1.isAdapter() || layerD1.getUnit(x1) != null);
		}
		
		// Comparable interface
		@Override
		public int compareTo(LayerIndex.D1 layerIndex){
			if (x1 < layerIndex.x1) {
				return -1;
			} else if (x1 > layerIndex.x1) {
				return 1;
			} else {
				return 0;
			}
		}
		
		@Override
		public boolean equals(Object obj) {
	        if (!(obj instanceof LayerIndex.D1))
	            return false;
	        LayerIndex.D1 ix = (LayerIndex.D1) obj;
	        return ( compareTo(ix) == 0 );
	    }

		@Override
	    public int hashCode() {
	        return x1;
	    }

		@Override
	    public String toString() {
	    	return "(" + String.valueOf(x1) + ")";
	    }
		
		@Override
		public D1 clone() {
			return new D1(this);
		}
		
		private D1(LayerIndex.D1 layerIndex) {
			this.layerD1 = layerIndex.layerD1;
			x1 = layerIndex.x1; 
		}
		
		// further items
		
		Layer.D1<?,?> layerD1;
		
		D1(Layer.D1<?,?> layerD1) {
			this.layerD1 = layerD1;
			x1 = 0;
			if ( !setNextX1(0) ) {
				x1 = layerD1.length();
			} 
		}
		
		void reset() {
			x1 = 0;
			if ( !setNextX1(0) ) {
				x1 = layerD1.length();
			} 
		}
		
		int[] getFrame(){
			return new int[layerD1.length()];
		}
		int getX1() { return x1; }
		int[] getIX() { return new int[]{x1}; }
		
		boolean hasNext(){
			return (x1 < layerD1.length());
		}
		void next() {
			if (!hasNext()){
				throw new NoSuchElementException("class XGenerator.D1.next()");
			}
			if ( !setNextX1(x1 + 1) ) {
				x1 = layerD1.length();
			} 
		}
		
	}
	
	static List<LayerIndex.D1> generateLayerIndexList(Layer.D1<?,?> layerD1) {
		List<LayerIndex.D1> ixList = new ArrayList<LayerIndex.D1>();
		for (LayerIndex.D1 xGen = new LayerIndex.D1(layerD1); xGen.hasNext(); xGen.next()){
			ixList.add(xGen.clone());
		}
		return ixList;
	}
	
	// class LayerIndex.D2
	static class D2 extends IndexCore.D2 implements Comparable<LayerIndex.D2> {
		
		// abstract items
		@Override
		protected int getLength() {
			return layerD2.length();
		}
		@Override
		protected int getLength(int x1) {
			return layerD2.length(x1);
		}
		@Override
		protected boolean validIndex(int x1, int x2) {
			return (!layerD2.isAdapter() || layerD2.getUnit(x1, x2) != null);
		}
		
		// Comparable interface
		@Override
		public int compareTo(LayerIndex.D2 layerIndex){
			if (x1 < layerIndex.x1) {
				return -1;
			} else if (x1 > layerIndex.x1) {
				return 1;
			} else if (x2 < layerIndex.x2) {
				return -1;
			} else if (x2 > layerIndex.x2) {
				return 1;
			} else {
				return 0;
			}
		}
		
		@Override
		public boolean equals(Object obj) {
	        if (!(obj instanceof LayerIndex.D2))
	            return false;
	        LayerIndex.D2 ix = (LayerIndex.D2) obj;
	        return ( compareTo(ix) == 0 );
	    }

		@Override
	    public int hashCode() {
	        return x1 * 31 + x2;
	    }

		@Override
	    public String toString() {
	    	return "(" + String.valueOf(x1) + ", " + String.valueOf(x2) + ")";
	    }
		
		@Override
		public D2 clone() {
			return new D2(this);
		}
		
		private D2(LayerIndex.D2 layerIndex) {
			this.layerD2 = layerIndex.layerD2;
			x1 = layerIndex.x1; 
			x2 = layerIndex.x2; 
		}
	    
	    // further items
		
		Layer.D2<?,?> layerD2;
		
		D2(Layer.D2<?,?> layerD2) {
			this.layerD2 = layerD2;
			x1 = 0; x2 = 0;
			if ( !setNextX2(0) && !setNextX12(0) ) {
				x1 = layerD2.length();
			}
		}
		
		void reset() {
			x1 = 0; x2 = 0;
			if ( !setNextX2(0) && !setNextX12(0) ) {
				x1 = layerD2.length();
			}
		}
		
		int[][] getFrame(){
			int[][] frame = new int[layerD2.length()][];
			for (int x1 = 0; x1 < layerD2.length(); x1++){
				frame[x1] = new int[layerD2.length(x1)];
			}
			return frame;
		}
		int getX1() { return x1; }
		int getX2() { return x2; }
		int[] getIX() { return new int[]{x1, x2}; }
		
		boolean hasNext(){
			return (x1 < layerD2.length());
		}
		void next(){
			if (!hasNext()){
				throw new NoSuchElementException("class LayerIndex.D2.next()");
			}
			if ( !setNextX2(x2 + 1) && !setNextX12(x1 + 1) ) {
				x1 = layerD2.length();
			} 
		}
		
	}
	
	static List<LayerIndex.D2> generateLayerIndexList(Layer.D2<?,?> layerD2) {
		List<LayerIndex.D2> ixList = new ArrayList<LayerIndex.D2>();
		for (LayerIndex.D2 xGen = new LayerIndex.D2(layerD2); xGen.hasNext(); xGen.next()){
			ixList.add(xGen.clone());
		}
		return ixList;
	}
	
	
	// class LayerIndex.D3
	static class D3 extends IndexCore.D3 implements Comparable<LayerIndex.D3> {
		
		// abstract items
		@Override
		protected int getLength() {
			return layerD3.length();
		}
		@Override
		protected int getLength(int x1) {
			return layerD3.length(x1);
		}
		@Override
		protected int getLength(int x1, int x2) {
			return layerD3.length(x1, x2);
		}
		@Override
		protected boolean validIndex(int x1, int x2, int x3) {
			return (!layerD3.isAdapter() || layerD3.getUnit(x1, x2, x3) != null);
		}
		
		// Comparable interface
		@Override
		public int compareTo(LayerIndex.D3 layerIndex){
			if (x1 < layerIndex.x1) {
				return -1;
			} else if (x1 > layerIndex.x1) {
				return 1;
			} else if (x2 < layerIndex.x2) {
				return -1;
			} else if (x2 > layerIndex.x2) {
				return 1;
			} else if (x3 < layerIndex.x3) {
				return -1;
			} else if (x3 > layerIndex.x3) {
				return 1;
			} else {
				return 0;
			}
		}
		
		@Override
		public boolean equals(Object obj) {
	        if (!(obj instanceof LayerIndex.D3))
	            return false;
	        LayerIndex.D3 ix = (LayerIndex.D3) obj;
	        return ( compareTo(ix) == 0 );
	    }

		@Override
	    public int hashCode() {
	        return x1 * 31 * 31 + x2 * 31 + x3;
	    }

		@Override
	    public String toString() {
	    	return "(" + String.valueOf(x1) + ", " + String.valueOf(x2) + ", " + String.valueOf(x3) + ")";
	    }
		
		@Override
		public D3 clone() {
			return new D3(this);
		}
		
		private D3(LayerIndex.D3 layerIndex) {
			this.layerD3 = layerIndex.layerD3;
			x1 = layerIndex.x1; 
			x2 = layerIndex.x2; 
			x3 = layerIndex.x3; 
		}
		
		// further items
		
		Layer.D3<?,?> layerD3;
		
		D3(Layer.D3<?,?> layerD3) {
			this.layerD3 = layerD3;
			x1 = 0; x2 = 0; x3 = 0;
			if ( !setNextX3(0) && !setNextX23(0) && !setNextX123(0) ) {
				x1 = layerD3.length();
			}
		}
		
		void reset() {
			x1 = 0; x2 = 0; x3 = 0;
			if ( !setNextX3(0) && !setNextX23(0) && !setNextX123(0) ) {
				x1 = layerD3.length();
			}
		}
		
		int[][][] getFrame(){
			int[][][] frame = new int[layerD3.length()][][];
			for (int x1 = 0; x1 < layerD3.length(); x1++){
				frame[x1] = new int[layerD3.length(x1)][];
		   		for (int x2 = 0; x2 < layerD3.length(x1); x2++){
		   			frame[x1][x2] = new int[layerD3.length(x1, x2)];
		   		}
			}
			return frame;
		}
		int getX1() { return x1; }
		int getX2() { return x2; }
		int getX3() { return x3; }
		int[] getIX() { return new int[]{x1, x2, x3}; }
		
		boolean hasNext(){
			return (x1 < layerD3.length());
		}
		void next(){
			if (!hasNext()){
				throw new NoSuchElementException("class LayerIndex.D3.next()");
			}
			if ( !setNextX3(x3 + 1) && !setNextX23(x2 + 1) && !setNextX123(x1 + 1) ) {
				x1 = layerD3.length();
			} 
		}
		
	}
	
	static List<LayerIndex.D3> generateLayerIndexList(Layer.D3<?,?> layerD3) {
		List<LayerIndex.D3> ixList = new ArrayList<LayerIndex.D3>();
		for (LayerIndex.D3 xGen = new LayerIndex.D3(layerD3); xGen.hasNext(); xGen.next()){
			ixList.add(xGen.clone());
		}
		return ixList;
	}
	
// class LayerIndex

}

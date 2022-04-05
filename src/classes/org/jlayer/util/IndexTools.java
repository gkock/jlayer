package org.jlayer.util;

import org.jlayer.model.Relation;

/**
 * This class is used for test purposes only.
 * 
 * @author gko
 *
 */
public class IndexTools {
	
	// orientation for indices of kind (x, y)
	public static enum Orientation {
		N, S, E, W, NE, NW, SE, SW, NNE, NEE, NNW, NWW, SSE, SEE, SSW, SWW
	}
	
	/**
	 * 
	 * @param from the base index
	 * @param to the goal index
	 * @return the {@link Orientation} or null, iff from and to denote identical indices
	 */
    public static Orientation getOrientation(int[] from, int[] to) {
    	Orientation dir = null;
    	
    	int delta0 = to[0] - from[0];
    	int delta1 = to[1] - from[1];
    	
    	if ( (delta0 > 0) && (delta1 > 0) ) {
    		if (delta0 > delta1) {
    			dir = Orientation.SSE;
    		} else if (delta0 < delta1) {
    			dir = Orientation.SEE;
    		} else {
    			dir = Orientation.SE;
    		}
    		
    	} else if ( (delta0 > 0) && (delta1 == 0) ) {
    		return Orientation.S;
    		
    	} else if ( (delta0 > 0) && (delta1 < 0) ) {
    		if (delta0 > -delta1) {
    			dir = Orientation.SSW;
    		} else if (delta0 < -delta1) {
    			dir = Orientation.SWW;
    		} else {
    			dir = Orientation.SW;
    		}
    		
    	} else if ( (delta0 == 0) && (delta1 > 0) ) {
    		dir = Orientation.E;
    		
    	} else if ( (delta0 == 0) && (delta1 == 0) ) {
    		dir = null;
    		
    	} else if ( (delta0 == 0) && (delta1 < 0) ) {
    		dir = Orientation.W;
    		
    	} else if ( (delta0 < 0) && (delta1 > 0) ) {
    		if (-delta0 > delta1) {
    			dir = Orientation.NNE;
    		} else if (-delta0 < delta1) {
    			dir = Orientation.NEE;
    		} else {
    			dir = Orientation.NE;
    		}
    		
    	} else if ( (delta0 < 0) && (delta1 == 0) ) {
    		dir = Orientation.N;
    		
    	} else if ( (delta0 < 0) && (delta1 < 0) ) {
    		if (-delta0 > -delta1) {
    			dir = Orientation.NNW;
    		} else if (-delta0 < -delta1) {
    			dir = Orientation.NWW;
    		} else {
    			dir = Orientation.NW;
    		}
    	}
    	  	
    	return dir;
    }
	
	public static abstract class D1D1 implements Relation {
		
		public abstract boolean contains(int x, int y);
		
		@Override
		public boolean contains(int[] x, int[] y) {
			if ( (x.length == 1) && (y.length == 1) ) {
				return contains(x[0], y[0]);
			} else {
				return false;
			}
		}

	}
	
	public static abstract class D1D2 implements Relation {
		
		public abstract boolean contains(int x1, int y1, int y2);
		
		@Override
		public boolean contains(int[] x, int[] y) {
			if ( (x.length == 1) && (y.length == 2) ) {
				return contains(x[0], y[0], y[1]);
			} else {
				return false;
			}
		}

	}
	
	public static abstract class D1D3 implements Relation {
		
		public abstract boolean contains(int x1, int y1, int y2, int y3);
		
		@Override
		public boolean contains(int[] x, int[] y) {
			if ( (x.length == 1) && (y.length == 3) ) {
				return contains(x[0], y[0], y[1], y[2]);
			} else {
				return false;
			}
		}

	}

	public static abstract class D2D1 implements Relation {
		
		public abstract boolean contains(int x1, int x2, int y1);
		
		@Override
		public boolean contains(int[] x, int[] y) {
			if ( (x.length == 2) && (y.length == 1) ) {
				return contains(x[0], x[1], y[0]);
			} else {
				return false;
			}
		}

	}
	
	public static abstract class D2D2 implements Relation {
		
		public abstract boolean contains(int x1, int x2, int y1, int y2);
		
		@Override
		public boolean contains(int[] x, int[] y) {
			if ( (x.length == 2) && (y.length == 2) ) {
				return contains(x[0], x[1], y[0], y[1]);
			} else {
				return false;
			}
		}

	}
	
	public static abstract class D2D3 implements Relation {
		
		public abstract boolean contains(int x1, int x2, int y1, int y2, int y3);
		
		@Override
		public boolean contains(int[] x, int[] y) {
			if ( (x.length == 2) && (y.length == 3) ) {
				return contains(x[0], x[1], y[0], y[1], y[2]);
			} else {
				return false;
			}
		}

	}
	
	public static abstract class D3D1 implements Relation {
		
		public abstract boolean contains(int x1, int x2, int x3, int y1);
		
		@Override
		public boolean contains(int[] x, int[] y) {
			if ( (x.length == 3) && (y.length == 1) ) {
				return contains(x[0], x[1], x[2], y[0]);
			} else {
				return false;
			}
		}

	}
	
	public static abstract class D3D2 implements Relation {
		
		public abstract boolean contains(int x1, int x2, int x3, int y1, int y2);
		
		@Override
		public boolean contains(int[] x, int[] y) {
			if ( (x.length == 3) && (y.length == 2) ) {
				return contains(x[0], x[1], x[2], y[0], y[1]);
			} else {
				return false;
			}
		}

	}
	
	public static abstract class D3D3 implements Relation {
		
		public abstract boolean contains(int x1, int x2, int x3, int y1, int y2, int y3);
		
		@Override
		public boolean contains(int[] x, int[] y) {
			if ( (x.length == 3) && (y.length == 3) ) {
				return contains(x[0], x[1], x[2], y[0], y[1], y[2]);
			} else {
				return false;
			}
		}

	}
	
	public static class Transpose implements Relation {

		Relation rel;
		public Transpose(Relation rel){
			this.rel = rel;
		}
		@Override
		public boolean contains(int[] x, int[] y) {
			return rel.contains(y, x);
		}

	}
	
	public static Relation fullD1D1 = new D1D1(){
        @Override
		public boolean contains(int x1, int y1){ 
            return true;
        }
	};
	
	public static Relation isNeighbour = new D2D2(){
        @Override
		public boolean contains(int x1, int x2, int y1, int y2){ 
            if ( !( (x1==y1) && (x2==y2) ) &&
                 (Math.abs(x1-y1) <= 1) && (Math.abs(x2-y2) <= 1) )
                return true;
            else
                return false;
        }
	};
	
}


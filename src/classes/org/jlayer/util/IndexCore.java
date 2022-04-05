package org.jlayer.util;

/**
 *
 * This class establishes three nested classes being used as core elements for index generators for indices of dimension 1, 2 or 3.
 * @author Gerd Kock
 *
 */
abstract class IndexCore {
	
	// class IndexCore.D1
	/**
	 * 
	 * The core of an index generator for indices of dimension 1.
	 * @author Gerd Kock
	 *
	 */
	abstract static class D1 {
		
		// abstract items
		protected abstract int getLength();
		protected abstract boolean validIndex(int x1);
		
		protected int x1 = 0;
		
		// sets x1 to next valid position - iff possible
		protected boolean setNextX1(int initial_x1){
			for (int i = initial_x1; i < getLength(); i++){
				if (!validIndex(i)) continue;
				x1 = i; return true;
			}
			return false;
		}
		
	}
	
	// class IndexCore.D2
	/**
	 * 
	 * The core of an index generator for indices of dimension 2.
	 * @author Gerd Kock
	 *
	 */
	abstract static class D2 {
		
		// abstract items
		protected abstract int getLength();
		protected abstract int getLength(int x1);
		protected abstract boolean validIndex(int x1, int x2);
		
		protected int x1 = 0, x2 = 0;
		
		// sets x2 to next valid position - iff possible
		protected boolean setNextX2(int initial_x2){
			for (int j = initial_x2; j < getLength(x1); j++){
				if (!validIndex(x1, j)) continue;
				x2 = j; return true;
			}
			return false;
		}
		// sets x1, x2 to next valid positions - iff possible
		protected boolean setNextX12(int initial_x1){
			for (int i = initial_x1; i < getLength(); i++){
				for (int j = 0; j < getLength(i); j++){
					if (!validIndex(i, j)) continue;
					x1 = i; x2 = j; return true;
				}
			}
			return false;
		}
		
	}
	
	
	// class IndexCore.D3
	/**
	 * 
	 * The core of an index generator for indices of dimension 3.
	 * @author Gerd Kock
	 *
	 */
	abstract static class D3 {
		
		// abstract items
		protected abstract int getLength();
		protected abstract int getLength(int x1);
		protected abstract int getLength(int x1, int x2);
		protected abstract boolean validIndex(int x1, int x2, int x3);
		
		protected int x1 = 0, x2 = 0, x3 = 0;
		
		// sets x3 to next valid position - iff possible
		protected boolean setNextX3(int initial_x3){
			for (int k = initial_x3; k < getLength(x1 , x2); k++){
				if (!validIndex(x1, x2, k)) continue;
					x3 = k; return true;
			}
			return false;
		}
		// sets x2, x3 to next valid positions - iff possible
		protected boolean setNextX23(int initial_x2){
			for (int j = initial_x2; j < getLength(x1); j++){
				for (int k = 0; k <  getLength(x1, j); k++){
					if (!validIndex(x1, j, k)) continue;
						x2 = j; x3 = k; return true;
				}
			}
			return false;
		}
		// sets x1, x2, x3 to next valid positions - iff possible
		protected boolean setNextX123(int initial_x1){
			for (int i = initial_x1; i < getLength(); i++){
				for (int j = 0; j < getLength(i); j++){
					for (int k = 0; k < getLength(i, j); k++){
						if (!validIndex(i, j, k)) continue;
							x1 = i; x2 = j; x3 = k; return true;
					}
				}
			}
			return false;
		}
		
	}
	
// class IndexCore

}

package org.jlayer.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Random;

import org.jlayer.model.*;

import basic.units.*;

/**
 * Tests for {@code VectorLayer*.D2.associate(VectorLayer.D3<?,T> layer, Relation rel)}. 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Associate1_D2D3_Test {
	
//	final int sz1 = 10;
	final int sz2 = 4;
	final int sz3 = 3;
	
	final int[][]   val16_1 = new int[][]{ {0,1,2,3}, {4,5,6,7}, {8,9,10,11}, {12,13,14,15} };
	final int[][][] val27_1 = new int[][][]{ { {0,1,2}, {3,4,5}, {6,7,8} }, { {9,10,11}, {12,13,14}, {15,16,17} }, { {18,19,20}, {21,22,23}, {24,25,26} } };
	final int[]     val27_2 = new int[]{ 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26 };
	
	boolean[][][][][] random1 = new boolean[sz2][sz2][sz3][sz3][sz3];
	int               trueno1 = 0;
	
	C_Associate1_D2D3_Test() {
		Random generator = new Random(System.nanoTime());
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
					for (int y1 = 0; y1 < sz3; y1++){
						for (int y2 = 0; y2 < sz3; y2++){
							for (int y3 = 0; y3 < sz3; y3++){
								if (generator.nextBoolean()) {
									random1[x1][x2][y1][y2][y3] = true;
									trueno1++;
								} else {
									random1[x1][x2][y1][y2][y3] = false;
								}
							}
						}
					}
			}
		}
	}
	
	Relation full = new Relation(){
		@Override
		public boolean contains(int[] x, int[] y){ return true; }
	};
	Relation rand = new IndexTools.D2D3(){
		@Override
		public boolean contains(int x1, int x2, int y1, int y2, int y3){ 
			return random1[x1][x2][y1][y2][y3]; 
		}
	};
	Relation xHalf = new IndexTools.D2D3(){
		@Override
		public boolean contains(int x1, int x2, int y1, int y2, int y3){
			return ((x1+x2)%2 == 0) ? true : false; 
		}
	};
	Relation yHalf = new IndexTools.D2D3(){
		@Override
		public boolean contains(int x1, int x2, int y1, int y2, int y3){
			return ((y1+y2+y3) > sz3) ? true : false; 
		}
	};
	
	/****************************************************************************/
	
	@Test // testing VectorLayer*.associate(VectorLayer<?,T> layer, Relation rel)
          // fully defined layer bases
	public void testVectorLayerNN1() {
		AssociateUtil.setAssociateForkJoinFlag(true);
		
		// init arrays
		Unit1[][]   unit1arrayD2 = new Unit1[sz2][sz2];
		Unit2[][][] unit2arrayD3 = new Unit2[sz3][sz3][sz3];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				unit1arrayD2[i][j] = new Unit1(i+j); 
			}
		}
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					unit2arrayD3[i][j][k] = new Unit2(i+j+k);
				}
			}
		}
		
		// init NN layers
		
		Layer_Unit1_ unit1layerD2 = new Layer_Unit1_(unit1arrayD2);
		Layer_Unit2_ unit2layerD3 = new Layer_Unit2_(unit2arrayD3);
		
		BasedVectorLayer<Unit1.SignalDouble> unit2layerD3_doubleVector = unit2layerD3.returnDoubleVector();
		BasedVectorLayer<Unit1>              unit1layerD2_unit1Link = unit1layerD2.returnUnit1Link();
		BasedVectorLayer<Unit2>              unit1layerD2_unit2Link = unit1layerD2.returnUnit2Link();
		BasedVectorLayer<Unit2>              unit2layerD3_unit2Link = unit2layerD3.returnUnit2Link();
		
		// variables used for checking
		int[][] xLen;
		int[][][] yLen;
		int intCheckSum;
		double doubleCheckSum;
		
		// associate using relation full
		unit1layerD2.intVector.associate(unit2layerD3.intVector, full);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz3][sz3][sz3];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y1 = 0; y1 < sz3; y1++){
					for (int y2 = 0; y2 < sz3; y2++){
						for (int y3 = 0; y3 < sz3; y3++){
							if (full.contains(new int[]{x1, x2}, new int[]{y1, y2, y3})) {
					       		Unit1.SignalInt obj = unit1layerD2.intVector.get(x1, x2)[xLen[x1][x2]];
					       		assertEquals(obj, unit2layerD3.intVector.get(y1, y2, y3)[yLen[y1][y2][y3]]);
					       		assertEquals(obj.val, 7);
					       		intCheckSum += obj.val;
					       		xLen[x1][x2]++; yLen[y1][y2][y3]++;
							}
						}
					}
				}
			}
		}
		assertEquals(intCheckSum, 3024);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit1.SignalInt[] vector = unit1layerD2.intVector.get(x1, x2);
				if (xLen[x1][x2] > 0) {
					assertEquals(vector.length, xLen[x1][x2]);
					assertEquals(vector.length, sz3*sz3*sz3);
				} else {
					fail();
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz3; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					Unit1.SignalInt[] vector = unit2layerD3.intVector.get(y1, y2, y3);
					if (yLen[y1][y2][y3] > 0) {
						assertEquals(vector.length, yLen[y1][y2][y3]);
						assertEquals(vector.length, sz2*sz2);
					} else {
						fail();
					}
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y = 0; y < sz2*sz2; y++){
						unit2layerD3.intVector.get(x1, x2, x3)[y].setVal(val27_1[x1][x2][x3]*27 + y);
					}
				}
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y = 0; y < sz3*sz3*sz3; y++){
					assertEquals(unit1layerD2.intVector.get(x1, x2)[y].getVal(), val27_2[y]*27 + val16_1[x1][x2]);
				}
			}
		}
		
		// associate using relation rand
		unit1layerD2.doubleVector.associate(unit2layerD3_doubleVector, rand);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz3][sz3][sz3];
		doubleCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y1 = 0; y1 < sz3; y1++){
					for (int y2 = 0; y2 < sz3; y2++){
						for (int y3 = 0; y3 < sz3; y3++){
							if (rand.contains(new int[]{x1, x2}, new int[]{y1, y2, y3})) {
					       		Unit1.SignalDouble obj = unit1layerD2.doubleVector.get(x1, x2)[xLen[x1][x2]];
					       		assertEquals(obj, unit2layerD3_doubleVector.get(y1, y2, y3)[yLen[y1][y2][y3]]);
					       		assertEquals(obj.val, 5.0);
					       		doubleCheckSum += obj.val;
					       		xLen[x1][x2]++; yLen[y1][y2][y3]++;
							}
						}
					}
		       	}	
			}
		}
		assertEquals(doubleCheckSum, trueno1 * 5.0);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit1.SignalDouble[] vector = unit1layerD2.doubleVector.get(x1, x2);
				if (xLen[x1][x2] > 0) {
					assertEquals(vector.length, xLen[x1][x2]);
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz3; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					Unit1.SignalDouble[] vector = unit2layerD3_doubleVector.get(y1, y2, y3);
					if (yLen[y1][y2][y3] > 0) {
						assertEquals(vector.length, yLen[y1][y2][y3]);
					}
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					int sz = unit2layerD3_doubleVector.get(x1, x2, x3).length;
					for (int y = 0; y < sz; y++){
						unit2layerD3_doubleVector.get(x1, x2, x3)[y].setVal(3.14);
					}
				}
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				int sz = unit1layerD2.doubleVector.get(x1, x2).length;
				for (int y = 0; y < sz; y++){
					assertEquals(unit1layerD2.doubleVector.get(x1, x2)[y].getVal(), 3.14);
				}
			}
		}
		
		// associate using relation xHalf
		unit1layerD2_unit1Link.associate(unit2layerD3.unit1Link, xHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz3][sz3][sz3];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y1 = 0; y1 < sz3; y1++){
					for (int y2 = 0; y2 < sz3; y2++){
						for (int y3 = 0; y3 < sz3; y3++){
							if (xHalf.contains(new int[]{x1, x2}, new int[]{y1, y2, y3})) {
					       		Unit1 obj = unit1layerD2_unit1Link.get(x1, x2)[xLen[x1][x2]];
					       		assertEquals(obj, unit2layerD3.unit1Link.get(y1, y2, y3)[yLen[y1][y2][y3]]);
					       		assertEquals(obj.noItem, 3);
					       		intCheckSum += obj.noItem;
					       		xLen[x1][x2]++; yLen[y1][y2][y3]++;
							}
						}
					}
				}
			}
		}
		assertEquals(intCheckSum, 648);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit1[] vector = unit1layerD2_unit1Link.get(x1, x2);
				if (xLen[x1][x2] > 0) {
					assertEquals(vector.length, xLen[x1][x2]);
					assertEquals(vector.length, sz3*sz3*sz3);
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz3; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					Unit1[] vector = unit2layerD3.unit1Link.get(y1, y2, y3);
					if (yLen[y1][y2][y3] > 0) {
						assertEquals(vector.length, yLen[y1][y2][y3]);
						assertEquals(vector.length, (sz2*sz2)/2);
					}
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y = 0; y < (sz2*sz2)/2; y++){
						unit2layerD3.unit1Link.get(x1, x2, x3)[y].noItem = (val27_1[x1][x2][x3]*27 + y);
					}
				}
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if ((x1+x2)%2 != 0) continue;
				for (int y = 0; y < sz3*sz3*sz3; y++){
					assertEquals(unit1layerD2_unit1Link.get(x1, x2)[y].noItem, val27_2[y]*27 + val16_1[x1][x2]/2);
				}
			}
		}
		
		// associate using relation yHalf
		unit1layerD2_unit2Link.associate(unit2layerD3_unit2Link, yHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz3][sz3][sz3];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y1 = 0; y1 < sz3; y1++){
					for (int y2 = 0; y2 < sz3; y2++){
						for (int y3 = 0; y3 < sz3; y3++){
							if (yHalf.contains(new int[]{x1, x2}, new int[]{y1, y2, y3})) {
					       		Unit2 obj = unit1layerD2_unit2Link.get(x1, x2)[xLen[x1][x2]];
					       		assertEquals(obj, unit2layerD3_unit2Link.get(y1, y2, y3)[yLen[y1][y2][y3]]);
					       		assertEquals(obj.noItem, 7);
					       		intCheckSum += obj.noItem;
					       		xLen[x1][x2]++; yLen[y1][y2][y3]++;
							}
						}
					}
		       	}	
			}
		}
		assertEquals(intCheckSum, 1120);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit2[] vector = unit1layerD2_unit2Link.get(x1, x2);
				if (xLen[x1][x2] > 0) {
					assertEquals(vector.length, xLen[x1][x2]);
					assertEquals(vector.length, 10);
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz3; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					Unit2[] vector = unit2layerD3_unit2Link.get(y1, y2, y3);
					if (yLen[y1][y2][y3] > 0) {
						assertEquals(vector.length, yLen[y1][y2][y3]);
						assertEquals(vector.length, sz2*sz2);
					}
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (((x1+x2+x3) <= sz3)) continue;
					for (int y = 0; y < sz2*sz2; y++){
						unit2layerD3_unit2Link.get(x1, x2, x3)[y].noItem = (val27_1[x1][x2][x3]*27 + y);
					}
				}
			}
		}
		int[] factor = new int[] {8, 14, 16, 17, 20, 22, 23, 24, 25, 26};
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y = 0; y < 10; y++){
					assertEquals(unit1layerD2_unit2Link.get(x1, x2)[y].noItem, factor[y]*27 + val16_1[x1][x2]);
				}
			}
		}
		
	}
	
	
	/****************************************************************************/
	
	@Test // testing VectorLayer*.associate(VectorLayer<?,T> layer, Relation rel)
    	  // partially defined layer bases
	public void testVectorLayerNN2() {
		AssociateUtil.setAssociateForkJoinFlag(true);
		
		// init arrays
		Unit1[][]   unit1arrayD2 = new Unit1[sz2][sz2];
		Unit2[][][] unit2arrayD3 = new Unit2[sz3][sz3][sz3];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				if ((i+j)%2==0) unit1arrayD2[i][j] = new Unit1(i+j); 
			}
		}
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					if ((i+j+k)%2!=0) unit2arrayD3[i][j][k] = new Unit2(i+j+k);
				}
			}
		}
		
		// init NN layers
		
		Layer_Unit1_ unit1layerD2 = new Layer_Unit1_(unit1arrayD2);
		Layer_Unit2_ unit2layerD3 = new Layer_Unit2_(unit2arrayD3);
		
		BasedVectorLayer<Unit1.SignalDouble> unit2layerD3_doubleVector = unit2layerD3.returnDoubleVector();
		BasedVectorLayer<Unit1>              unit1layerD2_unit1Link = unit1layerD2.returnUnit1Link();
		BasedVectorLayer<Unit2>              unit1layerD2_unit2Link = unit1layerD2.returnUnit2Link();
		BasedVectorLayer<Unit2>              unit2layerD3_unit2Link = unit2layerD3.returnUnit2Link();
		
		// variables used for checking
		int[][] xLen;
		int[][][] yLen;
		int intCheckSum;
		double doubleCheckSum;
		
		final int[] factor = new int[]{1,3,5,7,9,11,13,15,17,19,21,23,25};
		
		// associate using relation full
		unit1layerD2.intVector.associate(unit2layerD3.intVector, full);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz3][sz3][sz3];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if (unit1layerD2.getUnit(x1, x2) == null) continue;
				for (int y1 = 0; y1 < sz3; y1++){
					for (int y2 = 0; y2 < sz3; y2++){
						for (int y3 = 0; y3 < sz3; y3++){
							if (unit2layerD3.getUnit(y1, y2, y3) == null) continue;
							if (full.contains(new int[]{x1, x2}, new int[]{y1, y2, y3})) {
								Unit1.SignalInt obj = unit1layerD2.intVector.get(x1, x2)[xLen[x1][x2]];
					       		assertEquals(obj, unit2layerD3.intVector.get(y1, y2, y3)[yLen[y1][y2][y3]]);
					       		assertEquals(obj.val, 7);
					       		intCheckSum += obj.val;
					       		xLen[x1][x2]++; yLen[y1][y2][y3]++;
							}
						}
					}
		       	}	
			}
		}
		assertEquals(intCheckSum, 728);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if (unit1layerD2.getUnit(x1, x2) == null) continue;
				Unit1.SignalInt[] vector = unit1layerD2.intVector.get(x1, x2);
				if (xLen[x1][x2] > 0) {
					assertEquals(vector.length, xLen[x1][x2]);
					assertEquals(vector.length, 13);
				} else {
					fail();
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz3; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					if (unit2layerD3.getUnit(y1, y2, y3) == null) continue;
					Unit1.SignalInt[] vector = unit2layerD3.intVector.get(y1, y2, y3);
					if (yLen[y1][y2][y3] > 0) {
						assertEquals(vector.length, yLen[y1][y2][y3]);
						assertEquals(vector.length, 8);
					} else {
						fail();
					}
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (unit2layerD3.getUnit(x1, x2, x3) == null) continue;
					for (int y = 0; y < 8; y++){
						unit2layerD3.intVector.get(x1, x2, x3)[y].setVal(val27_1[x1][x2][x3]*27 + y);
					}
				}
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if (unit1layerD2.getUnit(x1, x2) == null) continue;
				for (int y = 0; y < 13; y++){
					assertEquals(unit1layerD2.intVector.get(x1, x2)[y].getVal(), factor[y]*27 + val16_1[x1][x2]/2);
				}
			}
		}
		
		// associate with relation rand
		unit1layerD2.doubleVector.associate(unit2layerD3_doubleVector, rand);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz3][sz3][sz3];
		doubleCheckSum = 0;
		int checkNo = trueno1;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y1 = 0; y1 < sz3; y1++){
					for (int y2 = 0; y2 < sz3; y2++){
						for (int y3 = 0; y3 < sz3; y3++){
							if (rand.contains(new int[]{x1, x2}, new int[]{y1, y2, y3})) {
								if (unit1layerD2.getUnit(x1, x2) == null) {
									checkNo--;
									continue;
								}
								Unit1.SignalDouble obj = unit1layerD2.doubleVector.get(x1, x2)[xLen[x1][x2]];
					       		assertEquals(obj, unit2layerD3_doubleVector.get(y1, y2, y3)[yLen[y1][y2][y3]]);
					       		assertEquals(obj.val, 5.0);
					       		doubleCheckSum += obj.val;
					       		xLen[x1][x2]++; yLen[y1][y2][y3]++;
							}
						}
					}
		       	}
			}
		}
		assertEquals(doubleCheckSum, checkNo * 5.0);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if (unit1layerD2.getUnit(x1, x2) == null) continue;
				Unit1.SignalDouble[] vector = unit1layerD2.doubleVector.get(x1, x2);
				if (xLen[x1][x2] > 0) {
					assertEquals(vector.length, xLen[x1][x2]);
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz3; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					Unit1.SignalDouble[] vector = unit2layerD3_doubleVector.get(y1, y2, y3);
					if (yLen[y1][y2][y3] > 0) {
						assertEquals(vector.length, yLen[y1][y2][y3]);
					}
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					int sz = unit2layerD3_doubleVector.get(x1, x2, x3).length;
					for (int y = 0; y < sz; y++){
						unit2layerD3_doubleVector.get(x1, x2, x3)[y].setVal(3.14);
					}
				}
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if (unit1layerD2.getUnit(x1, x2) == null) continue;
				int sz = unit1layerD2.doubleVector.get(x1, x2).length;
				for (int y = 0; y < sz; y++){
					assertEquals(unit1layerD2.doubleVector.get(x1, x2)[y].getVal(), 3.14);
				}
			}
		}
		
		// associate with relation xHalf
		unit1layerD2_unit1Link.associate(unit2layerD3.unit1Link, xHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz3][sz3][sz3];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y1 = 0; y1 < sz3; y1++){
					for (int y2 = 0; y2 < sz3; y2++){
						for (int y3 = 0; y3 < sz3; y3++){
							if (xHalf.contains(new int[]{x1, x2}, new int[]{y1, y2, y3})) {
								if (unit2layerD3.getUnit(y1, y2, y3) == null) continue;
								Unit1 obj = unit1layerD2_unit1Link.get(x1, x2)[xLen[x1][x2]];
					       		assertEquals(obj, unit2layerD3.unit1Link.get(y1, y2, y3)[yLen[y1][y2][y3]]);
					       		assertEquals(obj.noItem, 3);
					       		intCheckSum += obj.noItem;
					       		xLen[x1][x2]++; yLen[y1][y2][y3]++;
							}
						}
					}
		       	}	
			}
		}
		assertEquals(intCheckSum, 312);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit1[] vector = unit1layerD2_unit1Link.get(x1, x2);
				if (xLen[x1][x2] > 0) {
					assertEquals(vector.length, xLen[x1][x2]);
					assertEquals(xLen[x1][x2], 13);
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz3; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					if (unit2layerD3.getUnit(y1, y2, y3) == null) continue;
					Unit1[] vector = unit2layerD3.unit1Link.get(y1, y2, y3);
					if (yLen[y1][y2][y3] > 0) {
						assertEquals(vector.length, yLen[y1][y2][y3]);
						assertEquals(yLen[y1][y2][y3], 8);
					} else {
						fail();
					}
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (unit2layerD3.getUnit(x1, x2, x3) == null) continue;
					for (int y = 0; y < 8; y++){
						unit2layerD3.unit1Link.get(x1, x2, x3)[y].noItem = (val27_1[x1][x2][x3]*27 + y);
					}
				}
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if ((x1+x2)%2 != 0) continue;
				for (int y = 0; y < 13; y++){
					assertEquals(unit1layerD2_unit1Link.get(x1, x2)[y].noItem, factor[y]*27 + val16_1[x1][x2]/2);
				}
			}
		}
		
		// associate with relation yHalf
		unit1layerD2_unit2Link.associate(unit2layerD3_unit2Link, yHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz3][sz3][sz3];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y1 = 0; y1 < sz3; y1++){
					for (int y2 = 0; y2 < sz3; y2++){
						for (int y3 = 0; y3 < sz3; y3++){
							if (yHalf.contains(new int[]{x1, x2}, new int[]{y1, y2, y3})) {
								Unit2 obj = unit1layerD2_unit2Link.get(x1, x2)[xLen[x1][x2]];
					       		assertEquals(obj, unit2layerD3_unit2Link.get(y1, y2, y3)[yLen[y1][y2][y3]]);
					       		assertEquals(obj.noItem, 7);
					       		intCheckSum += obj.noItem;
					       		xLen[x1][x2]++; yLen[y1][y2][y3]++;
							}
						}
					}
				}	
			}
		}
		assertEquals(intCheckSum, 1120);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit2[] vector = unit1layerD2_unit2Link.get(x1, x2);
				if (xLen[x1][x2] > 0) {
					assertEquals(vector.length, xLen[x1][x2]);
					assertEquals(xLen[x1][x2], 10);
				} else {
					fail();
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz3; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					Unit2[] vector = unit2layerD3_unit2Link.get(y1, y2, y3);
					if (yLen[y1][y2][y3] > 0) {
						assertEquals(vector.length, yLen[y1][y2][y3]);
						assertEquals(yLen[y1][y2][y3], 16);
					}
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (((x1+x2+x3) <= sz3)) continue;
					for (int y = 0; y < 16; y++){
						unit2layerD3_unit2Link.get(x1, x2, x3)[y].noItem = (val27_1[x1][x2][x3]*27 + y);
					}
				}
			}
		}
		int[] yfactor = new int[] {8, 14, 16, 17, 20, 22, 23, 24, 25, 26};
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y = 0; y < 10; y++){
					assertEquals(unit1layerD2_unit2Link.get(x1, x2)[y].noItem, yfactor[y]*27 + val16_1[x1][x2]);
				}
			}
		}
		
	}
	
}

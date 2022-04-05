package org.jlayer.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Random;

import org.jlayer.model.*;

import basic.units.*;

/**
 * Tests for {@code VectorLayer*.D3.associate(VectorLayer.D2<?,T> layer, Relation rel)}. 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Associate0_D3D2_Test {
	
//	final int sz1 = 10;
	final int sz2 = 4;
	final int sz3 = 3;
	
	final int[][]   val16_1 = new int[][]{ {0,1,2,3}, {4,5,6,7}, {8,9,10,11}, {12,13,14,15} };
	final int[]     val16_2 = new int[]{ 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15 };
	final int[][][] val27_1 = new int[][][]{ { {0,1,2}, {3,4,5}, {6,7,8} }, { {9,10,11}, {12,13,14}, {15,16,17} }, { {18,19,20}, {21,22,23}, {24,25,26} } };
	final int[]     val27_2 = new int[]{ 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26 };
	
	boolean[][][][][] random1 = new boolean[sz3][sz3][sz3][sz2][sz2];
	int         	  trueno1 = 0;
	
	C_Associate0_D3D2_Test() {
		Random generator = new Random(System.nanoTime());
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz2; y1++){
						for (int y2 = 0; y2 < sz2; y2++){
							if (generator.nextBoolean()) {
								random1[x1][x2][x3][y1][y2] = true;
								trueno1++;
							} else {
								random1[x1][x2][x3][y1][y2] = false;
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
	Relation rand = new IndexTools.D3D2(){
		@Override
		public boolean contains(int x1, int x2, int x3, int y1, int y2){ 
			return random1[x1][x2][x3][y1][y2]; 
		}
	};
	Relation xHalf = new IndexTools.D3D2(){
		@Override
		public boolean contains(int x1, int x2, int x3, int y1, int y2){
			return ((x1+x2+x3)%2 == 0) ? true : false; 
		}
	};
	Relation yHalf = new IndexTools.D3D2(){
		@Override
		public boolean contains(int x1, int x2, int x3, int y1, int y2){
			return ((y1+y2) > 3) ? true : false; 
		}
	};
	
	/****************************************************************************/
	
	@Test // testing VectorLayer*.associate(VectorLayer<?,T> layer, Relation rel)
          // fully defined layer bases
	public void testVectorLayerNN1() {
		AssociateUtil.setAssociateForkJoinFlag(false);
		
		// init arrays
		Unit1[][][] unit1arrayD3 = new Unit1[sz3][sz3][sz3];
		Unit2[][]   unit2arrayD2 = new Unit2[sz2][sz2];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					unit1arrayD3[i][j][k] = new Unit1(i+j+k); 
				}
			}
		}
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				unit2arrayD2[i][j] = new Unit2(i+j);
			}
		}
		
		// init NN layers
		
		Layer_Unit1_ unit1layerD3 = new Layer_Unit1_(unit1arrayD3);
		Layer_Unit2_ unit2layerD2 = new Layer_Unit2_(unit2arrayD2);
		
		BasedVectorLayer<Unit1.SignalDouble> unit2layerD2_doubleVector = unit2layerD2.returnDoubleVector();
		BasedVectorLayer<Unit1>              unit1layerD3_unit1Link = unit1layerD3.returnUnit1Link();
		BasedVectorLayer<Unit2>              unit1layerD3_unit2Link = unit1layerD3.returnUnit2Link();
		BasedVectorLayer<Unit2>              unit2layerD2_unit2Link = unit2layerD2.returnUnit2Link();
		
		// variables used for checking
		int[][][] xLen;
		int[][]   yLen;
		int intCheckSum;
		double doubleCheckSum;
		
		// associate using relation full
		unit1layerD3.intVector.associate(unit2layerD2.intVector, full);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz2][sz2];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz2; y1++){
						for (int y2 = 0; y2 < sz2; y2++){
							if (full.contains(new int[]{x1, x2, x3}, new int[]{y1, y2})) {
					       		Unit1.SignalInt obj = unit1layerD3.intVector.get(x1, x2, x3)[xLen[x1][x2][x3]];
					       		assertEquals(obj, unit2layerD2.intVector.get(y1, y2)[yLen[y1][y2]]);
					       		assertEquals(obj.val, 7);
					       		intCheckSum += obj.val;
					       		xLen[x1][x2][x3]++; yLen[y1][y2]++;
							}
						}
					}
		       	}	
			}
		}
		assertEquals(intCheckSum, 3024);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1.SignalInt[] vector = unit1layerD3.intVector.get(x1, x2, x3);
					if (xLen[x1][x2][x3] > 0) {
						assertEquals(vector.length, xLen[x1][x2][x3]);
						assertEquals(vector.length, sz2*sz2);
					} else {
						fail();
					}
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz2; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				Unit1.SignalInt[] vector = unit2layerD2.intVector.get(y1, y2);
				if (yLen[y1][y2] > 0) {
					assertEquals(vector.length, yLen[y1][y2]);
					assertEquals(vector.length, sz3*sz3*sz3);
				} else {
					fail();
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y = 0; y < sz3*sz3*sz3; y++){
					unit2layerD2.intVector.get(x1, x2)[y].setVal(val16_1[x1][x2]*16 + y);
				}
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y = 0; y < sz2*sz2; y++){
						assertEquals(unit1layerD3.intVector.get(x1, x2, x3)[y].getVal(), val16_2[y]*16 + val27_1[x1][x2][x3]);
					}
				}
			}
		}
		
		// associate using relation rand
		unit1layerD3.doubleVector.associate(unit2layerD2_doubleVector, rand);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz2][sz2];
		doubleCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz2; y1++){
						for (int y2 = 0; y2 < sz2; y2++){
							if (rand.contains(new int[]{x1, x2, x3}, new int[]{y1, y2})) {
					       		Unit1.SignalDouble obj = unit1layerD3.doubleVector.get(x1, x2, x3)[xLen[x1][x2][x3]];
					       		assertEquals(obj, unit2layerD2_doubleVector.get(y1, y2)[yLen[y1][y2]]);
					       		assertEquals(obj.val, 5.0);
					       		doubleCheckSum += obj.val;
					       		xLen[x1][x2][x3]++; yLen[y1][y2]++;
							}
						}
					}
		       	}	
			}
		}
		assertEquals(doubleCheckSum, trueno1 * 5.0);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1.SignalDouble[] vector = unit1layerD3.doubleVector.get(x1, x2, x3);
					if (xLen[x1][x2][x3] > 0) {
						assertEquals(vector.length, xLen[x1][x2][x3]);
					}
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz2; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				Unit1.SignalDouble[] vector = unit2layerD2_doubleVector.get(y1, y2);
				if (yLen[y1][y2] > 0) {
					assertEquals(vector.length, yLen[y1][y2]);
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				int sz = unit2layerD2_doubleVector.get(x1, x2).length;
				for (int y = 0; y < sz; y++){
					unit2layerD2_doubleVector.get(x1, x2)[y].setVal(3.14);
				}
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					int sz = unit1layerD3.doubleVector.get(x1, x2, x3).length;
					for (int y = 0; y < sz; y++){
						assertEquals(unit1layerD3.doubleVector.get(x1, x2, x3)[y].getVal(), 3.14);
					}
				}
			}
		}
		
		// associate using relation xHalf
		unit1layerD3_unit1Link.associate(unit2layerD2.unit1Link, xHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz2][sz2];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz2; y1++){
						for (int y2 = 0; y2 < sz2; y2++){
							if (xHalf.contains(new int[]{x1, x2, x3}, new int[]{y1, y2})) {
					       		Unit1 obj = unit1layerD3_unit1Link.get(x1, x2, x3)[xLen[x1][x2][x3]];
					       		assertEquals(obj, unit2layerD2.unit1Link.get(y1, y2)[yLen[y1][y2]]);
					       		assertEquals(obj.noItem, 3);
					       		intCheckSum += obj.noItem;
					       		xLen[x1][x2][x3]++; yLen[y1][y2]++;
							}
						}
					}
		       	}	
			}
		}
		assertEquals(intCheckSum, 672);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1[] vector = unit1layerD3_unit1Link.get(x1, x2, x3);
					if (xLen[x1][x2][x3] > 0) {
						assertEquals(vector.length, xLen[x1][x2][x3]);
						assertEquals(vector.length, sz2*sz2);
					}
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz2; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				Unit1[] vector = unit2layerD2.unit1Link.get(y1, y2);
				if (yLen[y1][y2] > 0) {
					assertEquals(vector.length, yLen[y1][y2]);
					assertEquals(vector.length, 14);
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y = 0; y < 14; y++){
					unit2layerD2.unit1Link.get(x1, x2)[y].noItem = val16_1[x1][x2]*16 + y;
				}
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if ((x1+x2+x3)%2 != 0) continue;
					for (int y = 0; y < sz2*sz2; y++){
						assertEquals(unit1layerD3_unit1Link.get(x1, x2, x3)[y].noItem, val16_2[y]*16 + val27_1[x1][x2][x3]/2);
					}
				}
			}
		}
		
		// associate using relation yHalf
		unit1layerD3_unit2Link.associate(unit2layerD2_unit2Link, yHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz2][sz2];
		intCheckSum = 0;
		
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz2; y1++){
						for (int y2 = 0; y2 < sz2; y2++){
							if (yHalf.contains(new int[]{x1, x2, x3}, new int[]{y1, y2})) {
					       		Unit2 obj = unit1layerD3_unit2Link.get(x1, x2, x3)[xLen[x1][x2][x3]];
					       		assertEquals(obj, unit2layerD2_unit2Link.get(y1, y2)[yLen[y1][y2]]);
					       		assertEquals(obj.noItem, 7);
					       		intCheckSum += obj.noItem;
					       		xLen[x1][x2][x3]++; yLen[y1][y2]++;
							}
						}
					}
		       	}	
			}
		}
		assertEquals(intCheckSum, 1134);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit2[] vector = unit1layerD3_unit2Link.get(x1, x2, x3);
					if (xLen[x1][x2][x3] > 0) {
						assertEquals(vector.length, xLen[x1][x2][x3]);
						assertEquals(vector.length, 6);
					}
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz2; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				Unit2[] vector = unit2layerD2_unit2Link.get(y1, y2);
				if (yLen[y1][y2] > 0) {
					assertEquals(vector.length, yLen[y1][y2]);
					assertEquals(vector.length, sz3*sz3*sz3);
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if (((x1+x2) <= 3)) continue;
				for (int y = 0; y < sz3*sz3*sz3; y++){
					unit2layerD2_unit2Link.get(x1, x2)[y].noItem = val16_1[x1][x2]*16 + y;
				}
			}
		}
		int[] factor = new int[] {7, 10, 11, 13, 14, 15};
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y = 0; y < 6; y++){
						assertEquals(unit1layerD3_unit2Link.get(x1, x2, x3)[y].noItem, factor[y]*16 + val27_1[x1][x2][x3]);
					}
				}
			}
		}
		
	}
	
	
	/****************************************************************************/
	
	@Test // testing VectorLayer*.associate(VectorLayer<?,T> layer, Relation rel)
    	  // partially defined layer bases
	public void testVectorLayerNN2() {
		AssociateUtil.setAssociateForkJoinFlag(false);
		
		// init arrays
		Unit1[][][] unit1arrayD3 = new Unit1[sz3][sz3][sz3];
		Unit2[][]   unit2arrayD2 = new Unit2[sz2][sz2];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					if ((i+j+k)%2!=0) unit1arrayD3[i][j][k] = new Unit1(i+j+k); 
				}
			}
		}
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				if ((i+j)%2==0) unit2arrayD2[i][j] = new Unit2(i+j);
			}
		}
		
		// init NN layers
		
		Layer_Unit1_ unit1layerD3 = new Layer_Unit1_(unit1arrayD3);
		Layer_Unit2_ unit2layerD2 = new Layer_Unit2_(unit2arrayD2);
		
		BasedVectorLayer<Unit1.SignalDouble> unit2layerD2_doubleVector = unit2layerD2.returnDoubleVector();
		BasedVectorLayer<Unit1>              unit1layerD3_unit1Link = unit1layerD3.returnUnit1Link();
		BasedVectorLayer<Unit2>              unit1layerD3_unit2Link = unit1layerD3.returnUnit2Link();
		BasedVectorLayer<Unit2>              unit2layerD2_unit2Link = unit2layerD2.returnUnit2Link();
		
		// variables used for checking
		int[][][] xLen;
		int[][]   yLen;
		int intCheckSum;
		double doubleCheckSum;
		final int[] factor = new int[]{0, 2, 5, 7, 8, 10, 13, 15};
		
		// associate using relation full
		unit1layerD3.intVector.associate(unit2layerD2.intVector, full);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz2][sz2];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (unit1layerD3.getUnit(x1, x2, x3) == null) continue;
					for (int y1 = 0; y1 < sz2; y1++){
						for (int y2 = 0; y2 < sz2; y2++){
							if (unit2layerD2.getUnit(y1, y2) == null) continue;
							if (full.contains(new int[]{x1, x2, x3}, new int[]{y1, y2})) {
								Unit1.SignalInt obj = unit1layerD3.intVector.get(x1, x2, x3)[xLen[x1][x2][x3]];
					       		assertEquals(obj, unit2layerD2.intVector.get(y1, y2)[yLen[y1][y2]]);
					       		assertEquals(obj.val, 7);
					       		intCheckSum += obj.val;
					       		xLen[x1][x2][x3]++; yLen[y1][y2]++;
							}
						}
			       	}	
				}
			}
		}
		assertEquals(intCheckSum, 728);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (unit1layerD3.getUnit(x1, x2, x3) == null) continue;
					Unit1.SignalInt[] vector = unit1layerD3.intVector.get(x1, x2, x3);
					if (xLen[x1][x2][x3] > 0) {
						assertEquals(vector.length, xLen[x1][x2][x3]);
						assertEquals(vector.length, 8);
					} else {
						fail();
					}
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz2; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				if (unit2layerD2.getUnit(y1, y2) == null) continue;
				Unit1.SignalInt[] vector = unit2layerD2.intVector.get(y1, y2);
				if (yLen[y1][y2] > 0) {
					assertEquals(vector.length, yLen[y1][y2]);
					assertEquals(vector.length, 13);
				} else {
					fail();
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if (unit2layerD2.getUnit(x1, x2) == null) continue;
				for (int y = 0; y < 13; y++){
					unit2layerD2.intVector.get(x1, x2)[y].setVal(val16_1[x1][x2]*16 + y);
				}
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (unit1layerD3.getUnit(x1, x2, x3) == null) continue;
					for (int y = 0; y < 8; y++){
						assertEquals(unit1layerD3.intVector.get(x1, x2, x3)[y].getVal(), factor[y]*16 + val27_1[x1][x2][x3]/2);
					}
				}
			}
		}
		
		// associate with relation rand
		unit1layerD3.doubleVector.associate(unit2layerD2_doubleVector, rand);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz2][sz2];
		doubleCheckSum = 0;
		int checkNo = trueno1;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz2; y1++){
						for (int y2 = 0; y2 < sz2; y2++){
							if (rand.contains(new int[]{x1, x2, x3}, new int[]{y1, y2})) {
								if (unit1layerD3.getUnit(x1, x2, x3) == null) {
									checkNo--;
									continue;
								}
								Unit1.SignalDouble obj = unit1layerD3.doubleVector.get(x1, x2, x3)[xLen[x1][x2][x3]];
					       		assertEquals(obj, unit2layerD2_doubleVector.get(y1, y2)[yLen[y1][y2]]);
					       		assertEquals(obj.val, 5.0);
					       		doubleCheckSum += obj.val;
					       		xLen[x1][x2][x3]++; yLen[y1][y2]++;
							}
						}
			       	}
				}
			}
		}
		assertEquals(doubleCheckSum, checkNo * 5.0);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (unit1layerD3.getUnit(x1, x2, x3) == null) continue;
					Unit1.SignalDouble[] vector = unit1layerD3.doubleVector.get(x1, x2, x3);
					if (xLen[x1][x2][x3] > 0) {
						assertEquals(vector.length, xLen[x1][x2][x3]);
					}
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz2; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				Unit1.SignalDouble[] vector = unit2layerD2_doubleVector.get(y1, y2);
				if (yLen[y1][y2] > 0) {
					assertEquals(vector.length, yLen[y1][y2]);
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				int sz = unit2layerD2_doubleVector.get(x1, x2).length;
				for (int y = 0; y < sz; y++){
					unit2layerD2_doubleVector.get(x1, x2)[y].setVal(3.14);
				}
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (unit1layerD3.getUnit(x1, x2, x3) == null) continue;
					int sz = unit1layerD3.doubleVector.get(x1, x2, x3).length;
					for (int y = 0; y < sz; y++){
						assertEquals(unit1layerD3.doubleVector.get(x1, x2, x3)[y].getVal(), 3.14);
					}
				}
			}
		}
		
		// associate with relation xHalf
		unit1layerD3_unit1Link.associate(unit2layerD2.unit1Link, xHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz2][sz2];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz2; y1++){
						for (int y2 = 0; y2 < sz2; y2++){
							if (xHalf.contains(new int[]{x1, x2, x3}, new int[]{y1, y2})) {
								if (unit2layerD2.getUnit(y1, y2) == null) continue;
								Unit1 obj = unit1layerD3_unit1Link.get(x1, x2, x3)[xLen[x1][x2][x3]];
					       		assertEquals(obj, unit2layerD2.unit1Link.get(y1, y2)[yLen[y1][y2]]);
					       		assertEquals(obj.noItem, 3);
					       		intCheckSum += obj.noItem;
					       		xLen[x1][x2][x3]++; yLen[y1][y2]++;
							}
						}
			       	}	
				}
			}
		}
		assertEquals(intCheckSum, 336);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1[] vector = unit1layerD3_unit1Link.get(x1, x2, x3);
					if (xLen[x1][x2][x3] > 0) {
						assertEquals(vector.length, xLen[x1][x2][x3]);
						assertEquals(xLen[x1][x2][x3], 8);
					}
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz2; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				if (unit2layerD2.getUnit(y1, y2) == null) continue;
				Unit1[] vector = unit2layerD2.unit1Link.get(y1, y2);
				if (yLen[y1][y2] > 0) {
					assertEquals(vector.length, yLen[y1][y2]);
					assertEquals(yLen[y1][y2], 14);
				} else {
					fail();
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if (unit2layerD2.getUnit(x1, x2) == null) continue;
				for (int y = 0; y < 14; y++){
					unit2layerD2.unit1Link.get(x1, x2)[y].noItem = (val16_1[x1][x2]*16 + y);
				}
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if ((x1+x2+x3)%2 != 0) continue;
					for (int y = 0; y < 8; y++){
						assertEquals(unit1layerD3_unit1Link.get(x1, x2, x3)[y].noItem, factor[y]*16 + val27_1[x1][x2][x3]/2);
					}
				}
			}
		}
		
		// associate with relation yHalf
		unit1layerD3_unit2Link.associate(unit2layerD2_unit2Link, yHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz2][sz2];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz2; y1++){
						for (int y2 = 0; y2 < sz2; y2++){
							if (yHalf.contains(new int[]{x1, x2, x3}, new int[]{y1, y2})) {
								Unit2 obj = unit1layerD3_unit2Link.get(x1, x2, x3)[xLen[x1][x2][x3]];
					       		assertEquals(obj, unit2layerD2_unit2Link.get(y1, y2)[yLen[y1][y2]]);
					       		assertEquals(obj.noItem, 7);
					       		intCheckSum += obj.noItem;
					       		xLen[x1][x2][x3]++; yLen[y1][y2]++;
							}
						}
					}
		       	}	
			}
		}
		assertEquals(intCheckSum, 1134);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit2[] vector = unit1layerD3_unit2Link.get(x1, x2, x3);
					if (xLen[x1][x2][x3] > 0) {
						assertEquals(vector.length, xLen[x1][x2][x3]);
						assertEquals(xLen[x1][x2][x3], 6);
					} else {
						fail();
					}
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz2; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				Unit2[] vector = unit2layerD2_unit2Link.get(y1, y2);
				if (yLen[y1][y2] > 0) {
					assertEquals(vector.length, yLen[y1][y2]);
					assertEquals(yLen[y1][y2], 27);
				}
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if (((x1+x2) <= 3)) continue;
				for (int y = 0; y < 27; y++){
					unit2layerD2_unit2Link.get(x1, x2)[y].noItem = (val16_1[x1][x2]*16 + y);
				}
			}
		}
		int[] yfactor = new int[] {7, 10, 11, 13, 14, 15};
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y = 0; y < 6; y++){
						assertEquals(unit1layerD3_unit2Link.get(x1, x2, x3)[y].noItem, yfactor[y]*16 + val27_1[x1][x2][x3]);
					}
				}
			}
		}
		
	}
	
}

package org.jlayer.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Random;

import org.jlayer.model.*;

import basic.units.*;

/**
 * Tests for {@code VectorLayer*.D3.associate(VectorLayer.D1<?,T> layer, Relation rel)}. 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Associate0_D3D1_Test {
	
	final int sz1 = 10;
//	final int sz2 = 4;
	final int sz3 = 3;
	
	boolean[][][][] random1 = new boolean[sz3][sz3][sz3][sz1];
	int             trueno1 = 0;
	
	C_Associate0_D3D1_Test() {
		Random generator = new Random(System.nanoTime());
		for (int x1 = 0; x1 < sz3; x1++){
				for (int x2 = 0; x2 < sz3; x2++){
					for (int x3 = 0; x3 < sz3; x3++){
						for (int y1 = 0; y1 < sz1; y1++){
							if (generator.nextBoolean()) {
								random1[x1][x2][x3][y1] = true;
								trueno1++;
							} else {
								random1[x1][x2][x3][y1] = false;		
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
	Relation rand = new IndexTools.D3D1(){
		@Override
		public boolean contains(int x1, int x2, int x3, int y1){ 
			return random1[x1][x2][x3][y1]; 
		}
	};
	Relation xHalf = new IndexTools.D3D1(){
		@Override
		public boolean contains(int x1, int x2, int x3, int y1){
			return ((x1+x2+x3)%2 == 0) ? true : false; 
		}
	};
	Relation yHalf = new IndexTools.D3D1(){
		@Override
		public boolean contains(int x1, int x2, int x3, int y1){
			return (y1 > sz1/2) ? true : false; 
		}
	};
	
	/****************************************************************************/
	
	@Test // testing VectorLayer*.associate(VectorLayer<?,T> layer, Relation rel)
          // fully defined layer bases
	public void testVectorLayerNN1() {
		AssociateUtil.setAssociateForkJoinFlag(false);
		
		final int[][][] val27_3 = new int[][][]{ { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 } },
												 { { 9, 10, 11 }, { 12, 13, 14 }, { 15, 16, 17 } },	
												 { { 18, 19, 20 }, { 21, 22, 23 }, { 24, 25, 26 } } };
		
		// init arrays
		Unit1[][][] unit1arrayD3 = new Unit1[sz3][sz3][sz3];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					unit1arrayD3[i][j][k] = new Unit1(i+j+k);
				}
			}
		}
		for (int i = 0; i < sz1; i++) {	
			unit2arrayD1[i] = new Unit2(i); 
		}
		
		// init NN layers
		Layer_Unit1_ unit1layerD3 = new Layer_Unit1_(unit1arrayD3);
		Layer_Unit2_ unit2layerD1 = new Layer_Unit2_(unit2arrayD1);
		
		BasedVectorLayer<Unit1.SignalDouble> unit2layerD1_doubleVector = unit2layerD1.returnDoubleVector();
		BasedVectorLayer<Unit1>              unit1layerD3_unit1Link = unit1layerD3.returnUnit1Link();
		BasedVectorLayer<Unit2>              unit1layerD3_unit2Link = unit1layerD3.returnUnit2Link();
		BasedVectorLayer<Unit2>              unit2layerD1_unit2Link = unit2layerD1.returnUnit2Link();
		
		// variables used for checking
		int[][][] xLen;
		int[] yLen;
		int intCheckSum;
		double doubleCheckSum;
		
		// associate using relation full
		unit1layerD3.intVector.associate(unit2layerD1.intVector, full);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz1];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz1; y1++){
						if (full.contains(new int[]{x1, x2, x3}, new int[]{y1})) {
				       		Unit1.SignalInt obj = unit1layerD3.intVector.get(x1, x2, x3)[xLen[x1][x2][x3]];
				       		assertEquals(obj, unit2layerD1.intVector.get(y1)[yLen[y1]]);
				       		assertEquals(obj.val, 7);
				       		intCheckSum += obj.val;
				       		xLen[x1][x2][x3]++; yLen[y1]++;
						}
					}	
				}
			}
		}
		assertEquals(intCheckSum, 1890);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1.SignalInt[] vector = unit1layerD3.intVector.get(x1, x2, x3);
					if (xLen[x1][x2][x3] > 0) {
						assertEquals(vector.length, xLen[x1][x2][x3]);
						assertEquals(vector.length, sz1);
					} else {
						fail();
					}
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz1; y1++){
			Unit1.SignalInt[] vector = unit2layerD1.intVector.get(y1);
			if (yLen[y1] > 0) {
				assertEquals(vector.length, yLen[y1]);
				assertEquals(vector.length, sz3*sz3*sz3);
			} else {
				fail();
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz1; x1++){
			for (int y = 0; y < sz3*sz3*sz3; y++){
				unit2layerD1.intVector.get(x1)[y].setVal(x1*10 + y);
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y = 0; y < sz1; y++){
						assertEquals(unit1layerD3.intVector.get(x1, x2, x3)[y].getVal(), y*10 + val27_3[x1][x2][x3]);
					}
				}
			}
		}
		
		// associate using relation rand
		unit1layerD3.doubleVector.associate(unit2layerD1_doubleVector, rand);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz1];
		doubleCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz1; y1++){
						if (rand.contains(new int[]{x1, x2, x3}, new int[]{y1})) {
				       		Unit1.SignalDouble obj = unit1layerD3.doubleVector.get(x1, x2, x3)[xLen[x1][x2][x3]];
				       		assertEquals(obj, unit2layerD1_doubleVector.get(y1)[yLen[y1]]);
				       		assertEquals(obj.val, 5.0);
				       		doubleCheckSum += obj.val;
				       		xLen[x1][x2][x3]++; yLen[y1]++;
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
		for (int y1 = 0; y1 < sz1; y1++){
			Unit1.SignalDouble[] vector = unit2layerD1_doubleVector.get(y1);
			if (yLen[y1] > 0) {
				assertEquals(vector.length, yLen[y1]);
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz1; x1++){
			int sz = unit2layerD1_doubleVector.get(x1).length;
			for (int y = 0; y < sz; y++){
				unit2layerD1_doubleVector.get(x1)[y].setVal(3.14);
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
		unit1layerD3_unit1Link.associate(unit2layerD1.unit1Link, xHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz1];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz1; y1++){
						if (xHalf.contains(new int[]{x1, x2, x3}, new int[]{y1})) {
				       		Unit1 obj = unit1layerD3_unit1Link.get(x1, x2, x3)[xLen[x1][x2][x3]];
				       		assertEquals(obj, unit2layerD1.unit1Link.get(y1)[yLen[y1]]);
				       		assertEquals(obj.noItem, 3);
				       		intCheckSum += obj.noItem;
				       		xLen[x1][x2][x3]++; yLen[y1]++;
						}
			       	}	
				}
			}
		}
		assertEquals(intCheckSum, 420);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1[] vector = unit1layerD3_unit1Link.get(x1, x2, x3);
					if (xLen[x1][x2][x3] > 0) {
						assertEquals(vector.length, xLen[x1][x2][x3]);
						assertEquals(vector.length, sz1);
					}
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz1; y1++){
			Unit1[] vector = unit2layerD1.unit1Link.get(y1);
			if (yLen[y1] > 0) {
				assertEquals(vector.length, yLen[y1]);
				assertEquals(vector.length, 14);
			}
			
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz1; x1++){
			for (int y = 0; y < 14; y++){
				unit2layerD1.unit1Link.get(x1)[y].noItem = (x1*10 + y);
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if ((x1+x2+x3)%2 != 0) continue;
					for (int y = 0; y < sz1; y++){
						assertEquals(unit1layerD3_unit1Link.get(x1, x2, x3)[y].noItem, val27_3[x1][x2][x3]/2 + y*10);
					}
				}
			}
		}
		
		// associate using relation yHalf
		unit1layerD3_unit2Link.associate(unit2layerD1_unit2Link, yHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz1];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz1; y1++){
						if (yHalf.contains(new int[]{x1, x2, x3}, new int[]{y1})) {
				       		Unit2 obj = unit1layerD3_unit2Link.get(x1, x2, x3)[xLen[x1][x2][x3]];
				       		assertEquals(obj, unit2layerD1_unit2Link.get(y1)[yLen[y1]]);
				       		assertEquals(obj.noItem, 7);
				       		intCheckSum += obj.noItem;
				       		xLen[x1][x2][x3]++; yLen[y1]++;
						}	
			       	}	
				}
			}
		}
		assertEquals(intCheckSum, 756);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit2[] vector = unit1layerD3_unit2Link.get(x1, x2, x3);
					if (xLen[x1][x2][x3] > 0) {
						assertEquals(vector.length, xLen[x1][x2][x3]);
						assertEquals(vector.length, 4);
					} else {
						fail();
					}
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz1; y1++){
			Unit2[] vector = unit2layerD1_unit2Link.get(y1);
			if (yLen[y1] > 0) {
				assertEquals(vector.length, yLen[y1]);
				assertEquals(vector.length, 27);
			} else {
				assertTrue(y1<=(sz1/2));
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz1; x1++){
			if ((x1 <= sz1/2)) continue;
			for (int y = 0; y < 27; y++){
				unit2layerD1_unit2Link.get(x1)[y].noItem = (x1-6 + y);
			}
			
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y = 0; y < 4; y++){
						assertEquals(unit1layerD3_unit2Link.get(x1, x2, x3)[y].noItem, val27_3[x1][x2][x3] + y);
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
		
		final int[] val5_1 = new int[]{ 0, 1, 2, 3, 4 };
		final int[] map1_x1x2x3 = new int[]{ 0, 2, 2, 2, 4, 2, 2, 4, 4, 2, 4, 4, 4, 6 };
		final int[] map2_x1x2x3 = new int[]{ 0, 1, 2, 1, 2, 3, 2, 3, 4, 1, 2, 3, 2, 3, 4, 3, 4, 5, 2, 3, 4, 3, 4, 5, 4, 5, 6 };
		
		// init arrays
		Unit1[][][] unit1arrayD3 = new Unit1[sz3][sz3][sz3];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					if ((i+j+k)%2 == 0) unit1arrayD3[i][j][k] = new Unit1(i+j+k);
				}
			}
		}
		for (int i = 0; i < sz1; i++) {	
			if (i%2 == 0) unit2arrayD1[i] = new Unit2(i); 
		}
		
		// init NN layers
		Layer_Unit1_ unit1layerD3 = new Layer_Unit1_(unit1arrayD3);
		Layer_Unit2_ unit2layerD1 = new Layer_Unit2_(unit2arrayD1);
		
		BasedVectorLayer<Unit1.SignalDouble> unit2layerD1_doubleVector = unit2layerD1.returnDoubleVector();
		BasedVectorLayer<Unit1>              unit1layerD3_unit1Link = unit1layerD3.returnUnit1Link();
		BasedVectorLayer<Unit2>              unit1layerD3_unit2Link = unit1layerD3.returnUnit2Link();
		BasedVectorLayer<Unit2>              unit2layerD1_unit2Link = unit2layerD1.returnUnit2Link();
		
		// variables used for checking
		int[][][] xLen; 
		int[] yLen;
		int intCheckSum;
		double doubleCheckSum;

		// associate using relation full
		unit1layerD3.intVector.associate(unit2layerD1.intVector, full);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz1];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (unit1layerD3.getUnit(x1, x2, x3) == null) continue;
					for (int y1 = 0; y1 < sz1; y1++){
							if (unit2layerD1.getUnit(y1) == null) continue;
							if (full.contains(new int[]{x1, x2, x3}, new int[]{y1})) {
								Unit1.SignalInt obj = unit1layerD3.intVector.get(x1, x2, x3)[xLen[x1][x2][x3]];
					       		assertEquals(obj, unit2layerD1.intVector.get(y1)[yLen[y1]]);
					       		assertEquals(obj.val, 7);
					       		intCheckSum += obj.val;
					       		xLen[x1][x2][x3]++; yLen[y1]++;
							}
						}
			       }
			}
		}
		assertEquals(intCheckSum, 490);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (unit1layerD3.getUnit(x1, x2, x3) == null) continue;
					Unit1.SignalInt[] vector = unit1layerD3.intVector.get(x1, x2, x3);
					if (xLen[x1][x2][x3] > 0) {
						assertEquals(vector.length, xLen[x1][x2][x3]);
						assertEquals(vector.length, 5);
					} else {
						fail();
					}
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz1; y1++){
			if (unit2layerD1.getUnit(y1) == null) continue;
			Unit1.SignalInt[] vector = unit2layerD1.intVector.get(y1);
			if (yLen[y1] > 0) {
				assertEquals(vector.length, yLen[y1]);
				assertEquals(vector.length, 14);
			} else {
				fail();
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz1; x1++){
			if (unit2layerD1.getUnit(x1) == null) continue;
			for (int y = 0; y < 14; y++){
				unit2layerD1.intVector.get(x1)[y].setVal(val5_1[x1/2]*5 + map1_x1x2x3[y]);
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (unit1layerD3.getUnit(x1, x2, x3) == null) continue;
					for (int y = 0; y < 5; y++){
						assertEquals(unit1layerD3.intVector.get(x1, x2, x3)[y].getVal(), val5_1[y]*5 + x1+x2+x3);
					}
				}
			}
		}
		
		// associate with relation rand
		unit1layerD3.doubleVector.associate(unit2layerD1_doubleVector, rand);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz1];
		doubleCheckSum = 0;
		int checkNo = trueno1;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz1; y1++){
						if (rand.contains(new int[]{x1, x2, x3}, new int[]{y1})) {
							if (unit1layerD3.getUnit(x1, x2, x3) == null) {
								checkNo--;
								continue;
							}
							Unit1.SignalDouble obj = unit1layerD3.doubleVector.get(x1, x2, x3)[xLen[x1][x2][x3]];
				       		assertEquals(obj, unit2layerD1_doubleVector.get(y1)[yLen[y1]]);
				       		assertEquals(obj.val, 5.0);
				       		doubleCheckSum += obj.val;
				       		xLen[x1][x2][x3]++; yLen[y1]++;
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
		for (int y1 = 0; y1 < sz1; y1++){
			Unit1.SignalDouble[] vector = unit2layerD1_doubleVector.get(y1);
			if (yLen[y1] > 0) {
				assertEquals(vector.length, yLen[y1]);
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz1; x1++){
			int sz = unit2layerD1_doubleVector.get(x1).length;
			for (int y = 0; y < sz; y++){
				unit2layerD1_doubleVector.get(x1)[y].setVal(3.14);
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
		unit1layerD3_unit1Link.associate(unit2layerD1.unit1Link, xHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz1];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz1; y1++){
						if (xHalf.contains(new int[]{x1, x2, x3}, new int[]{y1})) {
							if (unit2layerD1.getUnit(y1) == null) continue;
							Unit1 obj = unit1layerD3_unit1Link.get(x1, x2, x3)[xLen[x1][x2][x3]];
				       		assertEquals(obj, unit2layerD1.unit1Link.get(y1)[yLen[y1]]);
				       		assertEquals(obj.noItem, 3);
				       		intCheckSum += obj.noItem;
				       		xLen[x1][x2][x3]++; yLen[y1]++;
						}	
					}
				}
			}
		}
		assertEquals(intCheckSum, 210);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1[] vector = unit1layerD3_unit1Link.get(x1, x2, x3);
					if (xLen[x1][x2][x3] > 0) {
						assertEquals(vector.length, xLen[x1][x2][x3]);
						assertEquals(xLen[x1][x2][x3], 5);
					}
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz1; y1++){
			if (unit2layerD1.getUnit(y1) == null) continue;
			Unit1[] vector = unit2layerD1.unit1Link.get(y1);
			if (yLen[y1] > 0) {
				assertEquals(vector.length, yLen[y1]);
				assertEquals(yLen[y1], 14);
			} else {
				fail();
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz1; x1++){
			if (unit2layerD1.getUnit(x1) == null) continue;
			for (int y = 0; y < 14; y++){
				unit2layerD1.unit1Link.get(x1)[y].noItem = (val5_1[x1/2]*5 + map1_x1x2x3[y]);
			}
			
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (unit1layerD3.getUnit(x1, x2, x3) == null) continue;
					for (int y = 0; y < 5; y++){
						assertEquals(unit1layerD3_unit1Link.get(x1, x2, x3)[y].noItem, val5_1[y]*5 + x1+x2+x3);
					}
				}
			}
		}
		
		// associate with relation yHalf
		unit1layerD3_unit2Link.associate(unit2layerD1_unit2Link, yHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz3][sz3][sz3];
		yLen   = new int[sz1];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz1; y1++){
						if (yHalf.contains(new int[]{x1, x2, x3}, new int[]{y1})) {
							Unit2 obj = unit1layerD3_unit2Link.get(x1, x2, x3)[xLen[x1][x2][x3]];
				       		assertEquals(obj, unit2layerD1_unit2Link.get(y1)[yLen[y1]]);
				       		assertEquals(obj.noItem, 7);
				       		intCheckSum += obj.noItem;
				       		xLen[x1][x2][x3]++; yLen[y1]++;
						}	
			       	}	
				}
			}
		}
		assertEquals(intCheckSum, 756);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit2[] vector = unit1layerD3_unit2Link.get(x1, x2, x3);
					if (xLen[x1][x2][x3] > 0) {
						assertEquals(vector.length, xLen[x1][x2][x3]);
						assertEquals(xLen[x1][x2][x3], 4);
					} else {
						fail();
					}
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz1; y1++){
			Unit2[] vector = unit2layerD1_unit2Link.get(y1);
			if (yLen[y1] > 0) {
				assertEquals(vector.length, yLen[y1]);
				assertEquals(yLen[y1], 27);
			} else {
				assertTrue(y1 < 6);
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz1; x1++){
			if (x1 < 6) continue;
			for (int y = 0; y < 27; y++){
				unit2layerD1_unit2Link.get(x1)[y].noItem = (val5_1[x1-6]*5 + map2_x1x2x3[y]);
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y = 0; y < 4; y++){
						assertEquals(unit1layerD3_unit2Link.get(x1, x2, x3)[y].noItem, val5_1[y]*5 + x1+x2+x3);
					}
				}
			}
		}
		
	}
	
}

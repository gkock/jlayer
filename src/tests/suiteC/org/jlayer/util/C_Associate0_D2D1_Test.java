package org.jlayer.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Random;

import org.jlayer.model.*;

import basic.units.*;

/**
 * Tests for {@code VectorLayer*.D2.associate(VectorLayer.D1<?,T> layer, Relation rel)}. 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Associate0_D2D1_Test {
	
	final int sz1 = 10;
	final int sz2 = 4;
//	final int sz3 = 3;
	
	boolean[][][] random1 = new boolean[sz2][sz2][sz1];
	int           trueno1 = 0;
	
	C_Associate0_D2D1_Test() {
		Random generator = new Random(System.nanoTime());
		for (int x1 = 0; x1 < sz2; x1++){
				for (int x2 = 0; x2 < sz2; x2++){
					for (int y1 = 0; y1 < sz1; y1++){
						if (generator.nextBoolean()) {
							random1[x1][x2][y1] = true;
							trueno1++;
						} else {
							random1[x1][x2][y1] = false;		
						}
				}
			}
		}
	}
	
	Relation full = new Relation(){
		@Override
		public boolean contains(int[] x, int[] y){ return true; }
	};
	Relation rand = new IndexTools.D2D1(){
		@Override
		public boolean contains(int x1, int x2, int y1){ 
			return random1[x1][x2][y1]; 
		}
	};
	Relation xHalf = new IndexTools.D2D1(){
		@Override
		public boolean contains(int x1, int x2, int y1){
			return ((x1+x2)%2 == 0) ? true : false; 
		}
	};
	Relation yHalf = new IndexTools.D2D1(){
		@Override
		public boolean contains(int x1, int x2, int y1){
			return (y1 > sz1/2) ? true : false; 
		}
	};
	
	/****************************************************************************/
	
	@Test // testing VectorLayer*.associate(VectorLayer<?,T> layer, Relation rel)
          // fully defined layer bases
	public void testVectorLayerNN1() {
		AssociateUtil.setAssociateForkJoinFlag(false);
		
		final int[][] val16_2 = new int[][]{ {0,1,2,3}, {4,5,6,7}, {8,9,10,11}, {12,13,14,15} };
		
		// init arrays
		Unit1[][] unit1arrayD2 = new Unit1[sz2][sz2];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				unit1arrayD2[i][j] = new Unit1(i+j);
			}
		}
		for (int i = 0; i < sz1; i++) {	
			unit2arrayD1[i] = new Unit2(i); 
		}
		
		// init NN layers
		Layer_Unit1_ unit1layerD2 = new Layer_Unit1_(unit1arrayD2);
		Layer_Unit2_ unit2layerD1 = new Layer_Unit2_(unit2arrayD1);
		
		BasedVectorLayer<Unit1.SignalDouble> unit2layerD1_doubleVector = unit2layerD1.returnDoubleVector();
		BasedVectorLayer<Unit1>              unit1layerD2_unit1Link = unit1layerD2.returnUnit1Link();
		BasedVectorLayer<Unit2>              unit1layerD2_unit2Link = unit1layerD2.returnUnit2Link();
		BasedVectorLayer<Unit2>              unit2layerD1_unit2Link = unit2layerD1.returnUnit2Link();
		
		// variables used for checking
		int[][] xLen;
		int[] yLen;
		int intCheckSum;
		double doubleCheckSum;
		
		// associate using relation full
		unit1layerD2.intVector.associate(unit2layerD1.intVector, full);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz1];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y1 = 0; y1 < sz1; y1++){
					if (full.contains(new int[]{x1, x2}, new int[]{y1})) {
			       		Unit1.SignalInt obj = unit1layerD2.intVector.get(x1, x2)[xLen[x1][x2]];
			       		assertEquals(obj, unit2layerD1.intVector.get(y1)[yLen[y1]]);
			       		assertEquals(obj.val, 7);
			       		intCheckSum += obj.val;
			       		xLen[x1][x2]++; yLen[y1]++;
					}
				}	
			}
		}
		assertEquals(intCheckSum, 1120);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit1.SignalInt[] vector = unit1layerD2.intVector.get(x1, x2);
				if (xLen[x1][x2] > 0) {
					assertEquals(vector.length, xLen[x1][x2]);
					assertEquals(vector.length, sz1);
				} else {
					fail();
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz1; y1++){
			Unit1.SignalInt[] vector = unit2layerD1.intVector.get(y1);
			if (yLen[y1] > 0) {
				assertEquals(vector.length, yLen[y1]);
				assertEquals(vector.length, sz2*sz2);
			} else {
				fail();
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz1; x1++){
			for (int y = 0; y < sz2*sz2; y++){
				unit2layerD1.intVector.get(x1)[y].setVal(x1*10 + y);
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y = 0; y < sz1; y++){
					assertEquals(unit1layerD2.intVector.get(x1, x2)[y].getVal(), val16_2[x1][x2] + y*10);
				}
			}
		}
		
		// associate using relation rand
		unit1layerD2.doubleVector.associate(unit2layerD1_doubleVector, rand);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz1];
		doubleCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y1 = 0; y1 < sz1; y1++){
					if (rand.contains(new int[]{x1, x2}, new int[]{y1})) {
			       		Unit1.SignalDouble obj = unit1layerD2.doubleVector.get(x1, x2)[xLen[x1][x2]];
			       		assertEquals(obj, unit2layerD1_doubleVector.get(y1)[yLen[y1]]);
			       		assertEquals(obj.val, 5.0);
			       		doubleCheckSum += obj.val;
			       		xLen[x1][x2]++; yLen[y1]++;
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
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				int sz = unit1layerD2.doubleVector.get(x1, x2).length;
				for (int y = 0; y < sz; y++){
					assertEquals(unit1layerD2.doubleVector.get(x1, x2)[y].getVal(), 3.14);
				}
			}
		}
		
		// associate using relation xHalf
		unit1layerD2_unit1Link.associate(unit2layerD1.unit1Link, xHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz1];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y1 = 0; y1 < sz1; y1++){
					if (xHalf.contains(new int[]{x1, x2}, new int[]{y1})) {
			       		Unit1 obj = unit1layerD2_unit1Link.get(x1, x2)[xLen[x1][x2]];
			       		assertEquals(obj, unit2layerD1.unit1Link.get(y1)[yLen[y1]]);
			       		assertEquals(obj.noItem, 3);
			       		intCheckSum += obj.noItem;
			       		xLen[x1][x2]++; yLen[y1]++;
					}
		       	}	
			}
		}
		assertEquals(intCheckSum, 240);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit1[] vector = unit1layerD2_unit1Link.get(x1, x2);
				if (xLen[x1][x2] > 0) {
					assertEquals(vector.length, xLen[x1][x2]);
					assertEquals(vector.length, sz1);
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz1; y1++){
			Unit1[] vector = unit2layerD1.unit1Link.get(y1);
			if (yLen[y1] > 0) {
				assertEquals(vector.length, yLen[y1]);
				assertEquals(vector.length, 8);
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz1; x1++){
			for (int y = 0; y < 8; y++){
				unit2layerD1.unit1Link.get(x1)[y].noItem = (x1*10 + y);
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if ((x1+x2)%2 != 0) continue;
				for (int y = 0; y < sz1; y++){
					assertEquals(unit1layerD2_unit1Link.get(x1, x2)[y].noItem, val16_2[x1][x2]/2 + y*10);
				}
			}
		}
		
		// associate using relation yHalf
		unit1layerD2_unit2Link.associate(unit2layerD1_unit2Link, yHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz1];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y1 = 0; y1 < sz1; y1++){
					if (yHalf.contains(new int[]{x1, x2}, new int[]{y1})) {
			       		Unit2 obj = unit1layerD2_unit2Link.get(x1, x2)[xLen[x1][x2]];
			       		assertEquals(obj, unit2layerD1_unit2Link.get(y1)[yLen[y1]]);
			       		assertEquals(obj.noItem, 7);
			       		intCheckSum += obj.noItem;
			       		xLen[x1][x2]++; yLen[y1]++;
					}	
		       	}	
			}
		}
		assertEquals(intCheckSum, 448);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit2[] vector = unit1layerD2_unit2Link.get(x1, x2);
				if (xLen[x1][x2] > 0) {
					assertEquals(vector.length, xLen[x1][x2]);
					assertEquals(vector.length, 4);
				} else {
					fail();
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz1; y1++){
			Unit2[] vector = unit2layerD1_unit2Link.get(y1);
			if (yLen[y1] > 0) {
				assertEquals(vector.length, yLen[y1]);
				assertEquals(vector.length, 16);
			} else {
				assertTrue(y1<=(sz1/2));
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz1; x1++){
			if ((x1 <= sz1/2)) continue;
			for (int y = 0; y < 16; y++){
				unit2layerD1_unit2Link.get(x1)[y].noItem = (x1-6 + y);
			}
			
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y = 0; y < 4; y++){
					assertEquals(unit1layerD2_unit2Link.get(x1, x2)[y].noItem, val16_2[x1][x2] + y);
				}
			}
		}
		
	}
	
	
	/****************************************************************************/
	
	@Test  // testing VectorLayer*.associate(VectorLayer<?,T> layer, Relation rel)
    	  // partially defined layer bases
	public void testVectorLayerNN2() {
		AssociateUtil.setAssociateForkJoinFlag(false);
		
		final int[] val5_1 = new int[]{ 0, 1, 2, 3, 4 };
		final int[] map1_x1x2 = new int[]{ 0, 2, 2, 4, 2, 4, 4, 6 };
		final int[] map2_x1x2 = new int[]{ 0, 1, 2, 3, 1, 2, 3, 4, 2, 3, 4, 5, 3, 4, 5, 6 };
		
		// init arrays
		Unit1[][] unit1arrayD2 = new Unit1[sz2][sz2];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				if ((i+j)%2 == 0) unit1arrayD2[i][j] = new Unit1(i+j);
			}
		}
		for (int i = 0; i < sz1; i++) {	
			if (i%2 != 0) unit2arrayD1[i] = new Unit2(i); 
		}
		
		// init NN layers
		Layer_Unit1_ unit1layerD2 = new Layer_Unit1_(unit1arrayD2);
		Layer_Unit2_ unit2layerD1 = new Layer_Unit2_(unit2arrayD1);
		
		BasedVectorLayer<Unit1.SignalDouble> unit2layerD1_doubleVector = unit2layerD1.returnDoubleVector();
		BasedVectorLayer<Unit1>              unit1layerD2_unit1Link = unit1layerD2.returnUnit1Link();
		BasedVectorLayer<Unit2>              unit1layerD2_unit2Link = unit1layerD2.returnUnit2Link();
		BasedVectorLayer<Unit2>              unit2layerD1_unit2Link = unit2layerD1.returnUnit2Link();
		
		// variables used for checking
		int[][] xLen;
		int[] yLen;
		int intCheckSum;
		double doubleCheckSum;

		// associate using relation full
		unit1layerD2.intVector.associate(unit2layerD1.intVector, full);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz1];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){	
			if (unit1layerD2.getUnit(x1, x2) == null) continue;
			for (int y1 = 0; y1 < sz1; y1++){
					if (unit2layerD1.getUnit(y1) == null) continue;
					if (full.contains(new int[]{x1, x2}, new int[]{y1})) {
						Unit1.SignalInt obj = unit1layerD2.intVector.get(x1, x2)[xLen[x1][x2]];
			       		assertEquals(obj, unit2layerD1.intVector.get(y1)[yLen[y1]]);
			       		assertEquals(obj.val, 7);
			       		intCheckSum += obj.val;
			       		xLen[x1][x2]++; yLen[y1]++;
					}
				}
	       	}	
		}
		assertEquals(intCheckSum, 280);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if (unit1layerD2.getUnit(x1, x2) == null) continue;
				Unit1.SignalInt[] vector = unit1layerD2.intVector.get(x1, x2);
				if (xLen[x1][x2] > 0) {
					assertEquals(vector.length, xLen[x1][x2]);
					assertEquals(vector.length, 5);
				} else {
					fail();
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz1; y1++){
			if (unit2layerD1.getUnit(y1) == null) continue;
			Unit1.SignalInt[] vector = unit2layerD1.intVector.get(y1);
			if (yLen[y1] > 0) {
				assertEquals(vector.length, yLen[y1]);
				assertEquals(vector.length, 8);
			} else {
				fail();
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz1; x1++){
			if (unit2layerD1.getUnit(x1) == null) continue;
			for (int y = 0; y < 8; y++){
				unit2layerD1.intVector.get(x1)[y].setVal(val5_1[x1/2]*5 + map1_x1x2[y]);
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if (unit1layerD2.getUnit(x1, x2) == null) continue;
				for (int y = 0; y < 5; y++){
					assertEquals(unit1layerD2.intVector.get(x1, x2)[y].getVal(), val5_1[y]*5 + x1+x2);
				}
			}
		}
		
		// associate with relation rand
		unit1layerD2.doubleVector.associate(unit2layerD1_doubleVector, rand);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz1];
		doubleCheckSum = 0;
		int checkNo = trueno1;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){	
				for (int y1 = 0; y1 < sz1; y1++){
					if (rand.contains(new int[]{x1, x2}, new int[]{y1})) {
						if (unit1layerD2.getUnit(x1, x2) == null) {
							checkNo--;
							continue;
						}
						Unit1.SignalDouble obj = unit1layerD2.doubleVector.get(x1, x2)[xLen[x1][x2]];
			       		assertEquals(obj, unit2layerD1_doubleVector.get(y1)[yLen[y1]]);
			       		assertEquals(obj.val, 5.0);
			       		doubleCheckSum += obj.val;
			       		xLen[x1][x2]++; yLen[y1]++;
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
		unit1layerD2_unit1Link.associate(unit2layerD1.unit1Link, xHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz1];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){	
				for (int y1 = 0; y1 < sz1; y1++){
					if (xHalf.contains(new int[]{x1, x2}, new int[]{y1})) {
						if (unit2layerD1.getUnit(y1) == null) continue;
						Unit1 obj = unit1layerD2_unit1Link.get(x1, x2)[xLen[x1][x2]];
			       		assertEquals(obj, unit2layerD1.unit1Link.get(y1)[yLen[y1]]);
			       		assertEquals(obj.noItem, 3);
			       		intCheckSum += obj.noItem;
			       		xLen[x1][x2]++; yLen[y1]++;
					}	
				}
			}
		}
		assertEquals(intCheckSum, 120);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){	
				Unit1[] vector = unit1layerD2_unit1Link.get(x1, x2);
				if (xLen[x1][x2] > 0) {
					assertEquals(vector.length, xLen[x1][x2]);
					assertEquals(xLen[x1][x2], 5);
				}
			}
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz1; y1++){
			if (unit2layerD1.getUnit(y1) == null) continue;
			Unit1[] vector = unit2layerD1.unit1Link.get(y1);
			if (yLen[y1] > 0) {
				assertEquals(vector.length, yLen[y1]);
				assertEquals(yLen[y1], 8);
			} else {
				fail();
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz1; x1++){
			if (unit2layerD1.getUnit(x1) == null) continue;
			for (int y = 0; y < 8; y++){
				unit2layerD1.unit1Link.get(x1)[y].noItem = (val5_1[x1/2]*5 + map1_x1x2[y]);
			}
			
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if (unit1layerD2.getUnit(x1, x2) == null) continue;
				for (int y = 0; y < 5; y++){
					assertEquals(unit1layerD2_unit1Link.get(x1, x2)[y].noItem, val5_1[y]*5 + x1+x2);
				}
			}
		}
		
		// associate with relation yHalf
		unit1layerD2_unit2Link.associate(unit2layerD1_unit2Link, yHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz2][sz2];
		yLen   = new int[sz1];
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){	
				for (int y1 = 0; y1 < sz1; y1++){
					if (yHalf.contains(new int[]{x1, x2}, new int[]{y1})) {
						Unit2 obj = unit1layerD2_unit2Link.get(x1, x2)[xLen[x1][x2]];
			       		assertEquals(obj, unit2layerD1_unit2Link.get(y1)[yLen[y1]]);
			       		assertEquals(obj.noItem, 7);
			       		intCheckSum += obj.noItem;
			       		xLen[x1][x2]++; yLen[y1]++;
					}	
		       	}	
			}
		}
		assertEquals(intCheckSum, 448);
		// checking the length of a link vector
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){	
				Unit2[] vector = unit1layerD2_unit2Link.get(x1, x2);
				if (xLen[x1][x2] > 0) {
					assertEquals(vector.length, xLen[x1][x2]);
					assertEquals(xLen[x1][x2], 4);
				} else {
					fail();
				}
			}
				
		}
		// checking the length of the other link vector
		for (int y1 = 0; y1 < sz1; y1++){
			Unit2[] vector = unit2layerD1_unit2Link.get(y1);
			if (yLen[y1] > 0) {
				assertEquals(vector.length, yLen[y1]);
				assertEquals(yLen[y1], 16);
			} else {
				assertTrue(y1 < 6);
			}
		}
		// set and check the associated signals
		for (int x1 = 0; x1 < sz1; x1++){
			if (x1 < 6) continue;
			for (int y = 0; y < 16; y++){
				unit2layerD1_unit2Link.get(x1)[y].noItem = (val5_1[x1-6]*5 + map2_x1x2[y]);
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y = 0; y < 4; y++){
					assertEquals(unit1layerD2_unit2Link.get(x1, x2)[y].noItem, val5_1[y]*5 + x1+x2);
				}
			}
		}
		
	}
	
}

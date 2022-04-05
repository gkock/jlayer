package org.jlayer.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Random;

import org.jlayer.model.*;

import basic.units.*;

/**
 * Testing {@code VectorLayerAdapter.D1.associate(VectorLayer.D1<?,T> layer, Relation rel)}
 *     and {@code VectorLayerBase.D1.associate(VectorLayer.D1<?,T> layer, Relation rel)}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Associate1_D1D1_Test {
	
	final int sz1 = 10;
//	final int sz2 = 4;
//	final int sz3 = 3;
	
	boolean[][] random1 = new boolean[sz1][sz1];
	int         trueno1 = 0;
	
	C_Associate1_D1D1_Test() {
		Random generator = new Random(System.nanoTime());
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (generator.nextBoolean()) {
					random1[x][y] = true;
					trueno1++;
				} else {
					random1[x][y] = false;
				}
				
			}
		}
	}
	
	Relation full = new Relation(){
		@Override
		public boolean contains(int[] x, int[] y){ return true; }
	};
	Relation rand = new IndexTools.D1D1(){
		@Override
		public boolean contains(int x, int y){ return random1[x][y]; }
	};
	Relation xHalf = new IndexTools.D1D1(){
		@Override
		public boolean contains(int x, int y){ return (x%2 == 0) ? true : false; }
	};
	Relation yHalf = new IndexTools.D1D1(){
		@Override
		public boolean contains(int x, int y){ return (y >= sz1/2) ? true : false; }
	};
	
	/****************************************************************************/
	
	@Test // VectorLayer*.D1.associate(VectorLayer.D1<?,T> layer, Relation rel)
		  // fully defined layer bases, relation full
	public void testVectorLayerD1D1full() {
		AssociateUtil.setAssociateForkJoinFlag(true);
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			unit1arrayD1[i] = new Unit1(i); 
			unit2arrayD1[i] = new Unit2(i);
		}
		
		// init D1 layers
		Layer_Unit1_.D1 unit1layerD1 = new Layer_Unit1_.D1(unit1arrayD1);
		Layer_Unit2_.D1 unit2layerD1 = new Layer_Unit2_.D1(unit2arrayD1);
		
		BasedVectorLayer.D1<Unit1.SignalDouble> unit2layerD1_doubleVector = unit2layerD1.returnDoubleVector();
		BasedVectorLayer.D1<Unit1>              unit1layerD1_unit1Link = unit1layerD1.returnUnit1Link();
		BasedVectorLayer.D1<Unit2>              unit1layerD1_unit2Link = unit1layerD1.returnUnit2Link();
		BasedVectorLayer.D1<Unit2>              unit2layerD1_unit2Link = unit2layerD1.returnUnit2Link();
		
		// variables used for checking
		int[] xLen = new int[sz1], yLen = new int[sz1];
		int intCheckSum = 0, unit1CheckSum = 0, unit2CheckSum = 0;
		double doubleCheckSum = 0.0;
		
		// associate using relation full
		unit1layerD1.intVector.associate(unit2layerD1.intVector, full);
		unit1layerD1.doubleVector.associate(unit2layerD1_doubleVector, full);
		unit1layerD1_unit1Link.associate(unit2layerD1.unit1Link, full);
		unit1layerD1_unit2Link.associate(unit2layerD1_unit2Link, full);
		
		// computing the length of the link vectors and checking the signals
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (full.contains(new int[]{x}, new int[]{y})) {
					
					Unit1.SignalInt intObj = unit1layerD1.intVector.get(x)[xLen[x]];
		       		assertEquals(intObj, unit2layerD1.intVector.get(y)[yLen[y]]);
		       		assertEquals(intObj.val, 7);
		       		intCheckSum += intObj.val;
		       		
		       		Unit1.SignalDouble doubleObj = unit1layerD1.doubleVector.get(x)[xLen[x]];
		       		assertEquals(doubleObj, unit2layerD1_doubleVector.get(y)[yLen[y]]);
		       		assertEquals(doubleObj.val, 5.0);
		       		doubleCheckSum += doubleObj.val;
		       		
		       		Unit1 unit1Obj = unit1layerD1_unit1Link.get(x)[xLen[x]];
		       		assertEquals(unit1Obj, unit2layerD1.unit1Link.get(y)[yLen[y]]);
		       		assertEquals(unit1Obj.noItem, 3);
		       		unit1CheckSum += unit1Obj.noItem;
		       		
		       		Unit2 unit2Obj = unit1layerD1_unit2Link.get(x)[xLen[x]];
		       		assertEquals(unit2Obj, unit2layerD1_unit2Link.get(y)[yLen[y]]);
		       		assertEquals(unit2Obj.noItem, 7);
		       		unit2CheckSum += unit2Obj.noItem;
		       		
		       		xLen[x]++; yLen[y]++;
		       	}	
			}
		}
		assertEquals(intCheckSum, 700);
		assertEquals(doubleCheckSum, 500.0);
		assertEquals(unit1CheckSum, 300);
		assertEquals(unit2CheckSum, 700);
		
		// checking the length of unit1 link vector
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] intVector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] doubleVector = unit1layerD1.doubleVector.get(x);
			Unit1[] unit1Link1 = unit1layerD1.unit1Link.get(x);
			Unit1[] unit1Link2 = unit1layerD1_unit1Link.get(x);
			Unit2[] unit2Link1 = unit1layerD1.unit2Link.get(x);
			Unit2[] unit2Link2 = unit1layerD1_unit2Link.get(x);
			if (xLen[x] > 0) {
				assertEquals(xLen[x], sz1);
				assertEquals(intVector.length, xLen[x]);
				assertEquals(doubleVector.length, xLen[x]);
				assertEquals(unit1Link1, null);
				assertEquals(unit1Link2.length, xLen[x]);
				assertEquals(unit2Link1, null);
				assertEquals(unit2Link2.length, xLen[x]);
			} else {
				fail();
			}
		}
		
		// checking the length of unit2 link vector
		for (int y = 0; y < sz1; y++){
			Unit1.SignalInt[] intVector = unit2layerD1.intVector.get(y);
			Unit1.SignalDouble[] doubleVector1 = unit2layerD1.doubleVector.get(y);
			Unit1.SignalDouble[] doubleVector2 = unit2layerD1_doubleVector.get(y);
			Unit1[] unit1Link = unit2layerD1.unit1Link.get(y);
			Unit2[] unit2Link1 = unit2layerD1.unit2Link.get(y);
			Unit2[] unit2Link2 = unit2layerD1_unit2Link.get(y);
			if (yLen[y] > 0) {
				assertEquals(yLen[y], sz1);
				assertEquals(intVector.length, yLen[y]);
				assertEquals(doubleVector1, null);
				assertEquals(doubleVector2.length, yLen[y]);
				assertEquals(unit1Link.length, yLen[y]);
				assertEquals(unit2Link1, null);
				assertEquals(unit2Link2.length, yLen[y]);
			} else {
				fail();
			}
		}
		
		// set and check the associated signals
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++) {
				unit2layerD1.intVector.get(x)[y].setVal(x*10 + y);
				unit2layerD1_doubleVector.get(x)[y].setVal(2*(x*10 + y));
				unit2layerD1.unit1Link.get(x)[y].noItem = 3*(x*10 + y);
				unit2layerD1_unit2Link.get(x)[y].noItem = 4*(x*10 + y);
			}
		}
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++) {
				assertEquals(unit1layerD1.intVector.get(x)[y].getVal(), x + y*10);
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 2.0*(x + y*10));
				assertEquals(unit1layerD1_unit1Link.get(x)[y].noItem, 3*(x + y*10));
				assertEquals(unit1layerD1_unit2Link.get(x)[y].noItem, 4*(x + y*10));
			}
		}
		
	}
	
	
	/****************************************************************************/
	
	@Test // VectorLayer*.D1.associate(VectorLayer.D1<?,T> layer, Relation rel)
	      // fully defined layer bases, relation rand
	public void testVectorLayerD1D1rand() {
		AssociateUtil.setAssociateForkJoinFlag(true);
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			unit1arrayD1[i] = new Unit1(i); 
			unit2arrayD1[i] = new Unit2(i);
		}
		
		// init D1 layers
		Layer_Unit1_.D1 unit1layerD1 = new Layer_Unit1_.D1(unit1arrayD1);
		Layer_Unit2_.D1 unit2layerD1 = new Layer_Unit2_.D1(unit2arrayD1);
		
		BasedVectorLayer.D1<Unit1.SignalDouble> unit2layerD1_doubleVector = unit2layerD1.returnDoubleVector();
		BasedVectorLayer.D1<Unit1>              unit1layerD1_unit1Link = unit1layerD1.returnUnit1Link();
		BasedVectorLayer.D1<Unit2>              unit1layerD1_unit2Link = unit1layerD1.returnUnit2Link();
		BasedVectorLayer.D1<Unit2>              unit2layerD1_unit2Link = unit2layerD1.returnUnit2Link();
		
		// variables used for checking
		int[] xLen = new int[sz1], yLen = new int[sz1];
		int intCheckSum = 0, unit1CheckSum = 0, unit2CheckSum = 0;
		double doubleCheckSum = 0.0;
		
		// associate using relation rand
		unit1layerD1.intVector.associate(unit2layerD1.intVector, rand);
		unit1layerD1.doubleVector.associate(unit2layerD1_doubleVector, rand);
		unit1layerD1_unit1Link.associate(unit2layerD1.unit1Link, rand);
		unit1layerD1_unit2Link.associate(unit2layerD1_unit2Link, rand);
		
		// computing the length of the link vectors and checking the signals
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (rand.contains(new int[]{x}, new int[]{y})) {
					
					Unit1.SignalInt intObj = unit1layerD1.intVector.get(x)[xLen[x]];
		       		assertEquals(intObj, unit2layerD1.intVector.get(y)[yLen[y]]);
		       		assertEquals(intObj.val, 7);
		       		intCheckSum += intObj.val;
		       		
		       		Unit1.SignalDouble doubleObj = unit1layerD1.doubleVector.get(x)[xLen[x]];
		       		assertEquals(doubleObj, unit2layerD1_doubleVector.get(y)[yLen[y]]);
		       		assertEquals(doubleObj.val, 5.0);
		       		doubleCheckSum += doubleObj.val;
		       		
		       		Unit1 unit1Obj = unit1layerD1_unit1Link.get(x)[xLen[x]];
		       		assertEquals(unit1Obj, unit2layerD1.unit1Link.get(y)[yLen[y]]);
		       		assertEquals(unit1Obj.noItem, 3);
		       		unit1CheckSum += unit1Obj.noItem;
		       		
		       		Unit2 unit2Obj = unit1layerD1_unit2Link.get(x)[xLen[x]];
		       		assertEquals(unit2Obj, unit2layerD1_unit2Link.get(y)[yLen[y]]);
		       		assertEquals(unit2Obj.noItem, 7);
		       		unit2CheckSum += unit2Obj.noItem;
		       		
		       		xLen[x]++; yLen[y]++;
		       	}	
			}
		}
		assertEquals(intCheckSum, trueno1 * 7);
		assertEquals(doubleCheckSum, trueno1 * 5.0);
		assertEquals(unit1CheckSum, trueno1 * 3);
		assertEquals(unit2CheckSum, trueno1 * 7);
		
		// checking the length of unit1 link vectors
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] intVector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] doubleVector = unit1layerD1.doubleVector.get(x);
			Unit1[] unit1Link1 = unit1layerD1.unit1Link.get(x);
			Unit1[] unit1Link2 = unit1layerD1_unit1Link.get(x);
			Unit2[] unit2Link1 = unit1layerD1.unit2Link.get(x);
			Unit2[] unit2Link2 = unit1layerD1_unit2Link.get(x);
			if (xLen[x] > 0) {
				assertEquals(intVector.length, xLen[x]);
				assertEquals(doubleVector.length, xLen[x]);
				assertEquals(unit1Link1, null);
				assertEquals(unit1Link2.length, xLen[x]);
				assertEquals(unit2Link1, null);
				assertEquals(unit2Link2.length, xLen[x]);
				
			}
		}
		
		// checking the length of unit2 link vectors
		for (int y = 0; y < sz1; y++){
			Unit1.SignalInt[] intVector = unit2layerD1.intVector.get(y);
			Unit1.SignalDouble[] doubleVector1 = unit2layerD1.doubleVector.get(y);
			Unit1.SignalDouble[] doubleVector2 = unit2layerD1_doubleVector.get(y);
			Unit1[] unit1Link = unit2layerD1.unit1Link.get(y);
			Unit2[] unit2Link1 = unit2layerD1.unit2Link.get(y);
			Unit2[] unit2Link2 = unit2layerD1_unit2Link.get(y);
			if (yLen[y] > 0) {
				assertEquals(intVector.length, yLen[y]);
				assertEquals(doubleVector1, null);
				assertEquals(doubleVector2.length, yLen[y]);
				assertEquals(unit1Link.length, yLen[y]);
				assertEquals(unit2Link1, null);
				assertEquals(unit2Link2.length, yLen[y]);
			}
		}
		
		// set and check the associated signals
		for (int x = 0; x < sz1; x++){
			int sz = unit2layerD1.intVector.get(x).length;
			for (int y = 0; y < sz; y++) {
				unit2layerD1.intVector.get(x)[y].setVal(x*10 + y);
				unit2layerD1_doubleVector.get(x)[y].setVal(2*(x*10 + y));
				unit2layerD1.unit1Link.get(x)[y].noItem = 3*(x*10 + y);
				unit2layerD1_unit2Link.get(x)[y].noItem = 4*(x*10 + y);
			}
		}
		for (int x = 0; x < sz1; x++){
			int sz = unit1layerD1.intVector.get(x).length;
			for (int y = 0; y < sz; y++) {
				int res = unit1layerD1.intVector.get(x)[y].getVal();
				assertEquals(unit1layerD1.intVector.get(x)[y].getVal(), res);
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 2.0*(res));
				assertEquals(unit1layerD1_unit1Link.get(x)[y].noItem, 3*(res));
				assertEquals(unit1layerD1_unit2Link.get(x)[y].noItem, 4*(res));
			}
		}
		
	}
	
	/****************************************************************************/
	
	@Test // VectorLayer*.D1.associate(VectorLayer.D1<?,T> layer, Relation rel)
	      // fully defined layer bases, relation xHalf
	public void testVectorLayerD1D1xHalf() {
		AssociateUtil.setAssociateForkJoinFlag(true);
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			unit1arrayD1[i] = new Unit1(i); 
			unit2arrayD1[i] = new Unit2(i);
		}
		
		// init D1 layers
		Layer_Unit1_.D1 unit1layerD1 = new Layer_Unit1_.D1(unit1arrayD1);
		Layer_Unit2_.D1 unit2layerD1 = new Layer_Unit2_.D1(unit2arrayD1);
		
		BasedVectorLayer.D1<Unit1.SignalDouble> unit2layerD1_doubleVector = unit2layerD1.returnDoubleVector();
		BasedVectorLayer.D1<Unit1>              unit1layerD1_unit1Link = unit1layerD1.returnUnit1Link();
		BasedVectorLayer.D1<Unit2>              unit1layerD1_unit2Link = unit1layerD1.returnUnit2Link();
		BasedVectorLayer.D1<Unit2>              unit2layerD1_unit2Link = unit2layerD1.returnUnit2Link();
		
		// variables used for checking
		int[] xLen = new int[sz1], yLen = new int[sz1];
		int intCheckSum = 0, unit1CheckSum = 0, unit2CheckSum = 0;
		double doubleCheckSum = 0.0;
		
		// associate using relation xHalf
		unit1layerD1.intVector.associate(unit2layerD1.intVector, xHalf);
		unit1layerD1.doubleVector.associate(unit2layerD1_doubleVector, xHalf);
		unit1layerD1_unit1Link.associate(unit2layerD1.unit1Link, xHalf);
		unit1layerD1_unit2Link.associate(unit2layerD1_unit2Link, xHalf);
		
		// computing the length of the link vectors and checking the signals
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (xHalf.contains(new int[]{x}, new int[]{y})) {
					
					Unit1.SignalInt intObj = unit1layerD1.intVector.get(x)[xLen[x]];
		       		assertEquals(intObj, unit2layerD1.intVector.get(y)[yLen[y]]);
		       		assertEquals(intObj.val, 7);
		       		intCheckSum += intObj.val;
		       		
		       		Unit1.SignalDouble doubleObj = unit1layerD1.doubleVector.get(x)[xLen[x]];
		       		assertEquals(doubleObj, unit2layerD1_doubleVector.get(y)[yLen[y]]);
		       		assertEquals(doubleObj.val, 5.0);
		       		doubleCheckSum += doubleObj.val;
		       		
		       		Unit1 unit1Obj = unit1layerD1_unit1Link.get(x)[xLen[x]];
		       		assertEquals(unit1Obj, unit2layerD1.unit1Link.get(y)[yLen[y]]);
		       		assertEquals(unit1Obj.noItem, 3);
		       		unit1CheckSum += unit1Obj.noItem;
		       		
		       		Unit2 unit2Obj = unit1layerD1_unit2Link.get(x)[xLen[x]];
		       		assertEquals(unit2Obj, unit2layerD1_unit2Link.get(y)[yLen[y]]);
		       		assertEquals(unit2Obj.noItem, 7);
		       		unit2CheckSum += unit2Obj.noItem;
		       		
		       		xLen[x]++; yLen[y]++;
		       	}	
			}
		}
		assertEquals(intCheckSum, 350);
		assertEquals(doubleCheckSum, 250.0);
		assertEquals(unit1CheckSum, 150);
		assertEquals(unit2CheckSum, 350);
		
		// checking the length of unit1 link vectors
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] intVector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] doubleVector = unit1layerD1.doubleVector.get(x);
			Unit1[] unit1Link1 = unit1layerD1.unit1Link.get(x);
			Unit1[] unit1Link2 = unit1layerD1_unit1Link.get(x);
			Unit2[] unit2Link1 = unit1layerD1.unit2Link.get(x);
			Unit2[] unit2Link2 = unit1layerD1_unit2Link.get(x);
			if (xLen[x] > 0) {
				assertTrue(x%2 == 0);
				assertEquals(xLen[x], sz1);
				assertEquals(intVector.length, xLen[x]);
				assertEquals(doubleVector.length, xLen[x]);
				assertEquals(unit1Link1, null);
				assertEquals(unit1Link2.length, xLen[x]);
				assertEquals(unit2Link1, null);
				assertEquals(unit2Link2.length, xLen[x]);
			} else {
				assertTrue(x%2 != 0);
				assertEquals(xLen[x], 0);
			}
		}
		
		// checking the length of unit2 link vectors
		for (int y = 0; y < sz1; y++){
			Unit1.SignalInt[] intVector = unit2layerD1.intVector.get(y);
			Unit1.SignalDouble[] doubleVector1 = unit2layerD1.doubleVector.get(y);
			Unit1.SignalDouble[] doubleVector2 = unit2layerD1_doubleVector.get(y);
			Unit1[] unit1Link = unit2layerD1.unit1Link.get(y);
			Unit2[] unit2Link1 = unit2layerD1.unit2Link.get(y);
			Unit2[] unit2Link2 = unit2layerD1_unit2Link.get(y);
			if (yLen[y] > 0) {
				assertEquals(yLen[y], sz1/2);
				assertEquals(intVector.length, yLen[y]);
				assertEquals(doubleVector1, null);
				assertEquals(doubleVector2.length, yLen[y]);
				assertEquals(unit1Link.length, yLen[y]);
				assertEquals(unit2Link1, null);
				assertEquals(unit2Link2.length, yLen[y]);
			} else {
				fail();
			}
		}
		
		// set and check the associated signals
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1/2; y++) {
				unit2layerD1.intVector.get(x)[y].setVal(x*10 + 2*y);
				unit2layerD1_doubleVector.get(x)[y].setVal(2*(x*10 + 2*y));
				unit2layerD1.unit1Link.get(x)[y].noItem = 3*(x*10 + 2*y);
				unit2layerD1_unit2Link.get(x)[y].noItem = 4*(x*10 + 2*y);
			}
		}
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) continue;
			for (int y = 0; y < sz1; y++) {
				assertEquals(unit1layerD1.intVector.get(x)[y].getVal(), x + y*10);
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 2.0*(x + y*10));
				assertEquals(unit1layerD1_unit1Link.get(x)[y].noItem, 3*(x + y*10));
				assertEquals(unit1layerD1_unit2Link.get(x)[y].noItem, 4*(x + y*10));
			}
		}
		
	}
	
	/****************************************************************************/
	
	@Test // VectorLayer*.D1.associate(VectorLayer.D1<?,T> layer, Relation rel)
	      // fully defined layer bases, relation yHalf
	public void testVectorLayerD1D1yHalf() {
		AssociateUtil.setAssociateForkJoinFlag(true);
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			unit1arrayD1[i] = new Unit1(i); 
			unit2arrayD1[i] = new Unit2(i);
		}
		
		// init D1 layers
		Layer_Unit1_.D1 unit1layerD1 = new Layer_Unit1_.D1(unit1arrayD1);
		Layer_Unit2_.D1 unit2layerD1 = new Layer_Unit2_.D1(unit2arrayD1);
		
		BasedVectorLayer.D1<Unit1.SignalDouble> unit2layerD1_doubleVector = unit2layerD1.returnDoubleVector();
		BasedVectorLayer.D1<Unit1>              unit1layerD1_unit1Link = unit1layerD1.returnUnit1Link();
		BasedVectorLayer.D1<Unit2>              unit1layerD1_unit2Link = unit1layerD1.returnUnit2Link();
		BasedVectorLayer.D1<Unit2>              unit2layerD1_unit2Link = unit2layerD1.returnUnit2Link();
		
		// variables used for checking
		int[] xLen = new int[sz1], yLen = new int[sz1];
		int intCheckSum = 0, unit1CheckSum = 0, unit2CheckSum = 0;
		double doubleCheckSum = 0.0;
		
		// associate using relation xHalf
		unit1layerD1.intVector.associate(unit2layerD1.intVector, yHalf);
		unit1layerD1.doubleVector.associate(unit2layerD1_doubleVector, yHalf);
		unit1layerD1_unit1Link.associate(unit2layerD1.unit1Link, yHalf);
		unit1layerD1_unit2Link.associate(unit2layerD1_unit2Link, yHalf);
		
		// computing the length of the link vectors and checking the signals
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (yHalf.contains(new int[]{x}, new int[]{y})) {
					
					Unit1.SignalInt intObj = unit1layerD1.intVector.get(x)[xLen[x]];
		       		assertEquals(intObj, unit2layerD1.intVector.get(y)[yLen[y]]);
		       		assertEquals(intObj.val, 7);
		       		intCheckSum += intObj.val;
		       		
		       		Unit1.SignalDouble doubleObj = unit1layerD1.doubleVector.get(x)[xLen[x]];
		       		assertEquals(doubleObj, unit2layerD1_doubleVector.get(y)[yLen[y]]);
		       		assertEquals(doubleObj.val, 5.0);
		       		doubleCheckSum += doubleObj.val;
		       		
		       		Unit1 unit1Obj = unit1layerD1_unit1Link.get(x)[xLen[x]];
		       		assertEquals(unit1Obj, unit2layerD1.unit1Link.get(y)[yLen[y]]);
		       		assertEquals(unit1Obj.noItem, 3);
		       		unit1CheckSum += unit1Obj.noItem;
		       		
		       		Unit2 unit2Obj = unit1layerD1_unit2Link.get(x)[xLen[x]];
		       		assertEquals(unit2Obj, unit2layerD1_unit2Link.get(y)[yLen[y]]);
		       		assertEquals(unit2Obj.noItem, 7);
		       		unit2CheckSum += unit2Obj.noItem;
		       		
		       		xLen[x]++; yLen[y]++;
		       	}	
			}
		}
		assertEquals(intCheckSum, 350);
		assertEquals(doubleCheckSum, 250.0);
		assertEquals(unit1CheckSum, 150);
		assertEquals(unit2CheckSum, 350);
		
		// checking the length of unit1 link vectors
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] intVector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] doubleVector = unit1layerD1.doubleVector.get(x);
			Unit1[] unit1Link1 = unit1layerD1.unit1Link.get(x);
			Unit1[] unit1Link2 = unit1layerD1_unit1Link.get(x);
			Unit2[] unit2Link1 = unit1layerD1.unit2Link.get(x);
			Unit2[] unit2Link2 = unit1layerD1_unit2Link.get(x);
			if (xLen[x] > 0) {
				assertEquals(xLen[x], sz1/2);
				assertEquals(intVector.length, xLen[x]);
				assertEquals(doubleVector.length, xLen[x]);
				assertEquals(unit1Link1, null);
				assertEquals(unit1Link2.length, xLen[x]);
				assertEquals(unit2Link1, null);
				assertEquals(unit2Link2.length, xLen[x]);
			} else {
				fail();
			}
		}
		
		// checking the length of unit2 link vectors
		for (int y = 0; y < sz1; y++){
			Unit1.SignalInt[] intVector = unit2layerD1.intVector.get(y);
			Unit1.SignalDouble[] doubleVector1 = unit2layerD1.doubleVector.get(y);
			Unit1.SignalDouble[] doubleVector2 = unit2layerD1_doubleVector.get(y);
			Unit1[] unit1Link = unit2layerD1.unit1Link.get(y);
			Unit2[] unit2Link1 = unit2layerD1.unit2Link.get(y);
			Unit2[] unit2Link2 = unit2layerD1_unit2Link.get(y);
			if (yLen[y] > 0) {
				assertTrue(y >= sz1/2);
				assertEquals(yLen[y], sz1);
				assertEquals(intVector.length, yLen[y]);
				assertEquals(doubleVector1, null);
				assertEquals(doubleVector2.length, yLen[y]);
				assertEquals(unit1Link.length, yLen[y]);
				assertEquals(unit2Link1, null);
				assertEquals(unit2Link2.length, yLen[y]);
			} else {
				assertTrue(y < sz1/2);
				assertEquals(yLen[y], 0);
			}
		}
		
		// set and check the associated signals
		for (int x = sz1/2; x < sz1; x++){
			for (int y = 0; y < sz1; y++) {
				unit2layerD1.intVector.get(x)[y].setVal(x*10 + y);
				unit2layerD1_doubleVector.get(x)[y].setVal(2*(x*10 + y));
				unit2layerD1.unit1Link.get(x)[y].noItem = 3*(x*10 + y);
				unit2layerD1_unit2Link.get(x)[y].noItem = 4*(x*10 + y);
			}
		}
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1/2; y++) {
				assertEquals(unit1layerD1.intVector.get(x)[y].getVal(), x + (y+5)*10);
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 2.0*(x + (y+5)*10));
				assertEquals(unit1layerD1_unit1Link.get(x)[y].noItem, 3*(x + (y+5)*10));
				assertEquals(unit1layerD1_unit2Link.get(x)[y].noItem, 4*(x + (y+5)*10));
			}
		}
		
	}
	
	/****************************************************************************/
	
	@Test // VectorLayer*.connect(VectorLayer<?,T> layer, Relation rel)
	      // fully defined layer bases
	public void testVectorLayerNN1() {
		AssociateUtil.setAssociateForkJoinFlag(true);
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			unit1arrayD1[i] = new Unit1(i); 
			unit2arrayD1[i] = new Unit2(i);
		}
		
		// init D1 layers
		Layer_Unit1_ unit1layerD1 = new Layer_Unit1_(unit1arrayD1);
		Layer_Unit2_ unit2layerD1 = new Layer_Unit2_(unit2arrayD1);
		
		BasedVectorLayer<Unit1.SignalDouble> unit2layerD1_doubleVector = unit2layerD1.returnDoubleVector();
		BasedVectorLayer<Unit1>              unit1layerD1_unit1Link = unit1layerD1.returnUnit1Link();
		BasedVectorLayer<Unit2>              unit1layerD1_unit2Link = unit1layerD1.returnUnit2Link();
		BasedVectorLayer<Unit2>              unit2layerD1_unit2Link = unit2layerD1.returnUnit2Link();
		
		// variables used for checking
		int[] xLen, yLen;
		int intCheckSum = 0, unit1CheckSum = 0, unit2CheckSum = 0;
		double doubleCheckSum = 0.0;
		
		// associate using relation full
		unit1layerD1.intVector.associate(unit2layerD1.intVector, full);
		// computing the length of the link vectors and checking the signals
		xLen = new int[sz1]; yLen = new int[sz1];
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (full.contains(new int[]{x}, new int[]{y})) {
					Unit1.SignalInt intObj = unit1layerD1.intVector.get(x)[xLen[x]];
		       		assertEquals(intObj, unit2layerD1.intVector.get(y)[yLen[y]]);
		       		assertEquals(intObj.val, 7);
		       		intCheckSum += intObj.val;
		       		xLen[x]++; yLen[y]++;
		       	}	
			}
		}
		assertEquals(intCheckSum, 700);
		// checking the length of unit1 link vectors
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] intVector = unit1layerD1.intVector.get(x);
			if (xLen[x] > 0) {
				assertEquals(intVector.length, xLen[x]);
			}
		}
		// checking the length of unit2 link vectors
		for (int y = 0; y < sz1; y++){
			Unit1.SignalInt[] intVector = unit2layerD1.intVector.get(y);
			if (yLen[y] > 0) {
				assertEquals(intVector.length, yLen[y]);
			}
		}
		// set and check the associated signals
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++) {
				unit2layerD1.intVector.get(x)[y].setVal(x*10 + y);
			}
		}
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++) {
				assertEquals(unit1layerD1.intVector.get(x)[y].getVal(), x + y*10);
			}
		}
				
		// associate using relation rand
		unit1layerD1.doubleVector.associate(unit2layerD1_doubleVector, rand);	
		// computing the length of the link vectors and checking the signals
		xLen = new int[sz1]; yLen = new int[sz1];
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (rand.contains(new int[]{x}, new int[]{y})) {
		       		Unit1.SignalDouble doubleObj = unit1layerD1.doubleVector.get(x)[xLen[x]];
		       		assertEquals(doubleObj, unit2layerD1_doubleVector.get(y)[yLen[y]]);
		       		assertEquals(doubleObj.val, 5.0);
		       		doubleCheckSum += doubleObj.val;
		       		xLen[x]++; yLen[y]++;
		       	}	
			}
		}
		assertEquals(doubleCheckSum, trueno1 * 5.0);
		// checking the length of unit1 link vectors
		for (int x = 0; x < sz1; x++){
			Unit1.SignalDouble[] doubleVector = unit1layerD1.doubleVector.get(x);
			if (xLen[x] > 0) {
				assertEquals(doubleVector.length, xLen[x]);
			}
		}
		// checking the length of unit2 link vectors
		for (int y = 0; y < sz1; y++){
			Unit1.SignalDouble[] doubleVector1 = unit2layerD1.doubleVector.get(y);
			Unit1.SignalDouble[] doubleVector2 = unit2layerD1_doubleVector.get(y);
			if (yLen[y] > 0) {
				assertEquals(doubleVector1, null);
				assertEquals(doubleVector2.length, yLen[y]);
			}
		}
		// set and check the associated signals
		for (int x = 0; x < sz1; x++){
			int sz = unit2layerD1_doubleVector.get(x).length;
			for (int y = 0; y < sz; y++) {
				unit2layerD1_doubleVector.get(x)[y].setVal(3.14);
			}
		}
		for (int x = 0; x < sz1; x++){
			int sz = unit1layerD1.doubleVector.get(x).length;
			for (int y = 0; y < sz; y++) {
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 3.14);
			}
		}
		
		// associate using relation xHalf
		unit1layerD1_unit1Link.associate(unit2layerD1.unit1Link, xHalf);
		// computing the length of the link vectors and checking the signals
		xLen = new int[sz1]; yLen = new int[sz1];
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (xHalf.contains(new int[]{x}, new int[]{y})) {
		       		Unit1 unit1Obj = unit1layerD1_unit1Link.get(x)[xLen[x]];
		       		assertEquals(unit1Obj, unit2layerD1.unit1Link.get(y)[yLen[y]]);
		       		assertEquals(unit1Obj.noItem, 3);
		       		unit1CheckSum += unit1Obj.noItem;
		       		xLen[x]++; yLen[y]++;
		       	}	
			}
		}
		assertEquals(unit1CheckSum, 150);
		// checking the length of unit1 link vectors
		for (int x = 0; x < sz1; x++){
			Unit1[] unit1Link1 = unit1layerD1.unit1Link.get(x);
			Unit1[] unit1Link2 = unit1layerD1_unit1Link.get(x);
			if (xLen[x] > 0) {
				assertEquals(unit1Link1, null);
				assertEquals(unit1Link2.length, xLen[x]);
			}
		}
		// checking the length of unit2 link vectors
		for (int y = 0; y < sz1; y++){
			Unit1[] unit1Link = unit2layerD1.unit1Link.get(y);
			if (yLen[y] > 0) {
				assertEquals(unit1Link.length, yLen[y]);
			}
		}
		// set and check the associated signals
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1/2; y++) {
				unit2layerD1.unit1Link.get(x)[y].noItem = 3*(x*10 + 2*y);
			}
		}
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) continue;
			for (int y = 0; y < sz1; y++) {
				assertEquals(unit1layerD1_unit1Link.get(x)[y].noItem, 3*(x + y*10));
			}
		}
				
		// associate using relation yHalf
		unit1layerD1_unit2Link.associate(unit2layerD1_unit2Link, yHalf);
		// computing the length of the link vectors and checking the signals
		xLen = new int[sz1]; yLen = new int[sz1];
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (yHalf.contains(new int[]{x}, new int[]{y})) {
		       		Unit2 unit2Obj = unit1layerD1_unit2Link.get(x)[xLen[x]];
		       		assertEquals(unit2Obj, unit2layerD1_unit2Link.get(y)[yLen[y]]);
		       		assertEquals(unit2Obj.noItem, 7);
		       		unit2CheckSum += unit2Obj.noItem;
		       		xLen[x]++; yLen[y]++;
		       	}	
			}
		}
		assertEquals(unit2CheckSum, 350);
		// checking the length of unit1 link vectors
		for (int x = 0; x < sz1; x++){
			Unit2[] unit2Link1 = unit1layerD1.unit2Link.get(x);
			Unit2[] unit2Link2 = unit1layerD1_unit2Link.get(x);
			if (xLen[x] > 0) {
				assertEquals(unit2Link1, null);
				assertEquals(unit2Link2.length, xLen[x]);
			}
		}
		// checking the length of unit2 link vectors
		for (int y = 0; y < sz1; y++){
			Unit2[] unit2Link1 = unit2layerD1.unit2Link.get(y);
			Unit2[] unit2Link2 = unit2layerD1_unit2Link.get(y);
			if (yLen[y] > 0) {
				assertEquals(unit2Link1, null);
				assertEquals(unit2Link2.length, yLen[y]);
			}
		}
		// set and check the associated signals
		for (int x = sz1/2; x < sz1; x++){
			for (int y = 0; y < sz1; y++) {
				unit2layerD1_unit2Link.get(x)[y].noItem = 4*(x*10 + y);
			}
		}
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1/2; y++) {
				assertEquals(unit1layerD1_unit2Link.get(x)[y].noItem, 4*(x + (y+5)*10));
			}
		}
		
	}
	
	/****************************************************************************/

	@Test // VectorLayerAdapter.associate(VectorLayer<?,T> layer, Relation rel)
	      // partially defined layer bases (the template of the next test method)
	public void testVectorLayerAdapterNN2() {
		AssociateUtil.setAssociateForkJoinFlag(true);
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			if (i%2==0) {
				unit1arrayD1[i] = new Unit1(i); 
			} else {
				unit2arrayD1[i] = new Unit2(i);
			}
		}
		
		// init layers
		Layer_Unit1_ unit1layerD1 = new Layer_Unit1_(unit1arrayD1);
		Layer_Unit2_ unit2layerD1 = new Layer_Unit2_(unit2arrayD1);
		
		// variables used for checking
		int[] xLen, yLen, xIndex, yIndex;
		int intCheckSum;
		double doubleCheckSum;
		
		// associate using relation full
		unit1layerD1.intVector.associate(unit2layerD1.intVector, full);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz1];
		yLen   = new int[sz1];
		xIndex = new int[sz1];
		yIndex = new int[sz1];
		intCheckSum = 0;
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (unit1layerD1.getUnit(x) == null) continue;
				if (unit2layerD1.getUnit(y) == null) continue;
				if (full.contains(new int[]{x}, new int[]{y})) {
					xLen[x]++; yLen[y]++;
					Unit1.SignalInt obj = unit1layerD1.intVector.get(x)[xIndex[x]++];
		       		assertEquals(obj, unit2layerD1.intVector.get(y)[yIndex[y]++]);
		       		assertEquals(obj.val, 7);
		       		intCheckSum += obj.val;
		       	}	
			}
		}
		assertEquals(intCheckSum, 175);
		// checking the length of a link vector
		for (int x = 0; x < sz1; x++){
			if (unit1layerD1.getUnit(x) == null) continue;
			Unit1.SignalInt[] vector = unit1layerD1.intVector.get(x);
			assertEquals(vector.length, sz1/2);
			if (xLen[x] > 0) {
				assertEquals(vector.length, xLen[x]);
				
			}
		}
		// checking the length of the other link vector
		for (int y = 0; y < sz1; y++){
			if (unit2layerD1.getUnit(y) == null) continue;
			Unit1.SignalInt[] vector = unit2layerD1.intVector.get(y);
			assertEquals(vector.length, sz1/2);
			if (yLen[y] > 0) {
				assertEquals(vector.length, yLen[y]);
			}
		}
		// set and check the associated signals
		for (int x = 0; x < sz1; x++){
			if (unit2layerD1.getUnit(x) == null) continue;
			for (int y = 0; y < sz1/2; y++) {
				unit2layerD1.intVector.get(x)[y].setVal(x*10 + y);
			}
		}
		for (int x = 0; x < sz1; x++){
			if (unit1layerD1.getUnit(x) == null) continue;
			for (int y = 0; y < sz1/2; y++) {
				assertEquals(unit1layerD1.intVector.get(x)[y].getVal(), x/2 + (2*y+1)*10);
			}
		}
		
		// associate with relation rand
		unit1layerD1.doubleVector.associate(unit2layerD1.doubleVector, rand);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz1];
		yLen   = new int[sz1];
		xIndex = new int[sz1];
		yIndex = new int[sz1];
		doubleCheckSum = 0;
		int checkNo = trueno1;
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (rand.contains(new int[]{x}, new int[]{y})) {
					if (unit1layerD1.getUnit(x) == null) {
						checkNo--;
						continue;
					}
					if (unit2layerD1.getUnit(y) == null) {
						checkNo--;
						continue;
					}
					xLen[x]++; yLen[y]++;
					Unit1.SignalDouble obj = unit1layerD1.doubleVector.get(x)[xIndex[x]++];
		       		assertEquals(obj, unit2layerD1.doubleVector.get(y)[yIndex[y]++]);
		       		assertEquals(obj.val, 5.0);
		       		doubleCheckSum += obj.val;
		       	}	
			}
		}
		assertEquals(doubleCheckSum, checkNo * 5.0);
		// checking the length of a link vector
		for (int x = 0; x < sz1; x++){
			if (unit1layerD1.getUnit(x) == null) continue;
			Unit1.SignalDouble[] vector = unit1layerD1.doubleVector.get(x);
			if (xLen[x] > 0) {
				assertEquals(vector.length, xLen[x]);
				
			}
		}
		// checking the length of the other link vector
		for (int y = 0; y < sz1; y++){
			if (unit2layerD1.getUnit(y) == null) continue;
			Unit1.SignalDouble[] vector = unit2layerD1.doubleVector.get(y);
			if (yLen[y] > 0) {
				assertEquals(vector.length, yLen[y]);
			}
		}
		// set and check the associated signals
		for (int x = 0; x < sz1; x++){
			if (unit2layerD1.getUnit(x) == null) continue;
			int sz = unit2layerD1.doubleVector.get(x).length;
			for (int y = 0; y < sz; y++) {
				unit2layerD1.doubleVector.get(x)[y].setVal(3.14);
			}
		}
		for (int x = 0; x < sz1; x++){
			if (unit1layerD1.getUnit(x) == null) continue;
			int sz = unit1layerD1.doubleVector.get(x).length;
			for (int y = 0; y < sz; y++) {
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 3.14);
			}
		}
		
		// associate with relation xHalf
		unit1layerD1.unit1Link.associate(unit2layerD1.unit1Link, xHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz1];
		yLen   = new int[sz1];
		xIndex = new int[sz1];
		yIndex = new int[sz1];
		intCheckSum = 0;
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (xHalf.contains(new int[]{x}, new int[]{y})) {
					if (unit1layerD1.getUnit(x) == null) continue;
					if (unit2layerD1.getUnit(y) == null) continue;
					xLen[x]++; yLen[y]++;
					Unit1 obj = unit1layerD1.unit1Link.get(x)[xIndex[x]++];
		       		assertEquals(obj, unit2layerD1.unit1Link.get(y)[yIndex[y]++]);
		       		assertEquals(obj.noItem, 3);
		       		intCheckSum += obj.noItem;
		       	}	
			}
		}
		assertEquals(intCheckSum, 75);
		// checking the length of a link vector
		for (int x = 0; x < sz1; x++){
			if (unit1layerD1.getUnit(x) == null) continue;
			Unit1[] vector = unit1layerD1.unit1Link.get(x);
			assertEquals(vector.length, sz1/2);
			if (xLen[x] > 0) {
				assertEquals(vector.length, xLen[x]);
			}
		}
		// checking the length of the other link vector
		for (int y = 0; y < sz1; y++){
			if (unit2layerD1.getUnit(y) == null) continue;
			Unit1[] vector = unit2layerD1.unit1Link.get(y);
			assertEquals(vector.length, sz1/2);
			if (yLen[y] > 0) {
				assertEquals(vector.length, yLen[y]);
			}
		}
		// set and check the associated signals
		for (int x = 0; x < sz1; x++){
			if (unit2layerD1.getUnit(x) == null) continue;
			for (int y = 0; y < sz1/2; y++) {
				unit2layerD1.unit1Link.get(x)[y].noItem = x*10 + y;
			}
		}
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) continue;
			for (int y = 0; y < sz1/2; y++) {
				assertEquals(unit1layerD1.unit1Link.get(x)[y].noItem, x/2 + (2*y+1)*10);
			}
		}
		
		// associate with relation yHalf
		unit1layerD1.unit2Link.associate(unit2layerD1.unit2Link, yHalf);
		// computing the length of the link vectors and checking the signals
		xLen   = new int[sz1];
		yLen   = new int[sz1];
		xIndex = new int[sz1];
		yIndex = new int[sz1];
		intCheckSum = 0;
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (yHalf.contains(new int[]{x}, new int[]{y})) {
					if (unit1layerD1.getUnit(x) == null) continue;
					if (unit2layerD1.getUnit(y) == null) continue;
					xLen[x]++; yLen[y]++;
					Unit2 obj = unit1layerD1.unit2Link.get(x)[xIndex[x]++];
		       		assertEquals(obj, unit2layerD1.unit2Link.get(y)[yIndex[y]++]);
		       		assertEquals(obj.noItem, 7);
		       		intCheckSum += obj.noItem;
		       	}	
			}
		}
		assertEquals(intCheckSum, 105);
		// checking the length of a link vector
		for (int x = 0; x < sz1; x++){
			if (unit1layerD1.getUnit(x) == null) continue;
			Unit2[] vector = unit1layerD1.unit2Link.get(x);
			assertEquals(vector.length, 3);
			if (xLen[x] > 0) {
				assertEquals(vector.length, xLen[x]);
			}
		}
		// checking the length of the other link vector
		for (int y = 0; y < sz1; y++){
			if (unit2layerD1.getUnit(y) == null) continue;
			Unit2[] vector = unit2layerD1.unit2Link.get(y);
			if (yLen[y] > 0) {
				assertEquals(vector.length, sz1/2);
				assertEquals(vector.length, yLen[y]);
			} else {
				assertTrue(y==1 || y==3);
			}
		}
		// set and check the associated signals
		for (int x = sz1/2; x < sz1; x++){
			if (unit2layerD1.getUnit(x) == null) continue;
			for (int y = 0; y < sz1/2; y++) {
				unit2layerD1.unit2Link.get(x)[y].noItem = ((x-5)/2 + 10*y);
			}
		}
		for (int x = 0; x < sz1; x++){
			if (unit1layerD1.getUnit(x) == null) continue;
			for (int y = 0; y < 3; y++) {
				assertEquals(unit1layerD1.unit2Link.get(x)[y].noItem, (10*(x/2) + y));
			}
		}
		
	}
	
	/****************************************************************************/
	
	@Test // VectorLayer*.associate(VectorLayer<?,T> layer, Relation rel)
          // partially defined layer bases
	public void testVectorLayerNN2() {
		AssociateUtil.setAssociateForkJoinFlag(true);
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			if (i%2==0) {
				unit1arrayD1[i] = new Unit1(i); 
			} else {
				unit2arrayD1[i] = new Unit2(i);
			}
		}
		
		// init D1 layers
		Layer_Unit1_ unit1layerD1 = new Layer_Unit1_(unit1arrayD1);
		Layer_Unit2_ unit2layerD1 = new Layer_Unit2_(unit2arrayD1);
		
		BasedVectorLayer<Unit1.SignalDouble> unit2layerD1_doubleVector = unit2layerD1.returnDoubleVector();
		BasedVectorLayer<Unit1>              unit1layerD1_unit1Link = unit1layerD1.returnUnit1Link();
		BasedVectorLayer<Unit2>              unit1layerD1_unit2Link = unit1layerD1.returnUnit2Link();
		BasedVectorLayer<Unit2>              unit2layerD1_unit2Link = unit2layerD1.returnUnit2Link();
		
		// variables used for checking
		int[] xLen, yLen;
		int intCheckSum, unit1CheckSum, unit2CheckSum;
		double doubleCheckSum;
		
		// associate using relation full
		unit1layerD1.intVector.associate(unit2layerD1.intVector, full);
		// computing the length of the link vectors and checking the signals
		xLen = new int[sz1]; yLen = new int[sz1];
		intCheckSum = 0;
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (unit1layerD1.getUnit(x) == null) continue;
				if (unit2layerD1.getUnit(y) == null) continue;
				if (full.contains(new int[]{x}, new int[]{y})) {
					Unit1.SignalInt intObj = unit1layerD1.intVector.get(x)[xLen[x]];
		       		assertEquals(intObj, unit2layerD1.intVector.get(y)[yLen[y]]);
		       		assertEquals(intObj.val, 7);
		       		intCheckSum += intObj.val;
		       		xLen[x]++; yLen[y]++;
		       	}	
			}
		}
		assertEquals(intCheckSum, 175);
		// checking the length of unit1 link vectors
		for (int x = 0; x < sz1; x++){
			if (unit1layerD1.getUnit(x) == null) continue;
			Unit1.SignalInt[] intVector = unit1layerD1.intVector.get(x);
			if (xLen[x] > 0) {
				assertEquals(intVector.length, xLen[x]);
			}
		}
		// checking the length of unit2 link vectors
		for (int y = 0; y < sz1; y++){
			if (unit2layerD1.getUnit(y) == null) continue;
			Unit1.SignalInt[] intVector = unit2layerD1.intVector.get(y);
			if (yLen[y] > 0) {
				assertEquals(intVector.length, yLen[y]);
			}
		}
		// set and check the associated signals
		for (int x = 0; x < sz1; x++){
			if (unit2layerD1.getUnit(x) == null) continue;
			for (int y = 0; y < sz1/2; y++) {
				unit2layerD1.intVector.get(x)[y].setVal(x*10 + y);
			}
		}
		for (int x = 0; x < sz1; x++){
			if (unit1layerD1.getUnit(x) == null) continue;
			for (int y = 0; y < sz1/2; y++) {
				assertEquals(unit1layerD1.intVector.get(x)[y].getVal(), x/2 + (2*y+1)*10);
			}
		}
			
		// associate using relation rand
		unit1layerD1.doubleVector.associate(unit2layerD1_doubleVector, rand);	
		// NOTE: unit2layerD1_doubleVector has a "fully defined layer base"
		// computing the length of the link vectors and checking the signals
		xLen = new int[sz1]; yLen = new int[sz1];
		doubleCheckSum = 0.0;
		int checkNo = trueno1;
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (rand.contains(new int[]{x}, new int[]{y})) {
					if (unit1layerD1.getUnit(x) == null) {
						checkNo--;
						continue;
					}
//					if (unit2layerD1.getUnit(y) == null) {
//						checkNo--;
//						continue;
//					}
		       		Unit1.SignalDouble doubleObj = unit1layerD1.doubleVector.get(x)[xLen[x]];
		       		assertEquals(doubleObj, unit2layerD1_doubleVector.get(y)[yLen[y]]);
		       		assertEquals(doubleObj.val, 5.0);
		       		doubleCheckSum += doubleObj.val;
		       		xLen[x]++; yLen[y]++;
		       	}	
			}
		}
		assertEquals(doubleCheckSum, checkNo * 5.0);
		// checking the length of unit1 link vectors
		for (int x = 0; x < sz1; x++){
			if (unit1layerD1.getUnit(x) == null) continue;
			Unit1.SignalDouble[] doubleVector = unit1layerD1.doubleVector.get(x);
			if (xLen[x] > 0) {
				assertEquals(doubleVector.length, xLen[x]);
			} else {
				assertEquals(x%2, 0);
			}
		}
		// checking the length of unit2 link vectors
		for (int y = 0; y < sz1; y++){
			if (unit2layerD1.getUnit(y) == null) continue;
			Unit1.SignalDouble[] doubleVector1 = unit2layerD1.doubleVector.get(y);
			Unit1.SignalDouble[] doubleVector2 = unit2layerD1_doubleVector.get(y);
			if (yLen[y] > 0) {
				assertEquals(doubleVector1, null);
				assertEquals(doubleVector2.length, yLen[y]);
			}
		}
		// set and check the associated signals
		for (int x = 0; x < sz1; x++){
			int sz = unit2layerD1_doubleVector.get(x).length;
			for (int y = 0; y < sz; y++) {
				unit2layerD1_doubleVector.get(x)[y].setVal(3.14);
			}
		}
		for (int x = 0; x < sz1; x++){
			if (unit1layerD1.getUnit(x) == null) continue;
			int sz = unit1layerD1.doubleVector.get(x).length;
			for (int y = 0; y < sz; y++) {
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 3.14);
			}
		}
		
		// associate using relation xHalf
		unit1layerD1_unit1Link.associate(unit2layerD1.unit1Link, xHalf);
		// NOTE: unit1layerD1_unit1Link has a "fully defined layer base"
		// computing the length of the link vectors and checking the signals
		xLen = new int[sz1]; yLen = new int[sz1];
		unit1CheckSum = 0;
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
//				if (unit1layerD1.getUnit(x) == null) continue;
				if (unit2layerD1.getUnit(y) == null) continue;
				if (xHalf.contains(new int[]{x}, new int[]{y})) {
		       		Unit1 unit1Obj = unit1layerD1_unit1Link.get(x)[xLen[x]];
		       		assertEquals(unit1Obj, unit2layerD1.unit1Link.get(y)[yLen[y]]);
		       		assertEquals(unit1Obj.noItem, 3);
		       		unit1CheckSum += unit1Obj.noItem;
		       		xLen[x]++; yLen[y]++;
		       	}	
			}
		}
		assertEquals(unit1CheckSum, 75);
		// checking the length of unit1 link vectors
		for (int x = 0; x < sz1; x++){
			if (x%2==0){
				assertEquals(unit1layerD1.unit1Link.get(x), null);
			} else {  
				assertEquals(unit1layerD1.getUnit(x), null);
			}
			Unit1[] unit1Link2 = unit1layerD1_unit1Link.get(x);
			if (xLen[x] > 0) {
				assertTrue((x%2==0) && (xLen[x] == 5));
				assertEquals(unit1Link2.length, xLen[x]);
			} else {
				assertTrue(xLen[x] == 0);
			}
		}
		// checking the length of unit2 link vectors
		for (int y = 0; y < sz1; y++){
			if (unit2layerD1.getUnit(y) == null) continue;
			Unit1[] unit1Link = unit2layerD1.unit1Link.get(y);
			assertTrue((y%2!=0) && (yLen[y] == 5));
			if (yLen[y] > 0) {
				assertEquals(unit1Link.length, yLen[y]);
			}
		}
		// set and check the associated signals
		for (int x = 0; x < sz1; x++){
			if (unit2layerD1.getUnit(x) == null) continue;
			for (int y = 0; y < sz1/2; y++) {
				unit2layerD1.unit1Link.get(x)[y].noItem = x*10 + y;
			}
		}
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) continue;
			for (int y = 0; y < sz1/2; y++) {
				assertEquals(unit1layerD1_unit1Link.get(x)[y].noItem, x/2 + (2*y+1)*10);
			}
		}
				
		// associate using relation yHalf
		unit1layerD1_unit2Link.associate(unit2layerD1_unit2Link, yHalf);
		// NOTE: unit1layerD1_unit2Link has a "fully defined layer base"
		// NOTE: unit2layerD1_unit2Link has a "fully defined layer base"
		// computing the length of the link vectors and checking the signals
		xLen = new int[sz1]; yLen = new int[sz1];
		unit2CheckSum = 0;
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (yHalf.contains(new int[]{x}, new int[]{y})) {
		       		Unit2 unit2Obj = unit1layerD1_unit2Link.get(x)[xLen[x]];
		       		assertEquals(unit2Obj, unit2layerD1_unit2Link.get(y)[yLen[y]]);
		       		assertEquals(unit2Obj.noItem, 7);
		       		unit2CheckSum += unit2Obj.noItem;
		       		xLen[x]++; yLen[y]++;
		       	}	
			}
		}
		assertEquals(unit2CheckSum, 350);
		// checking the length of unit1 link vectors
		for (int x = 0; x < sz1; x++){
			if (x%2==0){
				assertEquals(unit1layerD1.unit2Link.get(x), null);
			} else {  
				assertEquals(unit1layerD1.getUnit(x), null);
			}
			Unit2[] unit2Link2 = unit1layerD1_unit2Link.get(x);
			if (xLen[x] > 0) {
				assertTrue(xLen[x] == 5);
				assertEquals(unit2Link2.length, xLen[x]);
			} else {
				fail();
			}
		}
		// checking the length of unit2 link vectors
		for (int y = 0; y < sz1; y++){
			if (y%2!=0){
				assertEquals(unit2layerD1.unit2Link.get(y), null);
			} else {  
				assertEquals(unit2layerD1.getUnit(y), null);
			}
			Unit2[] unit2Link2 = unit2layerD1_unit2Link.get(y);
			if (yLen[y] > 0) {
				assertTrue((y >= 5) && (yLen[y] == 10));
				assertEquals(unit2Link2.length, yLen[y]);
			}
		}
		// set and check the associated signals
		for (int x = sz1/2; x < sz1; x++){
			for (int y = 0; y < sz1; y++) {
				unit2layerD1_unit2Link.get(x)[y].noItem = (x*10 + y);
			}
		}
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1/2; y++) {
				assertEquals(unit1layerD1_unit2Link.get(x)[y].noItem, (x + (y+5)*10));
			}
		}
		
	}
	
	/****************************************************************************/
	
	@Test // VectorLayerAdapter.associate(VectorLayer<?,T> layer, Relation rel)
	      // testing the resulting order, fully defined layer bases
		  // (the template of the next test method)
	public void testLayerOrder_Template() {
		AssociateUtil.setAssociateForkJoinFlag(true);
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			unit1arrayD1[i] = new Unit1(i); 
			unit2arrayD1[i] = new Unit2(i);
		}
		
		// init NN layers
		Layer_Unit1_ unit1layerD1 = new Layer_Unit1_(unit1arrayD1);
		Layer_Unit2_ unit2layerD1 = new Layer_Unit2_(unit2arrayD1);
		
		// associate using relation full
		unit1layerD1.intVector.associate(unit2layerD1.intVector, full);
		unit1layerD1.doubleVector.associate(unit2layerD1.doubleVector, full);
		// setting the association vectors
		for (int y = 0; y < sz1; y++){
			Unit1.SignalInt[] ivector = unit2layerD1.intVector.get(y);
			Unit1.SignalDouble[] dvector = unit2layerD1.doubleVector.get(y);
			assertEquals(ivector.length, sz1);
			assertEquals(dvector.length, sz1);
			assertEquals(ivector.length, dvector.length);
			for (int i = 0; i < dvector.length; i++) {
				ivector[i].setVal(y);
				dvector[i].setVal(y);
			}
		}
		// checking the association vectors
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] ivector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] dvector = unit1layerD1.doubleVector.get(x);
			assertEquals(ivector.length, sz1);
			assertEquals(dvector.length, sz1);
			assertEquals(ivector.length, dvector.length);
			for (int i = 0; i < dvector.length; i++) {
				assertEquals(i, ivector[i].getVal());
				double tmp = ivector[i].getVal();
				assertEquals(tmp, dvector[i].getVal());
			}
		}
		
		// associate using relation rand
		unit1layerD1.intVector.associate(unit2layerD1.intVector, rand);
		unit1layerD1.doubleVector.associate(unit2layerD1.doubleVector, rand);
		// setting the association vectors
		for (int y = 0; y < sz1; y++){
			Unit1.SignalInt[] ivector = unit2layerD1.intVector.get(y);
			Unit1.SignalDouble[] dvector = unit2layerD1.doubleVector.get(y);
			assertEquals(ivector.length, dvector.length);
			for (int i = 0; i < dvector.length; i++) {
				ivector[i].setVal(y);
				dvector[i].setVal(y);
			}
		}
		// checking the association vectors
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] ivector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] dvector = unit1layerD1.doubleVector.get(x);
			assertEquals(ivector.length, dvector.length);
			for (int i = 0; i < dvector.length; i++) {
				// assertEquals(?, ivector[i].get()); not possible for rand
				double tmp = ivector[i].getVal();
				assertEquals(tmp, dvector[i].getVal());
			}
		}
		
		// associate using relation xHalf
		unit1layerD1.intVector.associate(unit2layerD1.intVector, xHalf);
		unit1layerD1.doubleVector.associate(unit2layerD1.doubleVector, xHalf);
		// setting the association vectors
		for (int y = 0; y < sz1; y++){
			Unit1.SignalInt[] ivector = unit2layerD1.intVector.get(y);
			Unit1.SignalDouble[] dvector = unit2layerD1.doubleVector.get(y);
			assertEquals(ivector.length, sz1/2);
			assertEquals(dvector.length, sz1/2);
			assertEquals(ivector.length, dvector.length);
			for (int i = 0; i < dvector.length; i++) {
				ivector[i].setVal(y);
				dvector[i].setVal(y);
			}
		}
		// checking the association vectors
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] ivector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] dvector = unit1layerD1.doubleVector.get(x);
			if (x%2==0) {
				assertEquals(ivector.length, sz1);
				assertEquals(dvector.length, sz1);
			} else {
				assertEquals(ivector.length, 0);
				assertEquals(dvector.length, 0);
			}
			assertEquals(ivector.length, dvector.length);
			for (int i = 0; i < dvector.length; i++) {
				assertEquals(i, ivector[i].getVal());
				double tmp = ivector[i].getVal();
				assertEquals(tmp, dvector[i].getVal());
			}
		}
		
		// associate using relation yHalf
		unit1layerD1.intVector.associate(unit2layerD1.intVector, yHalf);
		unit1layerD1.doubleVector.associate(unit2layerD1.doubleVector, yHalf);
		// setting the association vectors
		for (int y = 0; y < sz1; y++){
			Unit1.SignalInt[] ivector = unit2layerD1.intVector.get(y);
			Unit1.SignalDouble[] dvector = unit2layerD1.doubleVector.get(y);
			assertEquals(ivector.length, dvector.length);
			for (int i = 0; i < dvector.length; i++) {
				ivector[i].setVal(y);
				dvector[i].setVal(y);
			}
		}
		// checking the association vectors
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] ivector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] dvector = unit1layerD1.doubleVector.get(x);
			assertEquals(ivector.length, dvector.length);
			for (int i = 0; i < dvector.length; i++) {
				assertEquals(5+i, ivector[i].getVal());
				double tmp = ivector[i].getVal();
				assertEquals(tmp, dvector[i].getVal());
			}
		}
		
	}
	
	/****************************************************************************/
	
	@Test // VectorLayer*.associate(VectorLayer<?,T> layer, Relation rel)
    	  // testing the resulting order, fully defined layer bases
	public void testLayerOrder() {
		AssociateUtil.setAssociateForkJoinFlag(true);
		
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			unit1arrayD1[i] = new Unit1(i); 
			unit2arrayD1[i] = new Unit2(i);
		}
		
		// init D1 layers
		Layer_Unit1_ unit1layerD1 = new Layer_Unit1_(unit1arrayD1);
		Layer_Unit2_ unit2layerD1 = new Layer_Unit2_(unit2arrayD1);
		
		BasedVectorLayer<Unit1.SignalDouble> unit2layerD1_doubleVector = unit2layerD1.returnDoubleVector();
		BasedVectorLayer<Unit1>              unit1layerD1_unit1Link = unit1layerD1.returnUnit1Link();
		BasedVectorLayer<Unit2>              unit1layerD1_unit2Link = unit1layerD1.returnUnit2Link();
		BasedVectorLayer<Unit2>              unit2layerD1_unit2Link = unit2layerD1.returnUnit2Link();
		
		// associate using relation full
		unit1layerD1.intVector.associate(unit2layerD1.intVector, full);
		unit1layerD1.doubleVector.associate(unit2layerD1_doubleVector, full);
		unit1layerD1_unit1Link.associate(unit2layerD1.unit1Link, full);
		unit1layerD1_unit2Link.associate(unit2layerD1_unit2Link, full);
		// setting the association vectors
		for (int y = 0; y < sz1; y++){
			Unit1.SignalInt[] ivector = unit2layerD1.intVector.get(y);
			Unit1.SignalDouble[] dvector = unit2layerD1_doubleVector.get(y);
			Unit1[] unit1Link = unit2layerD1.unit1Link.get(y);
			Unit2[] unit2Link = unit2layerD1_unit2Link.get(y);
			assertEquals(ivector.length, sz1);
			assertEquals(dvector.length, sz1);
			assertEquals(unit1Link.length, sz1);
			assertEquals(unit2Link.length, sz1);
			for (int i = 0; i < dvector.length; i++) {
				ivector[i].setVal(y);
				dvector[i].setVal(y);
				unit1Link[i].noItem = y;
				unit2Link[i].noItem = y;
			}
		}
		// checking the association vectors
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] ivector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] dvector = unit1layerD1.doubleVector.get(x);
			Unit1[] unit1Link = unit1layerD1_unit1Link.get(x);
			Unit2[] unit2Link = unit1layerD1_unit2Link.get(x);
			assertEquals(ivector.length, sz1);
			assertEquals(dvector.length, sz1);
			assertEquals(unit1Link.length, sz1);
			assertEquals(unit2Link.length, sz1);
			for (int i = 0; i < dvector.length; i++) {
				assertEquals(i, ivector[i].getVal());
				double tmp = ivector[i].getVal();
				assertEquals(tmp, dvector[i].getVal());
				assertEquals(i, unit1Link[i].noItem);
				assertEquals(i, unit2Link[i].noItem);
			}
		}
		
		// associate using relation rand
		unit1layerD1.intVector.associate(unit2layerD1.intVector, rand);
		unit1layerD1.doubleVector.associate(unit2layerD1_doubleVector, rand);
		unit1layerD1_unit1Link.associate(unit2layerD1.unit1Link, rand);
		unit1layerD1_unit2Link.associate(unit2layerD1_unit2Link, rand);
		// setting the association vectors
		for (int y = 0; y < sz1; y++){
			Unit1.SignalInt[] ivector = unit2layerD1.intVector.get(y);
			Unit1.SignalDouble[] dvector = unit2layerD1_doubleVector.get(y);
			Unit1[] unit1Link = unit2layerD1.unit1Link.get(y);
			Unit2[] unit2Link = unit2layerD1_unit2Link.get(y);
			assertEquals(ivector.length, dvector.length);
			assertEquals(dvector.length, unit1Link.length);
			assertEquals(unit1Link.length, unit2Link.length);
			for (int i = 0; i < dvector.length; i++) {
				ivector[i].setVal(y);
				dvector[i].setVal(y);
				unit1Link[i].noItem = y;
				unit2Link[i].noItem = y;
			}
		}
		// checking the association vectors
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] ivector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] dvector = unit1layerD1.doubleVector.get(x);
			Unit1[] unit1Link = unit1layerD1_unit1Link.get(x);
			Unit2[] unit2Link = unit1layerD1_unit2Link.get(x);
			assertEquals(ivector.length, dvector.length);
			assertEquals(dvector.length, unit1Link.length);
			assertEquals(unit1Link.length, unit2Link.length);
			for (int i = 0; i < dvector.length; i++) {
				// assertEquals(?, ivector[i].get()); not possible for rand
				double tmp = ivector[i].getVal();
				assertEquals(tmp, dvector[i].getVal());
				assertEquals(unit1Link[i].noItem, ivector[i].getVal());
				assertEquals(unit2Link[i].noItem, ivector[i].getVal());
			}
		}
		
		// associate using relation xHalf
		unit1layerD1.intVector.associate(unit2layerD1.intVector, xHalf);
		unit1layerD1.doubleVector.associate(unit2layerD1_doubleVector, xHalf);
		unit1layerD1_unit1Link.associate(unit2layerD1.unit1Link, xHalf);
		unit1layerD1_unit2Link.associate(unit2layerD1_unit2Link, xHalf);
		// setting the association vectors
		for (int y = 0; y < sz1; y++){
			Unit1.SignalInt[] ivector = unit2layerD1.intVector.get(y);
			Unit1.SignalDouble[] dvector = unit2layerD1_doubleVector.get(y);
			Unit1[] unit1Link = unit2layerD1.unit1Link.get(y);
			Unit2[] unit2Link = unit2layerD1_unit2Link.get(y);
			assertEquals(ivector.length, sz1/2);
			assertEquals(dvector.length, sz1/2);
			assertEquals(unit1Link.length, sz1/2);
			assertEquals(unit2Link.length, sz1/2);
			for (int i = 0; i < dvector.length; i++) {
				ivector[i].setVal(y);
				dvector[i].setVal(y);
				unit1Link[i].noItem = y;
				unit2Link[i].noItem = y;
			}
		}
		// checking the association vectors
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] ivector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] dvector = unit1layerD1.doubleVector.get(x);
			Unit1[] unit1Link = unit1layerD1_unit1Link.get(x);
			Unit2[] unit2Link = unit1layerD1_unit2Link.get(x);
			if (x%2==0) {
				assertEquals(ivector.length, sz1);
				assertEquals(dvector.length, sz1);
				assertEquals(unit1Link.length, sz1);
				assertEquals(unit2Link.length, sz1);
			} else {
				assertEquals(ivector.length, 0);
				assertEquals(dvector.length, 0);
				assertEquals(unit1Link.length, 0);
				assertEquals(unit2Link.length, 0);
			}
			for (int i = 0; i < dvector.length; i++) {
				assertEquals(i, ivector[i].getVal());
				double tmp = ivector[i].getVal();
				assertEquals(tmp, dvector[i].getVal());
				assertEquals(i, unit1Link[i].noItem);
				assertEquals(i, unit2Link[i].noItem);
			}
		}
		
		// associate using relation yHalf
		unit1layerD1.intVector.associate(unit2layerD1.intVector, yHalf);
		unit1layerD1.doubleVector.associate(unit2layerD1_doubleVector, yHalf);
		unit1layerD1_unit1Link.associate(unit2layerD1.unit1Link, yHalf);
		unit1layerD1_unit2Link.associate(unit2layerD1_unit2Link, yHalf);
		// setting the association vectors
		for (int y = 0; y < sz1; y++){
			Unit1.SignalInt[] ivector = unit2layerD1.intVector.get(y);
			Unit1.SignalDouble[] dvector = unit2layerD1_doubleVector.get(y);
			Unit1[] unit1Link = unit2layerD1.unit1Link.get(y);
			Unit2[] unit2Link = unit2layerD1_unit2Link.get(y);
			if (y>=sz1/2) {
				assertEquals(ivector.length, sz1);
				assertEquals(dvector.length, sz1);
				assertEquals(unit1Link.length, sz1);
				assertEquals(unit2Link.length, sz1);
			} else {
				assertEquals(ivector.length, 0);
				assertEquals(dvector.length, 0);
				assertEquals(unit1Link.length, 0);
				assertEquals(unit2Link.length, 0);
			}
			for (int i = 0; i < dvector.length; i++) {
				ivector[i].setVal(y);
				dvector[i].setVal(y);
				unit1Link[i].noItem = y;
				unit2Link[i].noItem = y;
			}
		}
		// checking the association vectors
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] ivector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] dvector = unit1layerD1.doubleVector.get(x);
			Unit1[] unit1Link = unit1layerD1_unit1Link.get(x);
			Unit2[] unit2Link = unit1layerD1_unit2Link.get(x);
			assertEquals(ivector.length, sz1/2);
			assertEquals(dvector.length, sz1/2);
			assertEquals(unit1Link.length, sz1/2);
			assertEquals(unit2Link.length, sz1/2);
			for (int i = 0; i < dvector.length; i++) {
				assertEquals(5+i, ivector[i].getVal());
				double tmp = ivector[i].getVal();
				assertEquals(tmp, dvector[i].getVal());
				assertEquals(5+i, unit1Link[i].noItem);
				assertEquals(5+i, unit2Link[i].noItem);
			}
		}
		
	}
	
}

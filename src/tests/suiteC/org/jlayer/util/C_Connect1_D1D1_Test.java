package org.jlayer.util;

import org.jlayer.model.*;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import java.util.Random;

import basic.units.*;

/**
 * Testing {@code VectorLayer*.D1.connect(Layer.D1<?,T> layer, Relation rel)}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Connect1_D1D1_Test {
	
	final int sz1 = 10;
//	final int sz2 = 4;
//	final int sz3 = 2;
	
	boolean[][] random1 = new boolean[sz1][sz1];
	int         trueno1 = 0;
	
	C_Connect1_D1D1_Test() {
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
	
	@Test // VectorLayerAdapter.D1.connect(Layer.D1<?,T> layer, Relation rel)
	      // fully defined layer bases (the template of the next 4 test methods)
	public void testVectorLayerAdapterD1D1() {
		ConnectUtil.setConnectForkJoinFlag(true);
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			unit1arrayD1[i] = new Unit1(i); 
			unit2arrayD1[i] = new Unit2(i);
			if (i%2 == 0) {
				unit2arrayD1[i].intSignal = new Unit1.SignalInt();
				unit2arrayD1[i].intSignal.setVal(23);
				unit2arrayD1[i].doubleSignal = new Unit1.SignalDouble();
				unit2arrayD1[i].doubleSignal.setVal(21.0);
				unit2arrayD1[i].unit1Item = new Unit1(9);
				unit2arrayD1[i].unit2Item = new Unit2(4);
			}
		}
		
		// init D1 layers
		Layer_Unit1_.D1 unit1layerD1 = new Layer_Unit1_.D1(unit1arrayD1);
		Layer_Unit2_.D1 unit2layerD1 = new Layer_Unit2_.D1(unit2arrayD1);
		
		// connect using relation full
		unit1layerD1.intVector.connect(unit2layerD1.intSignal, full);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] vector = unit1layerD1.intVector.get(x);
			assertEquals(vector.length, sz1);
			for (int y = 0; y < sz1; y++) {
				if (y%2 == 0) {
					assertEquals(vector[y].getVal(), 23);
				} else {
					assertEquals(vector[y].getVal(), 7);
				}	
			}
		}
		// set and check the connectd signals
		for (int x = 0; x < sz1; x++){
			unit2layerD1.intSignal.get(x).setVal(x);
		}
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++) {
				assertEquals(unit1layerD1.intVector.get(x)[y].getVal(), y);
			}
		}
		
		// connect using relation rand
		unit1layerD1.doubleVector.connect(unit2layerD1.doubleSignal, rand);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			Unit1.SignalDouble[] vector = unit1layerD1.doubleVector.get(x);
			int sz = vector.length;
			int cnt5 = 0;
			int cnt21 = 0;
			for (int y = 0; y < sz; y++) {
				int val = (int)vector[y].getVal();
				switch (val) {
					case 5:   cnt5++; break;
					case 21 : cnt21++; break;
					default: throw new JLayerException();
				}
			}
			assertEquals(cnt5 + cnt21, sz);
		}
		// set and check the connectd signals
		for (int x = 0; x < sz1; x++){
			if (unit2layerD1.doubleSignal.get(x) != null) {
				unit2layerD1.doubleSignal.get(x).setVal(3.14);
			}
		}
		for (int x = 0; x < sz1; x++){
			int len = unit1layerD1.doubleVector.get(x).length;
			for (int y = 0; y < len; y++) {
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 3.14);
			}
		}
		
		// connect using relation xHalf
		unit1layerD1.unit1Link.connect(unit2layerD1.unit1Item, xHalf);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			Unit1[] vector = unit1layerD1.unit1Link.get(x);
			if (x%2 != 0){
				assertEquals(vector.length, 0);
				continue;
			}
			assertEquals(vector.length, sz1);
			for (int y = 0; y < sz1; y++) {
				if (y%2 == 0) {
					assertEquals(vector[y].noItem, 9);
				} else {
					assertEquals(vector[y].noItem, 3);
				}	
			}
		}
		// set and check the connectd signals
		for (int x = 0; x < sz1; x++){
			unit2layerD1.unit1Item.get(x).noItem = x;
		}
		for (int x = 0; x < sz1; x++){
			int len = unit1layerD1.unit1Link.get(x).length;
			for (int y = 0; y < len; y++) {
				assertEquals(unit1layerD1.unit1Link.get(x)[y].noItem, y);
			}
		}
		
		// connect using relation yHalf
		unit1layerD1.unit2Link.connect(unit2layerD1.unit2Item, yHalf);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			Unit2[] vector = unit1layerD1.unit2Link.get(x);
			assertEquals(vector.length, sz1/2);
			for (int y = 0; y < sz1/2; y++) {
				if (y%2 == 0) {
					assertEquals(vector[y].noItem, 7);
				} else {
					assertEquals(vector[y].noItem, 4);
				}	
			}
		}
		// set and check the connectd signals
		for (int x = sz1/2; x < sz1; x++){
			unit2layerD1.unit2Item.get(x).noItem = x;
		}
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1/2; y++) {
				assertEquals(unit1layerD1.unit2Link.get(x)[y].noItem, y+5);
			}
		}
		
	}
	
	/****************************************************************************/
	
	@Test // VectorLayer*.D1.connect(Layer.D1<?,T> layer, Relation rel)
	      // fully defined layer bases, relation full
	public void testVectorLayerD1D1full() {
		ConnectUtil.setConnectForkJoinFlag(true);
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			unit1arrayD1[i] = new Unit1(i); 
			unit2arrayD1[i] = new Unit2(i);
			if (i%2 == 0) {
				unit2arrayD1[i].intSignal = new Unit1.SignalInt();
				unit2arrayD1[i].intSignal.setVal(23);  						// else: 7
				unit2arrayD1[i].doubleSignal = new Unit1.SignalDouble();
				unit2arrayD1[i].doubleSignal.setVal(21.0);						// else: 5.0
				unit2arrayD1[i].unit1Item = new Unit1(9);					// else: 3
				unit2arrayD1[i].unit2Item = new Unit2(4);					// else: 7
			}
		}
		
		// init D1 layers
		Layer_Unit1_.D1 unit1layerD1 = new Layer_Unit1_.D1(unit1arrayD1);
		Layer_Unit2_.D1 unit2layerD1 = new Layer_Unit2_.D1(unit2arrayD1);
		
		BasedLayer.D1<Unit1.SignalDouble> unit2layerD1_doubleSignal = unit2layerD1.returnDoubleSignal();
		BasedVectorLayer.D1<Unit1>             unit1layerD1_unit1Link = unit1layerD1.returnUnit1Link();
		BasedVectorLayer.D1<Unit2>             unit1layerD1_unit2Link = unit1layerD1.returnUnit2Link();
		BasedLayer.D1<Unit2>              unit2layerD1_unit2Item = unit2layerD1.returnUnit2Item();
		
		// connect using relation full
		unit1layerD1.intVector.connect(unit2layerD1.intSignal, full);
		unit1layerD1.doubleVector.connect(unit2layerD1_doubleSignal, full);
		unit1layerD1_unit1Link.connect(unit2layerD1.unit1Item, full);
		unit1layerD1_unit2Link.connect(unit2layerD1_unit2Item, full);
		
		// check the connected signals
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] ivector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] dvector = unit1layerD1.doubleVector.get(x);
			Unit1[] unit1Link = unit1layerD1_unit1Link.get(x);
			Unit2[] unit2Link = unit1layerD1_unit2Link.get(x);
			assertEquals(ivector.length, sz1);
			assertEquals(dvector.length, sz1);
			assertEquals(unit1Link.length, sz1);
			assertEquals(unit2Link.length, sz1);
			assertEquals(unit1layerD1.unit1Link.get(x), null);
			assertEquals(unit1layerD1.unit2Link.get(x), null);
			for (int y = 0; y < sz1; y++) {
				if (y%2 == 0) {
					assertEquals(ivector[y].getVal(), 23);
					assertEquals(dvector[y].getVal(), 21.0);
					assertEquals(unit1Link[y].noItem, 9);
					assertEquals(unit2Link[y].noItem, 4);
				} else {
					assertEquals(ivector[y].getVal(), 7);
					assertEquals(dvector[y].getVal(), 5.0);
					assertEquals(unit1Link[y].noItem, 3);
					assertEquals(unit2Link[y].noItem, 7);
				}	
			}
		}
		
		// set and check the connected signals
		for (int y = 0; y < sz1; y++) {
			unit2layerD1.intSignal.get(y).setVal(y);
			unit2layerD1_doubleSignal.get(y).setVal(2*y);
			unit2layerD1.unit1Item.get(y).noItem = 3*y;
			unit2layerD1_unit2Item.get(y).noItem = 4*y;
		}
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++) {
				assertEquals(unit1layerD1.intVector.get(x)[y].getVal(), y);
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 2.0*y);
				assertEquals(unit1layerD1_unit1Link.get(x)[y].noItem, 3*y);
				assertEquals(unit1layerD1_unit2Link.get(x)[y].noItem, 4*y);
			}
		}
		
	}
	
	
	/****************************************************************************/
	
	@Test // VectorLayer*.D1.connect(Layer.D1<?,T> layer, Relation rel)
	      // fully defined layer bases, relation rand
	public void testVectorLayerD1D1rand() {
		ConnectUtil.setConnectForkJoinFlag(true);
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			unit1arrayD1[i] = new Unit1(i); 
			unit2arrayD1[i] = new Unit2(i);
			if (i%2 == 0) {
				unit2arrayD1[i].intSignal = new Unit1.SignalInt();
				unit2arrayD1[i].intSignal.setVal(23);  						// else: 7
				unit2arrayD1[i].doubleSignal = new Unit1.SignalDouble();
				unit2arrayD1[i].doubleSignal.setVal(21.0);						// else: 5.0
				unit2arrayD1[i].unit1Item = new Unit1(9);					// else: 3
				unit2arrayD1[i].unit2Item = new Unit2(4);					// else: 7
			}
		}
		
		// init D1 layers
		Layer_Unit1_.D1 unit1layerD1 = new Layer_Unit1_.D1(unit1arrayD1);
		Layer_Unit2_.D1 unit2layerD1 = new Layer_Unit2_.D1(unit2arrayD1);
		
		BasedLayer.D1<Unit1.SignalDouble> unit2layerD1_doubleSignal = unit2layerD1.returnDoubleSignal();
		BasedVectorLayer.D1<Unit1>             unit1layerD1_unit1Link = unit1layerD1.returnUnit1Link();
		BasedVectorLayer.D1<Unit2>             unit1layerD1_unit2Link = unit1layerD1.returnUnit2Link();
		BasedLayer.D1<Unit2>              unit2layerD1_unit2Item = unit2layerD1.returnUnit2Item();
		
		// connect using relation full
		unit1layerD1.intVector.connect(unit2layerD1.intSignal, rand);
		unit1layerD1.doubleVector.connect(unit2layerD1_doubleSignal, rand);
		unit1layerD1_unit1Link.connect(unit2layerD1.unit1Item, rand);
		unit1layerD1_unit2Link.connect(unit2layerD1_unit2Item, rand);
		
		// check the connected signals
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] ivector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] dvector = unit1layerD1.doubleVector.get(x);
			Unit1[] unit1Link = unit1layerD1_unit1Link.get(x);
			Unit2[] unit2Link = unit1layerD1_unit2Link.get(x);
			int sz = dvector.length;
			assertEquals(ivector.length, sz);
			assertEquals(dvector.length, sz);
			assertEquals(unit1Link.length, sz);
			assertEquals(unit2Link.length, sz);
			assertEquals(unit1layerD1.unit1Link.get(x), null);
			assertEquals(unit1layerD1.unit2Link.get(x), null);
			int cntEven = 0, cntOdd =0;
			for (int y = 0; y < sz; y++) {
				switch (ivector[y].getVal()) {
					case 7: 
						cntOdd++;
						assertEquals(dvector[y].getVal(), 5.0);
						assertEquals(unit1Link[y].noItem, 3);
						assertEquals(unit2Link[y].noItem, 7);
						break;
					case 23:
						cntEven++;
						assertEquals(dvector[y].getVal(), 21.0);
						assertEquals(unit1Link[y].noItem, 9);
						assertEquals(unit2Link[y].noItem, 4);
						break;
					default: throw new JLayerException();
				}
			}
			assertEquals(cntEven + cntOdd, sz);
		}
		
		// check the not connected signals
		for (int y = 0; y < sz1; y++) {
			if (y%2 == 0) {
				assertEquals(unit2layerD1.doubleSignal.get(y).getVal(), 21.0);
				assertEquals(unit2layerD1.unit2Item.get(y).noItem, 4);
			} else {
				assertEquals(unit2layerD1.doubleSignal.get(y), null);
				assertEquals(unit2layerD1.unit2Item.get(y), null);
			}
		}
		
		// set and check the connected signals
		for (int y = 0; y < sz1; y++) {
			unit2layerD1.intSignal.get(y).setVal(y);
			unit2layerD1_doubleSignal.get(y).setVal(2*y);
			unit2layerD1.unit1Item.get(y).noItem = 3*y;
			unit2layerD1_unit2Item.get(y).noItem = 4*y;
		}
		for (int x = 0; x < sz1; x++){
			int sz = unit1layerD1.intVector.get(x).length;
			for (int y = 0; y < sz; y++) {
				int res = unit1layerD1.intVector.get(x)[y].getVal();
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 2.0*res);
				assertEquals(unit1layerD1_unit1Link.get(x)[y].noItem, 3*res);
				assertEquals(unit1layerD1_unit2Link.get(x)[y].noItem, 4*res);
			}
		}
		
	}
	
	/****************************************************************************/
	
	@Test // VectorLayer*.D1.connect(Layer.D1<?,T> layer, Relation rel)
	      // fully defined layer bases, relation xHalf
	public void testVectorLayerD1D1xHalf() {
		ConnectUtil.setConnectForkJoinFlag(false);
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			unit1arrayD1[i] = new Unit1(i); 
			unit2arrayD1[i] = new Unit2(i);
			if (i%2 == 0) {
				unit2arrayD1[i].intSignal = new Unit1.SignalInt();
				unit2arrayD1[i].intSignal.setVal(23);  						// else: 7
				unit2arrayD1[i].doubleSignal = new Unit1.SignalDouble();
				unit2arrayD1[i].doubleSignal.setVal(21.0);						// else: 5.0
				unit2arrayD1[i].unit1Item = new Unit1(9);					// else: 3
				unit2arrayD1[i].unit2Item = new Unit2(4);					// else: 7
			}
		}
		
		// init D1 layers
		Layer_Unit1_.D1 unit1layerD1 = new Layer_Unit1_.D1(unit1arrayD1);
		Layer_Unit2_.D1 unit2layerD1 = new Layer_Unit2_.D1(unit2arrayD1);
		
		BasedLayer.D1<Unit1.SignalDouble> unit2layerD1_doubleSignal = unit2layerD1.returnDoubleSignal();
		BasedVectorLayer.D1<Unit1>             unit1layerD1_unit1Link = unit1layerD1.returnUnit1Link();
		BasedVectorLayer.D1<Unit2>             unit1layerD1_unit2Link = unit1layerD1.returnUnit2Link();
		BasedLayer.D1<Unit2>              unit2layerD1_unit2Item = unit2layerD1.returnUnit2Item();
		
		// connect using relation xHalf
		unit1layerD1.intVector.connect(unit2layerD1.intSignal, xHalf);
		unit1layerD1.doubleVector.connect(unit2layerD1_doubleSignal, xHalf);
		unit1layerD1_unit1Link.connect(unit2layerD1.unit1Item, xHalf);
		unit1layerD1_unit2Link.connect(unit2layerD1_unit2Item, xHalf);
		
		// check the connected signals
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] ivector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] dvector = unit1layerD1.doubleVector.get(x);
			Unit1[] unit1Link = unit1layerD1_unit1Link.get(x);
			Unit2[] unit2Link = unit1layerD1_unit2Link.get(x);
			if (x%2 != 0) {
				assertEquals(ivector.length, 0);
				assertEquals(dvector.length, 0);
				assertEquals(unit1Link.length, 0);
				assertEquals(unit2Link.length, 0);
				continue;
			}
			assertEquals(ivector.length, sz1);
			assertEquals(dvector.length, sz1);
			assertEquals(unit1Link.length, sz1);
			assertEquals(unit2Link.length, sz1);
			assertEquals(unit1layerD1.unit1Link.get(x), null);
			assertEquals(unit1layerD1.unit2Link.get(x), null);
			for (int y = 0; y < sz1; y++) {
				if (y%2 == 0) {
					assertEquals(ivector[y].getVal(), 23);
					assertEquals(dvector[y].getVal(), 21.0);
					assertEquals(unit1Link[y].noItem, 9);
					assertEquals(unit2Link[y].noItem, 4);
				} else {
					assertEquals(ivector[y].getVal(), 7);
					assertEquals(dvector[y].getVal(), 5.0);
					assertEquals(unit1Link[y].noItem, 3);
					assertEquals(unit2Link[y].noItem, 7);
				}	
			}
		}
		
		// set and check the connected signals
		for (int y = 0; y < sz1; y++) {
			unit2layerD1.intSignal.get(y).setVal(y);
			unit2layerD1_doubleSignal.get(y).setVal(2*y);
			unit2layerD1.unit1Item.get(y).noItem = 3*y;
			unit2layerD1_unit2Item.get(y).noItem = 4*y;
		}
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) continue;
			for (int y = 0; y < sz1; y++) {
				assertEquals(unit1layerD1.intVector.get(x)[y].getVal(), y);
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 2.0*y);
				assertEquals(unit1layerD1_unit1Link.get(x)[y].noItem, 3*y);
				assertEquals(unit1layerD1_unit2Link.get(x)[y].noItem, 4*y);
			}
		}
		
	}
	
	/****************************************************************************/
	
	@Test // VectorLayer*.D1.connect(Layer.D1<?,T> layer, Relation rel)
	      // fully defined layer bases, relation yHalf
	public void testVectorLayerD1D1yHalf() {
		ConnectUtil.setConnectForkJoinFlag(false);
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			unit1arrayD1[i] = new Unit1(i); 
			unit2arrayD1[i] = new Unit2(i);
			if (i%2 == 0) {
				unit2arrayD1[i].intSignal = new Unit1.SignalInt();
				unit2arrayD1[i].intSignal.setVal(23);  						// else: 7
				unit2arrayD1[i].doubleSignal = new Unit1.SignalDouble();
				unit2arrayD1[i].doubleSignal.setVal(21.0);						// else: 5.0
				unit2arrayD1[i].unit1Item = new Unit1(9);					// else: 3
				unit2arrayD1[i].unit2Item = new Unit2(4);					// else: 7
			}
		}
		
		// init D1 layers
		Layer_Unit1_.D1 unit1layerD1 = new Layer_Unit1_.D1(unit1arrayD1);
		Layer_Unit2_.D1 unit2layerD1 = new Layer_Unit2_.D1(unit2arrayD1);
		
		BasedLayer.D1<Unit1.SignalDouble> unit2layerD1_doubleSignal = unit2layerD1.returnDoubleSignal();
		BasedVectorLayer.D1<Unit1>             unit1layerD1_unit1Link = unit1layerD1.returnUnit1Link();
		BasedVectorLayer.D1<Unit2>             unit1layerD1_unit2Link = unit1layerD1.returnUnit2Link();
		BasedLayer.D1<Unit2>              unit2layerD1_unit2Item = unit2layerD1.returnUnit2Item();
		
		// connect using relation yHalf
		unit1layerD1.intVector.connect(unit2layerD1.intSignal, yHalf);
		unit1layerD1.doubleVector.connect(unit2layerD1_doubleSignal, yHalf);
		unit1layerD1_unit1Link.connect(unit2layerD1.unit1Item, yHalf);
		unit1layerD1_unit2Link.connect(unit2layerD1_unit2Item, yHalf);
		
		// check the connected signals
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] ivector = unit1layerD1.intVector.get(x);
			Unit1.SignalDouble[] dvector = unit1layerD1.doubleVector.get(x);
			Unit1[] unit1Link = unit1layerD1_unit1Link.get(x);
			Unit2[] unit2Link = unit1layerD1_unit2Link.get(x);
			assertEquals(ivector.length, sz1/2);
			assertEquals(dvector.length, sz1/2);
			assertEquals(unit1Link.length, sz1/2);
			assertEquals(unit2Link.length, sz1/2);
			assertEquals(unit1layerD1.unit1Link.get(x), null);
			assertEquals(unit1layerD1.unit2Link.get(x), null);
			for (int y = 0; y < sz1/2; y++) {
				if (y%2 != 0) {
					assertEquals(ivector[y].getVal(), 23);
					assertEquals(dvector[y].getVal(), 21.0);
					assertEquals(unit1Link[y].noItem, 9);
					assertEquals(unit2Link[y].noItem, 4);
				} else {
					assertEquals(ivector[y].getVal(), 7);
					assertEquals(dvector[y].getVal(), 5.0);
					assertEquals(unit1Link[y].noItem, 3);
					assertEquals(unit2Link[y].noItem, 7);
				}	
			}
		}
		
		// set and check the connected signals
		for (int y = sz1/2; y < sz1; y++) {
			unit2layerD1.intSignal.get(y).setVal(y);
			unit2layerD1_doubleSignal.get(y).setVal(2*y);
			unit2layerD1.unit1Item.get(y).noItem = 3*y;
			unit2layerD1_unit2Item.get(y).noItem = 4*y;
		}
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1/2; y++) {
				assertEquals(unit1layerD1.intVector.get(x)[y].getVal(), y+5);
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 2.0*(y+5));
				assertEquals(unit1layerD1_unit1Link.get(x)[y].noItem, 3*(y+5));
				assertEquals(unit1layerD1_unit2Link.get(x)[y].noItem, 4*(y+5));
			}
		}
		
	}
	
	/****************************************************************************/
	
	@Test // VectorLayerAdapter.D1.connect(Layer.D1<?,T> layer, Relation rel)
    	  // fully defined layer bases (the template of the next test method)
	public void testVectorLayerAdapterNN1() {
		ConnectUtil.setConnectForkJoinFlag(false);
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			unit1arrayD1[i] = new Unit1(i); 
			unit2arrayD1[i] = new Unit2(i);
			if (i%2 == 0) {
				unit2arrayD1[i].intSignal = new Unit1.SignalInt();
				unit2arrayD1[i].intSignal.setVal(23);
				unit2arrayD1[i].doubleSignal = new Unit1.SignalDouble();
				unit2arrayD1[i].doubleSignal.setVal(21.0);
				unit2arrayD1[i].unit1Item = new Unit1(9);
				unit2arrayD1[i].unit2Item = new Unit2(4);
			}
		}
		
		// init D1 layers
		Layer_Unit1_ unit1layerD1 = new Layer_Unit1_(unit1arrayD1);
		Layer_Unit2_ unit2layerD1 = new Layer_Unit2_(unit2arrayD1);
				
		// connect using relation full
		unit1layerD1.intVector.connect(unit2layerD1.intSignal, full);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] vector = unit1layerD1.intVector.get(x);
			assertEquals(vector.length, sz1);
			for (int y = 0; y < sz1; y++) {
				if (y%2 == 0) {
					assertEquals(vector[y].getVal(), 23);
				} else {
					assertEquals(vector[y].getVal(), 7);
				}	
			}
		}
		// set and check the connected signals
		for (int y = 0; y < sz1; y++) {
			unit2layerD1.intSignal.get(y).setVal(y);
		}
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++) {
				assertEquals(unit1layerD1.intVector.get(x)[y].getVal(), y);
			}
		}
		
		// connect using relation rand
		unit1layerD1.doubleVector.connect(unit2layerD1.doubleSignal, rand);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			Unit1.SignalDouble[] vector = unit1layerD1.doubleVector.get(x);
			int sz = vector.length;
			int cnt5 = 0;
			int cnt21 = 0;
			for (int y = 0; y < sz; y++) {
				int val = (int)vector[y].getVal();
				switch (val) {
					case 5:   cnt5++; break;
					case 21 : cnt21++; break;
					default: throw new JLayerException();
				}
			}
			assertEquals(cnt5 + cnt21, sz);
		}
		// set and check the connected signals
		for (int y = 0; y < sz1; y++) {
			unit2layerD1.doubleSignal.get(y).setVal(3.14);
		}
		for (int x = 0; x < sz1; x++){
			int len = unit1layerD1.doubleVector.get(x).length;
			for (int y = 0; y < len; y++) {
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 3.14);
			}
		}
		
		// connect using relation xHalf
		unit1layerD1.unit1Link.connect(unit2layerD1.unit1Item, xHalf);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			Unit1[] vector = unit1layerD1.unit1Link.get(x);
			if (x%2 != 0){
				assertEquals(vector.length, 0);
				continue;
			}
			assertEquals(vector.length, sz1);
			for (int y = 0; y < sz1; y++) {
				if (y%2 == 0) {
					assertEquals(vector[y].noItem, 9);
				} else {
					assertEquals(vector[y].noItem, 3);
				}	
			}
		}
		// set and check the connectd signals
		for (int x = 0; x < sz1; x++){
			unit2layerD1.unit1Item.get(x).noItem = x;
		}
		for (int x = 0; x < sz1; x++){
			int len = unit1layerD1.unit1Link.get(x).length;
			for (int y = 0; y < len; y++) {
				assertEquals(unit1layerD1.unit1Link.get(x)[y].noItem, y);
			}
		}
		
		// connect using relation yHalf
		unit1layerD1.unit2Link.connect(unit2layerD1.unit2Item, yHalf);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			Unit2[] vector = unit1layerD1.unit2Link.get(x);
			assertEquals(vector.length, sz1/2);
			for (int y = 0; y < sz1/2; y++) {
				if (y%2 == 0) {
					assertEquals(vector[y].noItem, 7);
				} else {
					assertEquals(vector[y].noItem, 4);
				}	
			}
		}	
		// set and check the connectd signals
		for (int x = sz1/2; x < sz1; x++){
			unit2layerD1.unit2Item.get(x).noItem = x;
		}
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1/2; y++) {
				assertEquals(unit1layerD1.unit2Link.get(x)[y].noItem, y+5);
			}
		}
		
	}
	
	/****************************************************************************/
	
	@Test // VectorLayer*.D1.connect(Layer.D1<?,T> layer, Relation rel)
    	  // fully defined layer bases
	public void testVectorLayerNN1() {
		ConnectUtil.setConnectForkJoinFlag(false);
		
		// init arrays
		Unit1[] unit1arrayD1 = new Unit1[sz1];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			unit1arrayD1[i] = new Unit1(i); 
			unit2arrayD1[i] = new Unit2(i);
			if (i%2 == 0) {
				unit2arrayD1[i].intSignal = new Unit1.SignalInt();
				unit2arrayD1[i].intSignal.setVal(23);  						// else: 7
				unit2arrayD1[i].doubleSignal = new Unit1.SignalDouble();
				unit2arrayD1[i].doubleSignal.setVal(21.0);						// else: 5.0
				unit2arrayD1[i].unit1Item = new Unit1(9);					// else: 3
				unit2arrayD1[i].unit2Item = new Unit2(4);					// else: 7
			}
		}
		
		// init D1 layers
		Layer_Unit1_ unit1layerD1 = new Layer_Unit1_(unit1arrayD1);
		Layer_Unit2_ unit2layerD1 = new Layer_Unit2_(unit2arrayD1);
		
		BasedLayer<Unit1.SignalDouble> unit2layerD1_doubleSignal = unit2layerD1.returnDoubleSignal();
		BasedVectorLayer<Unit1>             unit1layerD1_unit1Link = unit1layerD1.returnUnit1Link();
		BasedVectorLayer<Unit2>             unit1layerD1_unit2Link = unit1layerD1.returnUnit2Link();
		BasedLayer<Unit2>              unit2layerD1_unit2Item = unit2layerD1.returnUnit2Item();
				
		// connect using relation full
		unit1layerD1.intVector.connect(unit2layerD1.intSignal, full);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			Unit1.SignalInt[] vector = unit1layerD1.intVector.get(x);
			assertEquals(vector.length, sz1);
			for (int y = 0; y < sz1; y++) {
				if (y%2 == 0) {
					assertEquals(vector[y].getVal(), 23);
				} else {
					assertEquals(vector[y].getVal(), 7);
				}	
			}
		}
		// set and check the connected signals
		for (int y = 0; y < sz1; y++) {
			unit2layerD1.intSignal.get(y).setVal(y);
		}
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++) {
				assertEquals(unit1layerD1.intVector.get(x)[y].getVal(), y);
			}
		}
		
		// connect using relation rand
		unit1layerD1.doubleVector.connect(unit2layerD1_doubleSignal, rand);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			Unit1.SignalDouble[] vector = unit1layerD1.doubleVector.get(x);
			int sz = vector.length;
			int cnt5 = 0;
			int cnt21 = 0;
			for (int y = 0; y < sz; y++) {
				int val = (int)vector[y].getVal();
				switch (val) {
					case 5:   cnt5++; break;
					case 21 : cnt21++; break;
					default: throw new JLayerException();
				}
			}
			assertEquals(cnt5 + cnt21, sz);
		}
		// set and check the connected signals
		for (int y = 0; y < sz1; y++) {
			unit2layerD1_doubleSignal.get(y).setVal(3.14);
		}
		for (int x = 0; x < sz1; x++){
			int len = unit1layerD1.doubleVector.get(x).length;
			for (int y = 0; y < len; y++) {
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 3.14);
			}
		}
		
		// connect using relation xHalf
		unit1layerD1_unit1Link.connect(unit2layerD1.unit1Item, xHalf);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			Unit1[] vector = unit1layerD1_unit1Link.get(x);
			if (x%2 != 0){
				assertEquals(vector.length, 0);
				continue;
			}
			assertEquals(vector.length, sz1);
			for (int y = 0; y < sz1; y++) {
				if (y%2 == 0) {
					assertEquals(vector[y].noItem, 9);
				} else {
					assertEquals(vector[y].noItem, 3);
				}	
			}
		}
		// set and check the connectd signals
		for (int x = 0; x < sz1; x++){
			unit2layerD1.unit1Item.get(x).noItem = x;
		}
		for (int x = 0; x < sz1; x++){
			int len = unit1layerD1_unit1Link.get(x).length;
			for (int y = 0; y < len; y++) {
				assertEquals(unit1layerD1_unit1Link.get(x)[y].noItem, y);
			}
		}
		
		// connect using relation yHalf
		unit1layerD1_unit2Link.connect(unit2layerD1_unit2Item, yHalf);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			Unit2[] vector = unit1layerD1_unit2Link.get(x);
			assertEquals(vector.length, sz1/2);
			for (int y = 0; y < sz1/2; y++) {
				if (y%2 == 0) {
					assertEquals(vector[y].noItem, 7);
				} else {
					assertEquals(vector[y].noItem, 4);
				}	
			}
		}	
		// set and check the connectd signals
		for (int x = sz1/2; x < sz1; x++){
			unit2layerD1_unit2Item.get(x).noItem = x;
		}
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1/2; y++) {
				assertEquals(unit1layerD1_unit2Link.get(x)[y].noItem, y+5);
			}
		}
		
	}
	
	/****************************************************************************/
	
	@Test // VectorLayerAdapter.connect(Layer<?,T> layer, Relation rel)
	      // partially defined layer bases (the template of the next test method)
	public void testVectorLayerAdapterNN2() {
		ConnectUtil.setConnectForkJoinFlag(false);
		
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
		for (int i : new int[]{3,5}) {
				unit2arrayD1[i].intSignal = new Unit1.SignalInt();
				unit2arrayD1[i].intSignal.setVal(23);
				unit2arrayD1[i].doubleSignal = new Unit1.SignalDouble();
				unit2arrayD1[i].doubleSignal.setVal(21.0);
				unit2arrayD1[i].unit1Item = new Unit1(9);
				unit2arrayD1[i].unit2Item = new Unit2(4);
		}
		
		// init D1 layers
		Layer_Unit1_ unit1layerD1 = new Layer_Unit1_(unit1arrayD1);
		Layer_Unit2_ unit2layerD1 = new Layer_Unit2_(unit2arrayD1);
	
		// connect using relation full
		unit1layerD1.intVector.connect(unit2layerD1.intSignal, full);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0){
				assertEquals(unit1layerD1.getUnit(x), null);
				continue;
			}
			Unit1.SignalInt[] vector = unit1layerD1.intVector.get(x);
			assertEquals(vector.length, sz1/2);
			for (int y = 0; y < sz1/2; y++) {
				if ((y==1) || (y==2)) {
					assertEquals(vector[y].getVal(), 23);
				} else {
					assertEquals(vector[y].getVal(), 7);
				}	
			}
		}
		// set and check the connectd signals
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) unit2layerD1.intSignal.get(x).setVal(x);
		}
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) continue;
			int check = 0;
			for (int y = 0; y < sz1/2; y++) {
				assertEquals(unit1layerD1.intVector.get(x)[y].getVal(), 2*y + 1);
				check += (2*y + 1);
			}
			assertEquals(check, 25);
		}
		
		// connect using relation rand
		unit1layerD1.doubleVector.connect(unit2layerD1.doubleSignal, rand);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0){
				assertEquals(unit1layerD1.getUnit(x), null);
				continue;
			}
			Unit1.SignalDouble[] vector = unit1layerD1.doubleVector.get(x);
			int sz = vector.length;
			int cnt5 = 0;
			int cnt21 = 0;
			for (int y = 0; y < sz; y++) {
				int val = (int)vector[y].getVal();
				switch (val) {
					case 5:   cnt5++; break;
					case 21 : cnt21++; break;
					default: throw new JLayerException();
				}
			}
			assertEquals(cnt5 + cnt21, sz);
		}
		// set and check the connectd signals
		for (int x = 0; x < sz1; x++){
			if ((x%2 != 0) && unit2layerD1.doubleSignal.get(x) != null) {
				unit2layerD1.doubleSignal.get(x).setVal(3.14);
			}
		}
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) continue;
			int len = unit1layerD1.doubleVector.get(x).length;
			for (int y = 0; y < len; y++) {
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 3.14);
			}
		}
		
		// connect using relation xHalf
		unit1layerD1.unit1Link.connect(unit2layerD1.unit1Item, xHalf);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0){
				assertEquals(unit1layerD1.getUnit(x), null);
				continue;
			}
			Unit1[] vector = unit1layerD1.unit1Link.get(x);
			assertEquals(vector.length, sz1/2);
			for (int y = 0; y < sz1/2; y++) {
				if ((y==1) || (y==2)) {
					assertEquals(vector[y].noItem, 9);
				} else {
					assertEquals(vector[y].noItem, 3);
				}	
			}
		}
		// set and check the connectd signals
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) unit2layerD1.unit1Item.get(x).noItem = x;
		}
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) continue;
			int check = 0;
			for (int y = 0; y < sz1/2; y++) {
				assertEquals(unit1layerD1.unit1Link.get(x)[y].noItem, 2*y + 1);
				check += (2*y + 1);
			}
			assertEquals(check, 25);
		}
		
		// connect using relation yHalf
		unit1layerD1.unit2Link.connect(unit2layerD1.unit2Item, yHalf);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0){
				assertEquals(unit1layerD1.getUnit(x), null);
				continue;
			}
			Unit2[] vector = unit1layerD1.unit2Link.get(x);
			assertEquals(vector.length, 3);
			assertEquals(vector[0].noItem, 4);
			assertEquals(vector[1].noItem, 7);
			assertEquals(vector[2].noItem, 7);
		}
		// set and check the connectd signals
		for (int x = sz1/2; x < sz1; x++){
			if (x%2 != 0) unit2layerD1.unit2Item.get(x).noItem = x;
		}
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) continue;
			int len = unit1layerD1.unit2Link.get(x).length;
			assertEquals(len, 3);
			for (int y = 0; y < len; y++) {
				assertEquals(unit1layerD1.unit2Link.get(x)[y].noItem, 5 + 2*y);
			}
		}
		
	}
	
/****************************************************************************/
	
	@Test // VectorLayer*.connect(Layer<?,T> layer, Relation rel)
    	  // partially defined layer bases
	public void testVectorLayerNN2() {
		ConnectUtil.setConnectForkJoinFlag(false);
	
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
		for (int i : new int[]{3,5}) {
				unit2arrayD1[i].intSignal = new Unit1.SignalInt();
				unit2arrayD1[i].intSignal.setVal(23);
				unit2arrayD1[i].doubleSignal = new Unit1.SignalDouble();
				unit2arrayD1[i].doubleSignal.setVal(21.0);
				unit2arrayD1[i].unit1Item = new Unit1(9);
				unit2arrayD1[i].unit2Item = new Unit2(4);
		}
		
		// init D1 layers
		Layer_Unit1_ unit1layerD1 = new Layer_Unit1_(unit1arrayD1);
		Layer_Unit2_ unit2layerD1 = new Layer_Unit2_(unit2arrayD1);
		
		BasedLayer<Unit1.SignalDouble> unit2layerD1_doubleSignal = unit2layerD1.returnDoubleSignal();
		BasedVectorLayer<Unit1>             unit1layerD1_unit1Link = unit1layerD1.returnUnit1Link();
		BasedVectorLayer<Unit2>             unit1layerD1_unit2Link = unit1layerD1.returnUnit2Link();
		BasedLayer<Unit2>              unit2layerD1_unit2Item = unit2layerD1.returnUnit2Item();
	
		// connect using relation full
		unit1layerD1.intVector.connect(unit2layerD1.intSignal, full);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0){
				assertEquals(unit1layerD1.getUnit(x), null);
				continue;
			}
			Unit1.SignalInt[] vector = unit1layerD1.intVector.get(x);
			assertEquals(vector.length, sz1/2);
			for (int y = 0; y < sz1/2; y++) {
				if ((y==1) || (y==2)) {
					assertEquals(vector[y].getVal(), 23);
				} else {
					assertEquals(vector[y].getVal(), 7);
				}	
			}
		}
		// set and check the connectd signals
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) unit2layerD1.intSignal.get(x).setVal(x);
		}
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) continue;
			int check = 0;
			for (int y = 0; y < sz1/2; y++) {
				assertEquals(unit1layerD1.intVector.get(x)[y].getVal(), 2*y + 1);
				check += (2*y + 1);
			}
			assertEquals(check, 25);
		}
	
		// connect using relation rand
		unit1layerD1.doubleVector.connect(unit2layerD1_doubleSignal, rand);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0){
				assertEquals(unit1layerD1.getUnit(x), null);
				continue;
			}
			Unit1.SignalDouble[] vector = unit1layerD1.doubleVector.get(x);
			int sz = vector.length;
			int cnt5 = 0;
			int cnt21 = 0;
			for (int y = 0; y < sz; y++) {
				int val = (int)vector[y].getVal();
				switch (val) {
					case 5:   cnt5++; break;
					case 21 : cnt21++; break;
					default: throw new JLayerException();
				}
			}
			assertEquals(cnt5 + cnt21, sz);
		}
		// set and check the connectd signals
		for (int x = 0; x < sz1; x++){
			if (unit2layerD1_doubleSignal.get(x) != null) {
				unit2layerD1_doubleSignal.get(x).setVal(3.14);
			}
		}
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) continue;
			int len = unit1layerD1.doubleVector.get(x).length;
			for (int y = 0; y < len; y++) {
				assertEquals(unit1layerD1.doubleVector.get(x)[y].getVal(), 3.14);
			}
		}
	
		// connect using relation xHalf
		unit1layerD1_unit1Link.connect(unit2layerD1.unit1Item, xHalf);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			Unit1[] vector = unit1layerD1_unit1Link.get(x);
			if (x%2 != 0) {
				assertEquals(vector.length, 0);
				continue;
			}
			assertEquals(vector.length, sz1/2);
			for (int y = 0; y < sz1/2; y++) {
				if ((y==1) || (y==2)) {
					assertEquals(vector[y].noItem, 9);
				} else {
					assertEquals(vector[y].noItem, 3);
				}	
			}
		}
		// set and check the connectd signals
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) unit2layerD1.unit1Item.get(x).noItem = x;
		}
		for (int x = 0; x < sz1; x++){
			if (x%2 != 0) continue;
			int check = 0;
			for (int y = 0; y < sz1/2; y++) {
				assertEquals(unit1layerD1_unit1Link.get(x)[y].noItem, 2*y + 1);
				check += (2*y + 1);
			}
			assertEquals(check, 25);
		}
	
		// connect using relation yHalf
		unit1layerD1_unit2Link.connect(unit2layerD1_unit2Item, yHalf);
		// checking the signals
		for (int x = 0; x < sz1; x++){
			Unit2[] vector = unit1layerD1_unit2Link.get(x);
			assertEquals(vector.length, sz1/2);
			assertEquals(vector[0].noItem, 4);
			for (int y = 1; y < sz1/2; y++){
				assertEquals(vector[y].noItem, 7);
			}
		}
		// set and check the connectd signals
		for (int x = sz1/2; x < sz1; x++){
			unit2layerD1_unit2Item.get(x).noItem = x;
		}
		for (int x = 0; x < sz1; x++){
			int len = unit1layerD1_unit2Link.get(x).length;
			assertEquals(len, sz1/2);
			for (int y = 0; y < len; y++) {
				assertEquals(unit1layerD1_unit2Link.get(x)[y].noItem, 5 + y);
			}
		}
	
	}
	
	
/****************************************************************************/
	
	

}

package org.jlayer.util;

import org.jlayer.model.*;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.Random;
import java.util.Set;
import java.util.HashSet;

import basic.units.*;

/**
 * Testing {@code VectorLayer*.D3.connect(Layer.D1<?,T> layer, Relation rel)}.
 *
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Connect0_D3D1_Test {
	
	final int sz1 = 10;
//	final int sz2 = 4;
	final int sz3 = 3;
	
	boolean[][][][] random1 = new boolean[sz3][sz3][sz3][sz1];
	int         	trueno1 = 0;
	
	C_Connect0_D3D1_Test() {
		Random generator = new Random(System.nanoTime());
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz3; y1++){
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
			return (y1 < 7) ? true : false; 
		}
	};
	
	/****************************************************************************/
	
	@Test // VectorLayer*.connect(Layer<?,T> layer, Relation rel)
	      // fully defined layer bases
	public void testVectorLayerNN1full() {
		ConnectUtil.setConnectForkJoinFlag(false);
		
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
			if ((i)%2 == 0) {
				unit2arrayD1[i].intSignal = new Unit1.SignalInt();
				unit2arrayD1[i].intSignal.setVal(23);  						// else: 7
				unit2arrayD1[i].doubleSignal = new Unit1.SignalDouble();
				unit2arrayD1[i].doubleSignal.setVal(21.0);						// else: 5.0
				unit2arrayD1[i].unit1Item = new Unit1(9);					// else: 3
				unit2arrayD1[i].unit2Item = new Unit2(4);					// else: 7
			}
		}
		
		// init D1 layers
		Layer_Unit1_ unit1layerD3 = new Layer_Unit1_(unit1arrayD3);
		Layer_Unit2_ unit2layerD1 = new Layer_Unit2_(unit2arrayD1);
		
		BasedLayer<Unit1.SignalDouble> unit2layerD1_doubleSignal = unit2layerD1.returnDoubleSignal();
		BasedVectorLayer<Unit1>             unit1layerD3_unit1Link = unit1layerD3.returnUnit1Link();
		BasedVectorLayer<Unit2>             unit1layerD3_unit2Link = unit1layerD3.returnUnit2Link();
		BasedLayer<Unit2>              unit2layerD1_unit2Item = unit2layerD1.returnUnit2Item();
		
		// connect using relation full
		unit1layerD3.intVector.connect(unit2layerD1.intSignal, full);
		// check the link vectors
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1.SignalInt[] ivector = unit1layerD3.intVector.get(x1, x2, x3);
					int len = ivector.length;
					assertEquals(len, sz1);
					for (int y = 0; y < len; y++) {
						if (y%2 == 0) {
							assertEquals(ivector[y].getVal(), 23);
						} else {
							assertEquals(ivector[y].getVal(), 7);
						}
					}
				}
			}
		}	
		// set and check the connected signals
		for (int y1 = 0; y1 < sz1; y1++){
			unit2layerD1.intSignal.get(y1).setVal(y1);
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y = 0; y < sz1; y++) {
						assertEquals(unit1layerD3.intVector.get(x1, x2, x3)[y].getVal(), y);
					}
				}
			}
		}
		
		// connect using relation rand
		unit1layerD3.doubleVector.connect(unit2layerD1_doubleSignal, rand);
		// check the link vectors
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1.SignalDouble[] dvector = unit1layerD3.doubleVector.get(x1, x2, x3);
					int len = dvector.length;
					int cnt5 = 0;
					int cnt21 = 0;
					for (int y = 0; y < len; y++) {
						int val = (int)dvector[y].getVal();
						switch (val) {
							case 5:   cnt5++; break;
							case 21 : cnt21++; break;
							default: throw new JLayerException();
						}
					}
					assertEquals(cnt5 + cnt21, len);
				}
			}
		}	
		// set and check the connected signals
		for (int y1 = 0; y1 < sz1; y1++){
			if (unit2layerD1_doubleSignal.get(y1) != null) {
				unit2layerD1_doubleSignal.get(y1).setVal(3.14);
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1.SignalDouble[] dvector = unit1layerD3.doubleVector.get(x1, x2, x3);
					int len = dvector.length;
					for (int y = 0; y < len; y++) {
						assertEquals(unit1layerD3.doubleVector.get(x1, x2, x3)[y].getVal(), 3.14);
					}
				}
			}
		}		
		
		// connect using relation xHalf
		unit1layerD3_unit1Link.connect(unit2layerD1.unit1Item, xHalf);
		// check the link vectors
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1[] unit1Link = unit1layerD3_unit1Link.get(x1, x2, x3);
					int len = unit1Link.length;
					if ((x1+x2+x3)%2 == 0) {
						assertEquals(len, sz1);
						for (int y = 0; y < len; y++) {
							if (y%2 == 0) {
								assertEquals(unit1Link[y].noItem, 9);
							} else {
								assertEquals(unit1Link[y].noItem, 3);
							}
						}
					} else {
						assertEquals(len, 0);
					}
				}
			}
		}
		// set and check the connected signals
		for (int y1 = 0; y1 < sz1; y1++){
			unit2layerD1.unit1Item.get(y1).noItem = y1;
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1[] unit1Link = unit1layerD3_unit1Link.get(x1, x2, x3);
					for (int y = 0; y < unit1Link.length; y++) {
						assertEquals(unit1Link[y].noItem, y);
					}
				}
			}
		}		
		
		// connect using relation yHalf
		unit1layerD3_unit2Link.connect(unit2layerD1_unit2Item, yHalf);
		// check the connected signals
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit2[] unit2Link = unit1layerD3_unit2Link.get(x1, x2, x3);
					assertEquals(unit2Link.length, 7);
					for (int y = 0; y < unit2Link.length; y++) {
						if (y%2 == 0) {
							assertEquals(unit2Link[y].noItem, 4);
						} else {
							assertEquals(unit2Link[y].noItem, 7);
						}
					}
				}
			}
		}
		// set and check the connected signals
		for (int y1 = 0; y1 < sz1; y1++){
			if ( y1 < 7 || (y1)%2==0) {
				unit2layerD1_unit2Item.get(y1).noItem = y1;
			} else {
				assertNull(unit2layerD1_unit2Item.get(y1));
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit2[] unit2Link = unit1layerD3_unit2Link.get(x1, x2, x3);
					for (int y = 0; y < unit2Link.length; y++) {
						assertEquals(unit1layerD3_unit2Link.get(x1, x2, x3)[y].noItem, y);
					}
				}
			}
		}		
		
	}
	
	
/****************************************************************************/
	
	@Test // VectorLayer*.connect(Layer<?,T> layer, Relation rel)
    	  // partially defined layer bases
	public void testVectorLayerNN2full() {	
		ConnectUtil.setConnectForkJoinFlag(false);
		
		// init arrays
		Unit1[][][] unit1arrayD3 = new Unit1[sz3][sz3][sz3];
		Unit2[] unit2arrayD1 = new Unit2[sz1];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					if (i>0) {
						unit1arrayD3[i][j][k] = new Unit1(i+j+k); 
					}
				}
			}
		}
		for (int i = 0; i < sz1; i++) {	
			if (i > 2) {
				unit2arrayD1[i] = new Unit2(i);
				if ((i)%2 == 0) {
					unit2arrayD1[i].intSignal = new Unit1.SignalInt();
					unit2arrayD1[i].intSignal.setVal(23);  						// else: 7
					unit2arrayD1[i].doubleSignal = new Unit1.SignalDouble();
					unit2arrayD1[i].doubleSignal.setVal(21.0);						// else: 5.0
					unit2arrayD1[i].unit1Item = new Unit1(9);					// else: 3
					unit2arrayD1[i].unit2Item = new Unit2(4);					// else: 7
				}
			}
		}
		
		// init D1 layers
		Layer_Unit1_ unit1layerD3 = new Layer_Unit1_(unit1arrayD3);
		Layer_Unit2_ unit2layerD1 = new Layer_Unit2_(unit2arrayD1);
		
		BasedLayer<Unit1.SignalDouble> unit2layerD1_doubleSignal = unit2layerD1.returnDoubleSignal();
		BasedVectorLayer<Unit1>             unit1layerD3_unit1Link = unit1layerD3.returnUnit1Link();
		BasedVectorLayer<Unit2>             unit1layerD3_unit2Link = unit1layerD3.returnUnit2Link();
		BasedLayer<Unit2>              unit2layerD1_unit2Item = unit2layerD1.returnUnit2Item();
		
		// variable used for checking
		int intCheckSum;
		
		// connect using relation full
		unit1layerD3.intVector.connect(unit2layerD1.intSignal, full);
		// check the link vectors
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (unit1layerD3.getUnit(x1, x2, x3) == null) continue;
					intCheckSum++;
					Unit1.SignalInt[] ivector = unit1layerD3.intVector.get(x1, x2, x3);
					int len = ivector.length;
					assertEquals(len, 7);
					for (int y = 0; y < 7; y++) {
						if (y%2 == 0) {
							assertEquals(ivector[y].getVal(), 7);
						} else {
							assertEquals(ivector[y].getVal(), 23);
						}
					}
				}
			}
		}	
		assertEquals(intCheckSum, sz3*sz3*2);
		// set and check the connected signals
		for (int y1 = 0; y1 < sz1; y1++){
			if (y1 > 2) {
				unit2layerD1.intSignal.get(y1).setVal(y1);
			} else {
				assertNull(unit2layerD1.intSignal.getUnit(y1));
			}
		}
		for (int x1 = 1; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y = 0; y < 7; y++) {
						assertEquals(unit1layerD3.intVector.get(x1, x2, x3)[y].getVal(), y+3);
					}
				}
			}
		}
		
		// connect using relation rand
		unit1layerD3.doubleVector.connect(unit2layerD1_doubleSignal, rand);
		// check the link vectors
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (unit1layerD3.getUnit(x1, x2, x3) == null) continue;
					intCheckSum++;
					Unit1.SignalDouble[] dvector = unit1layerD3.doubleVector.get(x1, x2, x3);
					int len = dvector.length;
					int cnt5 = 0;
					int cnt21 = 0;
					for (int y = 0; y < len; y++) {
						int val = (int)dvector[y].getVal();
						switch (val) {
							case 5:   cnt5++; break;
							case 21 : cnt21++; break;
							default: throw new JLayerException();
						}
					}
					assertEquals(cnt5 + cnt21, len);
				}
			}
		}	
		assertEquals(intCheckSum, sz3*sz3*2);
		// set and check the connected signals
		for (int y1 = 0; y1 < sz1; y1++){
			if (unit2layerD1_doubleSignal.get(y1) != null) {
				unit2layerD1_doubleSignal.get(y1).setVal(3.14);
			}
		}
		for (int x1 = 1; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1.SignalDouble[] dvector = unit1layerD3.doubleVector.get(x1, x2, x3);
					int len = dvector.length;
					for (int y = 0; y < len; y++) {
						assertEquals(unit1layerD3.doubleVector.get(x1, x2, x3)[y].getVal(), 3.14);
					}
				}
			}
		}			
		
		// connect using relation xHalf
		unit1layerD3_unit1Link.connect(unit2layerD1.unit1Item, xHalf);
		// check the link vectors
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					intCheckSum++;
					Unit1[] unit1Link = unit1layerD3_unit1Link.get(x1, x2, x3);
					int len = unit1Link.length;
					if ((x1+x2+x3)%2 == 0) {
						assertEquals(len, 7);
						for (int y = 0; y < len; y++) {
							
							if (y%2 == 0) {
								assertEquals(unit1Link[y].noItem, 3);
							} else {
								assertEquals(unit1Link[y].noItem, 9);
							}
						}
					} else {
						assertEquals(len, 0);
					}
				}
			}
		}
		assertEquals(intCheckSum, sz3*sz3*sz3);
		// set and check the connected signals
		for (int y1 = 0; y1 < sz1; y1++){
			if (y1 > 2) {
				unit2layerD1.unit1Item.get(y1).noItem = y1;
			} else {
				assertNull(unit2layerD1.unit1Item.getUnit(y1));
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1[] unit1Link = unit1layerD3_unit1Link.get(x1, x2, x3);
					for (int y = 0; y < unit1Link.length; y++) {
						assertEquals(unit1Link[y].noItem, y+3);
					}
				}
			}
		}		
		
		// connect using relation yHalf
		unit1layerD3_unit2Link.connect(unit2layerD1_unit2Item, yHalf);
		// check the connected signals
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					intCheckSum++;
					Unit2[] unit2Link = unit1layerD3_unit2Link.get(x1, x2, x3);
					assertEquals(unit2Link.length, 7);
					Set<Integer> is4 = new HashSet<Integer>();
					is4.add(4); is4.add(6);
					for (int y = 0; y < unit2Link.length; y++) {
						if (is4.contains(y)) {
							assertEquals(unit2Link[y].noItem, 4);
						} else {
							assertEquals(unit2Link[y].noItem, 7);
						}
					}
				}
			}
		}
		assertEquals(intCheckSum, sz3*sz3*sz3);
		// set and check the connected signals
		for (int y1 = 0; y1 < sz1; y1++){
			Set<Integer> is7 = new HashSet<Integer>();
			is7.add(7); is7.add(9);
			if ( is7.contains(y1) ) {
				assertNull(unit2layerD1_unit2Item.get(y1));
			} else {
				unit2layerD1_unit2Item.get(y1).noItem = y1;
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit2[] unit2Link = unit1layerD3_unit2Link.get(x1, x2, x3);
					for (int y = 0; y < unit2Link.length; y++) {
						assertEquals(unit1layerD3_unit2Link.get(x1, x2, x3)[y].noItem, y);
					}
				}
			}
		}		
		
	}	
	
	/****************************************************************************/
	
}

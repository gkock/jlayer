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
 * Testing {@code VectorLayer*.D2.connect(Layer.D3<?,T> layer, Relation rel)}.
 *
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Connect0_D2D3_Test {
	
//	final int sz1 = 10;
	final int sz2 = 4;
	final int sz3 = 3;
	
	boolean[][][][][] random1 = new boolean[sz2][sz2][sz3][sz3][sz3];
	int         	  trueno1 = 0;
	
	C_Connect0_D2D3_Test() {
		Random generator = new Random(System.nanoTime());
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
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
	
	final int[] val27_1 = new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26};
	final int[][][] val27_3 = new int[][][]{ { {0,1,2}, {3,4,5}, {6,7,8} }, { {9,10,11}, {12,13,14}, {15,16,17} }, { {18,19,20}, {21,22,23}, {24,25,26} } };
	
	/****************************************************************************/
	
	@Test // VectorLayer*.connect(Layer<?,T> layer, Relation rel)
	      // fully defined layer bases
	public void testVectorLayerNN1full() {
		ConnectUtil.setConnectForkJoinFlag(false);
		
		// init arrays
		Unit1[][] unit1arrayD2 = new Unit1[sz2][sz2];
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
					if ((i+j+k)%2 == 0) {
						unit2arrayD3[i][j][k].intSignal = new Unit1.SignalInt();
						unit2arrayD3[i][j][k].intSignal.setVal(23);  						// else: 7
						unit2arrayD3[i][j][k].doubleSignal = new Unit1.SignalDouble();
						unit2arrayD3[i][j][k].doubleSignal.setVal(21.0);					// else: 5.0
						unit2arrayD3[i][j][k].unit1Item = new Unit1(9);					// else: 3
						unit2arrayD3[i][j][k].unit2Item = new Unit2(4);					// else: 7
					}
				}
			}
		}
		
		// init D1 layers
		Layer_Unit1_ unit1layerD2 = new Layer_Unit1_(unit1arrayD2);
		Layer_Unit2_ unit2layerD3 = new Layer_Unit2_(unit2arrayD3);
		
		BasedLayer<Unit1.SignalDouble> unit2layerD3_doubleSignal = unit2layerD3.returnDoubleSignal();
		BasedVectorLayer<Unit1>             unit1layerD2_unit1Link = unit1layerD2.returnUnit1Link();
		BasedVectorLayer<Unit2>             unit1layerD2_unit2Link = unit1layerD2.returnUnit2Link();
		BasedLayer<Unit2>              unit2layerD3_unit2Item = unit2layerD3.returnUnit2Item();
		
		// connect using relation full
		unit1layerD2.intVector.connect(unit2layerD3.intSignal, full);
		// check the link vectors
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				
				Unit1.SignalInt[] ivector = unit1layerD2.intVector.get(x1, x2);
				int len = ivector.length;
				assertEquals(len, sz3*sz3*sz3);
				for (int y = 0; y < sz3*sz3*sz3; y++) {
					if (y%2 == 0) {
						assertEquals(ivector[y].getVal(), 23);
					} else {
						assertEquals(ivector[y].getVal(), 7);
					}	
				}
				
			}
		}	
		// set and check the connected signals
		for (int y1 = 0; y1 < sz3; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					unit2layerD3.intSignal.get(y1, y2, y3).setVal(val27_3[y1][y2][y3]);
				}
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y = 0; y < sz3*sz3*sz3; y++) {
					assertEquals(unit1layerD2.intVector.get(x1, x2)[y].getVal(), val27_1[y]);
				}
			}
		}
		
		// connect using relation rand
		unit1layerD2.doubleVector.connect(unit2layerD3_doubleSignal, rand);
		// check the link vectors
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit1.SignalDouble[] dvector = unit1layerD2.doubleVector.get(x1, x2);
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
		// set and check the connected signals
		for (int y1 = 0; y1 < sz3; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					if (unit2layerD3_doubleSignal.get(y1, y2, y3) != null) {
						unit2layerD3_doubleSignal.get(y1, y2, y3).setVal(3.14);
					}
				}
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit1.SignalDouble[] dvector = unit1layerD2.doubleVector.get(x1, x2);
				int len = dvector.length;
				for (int y = 0; y < len; y++) {
					assertEquals(unit1layerD2.doubleVector.get(x1, x2)[y].getVal(), 3.14);
				}
			}
		}		
		
		// connect using relation xHalf
		unit1layerD2_unit1Link.connect(unit2layerD3.unit1Item, xHalf);
		// check the link vectors
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit1[] unit1Link = unit1layerD2_unit1Link.get(x1, x2);
				int len = unit1Link.length;
				if ((x1+x2)%2 == 0) {
					assertEquals(len, sz3*sz3*sz3);
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
		// set and check the connected signals
		for (int y1 = 0; y1 < sz3; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					unit2layerD3.unit1Item.get(y1, y2, y3).noItem = val27_3[y1][y2][y3];
				}
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit1[] unit1Link = unit1layerD2_unit1Link.get(x1, x2);
				for (int y = 0; y < unit1Link.length; y++) {
					assertEquals(unit1Link[y].noItem, val27_1[y]);
				}
			}
		}		
		
		// connect using relation yHalf
		unit1layerD2_unit2Link.connect(unit2layerD3_unit2Item, yHalf);
		// check the connected signals
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit2[] unit2Link = unit1layerD2_unit2Link.get(x1, x2);
				assertEquals(unit2Link.length, 10);
				Set<Integer> is7 = new HashSet<Integer>();
				is7.add(3); is7.add(6); is7.add(8);
				for (int y = 0; y < unit2Link.length; y++) {
					if (is7.contains(y)) {
						assertEquals(unit2Link[y].noItem, 7);
					} else {
						assertEquals(unit2Link[y].noItem, 4);
					}
				}
			}
		}
		// set and check the connected signals
		for (int y1 = 0; y1 < sz3; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					if ( (y1+y2+y3)>sz3 || (y1+y2+y3)%2==0) {
						unit2layerD3_unit2Item.get(y1, y2, y3).noItem = val27_3[y1][y2][y3];
					} else {
						assertNull(unit2layerD3_unit2Item.get(y1, y2, y3));
					}
				}
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit2[] unit2Link = unit1layerD2_unit2Link.get(x1, x2);
				int[] val27 = new int[]{8,14,16,17,20,22,23,24,25,26};
				for (int y = 0; y < unit2Link.length; y++) {
					assertEquals(unit1layerD2_unit2Link.get(x1, x2)[y].noItem, val27[y]);
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
		Unit1[][] unit1arrayD2 = new Unit1[sz2][sz2];
		Unit2[][][] unit2arrayD3 = new Unit2[sz3][sz3][sz3];
		for (int i = 1; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				unit1arrayD2[i][j] = new Unit1(i+j); 
			}
		}
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					if (i<2) {
						unit2arrayD3[i][j][k] = new Unit2(i+j+k);
						if ((i+j+k)%2 == 0) {
							unit2arrayD3[i][j][k].intSignal = new Unit1.SignalInt();
							unit2arrayD3[i][j][k].intSignal.setVal(23);  						// else: 7
							unit2arrayD3[i][j][k].doubleSignal = new Unit1.SignalDouble();
							unit2arrayD3[i][j][k].doubleSignal.setVal(21.0);					// else: 5.0
							unit2arrayD3[i][j][k].unit1Item = new Unit1(9);					// else: 3
							unit2arrayD3[i][j][k].unit2Item = new Unit2(4);					// else: 7
						}
					}
				}
			}
		}
		
		// init D1 layers
		Layer_Unit1_ unit1layerD2 = new Layer_Unit1_(unit1arrayD2);
		Layer_Unit2_ unit2layerD3 = new Layer_Unit2_(unit2arrayD3);
		
		BasedLayer<Unit1.SignalDouble> unit2layerD3_doubleSignal = unit2layerD3.returnDoubleSignal();
		BasedVectorLayer<Unit1>             unit1layerD2_unit1Link = unit1layerD2.returnUnit1Link();
		BasedVectorLayer<Unit2>             unit1layerD2_unit2Link = unit1layerD2.returnUnit2Link();
		BasedLayer<Unit2>              unit2layerD3_unit2Item = unit2layerD3.returnUnit2Item();
		
		// variable used for checking
		int intCheckSum;
		
		// connect using relation full
		unit1layerD2.intVector.connect(unit2layerD3.intSignal, full);
		// check the link vectors
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if (unit1layerD2.getUnit(x1, x2) == null) continue;
				intCheckSum++;
				Unit1.SignalInt[] ivector = unit1layerD2.intVector.get(x1, x2);
				int len = ivector.length;
				assertEquals(len, sz3*sz3*2);
				for (int y = 0; y < sz3*sz3*2; y++) {
					if (y%2 == 0) {
						assertEquals(ivector[y].getVal(), 23);
					} else {
						assertEquals(ivector[y].getVal(), 7);
					}	
				}
			}
		}	
		assertEquals(intCheckSum, 12);
		// set and check the connected signals
		for (int y1 = 0; y1 < 2; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					unit2layerD3.intSignal.get(y1, y2, y3).setVal(val27_3[y1][y2][y3]);
				}
			}
		}
		for (int x1 = 1; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y = 0; y < sz3*sz3*2; y++) {
					assertEquals(unit1layerD2.intVector.get(x1, x2)[y].getVal(), val27_1[y]);
				}
			}
		}
		
		// connect using relation rand
		unit1layerD2.doubleVector.connect(unit2layerD3_doubleSignal, rand);
		// check the link vectors
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				if (unit1layerD2.getUnit(x1, x2) == null) continue;
				intCheckSum++;
				Unit1.SignalDouble[] dvector = unit1layerD2.doubleVector.get(x1, x2);
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
		assertEquals(intCheckSum, 12);
		// set and check the connected signals
		for (int y1 = 0; y1 < sz3; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					if (unit2layerD3_doubleSignal.get(y1, y2, y3) != null) {
						unit2layerD3_doubleSignal.get(y1, y2, y3).setVal(3.14);
					}
				}
			}
		}
		for (int x1 = 1; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit1.SignalDouble[] dvector = unit1layerD2.doubleVector.get(x1, x2);
				int len = dvector.length;
				for (int y = 0; y < len; y++) {
					assertEquals(unit1layerD2.doubleVector.get(x1, x2)[y].getVal(), 3.14);
				}
			}
		}			
		
		// connect using relation xHalf
		unit1layerD2_unit1Link.connect(unit2layerD3.unit1Item, xHalf);
		// check the link vectors
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				intCheckSum++;
				Unit1[] unit1Link = unit1layerD2_unit1Link.get(x1, x2);
				int len = unit1Link.length;
				if ((x1+x2)%2 == 0) {
					assertEquals(len, sz3*sz3*2);
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
		assertEquals(intCheckSum, sz2*sz2);
		// set and check the connected signals
		for (int y1 = 0; y1 < 2; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					unit2layerD3.unit1Item.get(y1, y2, y3).noItem = val27_3[y1][y2][y3];
				}
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit1[] unit1Link = unit1layerD2_unit1Link.get(x1, x2);
				for (int y = 0; y < unit1Link.length; y++) {
					assertEquals(unit1Link[y].noItem, val27_1[y]);
				}
			}
		}		
		
		// connect using relation yHalf
		unit1layerD2_unit2Link.connect(unit2layerD3_unit2Item, yHalf);
		// check the connected signals
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				intCheckSum++;
				Unit2[] unit2Link = unit1layerD2_unit2Link.get(x1, x2);
				assertEquals(unit2Link.length, 10);
				Set<Integer> is4 = new HashSet<Integer>();
				is4.add(0); is4.add(1); is4.add(2);
				for (int y = 0; y < unit2Link.length; y++) {
					if (is4.contains(y)) {
						assertEquals(unit2Link[y].noItem, 4);
					} else {
						assertEquals(unit2Link[y].noItem, 7);
					}
				}
			}
		}
		assertEquals(intCheckSum, sz2*sz2);
		// set and check the connected signals
		for (int y1 = 0; y1 < sz3; y1++){
			for (int y2 = 0; y2 < sz3; y2++){
				for (int y3 = 0; y3 < sz3; y3++){
					if ( (y1+y2+y3)>sz3 || (y1<2) && (y1+y2+y3)%2==0) {
						unit2layerD3_unit2Item.get(y1, y2, y3).noItem = val27_3[y1][y2][y3];
					} else {
						assertNull(unit2layerD3_unit2Item.get(y1, y2, y3));
					}
				}
			}
		}
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				Unit2[] unit2Link = unit1layerD2_unit2Link.get(x1, x2);
				int[] val27 = new int[]{8,14,16,17,20,22,23,24,25,26};
				for (int y = 0; y < unit2Link.length; y++) {
					assertEquals(unit1layerD2_unit2Link.get(x1, x2)[y].noItem, val27[y]);
				}
				
			}
		}		
	}	
	
	/****************************************************************************/
	
}

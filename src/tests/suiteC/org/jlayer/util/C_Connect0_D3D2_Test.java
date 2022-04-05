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
 * Testing {@code VectorLayer*.D3.connect(Layer.D2<?,T> layer, Relation rel)}.
 *
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Connect0_D3D2_Test {
	
//	final int sz1 = 10;
	final int sz2 = 4;
	final int sz3 = 3;
	
	boolean[][][][][] random1 = new boolean[sz3][sz3][sz3][sz2][sz2];
	int         	  trueno1 = 0;
	
	C_Connect0_D3D2_Test() {
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
			return ((y1+y2) >= sz2) ? true : false; 
		}
	};
	
	final int[] val16_1 = new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
	final int[][] val16_2 = new int[][]{ {0,1,2,3}, {4,5,6,7}, {8,9,10,11}, {12,13,14,15} };
	
	/****************************************************************************/
	
	@Test // VectorLayer*.connect(Layer<?,T> layer, Relation rel)
	      // fully defined layer bases
	public void testVectorLayerNN1full() {
		ConnectUtil.setConnectForkJoinFlag(false);
		
		// init arrays
		Unit1[][][] unit1arrayD3 = new Unit1[sz3][sz3][sz3];
		Unit2[][] unit2arrayD2 = new Unit2[sz2][sz2];
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
				if ((i+j)%2 == 0) {
					unit2arrayD2[i][j].intSignal = new Unit1.SignalInt();
					unit2arrayD2[i][j].intSignal.setVal(23);  						// else: 7
					unit2arrayD2[i][j].doubleSignal = new Unit1.SignalDouble();
					unit2arrayD2[i][j].doubleSignal.setVal(21.0);					// else: 5.0
					unit2arrayD2[i][j].unit1Item = new Unit1(9);				// else: 3
					unit2arrayD2[i][j].unit2Item = new Unit2(4);				// else: 7
				}
			}
		}
		final int[] intSignalCheck = new int[] {23,7,23,7,7,23,7,23,23,7,23,7,7,23,7,23};
		final int[] unit1ItemCheck = new int[] {9,3,9,3,3,9,3,9,9,3,9,3,3,9,3,9};
		
		// init D1 layers
		Layer_Unit1_ unit1layerD3 = new Layer_Unit1_(unit1arrayD3);
		Layer_Unit2_ unit2layerD2 = new Layer_Unit2_(unit2arrayD2);
		
		BasedLayer<Unit1.SignalDouble> unit2layerD2_doubleSignal = unit2layerD2.returnDoubleSignal();
		BasedVectorLayer<Unit1>             unit1layerD3_unit1Link = unit1layerD3.returnUnit1Link();
		BasedVectorLayer<Unit2>             unit1layerD3_unit2Link = unit1layerD3.returnUnit2Link();
		BasedLayer<Unit2>              unit2layerD2_unit2Item = unit2layerD2.returnUnit2Item();
		
		// connect using relation full
		unit1layerD3.intVector.connect(unit2layerD2.intSignal, full);
		// check the link vectors
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1.SignalInt[] ivector = unit1layerD3.intVector.get(x1, x2, x3);
					int len = ivector.length;
					assertEquals(len, sz2*sz2);
					for (int y = 0; y < len; y++) {
						assertEquals(ivector[y].getVal(), intSignalCheck[y]);
					}
				}
			}
		}	
		// set and check the connected signals
		for (int y1 = 0; y1 < sz2; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				unit2layerD2.intSignal.get(y1, y2).setVal(val16_2[y1][y2]);
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y = 0; y < sz2*sz2; y++) {
						assertEquals(unit1layerD3.intVector.get(x1, x2, x3)[y].getVal(), val16_1[y]);
					}
				}
			}
		}
		
		// connect using relation rand
		unit1layerD3.doubleVector.connect(unit2layerD2_doubleSignal, rand);
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
		for (int y1 = 0; y1 < sz2; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				if (unit2layerD2_doubleSignal.get(y1, y2) != null) {
					unit2layerD2_doubleSignal.get(y1, y2).setVal(3.14);
				}
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
		unit1layerD3_unit1Link.connect(unit2layerD2.unit1Item, xHalf);
		// check the link vectors
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1[] unit1Link = unit1layerD3_unit1Link.get(x1, x2, x3);
					int len = unit1Link.length;
					if ((x1+x2+x3)%2 == 0) {
						assertEquals(len, sz2*sz2);
						for (int y = 0; y < len; y++) {
							assertEquals(unit1Link[y].noItem, unit1ItemCheck[y]);
						}
					} else {
						assertEquals(len, 0);
					}
				}
			}
		}
		// set and check the connected signals
		for (int y1 = 0; y1 < sz2; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				unit2layerD2.unit1Item.get(y1, y2).noItem = val16_2[y1][y2];
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1[] unit1Link = unit1layerD3_unit1Link.get(x1, x2, x3);
					for (int y = 0; y < unit1Link.length; y++) {
						assertEquals(unit1Link[y].noItem, val16_1[y]);
					}
				}
			}
		}		
		
		// connect using relation yHalf
		unit1layerD3_unit2Link.connect(unit2layerD2_unit2Item, yHalf);
		// check the connected signals
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit2[] unit2Link = unit1layerD3_unit2Link.get(x1, x2, x3);
					assertEquals(unit2Link.length, 6);
					Set<Integer> is7 = new HashSet<Integer>();
					is7.add(2); is7.add(4);
					for (int y = 0; y < unit2Link.length; y++) {
						if (is7.contains(y)) {
							assertEquals(unit2Link[y].noItem, 7);
						} else {
							assertEquals(unit2Link[y].noItem, 4);
						}
					}
				}
			}
		}
		// set and check the connected signals
		for (int y1 = 0; y1 < sz2; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				if ( (y1+y2)>=sz2 || (y1+y2)%2==0) {
					unit2layerD2_unit2Item.get(y1, y2).noItem = val16_2[y1][y2];
				} else {
					assertNull(unit2layerD2_unit2Item.get(y1, y2));
				}
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit2[] unit2Link = unit1layerD3_unit2Link.get(x1, x2, x3);
					int[] val16 = new int[]{7,10,11,13,14,15};
					for (int y = 0; y < unit2Link.length; y++) {
						assertEquals(unit1layerD3_unit2Link.get(x1, x2, x3)[y].noItem, val16[y]);
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
		Unit2[][] unit2arrayD2 = new Unit2[sz2][sz2];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					if (i>0) {
						unit1arrayD3[i][j][k] = new Unit1(i+j+k); 
					}
				}
			}
		}
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				if (i<3) {
					unit2arrayD2[i][j] = new Unit2(i+j);
					if ((i+j)%2 == 0) {
						unit2arrayD2[i][j].intSignal = new Unit1.SignalInt();
						unit2arrayD2[i][j].intSignal.setVal(23);  						// else: 7
						unit2arrayD2[i][j].doubleSignal = new Unit1.SignalDouble();
						unit2arrayD2[i][j].doubleSignal.setVal(21.0);					// else: 5.0
						unit2arrayD2[i][j].unit1Item = new Unit1(9);				// else: 3
						unit2arrayD2[i][j].unit2Item = new Unit2(4);				// else: 7
					}
				}
			}
		}
		
		// init D1 layers
		Layer_Unit1_ unit1layerD3 = new Layer_Unit1_(unit1arrayD3);
		Layer_Unit2_ unit2layerD2 = new Layer_Unit2_(unit2arrayD2);
		
		BasedLayer<Unit1.SignalDouble> unit2layerD2_doubleSignal = unit2layerD2.returnDoubleSignal();
		BasedVectorLayer<Unit1>             unit1layerD3_unit1Link = unit1layerD3.returnUnit1Link();
		BasedVectorLayer<Unit2>             unit1layerD3_unit2Link = unit1layerD3.returnUnit2Link();
		BasedLayer<Unit2>              unit2layerD2_unit2Item = unit2layerD2.returnUnit2Item();
		
		// variable used for checking
		int intCheckSum;
		
		// connect using relation full
		unit1layerD3.intVector.connect(unit2layerD2.intSignal, full);
		// check the link vectors
		intCheckSum = 0;
		int[] valY1 = new int[]{23,7,23,7,7,23,7,23,23,7,23,7};
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					if (unit1layerD3.getUnit(x1, x2, x3) == null) continue;
					intCheckSum++;
					Unit1.SignalInt[] ivector = unit1layerD3.intVector.get(x1, x2, x3);
					int len = ivector.length;
					assertEquals(len, sz2*3);
					for (int y = 0; y < sz2*3; y++) {
						assertEquals(ivector[y].getVal(), valY1[y]);
					}
				}
			}
		}	
		assertEquals(intCheckSum, sz3*sz3*2);
		// set and check the connected signals
		for (int y1 = 0; y1 < sz2-1; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				unit2layerD2.intSignal.get(y1, y2).setVal(val16_2[y1][y2]);
			}
		}
		for (int x1 = 1; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y = 0; y < sz2*3; y++) {
						assertEquals(unit1layerD3.intVector.get(x1, x2, x3)[y].getVal(), val16_1[y]);
					}
				}
			}
		}
		
		// connect using relation rand
		unit1layerD3.doubleVector.connect(unit2layerD2_doubleSignal, rand);
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
		for (int y1 = 0; y1 < sz2; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				if (unit2layerD2_doubleSignal.get(y1, y2) != null) {
					unit2layerD2_doubleSignal.get(y1, y2).setVal(3.14);
				}
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
		unit1layerD3_unit1Link.connect(unit2layerD2.unit1Item, xHalf);
		// check the link vectors
		intCheckSum = 0;
		int[] valY2 = new int[]{9,3,9,3,3,9,3,9,9,3,9,3};
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					intCheckSum++;
					Unit1[] unit1Link = unit1layerD3_unit1Link.get(x1, x2, x3);
					int len = unit1Link.length;
					if ((x1+x2+x3)%2 == 0) {
						assertEquals(len, sz2*3);
						for (int y = 0; y < len; y++) {
							assertEquals(unit1Link[y].noItem, valY2[y]);
						}
					} else {
						assertEquals(len, 0);
					}
				}
			}
		}
		assertEquals(intCheckSum, sz3*sz3*sz3);
		// set and check the connected signals
		for (int y1 = 0; y1 < sz2-1; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				unit2layerD2.unit1Item.get(y1, y2).noItem = val16_2[y1][y2];
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit1[] unit1Link = unit1layerD3_unit1Link.get(x1, x2, x3);
					for (int y = 0; y < unit1Link.length; y++) {
						assertEquals(unit1Link[y].noItem, val16_1[y]);
					}
				}
			}
		}		
		
		// connect using relation yHalf
		unit1layerD3_unit2Link.connect(unit2layerD2_unit2Item, yHalf);
		// check the connected signals
		intCheckSum = 0;
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					intCheckSum++;
					Unit2[] unit2Link = unit1layerD3_unit2Link.get(x1, x2, x3);
					assertEquals(unit2Link.length, 6);
					Set<Integer> is4 = new HashSet<Integer>();
					is4.add(0); is4.add(1);
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
		for (int y1 = 0; y1 < sz2; y1++){
			for (int y2 = 0; y2 < sz2; y2++){
				if ( (y1+y2)>=sz2 || (y1<3) && (y1+y2)%2==0) {
					unit2layerD2_unit2Item.get(y1, y2).noItem = val16_2[y1][y2];
				} else {
					assertNull(unit2layerD2_unit2Item.get(y1, y2));
				}
			}
		}
		for (int x1 = 0; x1 < sz3; x1++){
			for (int x2 = 0; x2 < sz3; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					Unit2[] unit2Link = unit1layerD3_unit2Link.get(x1, x2, x3);
					int[] val16 = new int[]{7,10,11,13,14,15};
					for (int y = 0; y < unit2Link.length; y++) {
						assertEquals(unit1layerD3_unit2Link.get(x1, x2, x3)[y].noItem, val16[y]);
					}
				}
			}
		}		
		
	}	
	
	/****************************************************************************/
	
}

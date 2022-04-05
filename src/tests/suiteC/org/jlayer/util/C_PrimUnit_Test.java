package org.jlayer.util;

import java.util.Iterator;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import basic.units.*;

/**
 * tests around primitive types.
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_PrimUnit_Test {
	
	final int sz1 = 10;
	final int sz2 = 3;
	final int sz3 = 2;
	
	PrimUnit[] primArr1;
	PrimUnit[][] primArr2;
	PrimUnit[][][] primArr3;
	
	Boolean[] booleanArr1;
	Boolean[][] booleanArr2;
	Boolean[][][] booleanArr3;
	Byte[] byteArr1;
	Byte[][] byteArr2;
	Byte[][][] byteArr3;
	Character[] charArr1;
	Character[][] charArr2;
	Character[][][] charArr3;
	Double[] doubleArr1;
	Double[][] doubleArr2;
	Double[][][] doubleArr3;
	Float[] floatArr1;
	Float[][] floatArr2;
	Float[][][] floatArr3;
	Integer[] intArr1;
	Integer[][] intArr2;
	Integer[][][] intArr3;
	Long[] longArr1;
	Long[][] longArr2;
	Long[][][] longArr3;
	Short[] shortArr1;
	Short[][] shortArr2;
	Short[][][] shortArr3;
	
	Layer_PrimUnit_ unitLayer;
	
	LayerBase<Boolean> boolLayer;
	LayerBase<Byte> byteLayer;
	LayerBase<Character> charLayer;
	LayerBase<Double> doubleLayer;
	LayerBase<Float> floatLayer;
	LayerBase<Integer> intLayer;
	LayerBase<Long> longLayer;
	LayerBase<Short> shortLayer;
	
	@BeforeClass
    public void beforeClass() {
		// dim == 1
		primArr1 = new PrimUnit[sz1];
		booleanArr1 = new Boolean[sz1];
		byteArr1 = new Byte[sz1];
		charArr1 = new Character[sz1];
		doubleArr1 = new Double[sz1];
		floatArr1 = new Float[sz1];
		intArr1 = new Integer[sz1];
		longArr1 = new Long[sz1];
		shortArr1 = new Short[sz1];
		for (int i = 0; i < sz1; i++) {	
			primArr1[i] = new PrimUnit(4711); 
			booleanArr1[i] = i%2==0;
			byteArr1[i] = (byte)i;
			charArr1[i] = (char)i;
			doubleArr1[i] = (double)i;
			floatArr1[i] = (float)i;
			intArr1[i] = i;
			longArr1[i] = (long)i;
			shortArr1[i] = (short)i;
		}
		// dim == 2
		primArr2 = new PrimUnit[sz2][sz2];
		booleanArr2 = new Boolean[sz2][sz2];
		byteArr2 = new Byte[sz2][sz2];
		charArr2 = new Character[sz2][sz2];
		doubleArr2 = new Double[sz2][sz2];
		floatArr2 = new Float[sz2][sz2];
		intArr2 = new Integer[sz2][sz2];
		longArr2 = new Long[sz2][sz2];
		shortArr2 = new Short[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				primArr2[i][j] = new PrimUnit(4711);
				booleanArr2[i][j] = (i+j)%2==0;
				byteArr2[i][j] = (byte)(i+j);
				charArr2[i][j] = (char)(i+j);
				doubleArr2[i][j] = (double)(i+j);
				floatArr2[i][j] = (float)(i+j);
				intArr2[i][j] = i+j;
				longArr2[i][j] = (long)(i+j);
				shortArr2[i][j] = (short)(i+j);
			}
		}
		// dim == 3
		primArr3 = new PrimUnit[sz3][sz3][sz3];
		booleanArr3 = new Boolean[sz3][sz3][sz3];
		byteArr3 = new Byte[sz3][sz3][sz3];
		charArr3 = new Character[sz3][sz3][sz3];
		doubleArr3 = new Double[sz3][sz3][sz3];
		floatArr3 = new Float[sz3][sz3][sz3];
		intArr3 = new Integer[sz3][sz3][sz3];
		longArr3 = new Long[sz3][sz3][sz3];
		shortArr3 = new Short[sz3][sz3][sz3];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					primArr3[i][j][k] = new PrimUnit(4711);
					booleanArr3[i][j][k] = (i+j+k)%2==0;
					byteArr3[i][j][k] = (byte)(i+j+k);
					charArr3[i][j][k] = (char)(i+j+k);
					doubleArr3[i][j][k] = (double)(i+j+k);
					floatArr3[i][j][k] = (float)(i+j+k);
					intArr3[i][j][k] = i+j+k;
					longArr3[i][j][k] = (long)(i+j+k);
					shortArr3[i][j][k] = (short)(i+j+k);
				}
			}
		}
	}
	
	@Test
	public void testSetNnItem1() {
		unitLayer = new Layer_PrimUnit_(primArr1);
		boolLayer = new LayerBase<Boolean>(booleanArr1);
		byteLayer = new LayerBase<Byte>(byteArr1);
		charLayer = new LayerBase<Character>(charArr1);
		doubleLayer = new LayerBase<Double>(doubleArr1);
		floatLayer = new LayerBase<Float>(floatArr1);
		intLayer = new LayerBase<Integer>(intArr1);
		longLayer = new LayerBase<Long>(longArr1);
		shortLayer = new LayerBase<Short>(shortArr1);
		// setBooleanItem
		unitLayer.setBooleanItem(boolLayer);
		Iterator<Boolean> boolIter = unitLayer.booleanItem.iterator();
		int boolCnt = 0;
		while (boolIter.hasNext()){ if (boolIter.next()) boolCnt++; }
		assertEquals(boolCnt, 5);
		// setByteItem
		unitLayer.setByteItem(byteLayer);
		Iterator<Byte> byteIter = unitLayer.byteItem.iterator();
		byte byteCnt = 0;
		while (byteIter.hasNext()){ byteCnt += byteIter.next(); }
		assertEquals(byteCnt, 45);
		// setCharItem
		unitLayer.setCharItem(charLayer);
		Iterator<Character> charIter = unitLayer.charItem.iterator();
		char charCnt = 0;
		while (charIter.hasNext()){ charCnt += charIter.next(); }
		assertEquals(charCnt, 45);
		// setDoubleItem
		unitLayer.setDoubleItem(doubleLayer);
		Iterator<Double> doubleIter = unitLayer.doubleItem.iterator();
		double doubleCnt = 0D;
		while (doubleIter.hasNext()){ doubleCnt += doubleIter.next(); }
		assertEquals(doubleCnt, 45D);
		// setFloatItem
		unitLayer.setFloatItem(floatLayer);
		Iterator<Float> floatIter = unitLayer.floatItem.iterator();
		float floatCnt = 0F;
		while (floatIter.hasNext()){ floatCnt += floatIter.next(); }
		assertEquals(floatCnt, 45F);
		// setIntItem
		unitLayer.setIntItem(intLayer);
		Iterator<Integer> intIter = unitLayer.intItem.iterator();
		int intCnt = 0;
		while (intIter.hasNext()){ intCnt += intIter.next(); }
		assertEquals(intCnt, 45);
		// setLongItem
		unitLayer.setLongItem(longLayer);
		Iterator<Long> longIter = unitLayer.longItem.iterator();
		long longCnt = 0;
		while (longIter.hasNext()){ longCnt += longIter.next(); }
		assertEquals(longCnt, 45);
		// setShortItem
		unitLayer.setShortItem(shortLayer);
		Iterator<Short> shortIter = unitLayer.shortItem.iterator();
		long shortCnt = 0;
		while (shortIter.hasNext()){ shortCnt += shortIter.next(); }
		assertEquals(shortCnt, 45);
	}
	
	@Test
	public void testSetNnItem2() {
		unitLayer = new Layer_PrimUnit_(primArr2);
		boolLayer = new LayerBase<Boolean>(booleanArr2);
		byteLayer = new LayerBase<Byte>(byteArr2);
		charLayer = new LayerBase<Character>(charArr2);
		doubleLayer = new LayerBase<Double>(doubleArr2);
		floatLayer = new LayerBase<Float>(floatArr2);
		intLayer = new LayerBase<Integer>(intArr2);
		longLayer = new LayerBase<Long>(longArr2);
		shortLayer = new LayerBase<Short>(shortArr2);
		// setBooleanItem
		unitLayer.setBooleanItem(boolLayer);
		Iterator<Boolean> boolIter = unitLayer.booleanItem.iterator();
		int boolCnt = 0;
		while (boolIter.hasNext()){ if (boolIter.next()) boolCnt++; }
		assertEquals(boolCnt, 5);
		// setByteItem
		unitLayer.setByteItem(byteLayer);
		Iterator<Byte> byteIter = unitLayer.byteItem.iterator();
		byte byteCnt = 0;
		while (byteIter.hasNext()){ byteCnt += byteIter.next(); }
		assertEquals(byteCnt, 18);
		// setCharItem
		unitLayer.setCharItem(charLayer);
		Iterator<Character> charIter = unitLayer.charItem.iterator();
		char charCnt = 0;
		while (charIter.hasNext()){ charCnt += charIter.next(); }
		assertEquals(charCnt, 18);
		// setDoubleItem
		unitLayer.setDoubleItem(doubleLayer);
		Iterator<Double> doubleIter = unitLayer.doubleItem.iterator();
		double doubleCnt = 0D;
		while (doubleIter.hasNext()){ doubleCnt += doubleIter.next(); }
		assertEquals(doubleCnt, 18D);
		// setFloatItem
		unitLayer.setFloatItem(floatLayer);
		Iterator<Float> floatIter = unitLayer.floatItem.iterator();
		float floatCnt = 0F;
		while (floatIter.hasNext()){ floatCnt += floatIter.next(); }
		assertEquals(floatCnt, 18F);
		// setIntItem
		unitLayer.setIntItem(intLayer);
		Iterator<Integer> intIter = unitLayer.intItem.iterator();
		int intCnt = 0;
		while (intIter.hasNext()){ intCnt += intIter.next(); }
		assertEquals(intCnt, 18);
		// setLongItem
		unitLayer.setLongItem(longLayer);
		Iterator<Long> longIter = unitLayer.longItem.iterator();
		long longCnt = 0;
		while (longIter.hasNext()){ longCnt += longIter.next(); }
		assertEquals(longCnt, 18);
		// setShortItem
		unitLayer.setShortItem(shortLayer);
		Iterator<Short> shortIter = unitLayer.shortItem.iterator();
		long shortCnt = 0;
		while (shortIter.hasNext()){ shortCnt += shortIter.next(); }
		assertEquals(shortCnt, 18);
	}
	
	@Test
	public void testSetNnItem3() {
		unitLayer = new Layer_PrimUnit_(primArr3);
		boolLayer = new LayerBase<Boolean>(booleanArr3);
		byteLayer = new LayerBase<Byte>(byteArr3);
		charLayer = new LayerBase<Character>(charArr3);
		doubleLayer = new LayerBase<Double>(doubleArr3);
		floatLayer = new LayerBase<Float>(floatArr3);
		intLayer = new LayerBase<Integer>(intArr3);
		longLayer = new LayerBase<Long>(longArr3);
		shortLayer = new LayerBase<Short>(shortArr3);
		// setBooleanItem
		unitLayer.setBooleanItem(boolLayer);
		Iterator<Boolean> boolIter = unitLayer.booleanItem.iterator();
		int boolCnt = 0;
		while (boolIter.hasNext()){ if (boolIter.next()) boolCnt++; }
		assertEquals(boolCnt, 4);
		// setByteItem
		unitLayer.setByteItem(byteLayer);
		Iterator<Byte> byteIter = unitLayer.byteItem.iterator();
		byte byteCnt = 0;
		while (byteIter.hasNext()){ byteCnt += byteIter.next(); }
		assertEquals(byteCnt, 12);
		// setCharItem
		unitLayer.setCharItem(charLayer);
		Iterator<Character> charIter = unitLayer.charItem.iterator();
		char charCnt = 0;
		while (charIter.hasNext()){ charCnt += charIter.next(); }
		assertEquals(charCnt, 12);
		// setDoubleItem
		unitLayer.setDoubleItem(doubleLayer);
		Iterator<Double> doubleIter = unitLayer.doubleItem.iterator();
		double doubleCnt = 0D;
		while (doubleIter.hasNext()){ doubleCnt += doubleIter.next(); }
		assertEquals(doubleCnt, 12D);
		// setFloatItem
		unitLayer.setFloatItem(floatLayer);
		Iterator<Float> floatIter = unitLayer.floatItem.iterator();
		float floatCnt = 0F;
		while (floatIter.hasNext()){ floatCnt += floatIter.next(); }
		assertEquals(floatCnt, 12F);
		// setIntItem
		unitLayer.setIntItem(intLayer);
		Iterator<Integer> intIter = unitLayer.intItem.iterator();
		int intCnt = 0;
		while (intIter.hasNext()){ intCnt += intIter.next(); }
		assertEquals(intCnt, 12);
		// setLongItem
		unitLayer.setLongItem(longLayer);
		Iterator<Long> longIter = unitLayer.longItem.iterator();
		long longCnt = 0;
		while (longIter.hasNext()){ longCnt += longIter.next(); }
		assertEquals(longCnt, 12);
		// setShortItem
		unitLayer.setShortItem(shortLayer);
		Iterator<Short> shortIter = unitLayer.shortItem.iterator();
		long shortCnt = 0;
		while (shortIter.hasNext()){ shortCnt += shortIter.next(); }
		assertEquals(shortCnt, 12);
	}
	
	@Test
	public void testLayerSetGet1() {
		unitLayer = new Layer_PrimUnit_(primArr1);
		// setBooleanItem
		for (int i = 0; i < sz1; i++) unitLayer.booleanItem.set(i, ((i%2)!=0));
		for (int i = 0; i < sz1; i++) assertEquals((boolean)unitLayer.booleanItem.get(i), ((i%2)!=0));
		// setByteItem
		for (int i = 0; i < sz1; i++) unitLayer.byteItem.set(i, (byte)i);
		for (int i = 0; i < sz1; i++) assertEquals((byte)unitLayer.byteItem.get(i), i);
		// setCharItem
		for (int i = 0; i < sz1; i++) unitLayer.charItem.set(i, (char)i);
		for (int i = 0; i < sz1; i++) assertEquals((char)unitLayer.charItem.get(i), i);
		// setDoubleItem
		for (int i = 0; i < sz1; i++) unitLayer.doubleItem.set(i, (double)i);
		for (int i = 0; i < sz1; i++) assertEquals((double)unitLayer.doubleItem.get(i), (double)i);
		// setFloatItem
		for (int i = 0; i < sz1; i++) unitLayer.floatItem.set(i, (float)i);
		for (int i = 0; i < sz1; i++) assertEquals((float)unitLayer.floatItem.get(i), (float)i);
		// setIntItem
		for (int i = 0; i < sz1; i++) unitLayer.intItem.set(i, i);
		for (int i = 0; i < sz1; i++) assertEquals((int)unitLayer.intItem.get(i), i);
		// setLongItem
		for (int i = 0; i < sz1; i++) unitLayer.longItem.set(i, (long)i);
		for (int i = 0; i < sz1; i++) assertEquals((long)unitLayer.longItem.get(i), i);
		// setShortItem
		for (int i = 0; i < sz1; i++) unitLayer.shortItem.set(i, (short)i);
		for (int i = 0; i < sz1; i++) assertEquals((short)unitLayer.shortItem.get(i), i);
	}
	
	@Test
	public void testLayerSetGet2() {
		unitLayer = new Layer_PrimUnit_(primArr2);
		// setBooleanItem
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				unitLayer.booleanItem.set(i, j, (((i+j)%2)!=0));
			}
		}
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				assertEquals((boolean)unitLayer.booleanItem.get(i, j), (((i+j)%2)!=0));
			}
		}
		// setByteItem
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				unitLayer.byteItem.set(i, j, (byte)(i+j));
			}
		}
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				assertEquals((byte)unitLayer.byteItem.get(i, j), i+j);
			}
		}
		// setCharItem
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				unitLayer.charItem.set(i, j, (char)(i+j));
			}
		}
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				assertEquals((char)unitLayer.charItem.get(i, j), i+j);
			}
		}
		// setDoubleItem
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				unitLayer.doubleItem.set(i, j, (double)(i+j));
			}
		}
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				assertEquals((double)unitLayer.doubleItem.get(i, j), (double)(i+j));
			}
		}
		// setFloatItem
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				unitLayer.floatItem.set(i, j, (float)(i+j));
			}
		}
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				assertEquals((float)unitLayer.floatItem.get(i, j), (float)(i+j));
			}
		}
		// setIntItem
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				unitLayer.intItem.set(i, j, i+j);
			}
		}
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				assertEquals((int)unitLayer.intItem.get(i, j), i+j);
			}
		}
		// setLongItem
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				unitLayer.longItem.set(i, j, (long)(i+j));
			}
		}
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				assertEquals((long)unitLayer.longItem.get(i, j), i+j);
			}
		}
		// setShortItem
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				unitLayer.shortItem.set(i, j, (short)(i+j));
			}
		}
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				assertEquals((short)unitLayer.shortItem.get(i, j), i+j);
			}
		}
	}
	
	@Test
	public void testLayerSetGet3() {
		unitLayer = new Layer_PrimUnit_(primArr3);
		// setBooleanItem
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					unitLayer.booleanItem.set(i, j, k, (((i+j+k)%2)!=0));
				}
			}
		}
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					assertEquals((boolean)unitLayer.booleanItem.get(i, j, k), (((i+j+k)%2)!=0));
				}
			}
		}
		// setByteItem
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					unitLayer.byteItem.set(i, j, k, (byte)(i+j+k));
				}
			}
		}
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					assertEquals((byte)unitLayer.byteItem.get(i, j, k), i+j+k);
				}
			}
		}
		// setCharItem
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					unitLayer.charItem.set(i, j, k, (char)(i+j+k));
				}
			}
		}
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					assertEquals((char)unitLayer.charItem.get(i, j, k), i+j+k);
				}
			}
		}
		// setDoubleItem
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					unitLayer.doubleItem.set(i, j, k, (double)(i+j+k));
				}
			}
		}
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					assertEquals((double)unitLayer.doubleItem.get(i, j, k), (double)(i+j+k));
				}
			}
		}
		// setFloatItem
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					unitLayer.floatItem.set(i, j, k, (float)(i+j+k));
				}
			}
		}
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					assertEquals((float)unitLayer.floatItem.get(i, j, k), (float)(i+j+k));
				}
			}
		}
		// setIntItem
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					unitLayer.intItem.set(i, j, k, i+j+k);
				}
			}
		}
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					assertEquals((int)unitLayer.intItem.get(i, j, k), i+j+k);
				}
			}
		}
		// setLongItem
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					unitLayer.longItem.set(i, j, k,(long)(i+j+k));
				}
			}
		}
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					assertEquals((long)unitLayer.longItem.get(i, j, k), i+j+k);
				}
			}
		}
		// setShortItem
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					unitLayer.shortItem.set(i, j, k, (short)(i+j+k));
				}
			}
		}
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					assertEquals((short)unitLayer.shortItem.get(i, j, k), i+j+k);
				}
			}
		}
	}
  
}

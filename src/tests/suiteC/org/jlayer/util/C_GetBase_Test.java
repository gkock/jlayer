package org.jlayer.util;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNull;

import org.jlayer.model.*;

import basic.units.*;

/**
 * tests the layer methods getD1(), getD2(), getD3() and base().
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_GetBase_Test {
	
	final int sz1 = 10;
	final int sz2 = 3;
	final int sz3 = 2;
	
	Integer[] intArrD1;
	Integer[][] intArrD2;
	Integer[][][] intArrD3;
	Unit1[] unit1ArrD1;
	Unit1[][] unit1ArrD2;
	Unit1[][][] unit1ArrD3;
	
	@BeforeClass
    public void beforeClass() {
		unit1ArrD1 = new Unit1[sz1];
		intArrD1 = new Integer[sz1];
		for (int i = 0; i < sz1; i++) {	
			unit1ArrD1[i] = new Unit1(i); 
			intArrD1[i] = i; 
		}
		unit1ArrD2 = new Unit1[sz2][sz2];
		intArrD2 = new Integer[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				unit1ArrD2[i][j] = new Unit1(i+j); 
				intArrD2[i][j] = i+j; 
			}
		}
		unit1ArrD3 = new Unit1[sz3][sz3][sz3];
		intArrD3 = new Integer[sz3][sz3][sz3];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					intArrD3[i][j][k] = i+j+k; 
					unit1ArrD3[i][j][k] = new Unit1(i+j+k); 
				}
			}
		}
	}
	
	@Test
	public void testLayer_Unit1_D1() {
		Layer_Unit1_ layer = new Layer_Unit1_(unit1ArrD1);
		layer.setUnit1Item();
		// layer.getD1().base()
		Unit1[] testArr1 = layer.getD1().getBase();
		assertEquals(testArr1, unit1ArrD1);
		assertSame(testArr1, unit1ArrD1);
		// layer.returnThis().getD1().base();
		Unit1[] testArr2 = layer.returnThis().getD1().getBase();
		for (int i = 0; i < sz1; i++) {	
			assertEquals(testArr2[i].noItem, i);
		}
		assertNotSame(testArr2, unit1ArrD1);
		// layer.unit1Item.getD1()
		Layer.D1<Unit1,Unit1> testLayer = layer.unit1Item.getD1();
		for (int i = 0; i < sz1; i++) {	
			assertEquals(testLayer.get(i).noItem, i);
		}
		// etc.
		assertNull(layer.getD2());
		assertNull(layer.getD3());
	}
	
	@Test
	public void testLayer_Unit1_D2() {
		Layer_Unit1_ layer = new Layer_Unit1_(unit1ArrD2);
		layer.setUnit1Item();
		// layer.getD2().base()
		Unit1[][] testArr1 = layer.getD2().getBase();
		assertEquals(testArr1, unit1ArrD2);
		assertSame(testArr1, unit1ArrD2);
		// layer.returnThis().getD2().base();
		Unit1[][] testArr2 = layer.returnThis().getD2().getBase();
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				assertEquals(testArr2[i][j].noItem, i+j);
			}
		}
		assertNotSame(testArr2, unit1ArrD2);
		// layer.unit1Item.getD1()
		Layer.D2<Unit1,Unit1> testLayer = layer.unit1Item.getD2();
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				assertEquals(testLayer.get(i,j).noItem, i+j);
			}
		}
		// etc.
		assertNull(layer.getD1());
		assertNull(layer.getD3());
	}
	
	@Test
	public void testLayer_Unit1_D3() {
		Layer_Unit1_ layer = new Layer_Unit1_(unit1ArrD3);
		layer.setUnit1Item();
		// layer.getD3().base()
		Unit1[][][] testArr1 = layer.getD3().getBase();
		assertEquals(testArr1, unit1ArrD3);
		assertSame(testArr1, unit1ArrD3);
		// layer.returnThis().getD3().base();
		Unit1[][][] testArr2 = layer.returnThis().getD3().getBase();
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					assertEquals(testArr2[i][j][k].noItem, i+j+k);
				}
			}
		}
		assertNotSame(testArr2, unit1ArrD3);
		// layer.unit1Item.getD1()
		Layer.D3<Unit1,Unit1> testLayer = layer.unit1Item.getD3();
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					assertEquals(testLayer.get(i,j,k).noItem, i+j+k);
				}
			}
		}
		// etc.
		assertNull(layer.getD1());
		assertNull(layer.getD2());
	}
	
	@Test
	public void testLayer_int_D1() {
		Layer_Unit1_ layer = new Layer_Unit1_(unit1ArrD1);
		LayerBase<Integer> intLayer = new LayerBase<Integer>(intArrD1);
		layer.setIntItem_2(intLayer);
		// intLayer.getD1().base()
		Integer[] testArr1 = intLayer.getD1().getBase();
		assertEquals(testArr1, intArrD1);
		assertSame(testArr1, intArrD1);
		// layer.returnIntItem().getD1()
		Integer[] testArr2 = layer.returnIntItem().getD1().getBase();
		assertEquals(testArr2, intArrD1);
		assertNotSame(testArr2, intArrD1);
		// layer.intItem.getD1()
		Layer.D1<Unit1,Integer> testLayer = layer.intItem.getD1();
		for (int i = 0; i < sz1; i++) {	
			assertEquals(testLayer.get(i), (Integer)i);
		}
		// etc.
		assertNull(intLayer.getD2());
		assertNull(intLayer.getD3());
	}
	
	@Test
	public void testLayer_int_D2() {
		Layer_Unit1_ layer = new Layer_Unit1_(unit1ArrD2);
		LayerBase<Integer> intLayer = new LayerBase<Integer>(intArrD2);
		layer.setIntItem_2(intLayer);
		// intLayer.getD2().base()
		Integer[][] testArr1 = intLayer.getD2().getBase();
		assertEquals(testArr1, intArrD2);
		assertSame(testArr1, intArrD2);
		// layer.returnIntItem().getD2()
		Integer[][] testArr2 = layer.returnIntItem().getD2().getBase();
		assertEquals(testArr2, intArrD2);
		assertNotSame(testArr2, intArrD2);
		// layer.intItem.getD2()
		Layer.D2<Unit1,Integer> testLayer = layer.intItem.getD2();
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				assertEquals(testLayer.get(i,j), (Integer)(i+j));
			}
		}
		// etc.
		assertNull(intLayer.getD1());
		assertNull(intLayer.getD3());
	}
	
	@Test
	public void testLayer_int_D3() {
		Layer_Unit1_ layer = new Layer_Unit1_(unit1ArrD3);
		LayerBase<Integer> intLayer = new LayerBase<Integer>(intArrD3);
		layer.setIntItem_2(intLayer);
		// intLayer.getD3().base()
		Integer[][][] testArr1 = intLayer.getD3().getBase();
		assertEquals(testArr1, intArrD3);
		assertSame(testArr1, intArrD3);
		// layer.returnIntItem().getD3()
		Integer[][][] testArr2 = layer.returnIntItem().getD3().getBase();
		assertEquals(testArr2, intArrD3);
		assertNotSame(testArr2, intArrD3);
		// layer.intItem.getD3()
		Layer.D3<Unit1,Integer> testLayer = layer.intItem.getD3();
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					assertEquals(testLayer.get(i,j,k), (Integer)(i+j+k));
				}
			}
		}
		// etc.
		assertNull(intLayer.getD1());
		assertNull(intLayer.getD2());
	}
  
}

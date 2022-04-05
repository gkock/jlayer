package org.jlayer.util;

import java.util.Iterator;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import basic.units.*;

/**
 * 
 * tests the methods {@code Layer.D1.iterator()}, {@code Layer.D2.iterator()}, {@code Layer.D3.iterator()},
 * and {@code Layer.iterator()} for layers with incompletely defined units.
 * 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Iterator2_Test {
	
	// dimensionality settings
	
	final int dmx = 3;
	final int dmy[] = {3,0,4};
	final int dmz[][] = { {0,2,0} , {} , {0,3,0,0} , {} };
	
	// arrays and layers
	
	Unit1[] array1;
	Unit1[][] array2;
	Unit1[][][] array3;
	
	@BeforeClass
    public void beforeClass() {
		// create array1 = {U, U, U}
		array1 = new Unit1[dmx];
		for (int i = 0; i < dmx; i++) {	
			array1[i] = new Unit1(2*i); 
		}
		
		// create array2 = { {U, U, U}, {}, {U, U, U, U} }
		array2 = new Unit1[dmx][];
		for (int i = 0; i < dmx; i++) {	
			array2[i] = new Unit1[dmy[i]];
			for (int j = 0; j < dmy[i]; j++) {	
				array2[i][j] = new Unit1(2*i+2*j); 
			}
		}
		
		// create array3 = { { {}, {U, U}, {} } ; {} ; { {}, {U, U, U}, {}, {} } }
		array3 = new Unit1[dmx][][];
		for (int i = 0; i < dmx; i++) {	
			array3[i] = new Unit1[dmy[i]][];
			for (int j = 0; j < dmy[i]; j++) {	
				array3[i][j] = new Unit1[dmz[i][j]];
				for (int k = 0; k < dmz[i][j]; k++) {	
					array3[i][j][k] = new Unit1(2*i+2*j+2*k); 
				}
			}
		}
	}
	
	@Test
	public void testLayerD1() {
		Layer_Unit1_.D1 layerD1 = new Layer_Unit1_.D1(array1);
		int counter;
		
		for (int i = 0; i < dmx; i++) {
			// intItem
			layerD1.intItem.set(i, i);
			// noUnitItem
			NoUnit noUnit = new NoUnit(i);
			layerD1.noUnitItem.set(i, noUnit);
			// unit1Item
			Unit1 unit1 = new Unit1(i);
			layerD1.unit1Item.set(i, unit1);
			// unit2Item
			Unit2 unit2 = new Unit2(i);
			layerD1.unit2Item.set(i, unit2);
		}
		
		// layer1.iterator()
		Iterator<Unit1> itUnit1Elem = layerD1.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().noItem; }
		assertEquals(counter, 2*3);
		
		// layer1.intItem.iterator()
		Iterator<Integer> itInt = layerD1.intItem.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 3);
		// layer1.noUnitItem.iterator()
		Iterator<NoUnit> itNoUnit = layerD1.noUnitItem.iterator();
		counter = 0;
		while (itNoUnit.hasNext()){ counter += itNoUnit.next().value; }
		assertEquals(counter, 3);
		// layer1.unit1Item.iterator()
		Iterator<Unit1> itUnit1 = layerD1.unit1Item.iterator();
		counter = 0;
		while (itUnit1.hasNext()){ counter += itUnit1.next().noItem; }
		assertEquals(counter, 3);
		// layer1.unit2Item.iterator()
		Iterator<Unit2> itUnit2 = layerD1.unit2Item.iterator();
		counter = 0;
		while (itUnit2.hasNext()){ counter += itUnit2.next().noItem; }
		assertEquals(counter, 3);
	}
	
	@Test
	public void testLayerD2() {
		Layer_Unit1_.D2 layerD2 = new Layer_Unit1_.D2(array2);
		int counter;
		
		for (int i = 0; i < dmx; i++) {	
			for (int j = 0; j < dmy[i]; j++) {	
				// intItem
				layerD2.intItem.set(i, j, i+j);
				// noUnitItem
				NoUnit noUnit = new NoUnit(i+j);
				layerD2.noUnitItem.set(i, j, noUnit);
				// unit1Item
				Unit1 unit1 = new Unit1(i+j);
				layerD2.unit1Item.set(i, j, unit1);
				// unit2Item
				Unit2 unit2 = new Unit2(i+j);
				layerD2.unit2Item.set(i, j, unit2);
			}
		}
		
		// layer2.iterator()
		Iterator<Unit1> itUnit1Elem = layerD2.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().noItem; }
		assertEquals(counter, 2*17);
		
		// layer2.intItem.iterator()
		Iterator<Integer> itInt = layerD2.intItem.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 17);
		// layer2.noUnitItem.iterator()
		Iterator<NoUnit> itNoUnit = layerD2.noUnitItem.iterator();
		counter = 0;
		while (itNoUnit.hasNext()){ counter += itNoUnit.next().value; }
		assertEquals(counter, 17);
		// layer2.unit1Item.iterator()
		Iterator<Unit1> itUnit1 = layerD2.unit1Item.iterator();
		counter = 0;
		while (itUnit1.hasNext()){ counter += itUnit1.next().noItem; }
		assertEquals(counter, 17);
		// layer2.unit2Item.iterator()
		Iterator<Unit2> itUnit2 = layerD2.unit2Item.iterator();
		counter = 0;
		while (itUnit2.hasNext()){ counter += itUnit2.next().noItem; }
		assertEquals(counter, 17);		
	}
	
	@Test
	public void testLayerD3() {
		Layer_Unit1_.D3 layerD3 = new Layer_Unit1_.D3(array3);
		int counter;
		
		for (int i = 0; i < dmx; i++) {	
			for (int j = 0; j < dmy[i]; j++) {	
				for (int k = 0; k < dmz[i][j]; k++) {	
					// intItem
					layerD3.intItem.set(i, j, k, i+j+k);
					// noUnitItem
					NoUnit noUnit = new NoUnit(i+j+k);
					layerD3.noUnitItem.set(i, j, k, noUnit);
					// unit1Item
					Unit1 unit1 = new Unit1(i+j+k);
					layerD3.unit1Item.set(i, j, k, unit1);
					// unit2Item
					Unit2 unit2 = new Unit2(i+j+k);
					layerD3.unit2Item.set(i, j, k, unit2);
					
				}
			}
		}
		
		// layer3.iterator()
		Iterator<Unit1> itUnit1Elem = layerD3.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().noItem; }
		assertEquals(counter, 2*15);
		
		// layer3.intItem.iterator()
		Iterator<Integer> itInt = layerD3.intItem.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 15);
		// layer3.noUnitItem.iterator()
		Iterator<NoUnit> itNoUnit = layerD3.noUnitItem.iterator();
		counter = 0;
		while (itNoUnit.hasNext()){ counter += itNoUnit.next().value; }
		assertEquals(counter, 15);
		// layer3.unit1Item.iterator()
		Iterator<Unit1> itUnit1 = layerD3.unit1Item.iterator();
		counter = 0;
		while (itUnit1.hasNext()){ counter += itUnit1.next().noItem; }
		assertEquals(counter, 15);
		// layer3.unit2Item.iterator()
		Iterator<Unit2> itUnit2 = layerD3.unit2Item.iterator();
		counter = 0;
		while (itUnit2.hasNext()){ counter += itUnit2.next().noItem; }
		assertEquals(counter, 15);		
	}
  
	@Test
	public void testLayerNN1() {
		Layer_Unit1_ layer1 = new Layer_Unit1_(array1);
		int counter;
		
		for (int i = 0; i < dmx; i++) {
			// intItem
			layer1.intItem.set(i, 2*i);
			// noUnitItem
			NoUnit noUnit = new NoUnit(2*i);
			layer1.noUnitItem.set(i, noUnit);
			// unit1Item
			Unit1 unit1 = new Unit1(2*i);
			layer1.unit1Item.set(i, unit1);
			// unit2Item
			Unit2 unit2 = new Unit2(2*i);
			layer1.unit2Item.set(i, unit2);
		}
		
		// layer1.iterator()
		Iterator<Unit1> itUnit1Elem = layer1.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().noItem; }
		assertEquals(counter, 2*3);
		
		// layer1.intItem.iterator()
		Iterator<Integer> itInt = layer1.intItem.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 2*3);
		// layer1.noUnitItem.iterator()
		Iterator<NoUnit> itNoUnit = layer1.noUnitItem.iterator();
		counter = 0;
		while (itNoUnit.hasNext()){ counter += itNoUnit.next().value; }
		assertEquals(counter, 2*3);
		// layer1.unit1Item.iterator()
		Iterator<Unit1> itUnit1 = layer1.unit1Item.iterator();
		counter = 0;
		while (itUnit1.hasNext()){ counter += itUnit1.next().noItem; }
		assertEquals(counter, 2*3);
		// layer1.unit2Item.iterator()
		Iterator<Unit2> itUnit2 = layer1.unit2Item.iterator();
		counter = 0;
		while (itUnit2.hasNext()){ counter += itUnit2.next().noItem; }
		assertEquals(counter, 2*3);
	}
	
	@Test
	public void testLayerNN2() {
		Layer_Unit1_ layer2 = new Layer_Unit1_(array2);
		int counter;
		
		for (int i = 0; i < dmx; i++) {	
			for (int j = 0; j < dmy[i]; j++) {	
				// intItem
				layer2.intItem.set(i, j, 2*(i+j));
				// noUnitItem
				NoUnit noUnit = new NoUnit(2*(i+j));
				layer2.noUnitItem.set(i, j, noUnit);
				// unit1Item
				Unit1 unit1 = new Unit1(2*(i+j));
				layer2.unit1Item.set(i, j, unit1);
				// unit2Item
				Unit2 unit2 = new Unit2(2*(i+j));
				layer2.unit2Item.set(i, j, unit2);
			}
		}
		
		// layer2.iterator()
		Iterator<Unit1> itUnit1Elem = layer2.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().noItem; }
		assertEquals(counter, 2*17);
		
		// layer2.intItem.iterator()
		Iterator<Integer> itInt = layer2.intItem.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 2*17);
		// layer2.noUnitItem.iterator()
		Iterator<NoUnit> itNoUnit = layer2.noUnitItem.iterator();
		counter = 0;
		while (itNoUnit.hasNext()){ counter += itNoUnit.next().value; }
		assertEquals(counter, 2*17);
		// layer2.unit1Item.iterator()
		Iterator<Unit1> itUnit1 = layer2.unit1Item.iterator();
		counter = 0;
		while (itUnit1.hasNext()){ counter += itUnit1.next().noItem; }
		assertEquals(counter, 2*17);
		// layer2.unit2Item.iterator()
		Iterator<Unit2> itUnit2 = layer2.unit2Item.iterator();
		counter = 0;
		while (itUnit2.hasNext()){ counter += itUnit2.next().noItem; }
		assertEquals(counter, 2*17);		
	}
		
	@Test
	public void testLayerNN3() {
		Layer_Unit1_ layer3 = new Layer_Unit1_(array3);
		int counter;
		
		for (int i = 0; i < dmx; i++) {	
			for (int j = 0; j < dmy[i]; j++) {	
				for (int k = 0; k < dmz[i][j]; k++) {	
					// intItem
					layer3.intItem.set(i, j, k, 2*(i+j+k));
					// noUnitItem
					NoUnit noUnit = new NoUnit(2*(i+j+k));
					layer3.noUnitItem.set(i, j, k, noUnit);
					// unit1Item
					Unit1 unit1 = new Unit1(2*(i+j+k));
					layer3.unit1Item.set(i, j, k, unit1);
					// unit2Item
					Unit2 unit2 = new Unit2(2*(i+j+k));
					layer3.unit2Item.set(i, j, k, unit2);
					
				}
			}
		}
		
		// layer3.iterator()
		Iterator<Unit1> itUnit1Elem = layer3.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().noItem; }
		assertEquals(counter, 2*15);
		
		// layer3.intItem.iterator()
		Iterator<Integer> itInt = layer3.intItem.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 2*15);
		// layer3.noUnitItem.iterator()
		Iterator<NoUnit> itNoUnit = layer3.noUnitItem.iterator();
		counter = 0;
		while (itNoUnit.hasNext()){ counter += itNoUnit.next().value; }
		assertEquals(counter, 2*15);
		// layer3.unit1Item.iterator()
		Iterator<Unit1> itUnit1 = layer3.unit1Item.iterator();
		counter = 0;
		while (itUnit1.hasNext()){ counter += itUnit1.next().noItem; }
		assertEquals(counter, 2*15);
		// layer3.unit2Item.iterator()
		Iterator<Unit2> itUnit2 = layer3.unit2Item.iterator();
		counter = 0;
		while (itUnit2.hasNext()){ counter += itUnit2.next().noItem; }
		assertEquals(counter, 2*15);		
	}
	
	@Test
	public void testFurther1Iterator() {
		Unit1 u = new Unit1(1);
		Layer_Unit1_ xlayer;
		Iterator<Unit1> iter;
		int counter;
		
		// 1a
		Unit1[] xarr1a = {};
		xlayer = new Layer_Unit1_(xarr1a);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 0);
		
		// 1b
		Unit1[] xarr1b = {u};
		xlayer = new Layer_Unit1_(xarr1b);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 1);
		
		// 1c
		Unit1[] xarr1c = new Unit1[3];
		xlayer = new Layer_Unit1_(xarr1c);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 0);
		
		// 1d
		Unit1[] xarr1d = new Unit1[3];
		xlayer = new Layer_Unit1_(xarr1d);
		xarr1d[1] = u;
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 1);
		
		// 1e
		Unit1[] xarr1e = new Unit1[3];
		xlayer = new Layer_Unit1_(xarr1e);
		xarr1e[1] = u;
		xarr1e[2] = u;
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 2);
		
	}
		
	@Test
	public void testFurther2Iterator() {
		Unit1 u = new Unit1(1);
		Layer_Unit1_ xlayer;
		Iterator<Unit1> iter;
		int counter;
		
		// 2a
		Unit1[][] xarr2a = { {} };
		xlayer = new Layer_Unit1_(xarr2a);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 0);
		
		// 2b
		Unit1[][] xarr2b = { {} , {} , {} };
		xlayer = new Layer_Unit1_(xarr2b);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 0);
		
		// 2c
		Unit1[][] xarr2c = { {u} , {} , {} };
		xlayer = new Layer_Unit1_(xarr2c);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 1);
		
		// 2d
		Unit1[][] xarr2d = { {u} , {} , {u} };
		xlayer = new Layer_Unit1_(xarr2d);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 2);
		
		// 2e
		Unit1[][] xarr2e = { {} , {} , {u} };
		xlayer = new Layer_Unit1_(xarr2e);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 1);
		
		// 2f
		Unit1[][] xarr2f = new Unit1[3][3];
		xarr2f[0][1] = u;
		xarr2f[0][2] = u;
		xarr2f[1][0] = u;
		xarr2f[1][2] = u;
		xarr2f[2][0] = u;
		xarr2f[2][1] = u;
		xlayer = new Layer_Unit1_(xarr2f);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 6);
		
		// 2g
		Unit1[][] xarr2g = new Unit1[3][3];
		xarr2g[2][2] = u;
		xlayer = new Layer_Unit1_(xarr2g);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 1);
		
		// 2h
		Unit1[][] xarr2h = new Unit1[3][];
		xarr2h[0] = new Unit1[0];
		xarr2h[1] = new Unit1[0];
		xarr2h[2] = new Unit1[3];
		xarr2h[2][2] = u;
		xlayer = new Layer_Unit1_(xarr2h);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 1);
		
	}
	
	@Test
	public void testFurther3Iterator() {
		Unit1 u = new Unit1(1);
		Layer_Unit1_ xlayer;
		Iterator<Unit1> iter;
		int counter;
		
		// 3a
		Unit1[][][] xarr3a = { { {} } };
		xlayer = new Layer_Unit1_(xarr3a);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 0);
		
		// 3b
		Unit1[][][] xarr3b = { { {},{u},{} } , { {},{},{} } , { {} } };
		xlayer = new Layer_Unit1_(xarr3b);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 1);
		
		// 3c
		Unit1[][][] xarr3c = { { {},{u,u},{} } , { {u,u,u},{},{u,u},{} } };
		xlayer = new Layer_Unit1_(xarr3c);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 7);
		
		// 3d
		Unit1[][][] xarr3d = new Unit1[2][][];
		xarr3d[0] = new Unit1[2][2];
		xarr3d[1] = new Unit1[2][2];
		xarr3d[1][0][1] = u;
		xlayer = new Layer_Unit1_(xarr3d);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 1);
		
		// 3e
		Unit1[][][] xarr3e = new Unit1[3][][];
		xarr3e[0] = new Unit1[2][0];
		xarr3e[1] = new Unit1[2][2];
		xarr3e[2] = new Unit1[0][0];
		xarr3d[1][0][0] = u;
		xarr3d[1][0][1] = u;
		xlayer = new Layer_Unit1_(xarr3d);
		iter = xlayer.iterator();
		counter = 0;
		while (iter.hasNext()){ counter += iter.next().noItem; }
		assertEquals(counter, 2);

	}
  
}

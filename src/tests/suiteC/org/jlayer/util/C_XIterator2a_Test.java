package org.jlayer.util;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import basic.units.*;

import org.jlayer.model.XIterator;

/**
 * 
 * tests the nullary methods {@code Layer.D1.xIterator()}, {@code Layer.D2.xIterator()}, {@code Layer.D3.xIterator()},
 * and {@code Layer.xIterator()} for layers with incompletely defined units.
 * 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_XIterator2a_Test {
	
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
		int xCounter;
		
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
		
		// layer1.xIterator()
		XIterator.D1<Unit1> itUnit1Elem = layerD1.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1Elem.hasNext()){ 
			xCounter += itUnit1Elem.getX1();
			counter += itUnit1Elem.next().noItem; 
		}
		assertEquals(counter, 2*3);
		assertEquals(xCounter, 3);
		
		// layer1.intItem.xIterator()
		XIterator.D1<Integer> itInt = layerD1.intItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itInt.hasNext()){ 
			xCounter += itInt.getX1();
			counter += itInt.next(); 
		}
		assertEquals(counter, 3);
		assertEquals(xCounter, 3);
		// layer1.noUnitItem.xIterator()
		XIterator.D1<NoUnit> itNoUnit = layerD1.noUnitItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itNoUnit.hasNext()){ 
			xCounter += itNoUnit.getX1();
			counter += itNoUnit.next().value; 
		}
		assertEquals(counter, 3);
		assertEquals(xCounter, 3);
		// layer1.unit1Item.xIterator()
		XIterator.D1<Unit1> itUnit1 = layerD1.unit1Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1.hasNext()){ 
			xCounter += itUnit1.getX1();
			counter += itUnit1.next().noItem;
		}
		assertEquals(counter, 3);
		assertEquals(xCounter, 3);
		// layer1.unit2Item.xIterator()
		XIterator.D1<Unit2> itUnit2 = layerD1.unit2Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit2.hasNext()){ 
			xCounter += itUnit2.getX1();
			counter += itUnit2.next().noItem; 
		}
		assertEquals(counter, 3);
		assertEquals(xCounter, 3);
	}
	
	@Test
	public void testLayerD2() {
		Layer_Unit1_.D2 layerD2 = new Layer_Unit1_.D2(array2);
		int counter;
		int xCounter;
		
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
		
		// layer2.xIterator()
		XIterator.D2<Unit1> itUnit1Elem = layerD2.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1Elem.hasNext()){ 
			xCounter += itUnit1Elem.getX1() + itUnit1Elem.getX2();
			counter += itUnit1Elem.next().noItem; 
		}
		assertEquals(counter, 2*17);
		assertEquals(xCounter, 17);
		
		// layer2.intItem.xIterator()
		XIterator.D2<Integer> itInt = layerD2.intItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itInt.hasNext()){ 
			xCounter += itInt.getX1() + itInt.getX2();
			counter += itInt.next(); 
		}
		assertEquals(counter, 17);
		assertEquals(xCounter, 17);
		// layer2.noUnitItem.xIterator()
		XIterator.D2<NoUnit> itNoUnit = layerD2.noUnitItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itNoUnit.hasNext()){ 
			xCounter += itNoUnit.getX1() + itNoUnit.getX2();
			counter += itNoUnit.next().value;
		}
		assertEquals(counter, 17);
		assertEquals(xCounter, 17);
		// layer2.unit1Item.xIterator()
		XIterator.D2<Unit1> itUnit1 = layerD2.unit1Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1.hasNext()){ 
			xCounter += itUnit1.getX1() + itUnit1.getX2();
			counter += itUnit1.next().noItem; 
		}
		assertEquals(counter, 17);
		assertEquals(xCounter, 17);
		// layer2.unit2Item.xIterator()
		XIterator.D2<Unit2> itUnit2 = layerD2.unit2Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit2.hasNext()){
			xCounter += itUnit2.getX1() + itUnit2.getX2();
			counter += itUnit2.next().noItem;
		}
		assertEquals(counter, 17);
		assertEquals(xCounter, 17);
	}
	
	@Test
	public void testLayerD3() {
		Layer_Unit1_.D3 layerD3 = new Layer_Unit1_.D3(array3);
		int counter;
		int xCounter;
		
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
		
		// layer3.xIterator()
		XIterator.D3<Unit1> itUnit1Elem = layerD3.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1Elem.hasNext()){ 
			xCounter += itUnit1Elem.getX1() + itUnit1Elem.getX2() + itUnit1Elem.getX3();
			counter += itUnit1Elem.next().noItem; 
		}
		assertEquals(counter, 2*15);
		assertEquals(xCounter, 15);
		
		// layer3.intItem.xIterator()
		XIterator.D3<Integer> itInt = layerD3.intItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itInt.hasNext()){ 
			xCounter += itInt.getX1() + itInt.getX2() + itInt.getX3();
			counter += itInt.next();
		}
		assertEquals(counter, 15);
		assertEquals(xCounter, 15);
		// layer3.noUnitItem.xIterator()
		XIterator.D3<NoUnit> itNoUnit = layerD3.noUnitItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itNoUnit.hasNext()){ 
			xCounter += itNoUnit.getX1() + itNoUnit.getX2() + itNoUnit.getX3();
			counter += itNoUnit.next().value; 
		}
		assertEquals(counter, 15);
		assertEquals(xCounter, 15);
		// layer3.unit1Item.xIterator()
		XIterator.D3<Unit1> itUnit1 = layerD3.unit1Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1.hasNext()){ 
			xCounter += itUnit1.getX1() + itUnit1.getX2() + itUnit1.getX3();
			counter += itUnit1.next().noItem; 
		}
		assertEquals(counter, 15);
		assertEquals(xCounter, 15);
		// layer3.unit2Item.xIterator()
		XIterator.D3<Unit2> itUnit2 = layerD3.unit2Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit2.hasNext()){ 
			xCounter += itUnit2.getX1() + itUnit2.getX2() + itUnit2.getX3();
			counter += itUnit2.next().noItem; 
		}
		assertEquals(counter, 15);	
		assertEquals(xCounter, 15);
	}
  
	@Test
	public void testLayerNN1() {
		Layer_Unit1_ layer1 = new Layer_Unit1_(array1);
		int counter;
		int xCounter;
		
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
		
		// layer1.xIterator()
		XIterator<Unit1> itUnit1Elem = layer1.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1Elem.hasNext()){ 
			xCounter += itUnit1Elem.getXX()[0];
			counter += itUnit1Elem.next().noItem; 
		}
		assertEquals(counter, 2*3);
		assertEquals(xCounter, 3);
		
		// layer1.intItem.xIterator()
		XIterator<Integer> itInt = layer1.intItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itInt.hasNext()){ 
			xCounter += itInt.getXX()[0];
			counter += itInt.next();
		}
		assertEquals(counter, 2*3);
		assertEquals(xCounter, 3);
		// layer1.noUnitItem.xIterator()
		XIterator<NoUnit> itNoUnit = layer1.noUnitItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itNoUnit.hasNext()){ 
			xCounter += itNoUnit.getXX()[0];
			counter += itNoUnit.next().value; 
		}
		assertEquals(counter, 2*3);
		assertEquals(xCounter, 3);
		// layer1.unit1Item.xIterator()
		XIterator<Unit1> itUnit1 = layer1.unit1Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1.hasNext()){ 
			xCounter += itUnit1.getXX()[0];
			counter += itUnit1.next().noItem;
		}
		assertEquals(counter, 2*3);
		assertEquals(xCounter, 3);
		// layer1.unit2Item.xIterator()
		XIterator<Unit2> itUnit2 = layer1.unit2Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit2.hasNext()){
			xCounter += itUnit2.getXX()[0];
			counter += itUnit2.next().noItem;
		}
		assertEquals(counter, 2*3);
		assertEquals(xCounter, 3);
	}
	
	@Test
	public void testLayerNN2() {
		Layer_Unit1_ layer2 = new Layer_Unit1_(array2);
		int counter;
		int xCounter;
		
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
		
		// layer2.xIterator()
		XIterator<Unit1> itUnit1Elem = layer2.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1Elem.hasNext()){ 
			xCounter += itUnit1Elem.getXX()[0] + itUnit1Elem.getXX()[1];
			counter += itUnit1Elem.next().noItem; 
		}
		assertEquals(counter, 2*17);
		assertEquals(xCounter, 17);
		
		// layer2.intItem.xIterator()
		XIterator<Integer> itInt = layer2.intItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itInt.hasNext()){ 
			xCounter += itInt.getXX()[0] + itInt.getXX()[1];
			counter += itInt.next();
		}
		assertEquals(counter, 2*17);
		assertEquals(xCounter, 17);
		// layer2.noUnitItem.xIterator()
		XIterator<NoUnit> itNoUnit = layer2.noUnitItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itNoUnit.hasNext()){ 
			xCounter += itNoUnit.getXX()[0] + itNoUnit.getXX()[1];
			counter += itNoUnit.next().value; 
		}
		assertEquals(counter, 2*17);
		assertEquals(xCounter, 17);
		// layer2.unit1Item.xIterator()
		XIterator<Unit1> itUnit1 = layer2.unit1Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1.hasNext()){ 
			xCounter += itUnit1.getXX()[0] + itUnit1.getXX()[1];
			counter += itUnit1.next().noItem;
		}
		assertEquals(counter, 2*17);
		assertEquals(xCounter, 17);
		// layer2.unit2Item.xIterator()
		XIterator<Unit2> itUnit2 = layer2.unit2Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit2.hasNext()){ 
			xCounter += itUnit2.getXX()[0] + itUnit2.getXX()[1];
			counter += itUnit2.next().noItem; 
		}
		assertEquals(counter, 2*17);
		assertEquals(xCounter, 17);
	}
		
	@Test
	public void testLayerNN3() {
		Layer_Unit1_ layer3 = new Layer_Unit1_(array3);
		int counter;
		int xCounter;
		
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
		
		// layer3.xIterator()
		XIterator<Unit1> itUnit1Elem = layer3.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1Elem.hasNext()){ 
			xCounter += itUnit1Elem.getXX()[0] + itUnit1Elem.getXX()[1] + itUnit1Elem.getXX()[2];
			counter += itUnit1Elem.next().noItem; 
		}
		assertEquals(counter, 2*15);
		assertEquals(xCounter, 15);
		
		// layer3.intItem.xIterator()
		XIterator<Integer> itInt = layer3.intItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itInt.hasNext()){ 
			xCounter += itInt.getXX()[0] + itInt.getXX()[1] + itInt.getXX()[2];
			counter += itInt.next();
		}
		assertEquals(counter, 2*15);
		assertEquals(xCounter, 15);
		// layer3.noUnitItem.xIterator()
		XIterator<NoUnit> itNoUnit = layer3.noUnitItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itNoUnit.hasNext()){ 
			xCounter += itNoUnit.getXX()[0] + itNoUnit.getXX()[1] + itNoUnit.getXX()[2];
			counter += itNoUnit.next().value; 
		}
		assertEquals(counter, 2*15);
		assertEquals(xCounter, 15);
		// layer3.unit1Item.xIterator()
		XIterator<Unit1> itUnit1 = layer3.unit1Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1.hasNext()){
			xCounter += itUnit1.getXX()[0] + itUnit1.getXX()[1] + itUnit1.getXX()[2];
			counter += itUnit1.next().noItem;
		}
		assertEquals(counter, 2*15);
		assertEquals(xCounter, 15);
		// layer3.unit2Item.xIterator()
		XIterator<Unit2> itUnit2 = layer3.unit2Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit2.hasNext()){ 
			xCounter += itUnit2.getXX()[0] + itUnit2.getXX()[1] + itUnit2.getXX()[2];
			counter += itUnit2.next().noItem; 
		}
		assertEquals(counter, 2*15);	
		assertEquals(xCounter, 15);
	}
	
	@Test
	public void testFurther1Iterator() {
		Unit1 u = new Unit1(1);
		Layer_Unit1_ xlayer;
		XIterator<Unit1> iter;
		int counter;
		int xCounter;
		
		// 1a
		Unit1[] xarr1a = {};
		xlayer = new Layer_Unit1_(xarr1a);
		iter = xlayer.xIterator();
		while (iter.hasNext()){ fail(); }
		
		// 1b
		Unit1[] xarr1b = {u};
		xlayer = new Layer_Unit1_(xarr1b);
		iter = xlayer.xIterator();
		counter = 0;
		xCounter = 4711;
		while (iter.hasNext()){ 
			xCounter = iter.getXX()[0];
			counter += iter.next().noItem; 
		}
		assertEquals(counter, 1);
		assertEquals(xCounter, 0);
		
		// 1c
		Unit1[] xarr1c = new Unit1[3];
		xlayer = new Layer_Unit1_(xarr1c);
		iter = xlayer.xIterator();
		while (iter.hasNext()){ fail(); }
		
		// 1d
		Unit1[] xarr1d = new Unit1[3];
		xlayer = new Layer_Unit1_(xarr1d);
		xarr1d[1] = u;
		iter = xlayer.xIterator();
		counter = 0;
		xCounter = 0;
		while (iter.hasNext()){ 
			xCounter += iter.getXX()[0];
			counter += iter.next().noItem; 
		}
		assertEquals(counter, 1);
		assertEquals(xCounter, 1);
		
		// 1e
		Unit1[] xarr1e = new Unit1[3];
		xlayer = new Layer_Unit1_(xarr1e);
		xarr1e[1] = u;
		xarr1e[2] = u;
		iter = xlayer.xIterator();
		counter = 0;
		xCounter = 0;
		while (iter.hasNext()){ 
			xCounter += iter.getXX()[0];
			counter += iter.next().noItem; 
		}
		assertEquals(counter, 2);
		assertEquals(xCounter, 3);
		
	}
		
	@Test
	public void testFurther2Iterator() {
		Unit1 u = new Unit1(1);
		Layer_Unit1_ xlayer;
		XIterator<Unit1> iter;
		int counter;
		int xCounter;
		
		// 2a
		Unit1[][] xarr2a = { {} };
		xlayer = new Layer_Unit1_(xarr2a);
		iter = xlayer.xIterator();
		while (iter.hasNext()){ fail();	}
		
		// 2b
		Unit1[][] xarr2b = { {} , {} , {} };
		xlayer = new Layer_Unit1_(xarr2b);
		iter = xlayer.xIterator();
		while (iter.hasNext()){ fail();	}
		
		// 2c
		Unit1[][] xarr2c = { {u} , {} , {} };
		xlayer = new Layer_Unit1_(xarr2c);
		iter = xlayer.xIterator();
		counter = 0;
		xCounter = 4711;
		while (iter.hasNext()){ 
			xCounter = iter.getXX()[0] + iter.getXX()[1];
			counter += iter.next().noItem; 
		}
		assertEquals(counter, 1);
		assertEquals(xCounter, 0);
		
		// 2d
		Unit1[][] xarr2d = { {u} , {} , {u} };
		xlayer = new Layer_Unit1_(xarr2d);
		iter = xlayer.xIterator();
		counter = 0;
		xCounter = 0;
		while (iter.hasNext()){ 
			xCounter += iter.getXX()[0] + iter.getXX()[1];
			counter += iter.next().noItem; 
		}
		assertEquals(counter, 2);
		assertEquals(xCounter, 2);
		
		// 2e
		Unit1[][] xarr2e = { {} , {} , {u} };
		xlayer = new Layer_Unit1_(xarr2e);
		iter = xlayer.xIterator();
		counter = 0;
		xCounter = 0;
		while (iter.hasNext()){ 
			xCounter += iter.getXX()[0] + iter.getXX()[1];
			counter += iter.next().noItem; 
		}
		assertEquals(counter, 1);
		assertEquals(xCounter, 2);
		
		// 2f
		Unit1[][] xarr2f = new Unit1[3][3];
		xarr2f[0][1] = u;
		xarr2f[0][2] = u;
		xarr2f[1][0] = u;
		xarr2f[1][2] = u;
		xarr2f[2][0] = u;
		xarr2f[2][1] = u;
		xlayer = new Layer_Unit1_(xarr2f);
		iter = xlayer.xIterator();
		counter = 0;
		xCounter = 0;
		while (iter.hasNext()){ 
			xCounter += iter.getXX()[0] + iter.getXX()[1];
			counter += iter.next().noItem; 
		}
		assertEquals(counter, 6);
		assertEquals(xCounter, 12);
		
		// 2g
		Unit1[][] xarr2g = new Unit1[3][3];
		xarr2g[2][2] = u;
		xlayer = new Layer_Unit1_(xarr2g);
		iter = xlayer.xIterator();
		counter = 0;
		xCounter = 0;
		while (iter.hasNext()){ 
			xCounter += iter.getXX()[0] + iter.getXX()[1];
			counter += iter.next().noItem; 
		}
		assertEquals(counter, 1);
		assertEquals(xCounter, 4);
		
		// 2h
		Unit1[][] xarr2h = new Unit1[3][];
		xarr2h[0] = new Unit1[0];
		xarr2h[1] = new Unit1[0];
		xarr2h[2] = new Unit1[3];
		xarr2h[2][2] = u;
		xlayer = new Layer_Unit1_(xarr2h);
		iter = xlayer.xIterator();
		counter = 0;
		xCounter = 0;
		while (iter.hasNext()){ 
			xCounter += iter.getXX()[0] + iter.getXX()[1];
			counter += iter.next().noItem; 
		}
		assertEquals(counter, 1);
		assertEquals(xCounter, 4);
		
	}
	
	@Test
	public void testFurther3Iterator() {
		Unit1 u = new Unit1(1);
		Layer_Unit1_ xlayer;
		XIterator<Unit1> iter;
		int counter;
		int xCounter;
		
		// 3a
		Unit1[][][] xarr3a = { { {} } };
		xlayer = new Layer_Unit1_(xarr3a);
		iter = xlayer.xIterator();
		while (iter.hasNext()){ fail();	}
		
		// 3b
		Unit1[][][] xarr3b = { { {},{u},{} } , { {},{},{} } , { {} } };
		xlayer = new Layer_Unit1_(xarr3b);
		iter = xlayer.xIterator();
		counter = 0;
		xCounter = 0;
		while (iter.hasNext()){ 
			xCounter += iter.getXX()[0] + iter.getXX()[1] + iter.getXX()[2];
			counter += iter.next().noItem; 
		}
		assertEquals(counter, 1);
		assertEquals(xCounter, 1);
		
		// 3c
		Unit1[][][] xarr3c = { { {},{u,u},{} } , { {u,u,u},{},{u,u},{} } };
		xlayer = new Layer_Unit1_(xarr3c);
		iter = xlayer.xIterator();
		counter = 0;
		xCounter = 0;
		while (iter.hasNext()){ 
			xCounter += iter.getXX()[0] + iter.getXX()[1] + iter.getXX()[2];
			counter += iter.next().noItem; 
		}
		assertEquals(counter, 7);
		assertEquals(xCounter, 16);
		
		// 3d
		Unit1[][][] xarr3d = new Unit1[2][][];
		xarr3d[0] = new Unit1[2][2];
		xarr3d[1] = new Unit1[2][2];
		xarr3d[1][0][1] = u;
		xlayer = new Layer_Unit1_(xarr3d);
		iter = xlayer.xIterator();
		counter = 0;
		xCounter = 0;
		while (iter.hasNext()){ 
			xCounter += iter.getXX()[0] + iter.getXX()[1] + iter.getXX()[2];
			counter += iter.next().noItem; 
		}
		assertEquals(counter, 1);
		assertEquals(xCounter, 2);
		
		// 3e
		Unit1[][][] xarr3e = new Unit1[3][][];
		xarr3e[0] = new Unit1[2][0];
		xarr3e[1] = new Unit1[2][2];
		xarr3e[2] = new Unit1[0][0];
		xarr3e[1][0][0] = u;
		xarr3e[1][0][1] = u;
		xlayer = new Layer_Unit1_(xarr3e);
		iter = xlayer.xIterator();
		counter = 0;
		xCounter = 0;
		while (iter.hasNext()){ 
			xCounter += iter.getXX()[0] + iter.getXX()[1] + iter.getXX()[2];
			counter += iter.next().noItem; 
		}
		assertEquals(counter, 2);
		assertEquals(xCounter, 3);

	}
  
}

package org.jlayer.util;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;
import basic.units.*;

import org.jlayer.model.XIterator;

/**
 * 
 * tests the binary methods {@code Layer.xIterator(int, int)} for layers with incompletely defined units.
 * 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_XIterator2b_Test {
	
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
		XIterator<Unit1> itUnit1Elem1 = layer1.xIterator(0,1);
		XIterator<Unit1> itUnit1Elem2 = layer1.xIterator(1,3);
		counter = 0;
		xCounter = 0;
		while (itUnit1Elem1.hasNext()){ 
			xCounter += itUnit1Elem1.getXX()[0];
			counter += itUnit1Elem1.next().noItem; 
		}
		while (itUnit1Elem2.hasNext()){ 
			xCounter += itUnit1Elem2.getXX()[0];
			counter += itUnit1Elem2.next().noItem; 
		}
		assertEquals(counter, 2*3);
		assertEquals(xCounter, 3);
		
		// layer1.intItem.xIterator()
		XIterator<Integer> itInt1 = layer1.intItem.xIterator(0,2);
		XIterator<Integer> itInt2 = layer1.intItem.xIterator(2,3);
		counter = 0;
		xCounter = 0;
		while (itInt1.hasNext()){ 
			xCounter += itInt1.getXX()[0];
			counter += itInt1.next();
		}
		while (itInt2.hasNext()){ 
			xCounter += itInt2.getXX()[0];
			counter += itInt2.next();
		}
		assertEquals(counter, 2*3);
		assertEquals(xCounter, 3);
		// layer1.noUnitItem.xIterator()
		XIterator<NoUnit> itNoUnita = layer1.noUnitItem.xIterator(0,1);
		XIterator<NoUnit> itNoUnitb = layer1.noUnitItem.xIterator(1,3);
		counter = 0;
		xCounter = 0;
		while (itNoUnita.hasNext()){ 
			xCounter += itNoUnita.getXX()[0];
			counter += itNoUnita.next().value; 
		}
		while (itNoUnitb.hasNext()){ 
			xCounter += itNoUnitb.getXX()[0];
			counter += itNoUnitb.next().value; 
		}
		assertEquals(counter, 2*3);
		assertEquals(xCounter, 3);
		// layer1.unit1Item.xIterator()
		XIterator<Unit1> itUnit1a = layer1.unit1Item.xIterator(0,2);
		XIterator<Unit1> itUnit1b = layer1.unit1Item.xIterator(2,3);
		counter = 0;
		xCounter = 0;
		while (itUnit1a.hasNext()){ 
			xCounter += itUnit1a.getXX()[0];
			counter += itUnit1a.next().noItem;
		}
		while (itUnit1b.hasNext()){ 
			xCounter += itUnit1b.getXX()[0];
			counter += itUnit1b.next().noItem;
		}
		assertEquals(counter, 2*3);
		assertEquals(xCounter, 3);
		// layer1.unit2Item.xIterator()
		XIterator<Unit2> itUnit2a = layer1.unit2Item.xIterator(0,1);
		XIterator<Unit2> itUnit2b = layer1.unit2Item.xIterator(1,3);
		counter = 0;
		xCounter = 0;
		while (itUnit2a.hasNext()){
			xCounter += itUnit2a.getXX()[0];
			counter += itUnit2a.next().noItem;
		}
		while (itUnit2b.hasNext()){
			xCounter += itUnit2b.getXX()[0];
			counter += itUnit2b.next().noItem;
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
		XIterator<Unit1> itUnit1Elem1 = layer2.xIterator(0,1);
		XIterator<Unit1> itUnit1Elem2 = layer2.xIterator(1,3);
		counter = 0;
		xCounter = 0;
		while (itUnit1Elem1.hasNext()){ 
			xCounter += itUnit1Elem1.getXX()[0] + itUnit1Elem1.getXX()[1];
			counter += itUnit1Elem1.next().noItem; 
		}
		while (itUnit1Elem2.hasNext()){ 
			xCounter += itUnit1Elem2.getXX()[0] + itUnit1Elem2.getXX()[1];
			counter += itUnit1Elem2.next().noItem; 
		}
		assertEquals(counter, 2*17);
		assertEquals(xCounter, 17);
		
		// layer2.intItem.xIterator()
		XIterator<Integer> itInt1 = layer2.intItem.xIterator(1,3);
		XIterator<Integer> itInt2 = layer2.intItem.xIterator(0,1);
		counter = 0;
		xCounter = 0;
		while (itInt1.hasNext()){ 
			xCounter += itInt1.getXX()[0] + itInt1.getXX()[1];
			counter += itInt1.next();
		}
		while (itInt2.hasNext()){ 
			xCounter += itInt2.getXX()[0] + itInt2.getXX()[1];
			counter += itInt2.next();
		}
		assertEquals(counter, 2*17);
		assertEquals(xCounter, 17);
		// layer2.noUnitItem.xIterator()
		XIterator<NoUnit> itNoUnit1 = layer2.noUnitItem.xIterator(2,3);
		XIterator<NoUnit> itNoUnit2 = layer2.noUnitItem.xIterator(1,2);
		XIterator<NoUnit> itNoUnit3 = layer2.noUnitItem.xIterator(0,1);
		counter = 0;
		xCounter = 0;
		while (itNoUnit1.hasNext()){ 
			xCounter += itNoUnit1.getXX()[0] + itNoUnit1.getXX()[1];
			counter += itNoUnit1.next().value; 
		}
		while (itNoUnit2.hasNext()){ 
			xCounter += itNoUnit2.getXX()[0] + itNoUnit2.getXX()[1];
			counter += itNoUnit2.next().value; 
		}
		while (itNoUnit3.hasNext()){ 
			xCounter += itNoUnit3.getXX()[0] + itNoUnit3.getXX()[1];
			counter += itNoUnit3.next().value; 
		}
		assertEquals(counter, 2*17);
		assertEquals(xCounter, 17);
		// layer2.unit1Item.xIterator()
		XIterator<Unit1> itUnit1a = layer2.unit1Item.xIterator(0,2);
		XIterator<Unit1> itUnit1b = layer2.unit1Item.xIterator(2,3);
		counter = 0;
		xCounter = 0;
		while (itUnit1a.hasNext()){ 
			xCounter += itUnit1a.getXX()[0] + itUnit1a.getXX()[1];
			counter += itUnit1a.next().noItem;
		}
		while (itUnit1b.hasNext()){ 
			xCounter += itUnit1b.getXX()[0] + itUnit1b.getXX()[1];
			counter += itUnit1b.next().noItem;
		}
		assertEquals(counter, 2*17);
		assertEquals(xCounter, 17);
		// layer2.unit2Item.xIterator()
		XIterator<Unit2> itUnit2 = layer2.unit2Item.xIterator(0,3);
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
		XIterator<Unit1> itUnit1Ele1 = layer3.xIterator(0,2);
		XIterator<Unit1> itUnit1Ele2 = layer3.xIterator(2,3);
		counter = 0;
		xCounter = 0;
		while (itUnit1Ele1.hasNext()){ 
			xCounter += itUnit1Ele1.getXX()[0] + itUnit1Ele1.getXX()[1] + itUnit1Ele1.getXX()[2];
			counter += itUnit1Ele1.next().noItem; 
		}
		while (itUnit1Ele2.hasNext()){ 
			xCounter += itUnit1Ele2.getXX()[0] + itUnit1Ele2.getXX()[1] + itUnit1Ele2.getXX()[2];
			counter += itUnit1Ele2.next().noItem; 
		}
		assertEquals(counter, 2*15);
		assertEquals(xCounter, 15);
		
		// layer3.intItem.xIterator()
		XIterator<Integer> itInt1 = layer3.intItem.xIterator(0,1);
		XIterator<Integer> itInt2 = layer3.intItem.xIterator(1,3);
		counter = 0;
		xCounter = 0;
		while (itInt1.hasNext()){ 
			xCounter += itInt1.getXX()[0] + itInt1.getXX()[1] + itInt1.getXX()[2];
			counter += itInt1.next();
		}
		while (itInt2.hasNext()){ 
			xCounter += itInt2.getXX()[0] + itInt2.getXX()[1] + itInt2.getXX()[2];
			counter += itInt2.next();
		}
		assertEquals(counter, 2*15);
		assertEquals(xCounter, 15);
		
		// layer3.noUnitItem.xIterator()
		XIterator<NoUnit> itNoUnita = layer3.noUnitItem.xIterator(0,2);
		XIterator<NoUnit> itNoUnitb = layer3.noUnitItem.xIterator(2,2);
		XIterator<NoUnit> itNoUnitc = layer3.noUnitItem.xIterator(2,3);
		counter = 0;
		xCounter = 0;
		while (itNoUnita.hasNext()){ 
			xCounter += itNoUnita.getXX()[0] + itNoUnita.getXX()[1] + itNoUnita.getXX()[2];
			counter += itNoUnita.next().value; 
		}
		while (itNoUnitb.hasNext()){ 
			xCounter += itNoUnitb.getXX()[0] + itNoUnitb.getXX()[1] + itNoUnitb.getXX()[2];
			counter += itNoUnitb.next().value; 
		}
		while (itNoUnitc.hasNext()){ 
			xCounter += itNoUnitc.getXX()[0] + itNoUnitc.getXX()[1] + itNoUnitc.getXX()[2];
			counter += itNoUnitc.next().value; 
		}
		assertEquals(counter, 2*15);
		assertEquals(xCounter, 15);
		
		// layer3.unit1Item.xIterator()
		XIterator<Unit1> itUnit1a = layer3.unit1Item.xIterator(0,2);
		XIterator<Unit1> itUnit1b = layer3.unit1Item.xIterator(2,3);
		counter = 0;
		xCounter = 0;
		while (itUnit1a.hasNext()){
			xCounter += itUnit1a.getXX()[0] + itUnit1a.getXX()[1] + itUnit1a.getXX()[2];
			counter += itUnit1a.next().noItem;
		}
		while (itUnit1b.hasNext()){
			xCounter += itUnit1b.getXX()[0] + itUnit1b.getXX()[1] + itUnit1b.getXX()[2];
			counter += itUnit1b.next().noItem;
		}
		assertEquals(counter, 2*15);
		assertEquals(xCounter, 15);
		
		// layer3.unit2Item.xIterator()
		XIterator<Unit2> itUnit2a = layer3.unit2Item.xIterator(0,2);
		XIterator<Unit2> itUnit2b = layer3.unit2Item.xIterator(2,3);
		counter = 0;
		xCounter = 0;
		while (itUnit2a.hasNext()){ 
			xCounter += itUnit2a.getXX()[0] + itUnit2a.getXX()[1] + itUnit2a.getXX()[2];
			counter += itUnit2a.next().noItem; 
		}
		while (itUnit2b.hasNext()){ 
			xCounter += itUnit2b.getXX()[0] + itUnit2b.getXX()[1] + itUnit2b.getXX()[2];
			counter += itUnit2b.next().noItem; 
		}
		assertEquals(counter, 2*15);	
		assertEquals(xCounter, 15);
	}
	
	@Test
	public void testFurther1Iterator() {
		Unit1 u = new Unit1(1);
		Layer_Unit1_ xlayer;
		XIterator<Unit1> iter1;
		XIterator<Unit1> iter2;
		int counter;
		int xCounter;
		
		// 1a
		Unit1[] xarr1a = {};
		xlayer = new Layer_Unit1_(xarr1a);
		iter1 = xlayer.xIterator(0,0);
		while (iter1.hasNext()){ fail(); }
		
		// 1b
		Unit1[] xarr1b = {u};
		xlayer = new Layer_Unit1_(xarr1b);
		iter1 = xlayer.xIterator(0,1);
		counter = 0;
		xCounter = 4711;
		while (iter1.hasNext()){ 
			xCounter = iter1.getXX()[0];
			counter += iter1.next().noItem; 
		}
		assertEquals(counter, 1);
		assertEquals(xCounter, 0);
		
		// 1c
		Unit1[] xarr1c = new Unit1[3];
		xlayer = new Layer_Unit1_(xarr1c);
		iter1 = xlayer.xIterator(0,3);
		while (iter1.hasNext()){ fail(); }
		
		// 1d
		Unit1[] xarr1d = new Unit1[3];
		xlayer = new Layer_Unit1_(xarr1d);
		xarr1d[1] = u;
		iter1 = xlayer.xIterator(0,1);
		iter2 = xlayer.xIterator(1,3);
		counter = 0;
		xCounter = 0;
		while (iter1.hasNext()){ 
			xCounter += iter1.getXX()[0];
			counter += iter1.next().noItem; 
		}
		while (iter2.hasNext()){ 
			xCounter += iter2.getXX()[0];
			counter += iter2.next().noItem; 
		}
		assertEquals(counter, 1);
		assertEquals(xCounter, 1);
		
		// 1e
		Unit1[] xarr1e = new Unit1[3];
		xlayer = new Layer_Unit1_(xarr1e);
		xarr1e[1] = u;
		xarr1e[2] = u;
		iter1 = xlayer.xIterator(0,1);
		iter2 = xlayer.xIterator(1,3);
		counter = 0;
		xCounter = 0;
		while (iter1.hasNext()){ 
			xCounter += iter1.getXX()[0];
			counter += iter1.next().noItem; 
		}
		while (iter2.hasNext()){ 
			xCounter += iter2.getXX()[0];
			counter += iter2.next().noItem; 
		}
		assertEquals(counter, 2);
		assertEquals(xCounter, 3);
		
	}
		
	@Test
	public void testFurther2Iterator() {
		Unit1 u = new Unit1(1);
		Layer_Unit1_ xlayer;
		XIterator<Unit1> iter1;
		XIterator<Unit1> iter2;
		int counter;
		int xCounter;
		
		// 2a
		Unit1[][] xarr2a = { {} };
		xlayer = new Layer_Unit1_(xarr2a);
		iter1 = xlayer.xIterator(0,1);
		while (iter1.hasNext()){ fail();	}
		
		// 2b
		Unit1[][] xarr2b = { {} , {} , {} };
		xlayer = new Layer_Unit1_(xarr2b);
		iter1 = xlayer.xIterator(0,2);
		iter2 = xlayer.xIterator(2,3);
		while (iter1.hasNext()){ fail();	}
		while (iter2.hasNext()){ fail();	}
		
		// 2c
		Unit1[][] xarr2c = { {u} , {} , {} };
		xlayer = new Layer_Unit1_(xarr2c);
		iter1 = xlayer.xIterator(0,1);
		iter2 = xlayer.xIterator(1,3);
		counter = 0;
		xCounter = 4711;
		while (iter1.hasNext()){ 
			xCounter = iter1.getXX()[0] + iter1.getXX()[1];
			counter += iter1.next().noItem; 
		}
		while (iter2.hasNext()){ 
			xCounter = iter2.getXX()[0] + iter2.getXX()[1];
			counter += iter2.next().noItem; 
		}
		assertEquals(counter, 1);
		assertEquals(xCounter, 0);
		
		// 2d
		Unit1[][] xarr2d = { {u} , {} , {u} };
		xlayer = new Layer_Unit1_(xarr2d);
		iter1 = xlayer.xIterator(0,1);
		iter2 = xlayer.xIterator(1,3);
		counter = 0;
		xCounter = 0;
		while (iter1.hasNext()){ 
			xCounter += iter1.getXX()[0] + iter1.getXX()[1];
			counter += iter1.next().noItem; 
		}
		while (iter2.hasNext()){ 
			xCounter = iter2.getXX()[0] + iter2.getXX()[1];
			counter += iter2.next().noItem; 
		}
		assertEquals(counter, 2);
		assertEquals(xCounter, 2);
		
		// 2e
		Unit1[][] xarr2e = { {} , {} , {u} };
		xlayer = new Layer_Unit1_(xarr2e);
		iter1 = xlayer.xIterator(0,1);
		iter2 = xlayer.xIterator(1,3);
		counter = 0;
		xCounter = 0;
		while (iter1.hasNext()){ 
			xCounter += iter1.getXX()[0] + iter1.getXX()[1];
			counter += iter1.next().noItem; 
		}
		while (iter2.hasNext()){ 
			xCounter = iter2.getXX()[0] + iter2.getXX()[1];
			counter += iter2.next().noItem; 
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
		iter1 = xlayer.xIterator(0,2);
		iter2 = xlayer.xIterator(2,3);
		counter = 0;
		xCounter = 0;
		while (iter1.hasNext()){ 
			xCounter += iter1.getXX()[0] + iter1.getXX()[1];
			counter += iter1.next().noItem; 
		}
		while (iter2.hasNext()){ 
			xCounter += iter2.getXX()[0] + iter2.getXX()[1];
			counter += iter2.next().noItem; 
		}
		assertEquals(counter, 6);
		assertEquals(xCounter, 12);
		
		// 2g
		Unit1[][] xarr2g = new Unit1[3][3];
		xarr2g[2][2] = u;
		xlayer = new Layer_Unit1_(xarr2g);
		iter1 = xlayer.xIterator(0,2);
		iter2 = xlayer.xIterator(2,3);
		counter = 0;
		xCounter = 0;
		while (iter1.hasNext()){ 
			xCounter += iter1.getXX()[0] + iter1.getXX()[1];
			counter += iter1.next().noItem; 
		}
		while (iter2.hasNext()){ 
			xCounter = iter2.getXX()[0] + iter2.getXX()[1];
			counter += iter2.next().noItem; 
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
		iter1 = xlayer.xIterator(0,1);
		iter2 = xlayer.xIterator(1,3);
		counter = 0;
		xCounter = 0;
		while (iter1.hasNext()){ 
			xCounter += iter1.getXX()[0] + iter1.getXX()[1];
			counter += iter1.next().noItem; 
		}
		while (iter2.hasNext()){ 
			xCounter = iter2.getXX()[0] + iter2.getXX()[1];
			counter += iter2.next().noItem; 
		}
		assertEquals(counter, 1);
		assertEquals(xCounter, 4);
		
	}
	
	@Test
	public void testFurther3Iterator() {
		Unit1 u = new Unit1(1);
		Layer_Unit1_ xlayer;
		XIterator<Unit1> iter1;
		XIterator<Unit1> iter2;
		int counter;
		int xCounter;
		
		// 3a
		Unit1[][][] xarr3a = { { {} } };
		xlayer = new Layer_Unit1_(xarr3a);
		iter1 = xlayer.xIterator(0,1);
		while (iter1.hasNext()){ fail();	}
		
		// 3b
		Unit1[][][] xarr3b = { { {},{u},{} } , { {},{},{} } , { {} } };
		xlayer = new Layer_Unit1_(xarr3b);
		iter1 = xlayer.xIterator(0,1);
		iter2 = xlayer.xIterator(1,3);
		counter = 0;
		xCounter = 0;
		while (iter1.hasNext()){ 
			xCounter += iter1.getXX()[0] + iter1.getXX()[1] + iter1.getXX()[2];
			counter += iter1.next().noItem; 
		}
		while (iter2.hasNext()){ 
			xCounter += iter2.getXX()[0] + iter2.getXX()[1] + iter2.getXX()[2];
			counter += iter2.next().noItem; 
		}
		assertEquals(counter, 1);
		assertEquals(xCounter, 1);
		
		// 3c
		Unit1[][][] xarr3c = { { {},{u,u},{} } , { {u,u,u},{},{u,u},{} } };
		xlayer = new Layer_Unit1_(xarr3c);
		iter1 = xlayer.xIterator(0,1);
		iter2 = xlayer.xIterator(1,2);
		counter = 0;
		xCounter = 0;
		while (iter1.hasNext()){ 
			xCounter += iter1.getXX()[0] + iter1.getXX()[1] + iter1.getXX()[2];
			counter += iter1.next().noItem; 
		}
		while (iter2.hasNext()){ 
			xCounter += iter2.getXX()[0] + iter2.getXX()[1] + iter2.getXX()[2];
			counter += iter2.next().noItem; 
		}
		assertEquals(counter, 7);
		assertEquals(xCounter, 16);
		
		// 3d
		Unit1[][][] xarr3d = new Unit1[2][][];
		xarr3d[0] = new Unit1[2][2];
		xarr3d[1] = new Unit1[2][2];
		xarr3d[1][0][1] = u;
		xlayer = new Layer_Unit1_(xarr3d);
		iter1 = xlayer.xIterator(0,1);
		iter2 = xlayer.xIterator(1,2);
		counter = 0;
		xCounter = 0;
		while (iter1.hasNext()){ 
			xCounter += iter1.getXX()[0] + iter1.getXX()[1] + iter1.getXX()[2];
			counter += iter1.next().noItem; 
		}
		while (iter2.hasNext()){ 
			xCounter += iter2.getXX()[0] + iter2.getXX()[1] + iter2.getXX()[2];
			counter += iter2.next().noItem; 
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
		iter1 = xlayer.xIterator(0,1);
		iter2 = xlayer.xIterator(1,3);
		counter = 0;
		xCounter = 0;
		while (iter1.hasNext()){ 
			xCounter += iter1.getXX()[0] + iter1.getXX()[1] + iter1.getXX()[2];
			counter += iter1.next().noItem; 
		}
		while (iter2.hasNext()){ 
			xCounter += iter2.getXX()[0] + iter2.getXX()[1] + iter2.getXX()[2];
			counter += iter2.next().noItem; 
		}
		assertEquals(counter, 2);
		assertEquals(xCounter, 3);

	}
  
}

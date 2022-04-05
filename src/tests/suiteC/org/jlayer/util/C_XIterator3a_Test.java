package org.jlayer.util;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import basic.units.*;
import org.jlayer.model.XIterator;

/**
 * 
 * tests the nullary methods {@code Layer.xIterator()} for layers with completely defined units, but incompletely defined items.
 * 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_XIterator3a_Test {
	
	final int sz1 = 10;
	final int sz2 = 3;
	final int sz3 = 2;
	
	Unit1[] array1;
	Unit1[][] array2;
	Unit1[][][] array3;
	
	@BeforeClass
    public void beforeClass() {
		array1 = new Unit1[sz1];
		for (int i = 0; i < sz1; i++) {	
			array1[i] = new Unit1(4711); 
		}
		array2 = new Unit1[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array2[i][j] = new Unit1(4711); 
			}
		}
		array3 = new Unit1[sz3][sz3][sz3];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					array3[i][j][k] = new Unit1(4711); 
				}
			}
		}
	}
  
	@Test
	public void testLayerNN1() {
		Layer_Unit1_ layer1 = new Layer_Unit1_(array1);
		int counter;
		int xCounter;
		
		// initialization
		int[] ix = new int[1];
		for (int i = 0; i < sz1; i++) {
			ix[0] = i;
			// intItem
			if (i%2==0) layer1.intItem.set(ix, i);
			// noUnitItem
			NoUnit noUnit = new NoUnit(i);
			if (i%2==0) layer1.noUnitItem.set(ix, noUnit);
			// unit1Item
			Unit1 unit1 = new Unit1(i);
			if (i%2==0) layer1.unit1Item.set(ix, unit1);
			// unit2Item
			Unit2 unit2 = new Unit2(i);
			if (i%2==0) layer1.unit2Item.set(ix, unit2);
		}
		
		// layer1.xIterator()
		XIterator<Unit1> itUnit1Elem = layer1.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1Elem.hasNext()){ 
			xCounter += itUnit1Elem.getXX()[0];
			counter += itUnit1Elem.next().noItem; 
		}
		assertEquals(counter, 47110);
		assertEquals(xCounter, 45);
		
		// layer1.intItem.xIterator()
		XIterator<Integer> itInt = layer1.intItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itInt.hasNext()){ 
			xCounter += itInt.getXX()[0];
			counter += itInt.next(); 
		}
		assertEquals(counter, 20);
		assertEquals(xCounter, 45);
		
		// layer1.noUnitItem.xIterator()
		XIterator<NoUnit> itNoUnit = layer1.noUnitItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itNoUnit.hasNext()){ 
			xCounter += itNoUnit.getXX()[0];
			NoUnit noUnit = itNoUnit.next();
			if (noUnit != null) {
				counter += noUnit.value; 
			} else {
				fail();
			}
		}
		assertEquals(counter, 20);
		assertEquals(xCounter, 20);
		
		// layer1.unit1Item.xIterator()
		XIterator<Unit1> itUnit1 = layer1.unit1Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1.hasNext()){ 
			xCounter += itUnit1.getXX()[0];
			Unit1 unit1 = itUnit1.next();
			if (unit1 != null) {
				counter += unit1.noItem; 
			} else {
				fail();
			}
		}
		assertEquals(counter, 20);
		assertEquals(xCounter, 20);
		
		// layer1.unit2Item.xIterator()
		XIterator<Unit2> itUnit2 = layer1.unit2Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit2.hasNext()){ 
			xCounter += itUnit2.getXX()[0];
			Unit2 unit2 = itUnit2.next();
			if (unit2 != null) {
				counter += unit2.noItem; 
			} else {
				fail();
			}
		}
		assertEquals(counter, 20);
		assertEquals(xCounter, 20);
	}
	
	@Test
	public void testLayerNN2() {
		Layer_Unit1_ layer2 = new Layer_Unit1_(array2);
		int counter;
		int xCounter;
		
		int[] ix = new int[2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				ix[0] = i; ix[1] = j;
				// intItem
				if ((i+j)%2==0) layer2.intItem.set(ix, i+j);
				// noUnitItem
				NoUnit noUnit = new NoUnit(i+j);
				if ((i+j)%2==0) layer2.noUnitItem.set(ix, noUnit);
				// unit1Item
				Unit1 unit1 = new Unit1(i+j);
				if ((i+j)%2==0) layer2.unit1Item.set(ix, unit1);
				// unit2Item
				Unit2 unit2 = new Unit2(i+j);
				if ((i+j)%2==0) layer2.unit2Item.set(ix, unit2);
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
		assertEquals(counter, 42399);
		assertEquals(xCounter, 18);
		
		// layer2.intItem.xIterator()
		XIterator<Integer> itInt = layer2.intItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itInt.hasNext()){ 
			xCounter += itInt.getXX()[0] + itInt.getXX()[1];
			counter += itInt.next(); 
		}
		assertEquals(counter, 10);
		assertEquals(xCounter, 18);
		
		// layer2.noUnitItem.xIterator()
		XIterator<NoUnit> itNoUnit = layer2.noUnitItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itNoUnit.hasNext()){ 
			xCounter += itNoUnit.getXX()[0] + itNoUnit.getXX()[1];
			NoUnit noUnit = itNoUnit.next();
			if (noUnit != null) {
				counter += noUnit.value; 
			} else {
				fail();
			}
		}
		assertEquals(counter, 10);
		assertEquals(xCounter, 10);
		
		// layer2.unit1Item.xIterator()
		XIterator<Unit1> itUnit1 = layer2.unit1Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1.hasNext()){ 
			xCounter += itUnit1.getXX()[0] + itUnit1.getXX()[1];
			Unit1 unit1 = itUnit1.next();
			if (unit1 != null) {
				counter += unit1.noItem; 
			} else {
				fail();
			}
		}
		assertEquals(counter, 10);
		assertEquals(xCounter, 10);
		
		// layer2.unit2Item.xIterator()
		XIterator<Unit2> itUnit2 = layer2.unit2Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit2.hasNext()){ 
			xCounter += itUnit2.getXX()[0] + itUnit2.getXX()[1];
			Unit2 unit2 = itUnit2.next();
			if (unit2 != null) {
				counter += unit2.noItem; 
			} else {
				fail();
			}
		}
		assertEquals(counter, 10);
		assertEquals(xCounter, 10);
	}
	
	@Test
	public void testLayerNN3() {
		Layer_Unit1_ layer3 = new Layer_Unit1_(array3);
		int counter;
		int xCounter;
		
		int[] ix = new int[3];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					ix[0] = i; ix[1] = j; ix[2] = k;
					// intItem
					if ((i+j+k)%2==0) layer3.intItem.set(ix, i+j+k);
					// noUnitItem
					NoUnit noUnit = new NoUnit(i+j+k);
					if ((i+j+k)%2==0) layer3.noUnitItem.set(ix, noUnit);
					// unit1Item
					Unit1 unit1 = new Unit1(i+j+k);
					if ((i+j+k)%2==0) layer3.unit1Item.set(ix, unit1);
					// unit2Item
					Unit2 unit2 = new Unit2(i+j+k);
					if ((i+j+k)%2==0) layer3.unit2Item.set(ix, unit2);
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
		assertEquals(counter, 37688);
		assertEquals(xCounter, 12);
		
		// layer3.intItem.xIterator()
		XIterator<Integer> itInt = layer3.intItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itInt.hasNext()){ 
			xCounter += itInt.getXX()[0] + itInt.getXX()[1] + itInt.getXX()[2];
			counter += itInt.next();
		}
		assertEquals(counter, 6);
		assertEquals(xCounter, 12);
		
		// layer3.noUnitItem.xIterator()
		XIterator<NoUnit> itNoUnit = layer3.noUnitItem.xIterator();
		counter = 0;
		xCounter = 0;
		while (itNoUnit.hasNext()){ 
			xCounter += itNoUnit.getXX()[0] + itNoUnit.getXX()[1] + itNoUnit.getXX()[2];
			NoUnit noUnit = itNoUnit.next();
			if (noUnit != null) {
				counter += noUnit.value; 
			} else {
				fail();
			}
		}
		assertEquals(counter, 6);
		assertEquals(xCounter, 6);
		
		// layer3.unit1Item.xIterator()
		XIterator<Unit1> itUnit1 = layer3.unit1Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit1.hasNext()){ 
			xCounter += itUnit1.getXX()[0] + itUnit1.getXX()[1] + itUnit1.getXX()[2];
			Unit1 unit1 = itUnit1.next();
			if (unit1 != null) {
				counter += unit1.noItem; 
			} else {
				fail();
			}
		}
		assertEquals(counter, 6);
		assertEquals(xCounter, 6);
		
		// layer3.unit2Item.xIterator()
		XIterator<Unit2> itUnit2 = layer3.unit2Item.xIterator();
		counter = 0;
		xCounter = 0;
		while (itUnit2.hasNext()){ 
			xCounter += itUnit2.getXX()[0] + itUnit2.getXX()[1] + itUnit2.getXX()[2];
			Unit2 unit2 = itUnit2.next();
			if (unit2 != null) {
				counter += unit2.noItem; 
			} else {
				fail();
			}
		}
		assertEquals(counter, 6);
		assertEquals(xCounter, 6);
	}
  
}

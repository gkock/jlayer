package org.jlayer.util;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import basic.units.*;
import org.jlayer.model.XIterator;

/**
 * 
 * tests the binary methods {@code Layer.xIterator(int, int)} for layers with completely defined units, but incompletely defined items.
 * 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_XIterator3b_Test {
	
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
		array2 = new Unit1[sz1][sz2];
		for (int i = 0; i < sz1; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array2[i][j] = new Unit1(4711); 
			}
		}
		array3 = new Unit1[sz1][sz2][sz3];
		for (int i = 0; i < sz1; i++) {	
			for (int j = 0; j < sz2; j++) {	
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
		XIterator<Unit1> itUnit1Elem1 = layer1.xIterator(0,3);
		XIterator<Unit1> itUnit1Elem2 = layer1.xIterator(3,7);
		XIterator<Unit1> itUnit1Elem3 = layer1.xIterator(7,10);
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
		while (itUnit1Elem3.hasNext()){ 
			xCounter += itUnit1Elem3.getXX()[0];
			counter += itUnit1Elem3.next().noItem; 
		}
		assertEquals(counter, 47110);
		assertEquals(xCounter, 45);
		
		// layer1.intItem.xIterator()
		XIterator<Integer> itInt1 = layer1.intItem.xIterator(0,7);
		XIterator<Integer> itInt2 = layer1.intItem.xIterator(7,10);
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
		assertEquals(counter, 20);
		assertEquals(xCounter, 45);
		
		// layer1.noUnitItem.xIterator()
		XIterator<NoUnit> itNoUnit = layer1.noUnitItem.xIterator(0,10);
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
		XIterator<Unit1> itUnit1a = layer1.unit1Item.xIterator(0,3);
		XIterator<Unit1> itUnit1b = layer1.unit1Item.xIterator(3,4);
		XIterator<Unit1> itUnit1c = layer1.unit1Item.xIterator(4,10);
		counter = 0;
		xCounter = 0;
		while (itUnit1a.hasNext()){ 
			xCounter += itUnit1a.getXX()[0];
			Unit1 unit1 = itUnit1a.next();
			if (unit1 != null) {
				counter += unit1.noItem; 
			} else {
				fail();
			}
		}
		while (itUnit1b.hasNext()){ 
			xCounter += itUnit1b.getXX()[0];
			Unit1 unit1 = itUnit1b.next();
			if (unit1 != null) {
				counter += unit1.noItem; 
			} else {
				fail();
			}
		}
		while (itUnit1c.hasNext()){ 
			xCounter += itUnit1c.getXX()[0];
			Unit1 unit1 = itUnit1c.next();
			if (unit1 != null) {
				counter += unit1.noItem; 
			} else {
				fail(); // error
			}
		}
		assertEquals(counter, 20);
		assertEquals(xCounter, 20);
		
		// layer1.unit2Item.xIterator()
		XIterator<Unit2> itUnit2a = layer1.unit2Item.xIterator(0,2);
		XIterator<Unit2> itUnit2b = layer1.unit2Item.xIterator(2,2);
		XIterator<Unit2> itUnit2c = layer1.unit2Item.xIterator(2,10);
		counter = 0;
		xCounter = 0;
		while (itUnit2a.hasNext()){ 
			xCounter += itUnit2a.getXX()[0];
			Unit2 unit2 = itUnit2a.next();
			if (unit2 != null) {
				counter += unit2.noItem; 
			} else {
				fail();
			}
		}
		while (itUnit2b.hasNext()){ 
			xCounter += itUnit2b.getXX()[0];
			Unit2 unit2 = itUnit2b.next();
			if (unit2 != null) {
				counter += unit2.noItem; 
			} else {
				fail();
			}
		}
		while (itUnit2c.hasNext()){ 
			xCounter += itUnit2c.getXX()[0];
			Unit2 unit2 = itUnit2c.next();
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
		for (int i = 0; i < sz1; i++) {	
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
		XIterator<Unit1> itUnit1Elem1 = layer2.xIterator(0,9);
		XIterator<Unit1> itUnit1Elem2 = layer2.xIterator(9,10);
		counter = 0;
		xCounter = 0;
		while (itUnit1Elem1.hasNext()){
			xCounter += itUnit1Elem1.getXX()[0] + itUnit1Elem1.getXX()[1];
			counter += itUnit1Elem1.next().noItem; 
		}
		System.out.println(" ");
		while (itUnit1Elem2.hasNext()){
			xCounter += itUnit1Elem2.getXX()[0] + itUnit1Elem2.getXX()[1];
			counter += itUnit1Elem2.next().noItem; 
		}
		assertEquals(counter, 141330);
		assertEquals(xCounter, 165);
		
		// layer2.intItem.xIterator()
		XIterator<Integer> itInt1 = layer2.intItem.xIterator(0,4);
		XIterator<Integer> itInt2 = layer2.intItem.xIterator(4,4);
		XIterator<Integer> itInt3 = layer2.intItem.xIterator(4,10);
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
		while (itInt3.hasNext()){ 
			xCounter += itInt3.getXX()[0] + itInt3.getXX()[1];
			counter += itInt3.next(); 
		}
		assertEquals(counter, 80);
		assertEquals(xCounter, 165);
		
		// layer2.noUnitItem.xIterator()
		XIterator<NoUnit> itNoUnit1 = layer2.noUnitItem.xIterator(0,3);
		XIterator<NoUnit> itNoUnit2 = layer2.noUnitItem.xIterator(3,6);
		XIterator<NoUnit> itNoUnit3 = layer2.noUnitItem.xIterator(6,10);
		counter = 0;
		xCounter = 0;
		while (itNoUnit1.hasNext()){ 
			xCounter += itNoUnit1.getXX()[0] + itNoUnit1.getXX()[1];
			NoUnit noUnit = itNoUnit1.next();
			if (noUnit != null) {
				counter += noUnit.value; 
			} else {
				fail();
			}
		}
		while (itNoUnit2.hasNext()){ 
			xCounter += itNoUnit2.getXX()[0] + itNoUnit2.getXX()[1];
			NoUnit noUnit = itNoUnit2.next();
			if (noUnit != null) {
				counter += noUnit.value; 
			} else {
				fail();
			}
		}
		while (itNoUnit3.hasNext()){ 
			xCounter += itNoUnit3.getXX()[0] + itNoUnit3.getXX()[1];
			NoUnit noUnit = itNoUnit3.next();
			if (noUnit != null) {
				counter += noUnit.value; 
			} else {
				fail();
			}
		}
		assertEquals(counter, 80);
		assertEquals(xCounter, 80);
		
		// layer2.unit1Item.xIterator()
		XIterator<Unit1> itUnit1a = layer2.unit1Item.xIterator(0,5);
		XIterator<Unit1> itUnit1b = layer2.unit1Item.xIterator(5,10);
		counter = 0;
		xCounter = 0;
		while (itUnit1a.hasNext()){ 
			xCounter += itUnit1a.getXX()[0] + itUnit1a.getXX()[1];
			Unit1 unit1 = itUnit1a.next();
			if (unit1 != null) {
				counter += unit1.noItem; 
			} else {
				fail();
			}
		}
		while (itUnit1b.hasNext()){ 
			xCounter += itUnit1b.getXX()[0] + itUnit1b.getXX()[1];
			Unit1 unit1 = itUnit1b.next();
			if (unit1 != null) {
				counter += unit1.noItem; 
			} else {
				fail();
			}
		}
		assertEquals(counter, 80);
		assertEquals(xCounter, 80);
		
		// layer2.unit2Item.xIterator()
		XIterator<Unit2> itUnit2a = layer2.unit2Item.xIterator(0,0);
		XIterator<Unit2> itUnit2b = layer2.unit2Item.xIterator(0,3);
		XIterator<Unit2> itUnit2c = layer2.unit2Item.xIterator(3,10);
		counter = 0;
		xCounter = 0;
		while (itUnit2a.hasNext()){ 
			xCounter += itUnit2a.getXX()[0] + itUnit2a.getXX()[1];
			Unit2 unit2 = itUnit2a.next();
			if (unit2 != null) {
				counter += unit2.noItem; 
			} else {
				fail();
			}
		}
		while (itUnit2b.hasNext()){ 
			xCounter += itUnit2b.getXX()[0] + itUnit2b.getXX()[1];
			Unit2 unit2 = itUnit2b.next();
			if (unit2 != null) {
				counter += unit2.noItem; 
			} else {
				fail();
			}
		}
		while (itUnit2c.hasNext()){ 
			xCounter += itUnit2c.getXX()[0] + itUnit2c.getXX()[1];
			Unit2 unit2 = itUnit2c.next();
			if (unit2 != null) {
				counter += unit2.noItem; 
			} else {
				fail();
			}
		}
		assertEquals(counter, 80);
		assertEquals(xCounter, 80);
	}
	
	@Test
	public void testLayerNN3() {
		Layer_Unit1_ layer3 = new Layer_Unit1_(array3);
		int counter;
		int xCounter;
		
		int[] ix = new int[3];
		for (int i = 0; i < sz1; i++) {	
			for (int j = 0; j < sz2; j++) {	
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
		XIterator<Unit1> itUnit1Elem1 = layer3.xIterator(0,9);
		XIterator<Unit1> itUnit1Elem2 = layer3.xIterator(9,10);
		counter = 0;
		xCounter = 0;
		while (itUnit1Elem1.hasNext()){
			xCounter += itUnit1Elem1.getXX()[0] + itUnit1Elem1.getXX()[1] + itUnit1Elem1.getXX()[2];
			counter += itUnit1Elem1.next().noItem; 
		}
		System.out.println(" ");
		while (itUnit1Elem2.hasNext()){
			xCounter += itUnit1Elem2.getXX()[0] + itUnit1Elem2.getXX()[1] + itUnit1Elem2.getXX()[2];
			counter += itUnit1Elem2.next().noItem; 
		}
		assertEquals(counter, 282660);
		assertEquals(xCounter, 360);
		
		// layer3.intItem.xIterator()
		XIterator<Integer> itInt1 = layer3.intItem.xIterator(0,4);
		XIterator<Integer> itInt2 = layer3.intItem.xIterator(4,4);
		XIterator<Integer> itInt3 = layer3.intItem.xIterator(4,10);
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
		while (itInt3.hasNext()){ 
			xCounter += itInt3.getXX()[0] + itInt3.getXX()[1] + itInt3.getXX()[2];
			counter += itInt3.next(); 
		}
		assertEquals(counter, 180);
		assertEquals(xCounter, 360);
		
		// layer3.noUnitItem.xIterator()
		XIterator<NoUnit> itNoUnit1 = layer3.noUnitItem.xIterator(0,3);
		XIterator<NoUnit> itNoUnit2 = layer3.noUnitItem.xIterator(3,6);
		XIterator<NoUnit> itNoUnit3 = layer3.noUnitItem.xIterator(6,10);
		counter = 0;
		xCounter = 0;
		while (itNoUnit1.hasNext()){ 
			xCounter += itNoUnit1.getXX()[0] + itNoUnit1.getXX()[1] + itNoUnit1.getXX()[2];
			NoUnit noUnit = itNoUnit1.next();
			if (noUnit != null) {
				counter += noUnit.value; 
			} else {
				fail();
			}
		}
		while (itNoUnit2.hasNext()){ 
			xCounter += itNoUnit2.getXX()[0] + itNoUnit2.getXX()[1] + itNoUnit2.getXX()[2];
			NoUnit noUnit = itNoUnit2.next();
			if (noUnit != null) {
				counter += noUnit.value; 
			} else {
				fail();
			}
		}
		while (itNoUnit3.hasNext()){ 
			xCounter += itNoUnit3.getXX()[0] + itNoUnit3.getXX()[1] + itNoUnit3.getXX()[2];
			NoUnit noUnit = itNoUnit3.next();
			if (noUnit != null) {
				counter += noUnit.value; 
			} else {
				fail();
			}
		}
		assertEquals(counter, 180);
		assertEquals(xCounter, 180);
		
		// layer3.unit1Item.xIterator()
		XIterator<Unit1> itUnit1a = layer3.unit1Item.xIterator(0,5);
		XIterator<Unit1> itUnit1b = layer3.unit1Item.xIterator(5,10);
		counter = 0;
		xCounter = 0;
		while (itUnit1a.hasNext()){ 
			xCounter += itUnit1a.getXX()[0] + itUnit1a.getXX()[1] + itUnit1a.getXX()[2];
			Unit1 unit1 = itUnit1a.next();
			if (unit1 != null) {
				counter += unit1.noItem; 
			} else {
				fail();
			}
		}
		while (itUnit1b.hasNext()){ 
			xCounter += itUnit1b.getXX()[0] + itUnit1b.getXX()[1] + itUnit1b.getXX()[2];
			Unit1 unit1 = itUnit1b.next();
			if (unit1 != null) {
				counter += unit1.noItem; 
			} else {
				fail();
			}
		}
		assertEquals(counter, 180);
		assertEquals(xCounter, 180);
		
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
		assertEquals(counter, 180);
		assertEquals(xCounter, 180);
	}
  
}

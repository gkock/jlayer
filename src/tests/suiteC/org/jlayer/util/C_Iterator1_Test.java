package org.jlayer.util;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import java.util.Iterator;
import basic.units.*;

/**
 * 
 * tests the methods {@code Layer.D1.iterator()}, {@code Layer.D2.iterator()}, {@code Layer.D3.iterator()},
 * and {@code Layer.iterator()} for layers with completely defined units.
 * 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Iterator1_Test {
	
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
	public void testLayerD1() {
		Layer_Unit1_.D1 layerD1 = new Layer_Unit1_.D1(array1);
		int counter;
		
		// initialization
		for (int i = 0; i < sz1; i++) {
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
		
		// layerD1.iterator()
		Iterator<Unit1> itUnit1Elem = layerD1.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().intItem; }
		assertEquals(counter, 45);
		// layerD1.intItem.iterator()
		Iterator<Integer> itInt = layerD1.intItem.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 45);
		// layerD1.noUnitItem.iterator()
		Iterator<NoUnit> itNoUnit = layerD1.noUnitItem.iterator();
		counter = 0;
		while (itNoUnit.hasNext()){ counter += itNoUnit.next().value; }
		assertEquals(counter, 45);
		// layer1.unit1Item.iterator()
		Iterator<Unit1> itUnit1 = layerD1.unit1Item.iterator();
		counter = 0;
		while (itUnit1.hasNext()){ counter += itUnit1.next().noItem; }
		assertEquals(counter, 45);
		// layer1.unit2Item.iterator()
		Iterator<Unit2> itUnit2 = layerD1.unit2Item.iterator();
		counter = 0;
		while (itUnit2.hasNext()){ counter += itUnit2.next().noItem; }
		assertEquals(counter, 45);
	}
	
	@Test
	public void testLayerD2() {
		Layer_Unit1_.D2 layerD2 = new Layer_Unit1_.D2(array2);
		int counter;
		
		// initialization
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
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
		
		// layerD2.iterator()
		Iterator<Unit1> itUnit1Elem = layerD2.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().intItem; }
		assertEquals(counter, 18);
		// layerD1.intItem.iterator()
		Iterator<Integer> itInt = layerD2.intItem.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 18);
		// layerD1.noUnitItem.iterator()
		Iterator<NoUnit> itNoUnit = layerD2.noUnitItem.iterator();
		counter = 0;
		while (itNoUnit.hasNext()){ counter += itNoUnit.next().value; }
		assertEquals(counter, 18);
		// layer1.unit1Item.iterator()
		Iterator<Unit1> itUnit1 = layerD2.unit1Item.iterator();
		counter = 0;
		while (itUnit1.hasNext()){ counter += itUnit1.next().noItem; }
		assertEquals(counter, 18);
		// layer1.unit2Item.iterator()
		Iterator<Unit2> itUnit2 = layerD2.unit2Item.iterator();
		counter = 0;
		while (itUnit2.hasNext()){ counter += itUnit2.next().noItem; }
		assertEquals(counter, 18);
	}
	
	@Test
	public void testLayerD3() {
		Layer_Unit1_.D3 layerD3 = new Layer_Unit1_.D3(array3);
		int counter;
		
		// initialization
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
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
		
		// layerD3.iterator()
		Iterator<Unit1> itUnit1Elem = layerD3.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().intItem; }
		assertEquals(counter, 12);
		// layerD1.intItem.iterator()
		Iterator<Integer> itInt = layerD3.intItem.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 12);
		// layerD1.noUnitItem.iterator()
		Iterator<NoUnit> itNoUnit = layerD3.noUnitItem.iterator();
		counter = 0;
		while (itNoUnit.hasNext()){ counter += itNoUnit.next().value; }
		assertEquals(counter, 12);
		// layer1.unit1Item.iterator()
		Iterator<Unit1> itUnit1 = layerD3.unit1Item.iterator();
		counter = 0;
		while (itUnit1.hasNext()){ counter += itUnit1.next().noItem; }
		assertEquals(counter, 12);
		// layer1.unit2Item.iterator()
		Iterator<Unit2> itUnit2 = layerD3.unit2Item.iterator();
		counter = 0;
		while (itUnit2.hasNext()){ counter += itUnit2.next().noItem; }
		assertEquals(counter, 12);
	}
  
	@Test
	public void testLayerNN1() {
		Layer_Unit1_ layer1 = new Layer_Unit1_(array1);
		int counter;
		
		// initialization
		int[] ix = new int[1];
		for (int i = 0; i < sz1; i++) {
			ix[0] = i;
			// intItem
			layer1.intItem.set(ix, 2*i);
			// noUnitItem
			NoUnit noUnit = new NoUnit(2*i);
			layer1.noUnitItem.set(ix, noUnit);
			// unit1Item
			Unit1 unit1 = new Unit1(2*i);
			layer1.unit1Item.set(ix, unit1);
			// unit2Item
			Unit2 unit2 = new Unit2(2*i);
			layer1.unit2Item.set(ix, unit2);
		}
		
		// layer1.iterator()
		Iterator<Unit1> itUnit1Elem = layer1.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().intItem; }
		assertEquals(counter, 2*45);
		
		// layer1.intItem.iterator()
		Iterator<Integer> itInt = layer1.intItem.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 2*45);
		// layer1.noUnitItem.iterator()
		Iterator<NoUnit> itNoUnit = layer1.noUnitItem.iterator();
		counter = 0;
		while (itNoUnit.hasNext()){ counter += itNoUnit.next().value; }
		assertEquals(counter, 2*45);
		// layer1.unit1Item.iterator()
		Iterator<Unit1> itUnit1 = layer1.unit1Item.iterator();
		counter = 0;
		while (itUnit1.hasNext()){ counter += itUnit1.next().noItem; }
		assertEquals(counter, 2*45);
		// layer1.unit2Item.iterator()
		Iterator<Unit2> itUnit2 = layer1.unit2Item.iterator();
		counter = 0;
		while (itUnit2.hasNext()){ counter += itUnit2.next().noItem; }
		assertEquals(counter, 2*45);
	}
	
	@Test
	public void testLayerNN2() {
		Layer_Unit1_ layer2 = new Layer_Unit1_(array2);
		int counter;
		
		int[] ix = new int[2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				ix[0] = i; ix[1] = j;
				// intItem
				layer2.intItem.set(ix, 2*(i+j));
				// noUnitItem
				NoUnit noUnit = new NoUnit(2*(i+j));
				layer2.noUnitItem.set(ix, noUnit);
				// unit1Item
				Unit1 unit1 = new Unit1(2*(i+j));
				layer2.unit1Item.set(ix, unit1);
				// unit2Item
				Unit2 unit2 = new Unit2(2*(i+j));
				layer2.unit2Item.set(ix, unit2);
			}
		}

		// layer2.iterator()
		Iterator<Unit1> itUnit1Elem = layer2.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().intItem; }
		assertEquals(counter, 2*18);
		
		// layer2.intItem.iterator()
		Iterator<Integer> itInt = layer2.intItem.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 2*18);
		// layer2.noUnitItem.iterator()
		Iterator<NoUnit> itNoUnit = layer2.noUnitItem.iterator();
		counter = 0;
		while (itNoUnit.hasNext()){ counter += itNoUnit.next().value; }
		assertEquals(counter, 2*18);
		// layer2.unit1Item.iterator()
		Iterator<Unit1> itUnit1 = layer2.unit1Item.iterator();
		counter = 0;
		while (itUnit1.hasNext()){ counter += itUnit1.next().noItem; }
		assertEquals(counter, 2*18);
		// layer2.unit2Item.iterator()
		Iterator<Unit2> itUnit2 = layer2.unit2Item.iterator();
		counter = 0;
		while (itUnit2.hasNext()){ counter += itUnit2.next().noItem; }
		assertEquals(counter, 2*18);
	}
	
	@Test
	public void testLayerNN3() {
		Layer_Unit1_ layer3 = new Layer_Unit1_(array3);
		int counter;
		
		int[] ix = new int[3];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					ix[0] = i; ix[1] = j; ix[2] = k;
					// intItem
					layer3.intItem.set(ix, 2*(i+j+k));
					// noUnitItem
					NoUnit noUnit = new NoUnit(2*(i+j+k));
					layer3.noUnitItem.set(ix, noUnit);
					// unit1Item
					Unit1 unit1 = new Unit1(2*(i+j+k));
					layer3.unit1Item.set(ix, unit1);
					// unit2Item
					Unit2 unit2 = new Unit2(2*(i+j+k));
					layer3.unit2Item.set(ix, unit2);
				}
			}
		}
		
		// layer3.iterator()
		Iterator<Unit1> itUnit1Elem = layer3.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().intItem; }
		assertEquals(counter, 2*12);
		
		// layer3.intItem.iterator()
		Iterator<Integer> itInt = layer3.intItem.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 2*12);
		// layer3.noUnitItem.iterator()
		Iterator<NoUnit> itNoUnit = layer3.noUnitItem.iterator();
		counter = 0;
		while (itNoUnit.hasNext()){ counter += itNoUnit.next().value; }
		assertEquals(counter, 2*12);
		// layer3.unit1Item.iterator()
		Iterator<Unit1> itUnit1 = layer3.unit1Item.iterator();
		counter = 0;
		while (itUnit1.hasNext()){ counter += itUnit1.next().noItem; }
		assertEquals(counter, 2*12);
		// layer3.unit2Item.iterator()
		Iterator<Unit2> itUnit2 = layer3.unit2Item.iterator();
		counter = 0;
		while (itUnit2.hasNext()){ counter += itUnit2.next().noItem; }
		assertEquals(counter, 2*12);
	}
  
}

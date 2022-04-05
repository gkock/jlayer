package org.jlayer.util;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

import basic.units.*;

/**
 * tests layers with a "regular" shape - i.e. all elements are defined
 * specifically methods {@code set()} and {@code get()}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_SetGet1_Test {
	
	final int sz1 = 10;
	final int sz2 = 3;
	final int sz3 = 2;
	
	Unit1[] array1;
	Unit1[][] array2;
	Unit1[][][] array3;
	Layer_Unit1_ layer1, layer2, layer3;
	
	@BeforeClass
    public void beforeClass() {
		array1 = new Unit1[sz1];
		for (int i = 0; i < sz1; i++) {	
			array1[i] = new Unit1(); 
		}
		array2 = new Unit1[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array2[i][j] = new Unit1(); 
			}
		}
		array3 = new Unit1[sz3][sz3][sz3];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					array3[i][j][k] = new Unit1(); 
				}
			}
		}
		layer1 = new Layer_Unit1_(array1);
		layer2 = new Layer_Unit1_(array2);
		layer3 = new Layer_Unit1_(array3);
	}
	
	@Test
	public void  testBasics() {
		assertNotNull(layer1);
		assertNotNull(layer2);
		assertNotNull(layer3);
		assertEquals(layer1.dims(), 1);
		assertEquals(layer2.dims(), 2);
		assertEquals(layer3.dims(), 3);
		assertEquals(layer1.length(), sz1);
		assertEquals(layer2.length(), sz2);
		for (int i = 0; i < layer2.length(); i++) {
			assertEquals(layer2.length(i), sz2);
		}
		assertEquals(layer3.length(), sz3);
		for (int i = 0; i < layer3.length(); i++) {
			assertEquals(layer3.length(i), sz3);
			for (int j = 0; j < layer3.length(i); j++) {
				assertEquals(layer3.length(i,j), sz3);
			}
		}
	}
  
	@Test
	public void testLayer1() {
		int[] ix = new int[1];
		int counter = 0;
		for (int i = 0; i < sz1; i++) {
			ix[0] = i;
			// intItem
			layer1.intItem.set(ix, i);
			// noUnitItem
			NoUnit noUnit = new NoUnit(i);
			layer1.noUnitItem.set(ix, noUnit);
			// unit1Item
			Unit1 unit1 = new Unit1(i);
			layer1.unit1Item.set(ix, unit1);
			// unit2Item
			Unit2 unit2 = new Unit2(i);
			layer1.unit2Item.set(ix, unit2);
		}
		for (int i = 0; i < sz1; i++) {
			ix[0] = i;
			counter += layer1.intItem.get(ix);
			// intItem
			assertEquals(layer1.get(ix).intItem, (int)layer1.intItem.get(ix));
			assertEquals(i, (int)layer1.intItem.get(ix));
			// noUnitItem
			assertEquals(layer1.get(ix).noUnitItem, layer1.noUnitItem.get(ix));
			assertEquals(layer1.get(ix).noUnitItem.value, layer1.noUnitItem.get(ix).value);
			assertEquals(i, layer1.noUnitItem.get(ix).value);
			// unit1Item
			assertEquals(layer1.get(ix).unit1Item, layer1.unit1Item.get(ix));
			assertEquals(layer1.get(ix).unit1Item.noItem, layer1.unit1Item.get(ix).noItem);
			assertEquals(i, layer1.unit1Item.get(ix).noItem);
			// unit2Item
			assertEquals(layer1.get(ix).unit2Item, layer1.unit2Item.get(ix));
			assertEquals(layer1.get(ix).unit2Item.noItem, layer1.unit2Item.get(ix).noItem);
			assertEquals(i, layer1.unit2Item.get(ix).noItem);
		}
		assertEquals(counter, 45);
	}
	
	@Test
	public void testLayer2() {
		int[] ix = new int[2];
		int counter = 0;
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				ix[0] = i; ix[1] = j;
				// intItem
				layer2.intItem.set(ix, i+j);
				// noUnitItem
				NoUnit noUnit = new NoUnit(i+j);
				layer2.noUnitItem.set(ix, noUnit);
				// unit1Item
				Unit1 unit1 = new Unit1(i+j);
				layer2.unit1Item.set(ix, unit1);
				// unit2Item
				Unit2 unit2 = new Unit2(i+j);
				layer2.unit2Item.set(ix, unit2);
			}
		}
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				ix[0] = i; ix[1] = j;
				counter += layer2.intItem.get(ix);
				// intItem
				assertEquals(layer2.get(ix).intItem, (int)layer2.intItem.get(ix));
				assertEquals(i+j, (int)layer2.intItem.get(ix));
				// noUnitItem
				assertEquals(layer2.get(ix).noUnitItem, layer2.noUnitItem.get(ix));
				assertEquals(layer2.get(ix).noUnitItem.value, layer2.noUnitItem.get(ix).value);
				assertEquals(i+j, layer2.noUnitItem.get(ix).value);
				// unit1Item
				assertEquals(layer2.get(ix).unit1Item, layer2.unit1Item.get(ix));
				assertEquals(layer2.get(ix).unit1Item.noItem, layer2.unit1Item.get(ix).noItem);
				assertEquals(i+j, layer2.unit1Item.get(ix).noItem);
				// unit2Item
				assertEquals(layer2.get(ix).unit2Item, layer2.unit2Item.get(ix));
				assertEquals(layer2.get(ix).unit2Item.noItem, layer2.unit2Item.get(ix).noItem);
				assertEquals(i+j, layer2.unit2Item.get(ix).noItem);
			}
		}
		assertEquals(counter, 18);
	}
	
	@Test
	public void testLayer3() {
		int[] ix = new int[3];
		int counter = 0;
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					ix[0] = i; ix[1] = j; ix[2] = k;
					// intItem
					layer3.intItem.set(ix, i+j+k);
					// noUnitItem
					NoUnit noUnit = new NoUnit(i+j+k);
					layer3.noUnitItem.set(ix, noUnit);
					// unit1Item
					Unit1 unit1 = new Unit1(i+j+k);
					layer3.unit1Item.set(ix, unit1);
					// unit2Item
					Unit2 unit2 = new Unit2(i+j+k);
					layer3.unit2Item.set(ix, unit2);
				}
			}
		}
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					ix[0] = i; ix[1] = j; ix[2] = k;
					counter += layer3.intItem.get(ix);
					// intItem
					assertEquals(layer3.get(ix).intItem, (int)layer3.intItem.get(ix));
					assertEquals(i+j+k, (int)layer3.intItem.get(ix));
					// noUnitItem
					assertEquals(layer3.get(ix).noUnitItem, layer3.noUnitItem.get(ix));
					assertEquals(layer3.get(ix).noUnitItem.value, layer3.noUnitItem.get(ix).value);
					assertEquals(i+j+k, layer3.noUnitItem.get(ix).value);
					// unit1Item
					assertEquals(layer3.get(ix).unit1Item, layer3.unit1Item.get(ix));
					assertEquals(layer3.get(ix).unit1Item.noItem, layer3.unit1Item.get(ix).noItem);
					assertEquals(i+j+k, layer3.unit1Item.get(ix).noItem);
					// unit2Item
					assertEquals(layer3.get(ix).unit2Item, layer3.unit2Item.get(ix));
					assertEquals(layer3.get(ix).unit2Item.noItem, layer3.unit2Item.get(ix).noItem);
					assertEquals(i+j+k, layer3.unit2Item.get(ix).noItem);
				}
			}
		}
		assertEquals(counter, 12);
	}
  
}

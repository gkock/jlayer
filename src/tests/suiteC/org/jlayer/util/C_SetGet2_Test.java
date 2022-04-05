package org.jlayer.util;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

import basic.units.*;

/**
 * tests layers with an "irregular" shape - i.e. some elements are not defined
 * specifically methods {@code set()} and {@code get()}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_SetGet2_Test {
	
	// dimensionality settings
	
	final int dmx = 3;
	final int dmy[] = {3,0,4};
	final int dmz[][] = { {0,2,0} , {} , {0,3,0,0} , {} };
	
	// arrays and layers
	
	Unit1[] array1;
	Unit1[][] array2;
	Unit1[][][] array3;
	Layer_Unit1_ layer1, layer2, layer3;
	
	@BeforeClass
    public void beforeClass() {
		// create array1 = {U, U, U}
		array1 = new Unit1[dmx];
		for (int i = 0; i < dmx; i++) {	
			array1[i] = new Unit1(); 
		}
		
		// create array2 = { {U, U, U}, {}, {U, U, U, U} }
		array2 = new Unit1[dmx][];
		for (int i = 0; i < dmx; i++) {	
			array2[i] = new Unit1[dmy[i]];
			for (int j = 0; j < dmy[i]; j++) {	
				array2[i][j] = new Unit1(); 
			}
		}
		
		// create array3 = { { {}, {U, U}, {} } ; {} ; { {}, {U, U, U}, {}, {} } }
		array3 = new Unit1[dmx][][];
		for (int i = 0; i < dmx; i++) {	
			array3[i] = new Unit1[dmy[i]][];
			for (int j = 0; j < dmy[i]; j++) {	
				array3[i][j] = new Unit1[dmz[i][j]];
				for (int k = 0; k < dmz[i][j]; k++) {	
					array3[i][j][k] = new Unit1(); 
				}
			}
		}
		
		// create the layers
		layer1 = new Layer_Unit1_(array1);
		layer2 = new Layer_Unit1_(array2);
		layer3 = new Layer_Unit1_(array3);
	}
	
	@Test
	public void  testLayer1Dims() {
		int[] ix0 = new int[0];
		
		// layer1
		assertNotNull(layer1);
		assertEquals(layer1.dims(), 1);
		assertEquals(layer1.length(), dmx);
		assertEquals(layer1.length(ix0), dmx);
	}
	
	@Test
	public void  testLayer2Dims() {
		int[] ix1 = new int[1];
		
		// layer2
		assertNotNull(layer2);
		assertEquals(layer2.dims(), 2);
		assertEquals(layer2.length(), dmx);
		for (int i = 0; i < dmx; i++) {	
			ix1[0] = i;
			assertEquals(layer2.length(ix1), dmy[i]);
			assertEquals(layer2.length(i), dmy[i]);
		}
	}
	
	@Test
	public void  testLayer3Dims() {
		int[] ix1 = new int[1];
		int[] ix2 = new int[2];
		
		// layer3
		assertNotNull(layer3);
		assertEquals(layer3.dims(), 3);
		assertEquals(layer3.length(), dmx);
		for (int i = 0; i < dmx; i++) {	
			ix1[0] = i;
			assertEquals(layer3.length(ix1), dmy[i]);
			assertEquals(layer3.length(i), dmy[i]);
			for (int j = 0; j < dmy[i]; j++) {	
				ix2[0] = i; ix2[1] = j;
				assertEquals(layer3.length(ix2), dmz[i][j]);
				assertEquals(layer3.length(i, j), dmz[i][j]);
			}
		}
	}
  
	@Test
	public void testLayer1SetAndGet() {
		int counter = 0;
		for (int i = 0; i < dmx; i++) {
			// intItem
			layer1.intItem.set(i, i);
			// noUnitItem
			NoUnit noUnit = new NoUnit(i);
			layer1.noUnitItem.set(i, noUnit);
			// unit1Item
			Unit1 unit1 = new Unit1(i);
			layer1.unit1Item.set(i, unit1);
			// unit2Item
			Unit2 unit2 = new Unit2(i);
			layer1.unit2Item.set(i, unit2);
		}
		for (int i = 0; i < dmx; i++) {
			counter += layer1.intItem.get(i);
			// intItem
			assertEquals(layer1.get(i).intItem, (int)layer1.intItem.get(i));
			assertEquals(i, (int)layer1.intItem.get(i));
			// noUnitItem
			assertEquals(layer1.get(i).noUnitItem, layer1.noUnitItem.get(i));
			assertEquals(layer1.get(i).noUnitItem.value, layer1.noUnitItem.get(i).value);
			assertEquals(i, layer1.noUnitItem.get(i).value);
			// unit1Item
			assertEquals(layer1.get(i).unit1Item, layer1.unit1Item.get(i));
			assertEquals(layer1.get(i).unit1Item.noItem, layer1.unit1Item.get(i).noItem);
			assertEquals(i, layer1.unit1Item.get(i).noItem);
			// unit2Item
			assertEquals(layer1.get(i).unit2Item, layer1.unit2Item.get(i));
			assertEquals(layer1.get(i).unit2Item.noItem, layer1.unit2Item.get(i).noItem);
			assertEquals(i, layer1.unit2Item.get(i).noItem);
		}
		assertEquals(counter, 3);
	}
	
	@Test
	public void testLayer2SetAndGet() {
		int counter = 0;
		for (int i = 0; i < dmx; i++) {	
			for (int j = 0; j < dmy[i]; j++) {	
				// intItem
				layer2.intItem.set(i, j, i+j);
				// noUnitItem
				NoUnit noUnit = new NoUnit(i+j);
				layer2.noUnitItem.set(i, j, noUnit);
				// unit1Item
				Unit1 unit1 = new Unit1(i+j);
				layer2.unit1Item.set(i, j, unit1);
				// unit2Item
				Unit2 unit2 = new Unit2(i+j);
				layer2.unit2Item.set(i, j, unit2);
			}
		}
		for (int i = 0; i < dmx; i++) {	
			for (int j = 0; j < dmy[i]; j++) {	
				counter += layer2.intItem.get(i, j);
				// intItem
				assertEquals(layer2.get(i, j).intItem, (int)layer2.intItem.get(i, j));
				assertEquals(i+j, (int)layer2.intItem.get(i, j));
				// noUnitItem
				assertEquals(layer2.get(i, j).noUnitItem, layer2.noUnitItem.get(i, j));
				assertEquals(layer2.get(i, j).noUnitItem.value, layer2.noUnitItem.get(i, j).value);
				assertEquals(i+j, layer2.noUnitItem.get(i, j).value);
				// unit1Item
				assertEquals(layer2.get(i, j).unit1Item, layer2.unit1Item.get(i, j));
				assertEquals(layer2.get(i, j).unit1Item.noItem, layer2.unit1Item.get(i, j).noItem);
				assertEquals(i+j, layer2.unit1Item.get(i, j).noItem);
				// unit2Item
				assertEquals(layer2.get(i, j).unit2Item, layer2.unit2Item.get(i, j));
				assertEquals(layer2.get(i, j).unit2Item.noItem, layer2.unit2Item.get(i, j).noItem);
				assertEquals(i+j, layer2.unit2Item.get(i, j).noItem);
			}
		}
		assertEquals(counter, 17);
		
	}
		
	@Test
	public void testLayer3SetAndGet() {
		int counter = 0;
		for (int i = 0; i < dmx; i++) {	
			for (int j = 0; j < dmy[i]; j++) {	
				for (int k = 0; k < dmz[i][j]; k++) {	
					// intItem
					layer3.intItem.set(i, j, k, i+j+k);
					// noUnitItem
					NoUnit noUnit = new NoUnit(i+j+k);
					layer3.noUnitItem.set(i, j, k, noUnit);
					// unit1Item
					Unit1 unit1 = new Unit1(i+j+k);
					layer3.unit1Item.set(i, j, k, unit1);
					// unit2Item
					Unit2 unit2 = new Unit2(i+j+k);
					layer3.unit2Item.set(i, j, k, unit2);
					
				}
			}
		}
		for (int i = 0; i < dmx; i++) {	
			for (int j = 0; j < dmy[i]; j++) {	
				for (int k = 0; k < dmz[i][j]; k++) {	
					counter += layer3.intItem.get(i, j, k);
					// intItem
					assertEquals(layer3.get(i, j, k).intItem, (int)layer3.intItem.get(i, j, k));
					assertEquals(i+j+k, (int)layer3.intItem.get(i, j, k));
					// noUnitItem
					assertEquals(layer3.get(i, j, k).noUnitItem, layer3.noUnitItem.get(i, j, k));
					assertEquals(layer3.get(i, j, k).noUnitItem.value, layer3.noUnitItem.get(i, j, k).value);
					assertEquals(i+j+k, layer3.noUnitItem.get(i, j, k).value);
					// unit1Item
					assertEquals(layer3.get(i, j, k).unit1Item, layer3.unit1Item.get(i, j, k));
					assertEquals(layer3.get(i, j, k).unit1Item.noItem, layer3.unit1Item.get(i, j, k).noItem);
					assertEquals(i+j+k, layer3.unit1Item.get(i, j, k).noItem);
					// unit2Item
					assertEquals(layer3.get(i, j, k).unit2Item, layer3.unit2Item.get(i, j, k));
					assertEquals(layer3.get(i, j, k).unit2Item.noItem, layer3.unit2Item.get(i, j, k).noItem);
					assertEquals(i+j+k, layer3.unit2Item.get(i, j, k).noItem);
				}
			}
		}
		assertEquals(counter, 15);
	}
  
}

package org.jlayer.util;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

import basic.units.*;

/**
 * tests the generated {@code Layer_MyUnit_}, 
 * UNDER DEVELOPMENT.
 * @author Gerd Kock
 */
//@Test(groups = {"suiteC"})
@Test(groups = {"suiteC", "choice"})
public class C_MyUnit_Test {
	
	final int sz1 = 9;
	final int sz2 = 6;
	final int sz3 = 3;
	
	MyUnit[] array1;
	MyUnit[][] array2;
	MyUnit[][][] array3;
	Layer_MyUnit_ layer1, layer2, layer3;
	
	@BeforeClass
    public void beforeClass() {
		// init arrays
		array1 = new MyUnit[sz1];
		array2 = new MyUnit[sz1][sz2];
		array3 = new MyUnit[sz1][sz2][sz3];
		for (int i=0; i<sz1; i++) {
			array1[i] = new MyUnit();
			for (int j=0; j<sz2; j++) {
				array2[i][j] = new MyUnit();
				for (int k=0; k<sz3; k++) {
					array3[i][j][k] = new MyUnit();
				}
			}
		}
		// wrap arrays
		layer1 = new Layer_MyUnit_(array1);
		layer2 = new Layer_MyUnit_(array2);
		layer3 = new Layer_MyUnit_(array3);
	}
	
	@Test
	public void  testArraySizes() {
		for (int i=0; i<sz1; i++) {
			assertEquals(array1.length, sz1);
			assertEquals(array2.length, sz1);
			assertEquals(array3.length, sz1);
			assertEquals(array2[i].length, sz2);
			assertEquals(array3[i].length, sz2);
			for (int j=0; j<sz2; j++) {
				assertEquals(array3[i][j].length, sz3);
			}
		}
	}
	
	@Test
	public void  testIndexFields() {
		for (int i=0; i<sz1; i++) {
			assertEquals(array1[i].index[0], i);
			for (int j=0; j<sz2; j++) {
				assertEquals(array2[i][j].index[0], i);
				assertEquals(array2[i][j].index[1], j);
				for (int k=0; k<sz3; k++) {
					assertEquals(array3[i][j][k].index[0], i);
					assertEquals(array3[i][j][k].index[1], j);
					assertEquals(array3[i][j][k].index[2], k);
				}
			}
		}
	}
  
}

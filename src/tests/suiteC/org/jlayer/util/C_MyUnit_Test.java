package org.jlayer.util;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import basic.units.*;

/**
 * tests the generated {@code Layer_Unit1_}, 
 * UNDER DEVELOPMENT.
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
//@Test(groups = {"suiteC", "choice"})
public class C_MyUnit_Test {
	
	final int sz1 = 1000;
	final int sz2 = 200;
	final int sz3 = 40;
	
	MyUnit[] array1;
	MyUnit[][] array2;
	MyUnit[][][] array3;
	Layer_MyUnit_ layer1, layer2, layer3;
	
	@BeforeClass
    public void beforeClass() {
		
	}
	
	int param = 1000000;
	
	@Test
	public void  testLayer1A() {
		long startTime = System.currentTimeMillis();
//		layer1.returnInt();
		long endTime = System.currentTimeMillis();
	}
  
}

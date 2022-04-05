package org.jlayer.util;

import org.testng.annotations.Test;
import static org.testng.Assert.assertNotNull;

import basic.units.*;

/**
 * TEST UNDER DEVELOPMENT.
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_LifeUnit_Test {
  
  @Test
  public void testLifeUnit() {
	  LifeUnit[][] array = new LifeUnit[10][10];
	  Layer_LifeUnit_ layer = new Layer_LifeUnit_(array);
	  assertNotNull(layer);
  }
  
}

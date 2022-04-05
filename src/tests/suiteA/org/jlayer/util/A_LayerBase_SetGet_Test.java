package org.jlayer.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

/**
 * contains tests for class {@code org.jlayer.util.LayerBase}, except for method {@code iterator()}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteA"})
public class A_LayerBase_SetGet_Test {
	
	static class NoUnit {
		public NoUnit(int i){ value = i; }
		public int value;
	}
	
	NoUnit[] array1 = new NoUnit[10];
	NoUnit[][] array2 = new NoUnit[3][3];
	NoUnit[][][] array3 = new NoUnit[2][2][2];
	LayerBase<NoUnit> layer1, layer2, layer3;
	
	@BeforeClass
	public void beforeClass() {
		layer1 = new LayerBase<NoUnit>(array1);
		layer2 = new LayerBase<NoUnit>(array2);
		layer3 = new LayerBase<NoUnit>(array3);
	}
	
	@Test
	public void testBasics() {
		assertNotNull(layer1);
		assertNotNull(layer2);
		assertNotNull(layer3);
		assertEquals(layer1.dims(), 1);
		assertEquals(layer2.dims(), 2);
		assertEquals(layer3.dims(), 3);
	}
	
	// test length()
	
	@Test
	public void testLength() {
		int len;
		// layer1
		len = layer1.length();
		assertEquals(len, 10);
		// layer2
		for (int i = 0; i < layer2.length(); i++) {
			len = layer2.length(i);
			assertEquals(len, 3);
		}
		// layer3
		for (int i = 0; i < layer3.length(); i++) {
			len = layer3.length(i);
			assertEquals(len, 2);
		}
		for (int i = 0; i < layer3.length(); i++) {
			for (int j = 0; j < layer3.length(i); j++) {
				len = layer3.length(i,j);
				assertEquals(len, 2);
			}
		}
	}
	
	@Test(expectedExceptions = IndexOutOfBoundsException.class) 
	public void testLengthForException() {
		layer1.length(0);
	}
	
	@Test(expectedExceptions = IndexOutOfBoundsException.class) 
	public void testLengthForException3() {
		layer2.length(0,0);
	}
	
	@Test(expectedExceptions = IndexOutOfBoundsException.class) 
	public void testLengthForException4() {
		layer3.length(0,0,0);
	}
	
	// test set() and get() for correct use
	
	@Test
	public void testLayer1SetGet() {
		int[] index = new int[1];
		for (int i = 0; i < layer1.length(); i++) {
			index[0] = i;
			layer1.set(index, new NoUnit(i));
		}
		for (int i = 0; i < layer1.length(); i++) {
			index[0] = i;
			NoUnit result = layer1.get(index);
			assertEquals(i, result.value);
		}
	}
	
	@Test
	public void testLayer2SetGet() {
		int[] index = new int[2];
		for (int i = 0; i < layer2.length(); i++) {
			for (int j = 0; j < layer2.length(i); j++) {
				index[0] = i; index[1] = j;
				layer2.set(index, new NoUnit(i+j));
			}
		}
		for (int i = 0; i < layer2.length(); i++) {
			for (int j = 0; j < layer2.length(i); j++) {
				index[0] = i; index[1] = j;
				NoUnit result = layer2.get(index);
				assertEquals(i+j, result.value);
			}
		}
	}
	
	@Test
	public void testLayer3SetGet() {
		int[] index = new int[3];
		for (int i = 0; i < layer3.length(); i++) {
			for (int j = 0; j < layer3.length(i); j++) {
				for (int k = 0; k < layer3.length(i, j); k++) {
					index[0] = i; index[1] = j; index[2] = k;
					layer3.set(index, new NoUnit(i+j+k));
				}
			}
		}
		for (int i = 0; i < layer3.length(); i++) {
			for (int j = 0; j < layer3.length(i); j++) {
				for (int k = 0; k < layer3.length(i, j); k++) {
					index[0] = i; index[1] = j; index[2] = k;
					NoUnit result = layer3.get(index);
					assertEquals(i+j+k, result.value);
				}
			}
		}
	}
	
	// test set() for exceptions
	
	@Test(expectedExceptions = ArrayIndexOutOfBoundsException.class) 
	public void testSetForException1() {
		layer1.set(20, new NoUnit(20));
	}
	
	@Test(expectedExceptions = IndexOutOfBoundsException.class) 
	public void testSetForException2() {
		layer2.set(2, new NoUnit(2));
	}
	
	@Test(expectedExceptions = IndexOutOfBoundsException.class) 
	public void testSetForException3() {
		int[] index = {1,1};
		layer3.set(index, new NoUnit(2));
	}
	
	@Test(expectedExceptions = ArrayIndexOutOfBoundsException.class) 
	public void testSetForException4() {
		int[] index = {3,3,3};
		layer3.set(index, new NoUnit(9));
	}
	
	// test get() for exceptions
	
	@Test(expectedExceptions = ArrayIndexOutOfBoundsException.class) 
	public void testGetForException1() {
		layer1.get(20);
	}
	
	@Test(expectedExceptions = IndexOutOfBoundsException.class) 
	public void testGetForException2() {
		layer2.get(2);
	}
	
	@Test(expectedExceptions = IndexOutOfBoundsException.class) 
	public void testGetForException3() {
		int[] index = {1,1};
		layer3.get(index);
	}
	
	@Test(expectedExceptions = ArrayIndexOutOfBoundsException.class) 
	public void testGetForException4() {
		int[] index = {3,3,3};
		layer3.get(index);
	}
	
}

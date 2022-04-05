package org.jlayer.util;


import java.util.Iterator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.jlayer.model.XIterator;

/**
 * contains tests for class {@code org.jlayer.util.LayerBase.D3}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteA"})
public class A_LayerBase_D3_Test {
	
	static class NoUnit {
		public NoUnit(int i){ value = i; }
		public int value;
	}
	
	NoUnit[][][] array = new NoUnit[3][3][3];
	LayerBase.D3<NoUnit> layerD3, otherD3;
	LayerBase<NoUnit> layer;
	
	@BeforeClass
	public void beforeClass() {
		layerD3 = new LayerBase.D3<NoUnit>(array);
		layer   = new LayerBase<NoUnit>(layerD3);
		otherD3 = new LayerBase.D3<NoUnit>(layer.getD3().getBase());
	}
	
	@Test
	public void testBasics() {
		assertNotNull(layerD3);
		assertEquals(layerD3.length(), 3);
		for (int i = 0; i < layerD3.length(); i++) {
			assertEquals(layerD3.length(i), 3);
			for (int j = 0; j < layerD3.length(i); j++) {
				assertEquals(layerD3.length(i,j), 3);
			}
		}
		assertEquals(layer.length(), 3);
		for (int i = 0; i < layer.length(); i++) {
			assertEquals(layer.length(i), 3);
			for (int j = 0; j < layer.length(i); j++) {
				assertEquals(layer.length(i,j), 3);
			}
		}
		assertNotEquals(layerD3, otherD3);
		assertEquals(layerD3.getBase(), array);
		assertEquals(otherD3.getBase(), array);
	}
	
	@Test
	public void testSetGet1() {
		for (int i = 0; i < layerD3.length(); i++) {
			for (int j = 0; j < layerD3.length(i); j++) {
				for (int k = 0; k < layerD3.length(i,j); k++) {
					layerD3.set(i, j, k, new NoUnit(i+j+k));
				}
			}
		}
		for (int i = 0; i < layerD3.length(); i++) {
			for (int j = 0; j < layerD3.length(i); j++) {
				for (int k = 0; k < layerD3.length(i,j); k++) {
					NoUnit result = layerD3.get(i, j, k);
					assertEquals(i+j+k, result.value);
					result = layer.get(i, j, k);
					assertEquals(i+j+k, result.value);
				}
			}
		}
	}
	
	@Test
	public void testSetGet2() {
		for (int i = 0; i < layer.length(); i++) {
			for (int j = 0; j < layer.length(i); j++) {
				for (int k = 0; k < layer.length(i,j); k++) {
					layer.set(i, j, k, new NoUnit(i+j+k+2));
				}
			}
		}
		for (int i = 0; i < layerD3.length(); i++) {
			for (int j = 0; j < layerD3.length(i); j++) {
				for (int k = 0; k < layerD3.length(i,j); k++) {
					NoUnit result = layerD3.get(i, j, k);
					assertEquals(i+j+k+2, result.value);
					result = layer.get(i, j, k);
					assertEquals(i+j+k+2, result.value);
				}
			}
		}
	}
	
	@Test
	public void  testIteration() {
		for (int i = 0; i < layerD3.length(); i++) {
			for (int j = 0; j < layerD3.length(i); j++) {
				for (int k = 0; k < layerD3.length(i,j); k++) {
					layerD3.set(i, j, k, new NoUnit(i+j+k));
				}
			}
		}
		final int checksum = 81; // checksum is the sum of all indices
		int cnt = 0;
		for (Iterator<NoUnit> iterator = layerD3.iterator(); iterator.hasNext();) {
			NoUnit unit = iterator.next();
			cnt += unit.value;
		}
		assertEquals(cnt, checksum);
	}
	
	@Test
	public void  testLayerIteration() {
		for (int i = 0; i < layerD3.length(); i++) {
			for (int j = 0; j < layerD3.length(i); j++) {
				for (int k = 0; k < layerD3.length(i,j); k++) {
					layerD3.set(i, j, k, new NoUnit(i+j+k));
				}
			}
		}
		final int checksum = 81; // checksum is the sum of all indices
		int cnt = 0;
		for (XIterator.D3<NoUnit> iterator = layerD3.xIterator(); iterator.hasNext();) {
			cnt += iterator.getX1();
			cnt += iterator.getX2();
			cnt += iterator.getX3();
			iterator.next();
		}
		assertEquals(cnt, checksum);
	}
	
	// test set() for exceptions
	
	@Test(expectedExceptions = ArrayIndexOutOfBoundsException.class) 
	public void testSetForException() {
		layerD3.set(2, 3, 2, new NoUnit(20));
	}
	
	// test get() for exceptions
	
	@Test(expectedExceptions = ArrayIndexOutOfBoundsException.class) 
	public void testGetForException1() {
		layerD3.get(3, 2, 3);
	}
	
}

package org.jlayer.util;


import java.util.Iterator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.jlayer.model.XIterator;

/**
 * contains tests for class {@code org.jlayer.util.LayerBase.D2}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteA"})
public class A_LayerBase_D2_Test {
	
	static class NoUnit {
		public NoUnit(int i){ value = i; }
		public int value;
	}
	
	NoUnit[][] array = new NoUnit[3][3];
	LayerBase.D2<NoUnit> layerD2, otherD2;
	LayerBase<NoUnit> layer;
	
	@BeforeClass
	public void beforeClass() {
		layerD2 = new LayerBase.D2<NoUnit>(array);
		layer   = new LayerBase<NoUnit>(layerD2);
		otherD2 = new LayerBase.D2<NoUnit>(layer.getD2().getBase());
	}
	
	@Test
	public void testBasics() {
		assertNotNull(layerD2);
		assertEquals(layerD2.length(), 3);
		for (int i = 0; i < layerD2.length(); i++) {
			assertEquals(layerD2.length(i), 3);
		}
		assertEquals(layer.length(), 3);
		for (int i = 0; i < layer.length(); i++) {
			assertEquals(layer.length(i), 3);
		}
		assertNotEquals(layerD2, otherD2);
		assertEquals(layerD2.getBase(), array);
		assertEquals(otherD2.getBase(), array);
	}
	
	@Test
	public void testSetGet1() {
		for (int i = 0; i < layerD2.length(); i++) {
			for (int j = 0; j < layerD2.length(i); j++) {
				layerD2.set(i, j, new NoUnit(i+j));
			}
		}
		for (int i = 0; i < layerD2.length(); i++) {
			for (int j = 0; j < layerD2.length(i); j++) {
				NoUnit result = layerD2.get(i, j);
				assertEquals(i+j, result.value);
				result = layer.get(i, j);
				assertEquals(i+j, result.value);
			}
		}
	}
	
	@Test
	public void testSetGet2() {
		for (int i = 0; i < layer.length(); i++) {
			for (int j = 0; j < layer.length(i); j++) {
				layer.set(i, j, new NoUnit(i+j+2));
			}
		}
		for (int i = 0; i < layer.length(); i++) {
			for (int j = 0; j < layer.length(i); j++) {
				NoUnit result = layerD2.get(i, j);
				assertEquals(i+j+2, result.value);
				result = layer.get(i, j);
				assertEquals(i+j+2, result.value);
			}
		}
	}
	
	@Test
	public void  testIteration() {
		for (int i = 0; i < layerD2.length(); i++) {
			for (int j = 0; j < layerD2.length(i); j++) {
				layerD2.set(i, j, new NoUnit(i+j));
			}
		}
		final int checksum = 18; // checksum is the sum of all indices
		int cnt = 0;
		for (Iterator<NoUnit> iterator = layerD2.iterator(); iterator.hasNext();) {
			NoUnit unit = iterator.next();
			cnt += unit.value;
		}
		assertEquals(cnt, checksum);
	}
	
	@Test
	public void  testLayerIteration() {
		for (int i = 0; i < layerD2.length(); i++) {
			for (int j = 0; j < layerD2.length(i); j++) {
				layerD2.set(i, j, new NoUnit(i+j));
			}
		}
		final int checksum = 18; // checksum is the sum of all indices
		int cnt = 0;
		for (XIterator.D2<NoUnit> iterator = layerD2.xIterator(); iterator.hasNext();) {
			cnt += iterator.getX1();
			cnt += iterator.getX2();
			iterator.next();
		}
		assertEquals(cnt, checksum);
	}
	
	// test set() for exceptions
	
	@Test(expectedExceptions = ArrayIndexOutOfBoundsException.class) 
	public void testSetForException() {
		layerD2.set(2, 3, new NoUnit(20));
	}
	
	// test get() for exceptions
	
	@Test(expectedExceptions = ArrayIndexOutOfBoundsException.class) 
	public void testGetForException1() {
		layerD2.get(3, 2);
	}
	
}

package org.jlayer.util;

import java.util.Iterator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.jlayer.model.XIterator;

/**
 * contains tests for class {@code org.jlayer.util.LayerBase.D1}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteA"})
public class A_LayerBase_D1_Test {
	
	static class NoUnit {
		public NoUnit(int i){ value = i; }
		public int value;
	}
	
	NoUnit[] array = new NoUnit[10];
	LayerBase.D1<NoUnit> layerD1, otherD1;
	LayerBase<NoUnit> layer;
	
	@BeforeClass
	public void beforeClass() {
		layerD1 = new LayerBase.D1<NoUnit>(array);
		layer   = new LayerBase<NoUnit>(layerD1);
		otherD1 = new LayerBase.D1<NoUnit>(layer.getD1().getBase());
	}
	
	@Test
	public void testBasics() {
		assertNotNull(layerD1);
		assertEquals(layerD1.length(), 10);
		assertEquals(layer.length(), 10);
		assertNotEquals(layerD1, otherD1);
		assertEquals(layerD1.getBase(), array);
		assertEquals(otherD1.getBase(), array);
	}
	
	@Test
	public void testSetGet1() {
		for (int i = 0; i < layerD1.length(); i++) {
			layerD1.set(i, new NoUnit(i));
		}
		for (int i = 0; i < layerD1.length(); i++) {
			NoUnit result = layerD1.get(i);
			assertEquals(i, result.value);
			result = layer.get(i);
			assertEquals(i, result.value);
		}
	}
	
	@Test
	public void testSetGet2() {
		for (int i = 0; i < layer.length(); i++) {
			layer.set(i, new NoUnit(i+2));
		}
		for (int i = 0; i < layer.length(); i++) {
			NoUnit result = layerD1.get(i);
			assertEquals(i+2, result.value);
			result = layer.get(i);
			assertEquals(i+2, result.value);
		}
	}
	
	@Test
	public void  testIteration() {
		for (int i = 0; i < layerD1.length(); i++) {
			layerD1.set(i, new NoUnit(i));
		}
		final int checksum = 45; // checksum is the sum of all indices
		int cnt = 0;
		for (Iterator<NoUnit> iterator = layerD1.iterator(); iterator.hasNext();) {
			NoUnit unit = iterator.next();
			cnt += unit.value;
		}
		assertEquals(cnt, checksum);
	}
	
	@Test
	public void  testLayerIteration() {
		for (int i = 0; i < layerD1.length(); i++) {
			layerD1.set(i, new NoUnit(i));
		}
		final int checksum = 45; // checksum is the sum of all indices
		int cnt = 0;
		for (XIterator.D1<NoUnit> iterator = layerD1.xIterator(); iterator.hasNext();) {
			cnt += iterator.getX1();
			iterator.next();
		}
		assertEquals(cnt, checksum);
	}
	
	// test set() for exceptions
	
	@Test(expectedExceptions = ArrayIndexOutOfBoundsException.class) 
	public void testSetForException() {
		layerD1.set(20, new NoUnit(20));
	}
	
	// test get() for exceptions
	
	@Test(expectedExceptions = ArrayIndexOutOfBoundsException.class) 
	public void testGetForException1() {
		layerD1.get(20);
	}
	
}

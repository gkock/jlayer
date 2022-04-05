package org.jlayer.util;

import java.util.NoSuchElementException;
import java.util.Iterator;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * contains tests for method {@code iterator()} of class {@code org.jlayer.util.LayerBase}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteA"})
public class A_LayerBase_DimsIter_Test {
	
	static class NoUnit {
		public NoUnit(int i){ value = i; }
		public int value;
	}
	
	// dimensionalities
	
	final int dmx = 3;
	final int dm0y = 3;
	final int dm1y = 1;
	final int dm2y = 4;
	
	final int dm00z = 1;
	final int dm01z = 2;
	final int dm02z = 1;
	final int dm10z = 3;
	final int dm20z = 1;
	final int dm21z = 3;
	final int dm22z = 1;
	final int dm23z = 1;
	
	NoUnit[] array1;
	NoUnit[][] array2;
	NoUnit[][][] array3;
	LayerBase<NoUnit> layer1, layer2, layer3;
	
	// create arrays and corresponding layers
	
	@BeforeClass
	public void beforeClass() {
		// create array1 = {U, U, U}
		
		array1 = new NoUnit[dmx];
		for (int i = 0; i < dmx; i++) {	
			array1[i] = new NoUnit(i); 
		}
		
		// create array2 = { {U, U, U}, {U}, {U, U, U, U} }
		
		array2 = new NoUnit[dmx][];
		
		array2[0] = new NoUnit[dm0y];
		for (int i = 0; i < dm0y; i++) {	
			array2[0][i] = new NoUnit(i); 
		}
		
		array2[1] = new NoUnit[dm1y];
		for (int i = 0; i < dm1y; i++) {	
			array2[1][i] = new NoUnit(1+i); 
		}
		
		array2[2] = new NoUnit[dm2y];
		for (int i = 0; i < dm2y; i++) {	
			array2[2][i] = new NoUnit(2+i); 
		}
		
		// create array3 = { { {U}, {U, U}, {U} } ; {{U, U, U}} ; { {U}, {U, U, U}, {U}, {U} } }
		
		array3 = new NoUnit[dmx][][];
		
		array3[0] = new NoUnit[dm0y][];
		array3[0][0] = new NoUnit[dm00z];
		for (int i = 0; i < dm00z; i++) {	
			array3[0][0][i] = new NoUnit(i); 
		}
		array3[0][1] = new NoUnit[dm01z];
		for (int i = 0; i < dm01z; i++) {	
			array3[0][1][i] = new NoUnit(1+i); 
		}
		array3[0][2] = new NoUnit[dm02z];
		for (int i = 0; i < dm02z; i++) {	
			array3[0][2][i] = new NoUnit(2+i); 
		}
		
		array3[1] = new NoUnit[dm1y][];
		array3[1][0] = new NoUnit[dm10z];
		for (int i = 0; i < dm10z; i++) {	
			array3[1][0][i] = new NoUnit(1+i); 
		}
		
		array3[2] = new NoUnit[dm2y][];
		array3[2][0] = new NoUnit[dm20z];
		for (int i = 0; i < dm20z; i++) {	
			array3[2][0][i] = new NoUnit(2+i); 
		}
		array3[2][1] = new NoUnit[dm21z];
		for (int i = 0; i < dm21z; i++) {	
			array3[2][1][i] = new NoUnit(2+1+i); 
		}
		array3[2][2] = new NoUnit[dm22z];
		for (int i = 0; i < dm22z; i++) {	
			array3[2][2][i] = new NoUnit(2+2+i); 
		}
		array3[2][3] = new NoUnit[dm23z];
		for (int i = 0; i < dm23z; i++) {	
			array3[2][3][i] = new NoUnit(2+3+i); 
		}
		
		// create the layers
		
		layer1 = new LayerBase<NoUnit>(array1);
		layer2 = new LayerBase<NoUnit>(array2);
		layer3 = new LayerBase<NoUnit>(array3);
	}
	
	// test basics
	
	@Test
	public void testBasics() {
		assertNotNull(layer1);
		assertNotNull(layer2);
		assertNotNull(layer3);
		assertEquals(layer1.dims(), 1);
		assertEquals(layer2.dims(), 2);
		assertEquals(layer3.dims(), 3);
	}
	
	// test dims(), length()
	
	@Test
	public void  testLayer1() {
		int[] ix0 = new int[0];
		
		assertNotNull(layer1);
		assertEquals(layer1.dims(), 1);
		assertEquals(layer1.length(), dmx);
		assertEquals(layer1.length(ix0), dmx);
	}
	
	@Test
	public void  testLayer2() {
		int[] ix1 = new int[1];
		
		assertNotNull(layer2);
		assertEquals(layer2.dims(), 2);
		assertEquals(layer2.length(), dmx);
		ix1[0] = 0;
		assertEquals(layer2.length(ix1), dm0y);
		assertEquals(layer2.length(0), dm0y);
		ix1[0] = 1;
		assertEquals(layer2.length(ix1), dm1y);
		assertEquals(layer2.length(1), dm1y);
		ix1[0] = 2;
		assertEquals(layer2.length(ix1), dm2y);
		assertEquals(layer2.length(2), dm2y);
	}
	
	@Test
	public void  testLayer3() {
		int[] ix1 = new int[1];
		int[] ix2 = new int[2];
		
		assertNotNull(layer3);
		assertEquals(layer3.dims(), 3);
		assertEquals(layer3.length(), dmx);
		
		ix1[0] = 0;
		assertEquals(layer3.length(ix1), dm0y);
		assertEquals(layer3.length(0), dm0y);
		ix2[0] = 0; ix2[1] = 0;
		assertEquals(layer3.length(ix2), dm00z);
		assertEquals(layer3.length(0, 0), dm00z);
		ix2[0] = 0; ix2[1] = 1;
		assertEquals(layer3.length(ix2), dm01z);
		assertEquals(layer3.length(0, 1), dm01z);
		ix2[0] = 0; ix2[1] = 2;
		assertEquals(layer3.length(ix2), dm02z);
		assertEquals(layer3.length(0, 2), dm02z);
		
		ix1[0] = 1;
		assertEquals(layer3.length(ix1), dm1y);
		assertEquals(layer3.length(1), dm1y);
		ix2[0] = 1; ix2[1] = 0;
		assertEquals(layer3.length(ix2), dm10z);
		assertEquals(layer3.length(1, 0), dm10z);
		
		ix1[0] = 2;
		assertEquals(layer3.length(ix1), dm2y);
		assertEquals(layer3.length(2), dm2y);
		ix2[0] = 2; ix2[1] = 0;
		assertEquals(layer3.length(ix2), dm20z);
		assertEquals(layer3.length(2, 0), dm20z);
		ix2[0] = 2; ix2[1] = 1;
		assertEquals(layer3.length(ix2), dm21z);
		assertEquals(layer3.length(2, 1), dm21z);
		ix2[0] = 2; ix2[1] = 2;
		assertEquals(layer3.length(ix2), dm22z);
		assertEquals(layer3.length(2, 2), dm22z);
		ix2[0] = 2; ix2[1] = 3;
		assertEquals(layer3.length(ix2), dm23z);
		assertEquals(layer3.length(2, 3), dm23z);
	}
	
	// test iterator()
	
	@Test
	public void  testLayer1Iteration() {
		final int checksum = 3; // checksum is the sum of all indices
		int cnt = 0;
		for (Iterator<NoUnit> iterator = layer1.iterator(); iterator.hasNext();) {
			NoUnit unit = iterator.next();
			cnt += unit.value;
		}
		assertEquals(cnt, checksum);
	}
	
	@Test
	public void  testLayer2Iteration() {
		final int checksum = 18; // checksum is the sum of all indices
		int cnt = 0;
		for (Iterator<NoUnit> iterator = layer2.iterator(); iterator.hasNext();) {
			NoUnit unit = iterator.next();
			cnt += unit.value;
		}
		assertEquals(cnt, checksum);
	}
	
	@Test
	public void  testLayer3Iteration() {
		final int checksum = 34; // checksum is the sum of all indices
		int cnt = 0;
		for (Iterator<NoUnit> iterator = layer3.iterator(); iterator.hasNext();) {
			NoUnit unit = iterator.next();
			cnt += unit.value;
		}
		assertEquals(cnt, checksum);
	}
	
	// test iterator() exceptions
	
	@Test(expectedExceptions = NoSuchElementException.class)  
	public void  testLayer1NextException() {
		Iterator<NoUnit> iterator;
		for (iterator = layer1.iterator(); iterator.hasNext();) {
			iterator.next();
		}
		iterator.next();
	}
	
	@Test(expectedExceptions = NoSuchElementException.class)  
	public void  testLayer2NextException() {
		Iterator<NoUnit> iterator;
		for (iterator = layer2.iterator(); iterator.hasNext();) {
			iterator.next();
		}
		iterator.next();
	}
	
	@Test(expectedExceptions = NoSuchElementException.class)  
	public void  testLayer3NextException() {
		Iterator<NoUnit> iterator;
		for (iterator = layer3.iterator(); iterator.hasNext();) {
			iterator.next();
		}
		iterator.next();
	}
	
	@Test(expectedExceptions = UnsupportedOperationException.class)  
	public void  testLayer1RemoveException() {
		for (Iterator<NoUnit> iterator = layer1.iterator(); iterator.hasNext();) {
			NoUnit unit = iterator.next();
			if (unit.value == 1){
				iterator.remove();
			}
		}
	}
	
	@Test(expectedExceptions = UnsupportedOperationException.class)  
	public void  testLayer2RemoveException() {
		for (Iterator<NoUnit> iterator = layer2.iterator(); iterator.hasNext();) {
			NoUnit unit = iterator.next();
			if (unit.value == 3){
				iterator.remove();
			}
		}
	}
	
	@Test(expectedExceptions = UnsupportedOperationException.class)  
	public void  testLayer3RemoveException() {
		for (Iterator<NoUnit> iterator = layer3.iterator(); iterator.hasNext();) {
			NoUnit unit = iterator.next();
			if (unit.value == 4){
				iterator.remove();
			}
		}
	}
	
}

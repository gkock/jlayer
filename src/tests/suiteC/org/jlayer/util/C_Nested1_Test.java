package org.jlayer.util;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import java.util.Iterator;
import basic.units.*;

/**
 * tests layers based on nested units, 
 * specifically the method {@code iterator()}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Nested1_Test {
	
	final int sz1 = 10;
	final int sz2 = 3;
	final int sz3 = 2;
	
	Unit1.SignalInt[] array1;
	Unit1.SignalInt[][] array2;
	Unit1.SignalInt[][][] array3;
	
	@BeforeClass
    public void beforeClass() {
		array1 = new Unit1.SignalInt[sz1];
		for (int i = 0; i < sz1; i++) {	
			array1[i] = new Unit1.SignalInt(); 
		}
		array2 = new Unit1.SignalInt[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array2[i][j] = new Unit1.SignalInt(); 
			}
		}
		array3 = new Unit1.SignalInt[sz3][sz3][sz3];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					array3[i][j][k] = new Unit1.SignalInt(); 
				}
			}
		}
	}
	
	@Test
	public void testLayerD1() {
		Layer_Unit1_.Layer_SignalInt_.D1 layerD1 = new Layer_Unit1_.Layer_SignalInt_.D1(array1);
		int counter;
		
		// initialization
		for (int i = 0; i < sz1; i++) {
			// signal
			layerD1.val.set(i, i);
		}
		
		// layerD1.iterator()
		Iterator<Unit1.SignalInt> itUnit1Elem = layerD1.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().val; }
		assertEquals(counter, 45);
		// layerD1.signal.iterator()
		Iterator<Integer> itInt = layerD1.val.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 45);
	}
	
	@Test
	public void testLayerD2() {
		Layer_Unit1_.Layer_SignalInt_.D2 layerD2 = new Layer_Unit1_.Layer_SignalInt_.D2(array2);
		int counter;
		
		// initialization
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				// signal
				layerD2.val.set(i, j, i+j);
			}
		}
		
		// layerD2.iterator()
		Iterator<Unit1.SignalInt> itUnit1Elem = layerD2.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().val; }
		assertEquals(counter, 18);
		// layerD1.signal.iterator()
		Iterator<Integer> itInt = layerD2.val.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 18);
	}
	
	@Test
	public void testLayerD3() {
		Layer_Unit1_.Layer_SignalInt_.D3 layerD3 = new Layer_Unit1_.Layer_SignalInt_.D3(array3);
		int counter;
		
		// initialization
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					// signal
					layerD3.val.set(i, j, k, i+j+k);
				}
			}
		}
		
		// layerD3.iterator()
		Iterator<Unit1.SignalInt> itUnit1Elem = layerD3.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().val; }
		assertEquals(counter, 12);
		// layerD1.signal.iterator()
		Iterator<Integer> itInt = layerD3.val.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 12);
	}
  
	@Test
	public void testLayer1dim() {
		Layer_Unit1_.Layer_SignalInt_ layer1 = new Layer_Unit1_.Layer_SignalInt_(array1);
		int counter;
		
		// initialization
		int[] ix = new int[1];
		for (int i = 0; i < sz1; i++) {
			ix[0] = i;
			// signal
			layer1.val.set(ix, 2*i);
		}
		
		// layer1.iterator()
		Iterator<Unit1.SignalInt> itUnit1Elem = layer1.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().val; }
		assertEquals(counter, 2*45);
		
		// layer1.signal.iterator()
		Iterator<Integer> itInt = layer1.val.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 2*45);
	}
	
	@Test
	public void testLayer2dim() {
		Layer_Unit1_.Layer_SignalInt_ layer2 = new Layer_Unit1_.Layer_SignalInt_(array2);
		int counter;
		
		int[] ix = new int[2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				ix[0] = i; ix[1] = j;
				// signal
				layer2.val.set(ix, 2*(i+j));
			}
		}

		// layer2.iterator()
		Iterator<Unit1.SignalInt> itUnit1Elem = layer2.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().val; }
		assertEquals(counter, 2*18);
		
		// layer2.signal.iterator()
		Iterator<Integer> itInt = layer2.val.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 2*18);
	}
	
	@Test
	public void testLayer3dim() {
		Layer_Unit1_.Layer_SignalInt_ layer3 = new Layer_Unit1_.Layer_SignalInt_(array3);
		int counter;
		
		int[] ix = new int[3];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					ix[0] = i; ix[1] = j; ix[2] = k;
					// signal
					layer3.val.set(ix, 2*(i+j+k));
				}
			}
		}
		
		// layer3.iterator()
		Iterator<Unit1.SignalInt> itUnit1Elem = layer3.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().val; }
		assertEquals(counter, 2*12);
		
		// layer3.signal.iterator()
		Iterator<Integer> itInt = layer3.val.iterator();
		counter = 0;
		while (itInt.hasNext()){ counter += itInt.next(); }
		assertEquals(counter, 2*12);
	}
  
}

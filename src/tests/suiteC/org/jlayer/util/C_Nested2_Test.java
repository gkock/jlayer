package org.jlayer.util;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

import java.util.Iterator;

import org.jlayer.model.*;

import basic.units.*;

/**
 * tests layers based on nested units, 
 * specifically the method {@code iterator()}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Nested2_Test {
	
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
		Layer.D1<Unit1.SignalInt,Unit1.SignalInt> layerD1 = new LayerBase.D1<Unit1.SignalInt>(array1);
		int counter;
		
		// initialization
		for (int i = 0; i < sz1; i++) {
			// signal
			layerD1.get(i).setVal(i);
		}
		
		// layerD1.iterator()
		Iterator<Unit1.SignalInt> itUnit1Elem = layerD1.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().val; }
		assertEquals(counter, 45);
	}
	
	@Test
	public void testLayerD2() {
		Layer.D2<Unit1.SignalInt,Unit1.SignalInt> layerD2 = new LayerBase.D2<Unit1.SignalInt>(array2);
		int counter;
		
		// initialization
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				// signal
				layerD2.get(i, j).setVal(i+j);
			}
		}
		
		// layerD2.iterator()
		Iterator<Unit1.SignalInt> itUnit1Elem = layerD2.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().val; }
		assertEquals(counter, 18);
	}
	
	@Test
	public void testLayerD3() {
		Layer.D3<Unit1.SignalInt,Unit1.SignalInt> layerD3 = new LayerBase.D3<Unit1.SignalInt>(array3);
		int counter;
		
		// initialization
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					// signal
					layerD3.get(i, j, k).setVal(i+j+k);
				}
			}
		}
		
		// layerD3.iterator()
		Iterator<Unit1.SignalInt> itUnit1Elem = layerD3.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().val; }
		assertEquals(counter, 12);
	}
  
	@Test
	public void testLayer1dim() {
		Layer<Unit1.SignalInt,Unit1.SignalInt> layer1 = new LayerBase<Unit1.SignalInt>(array1);
		int counter;
		
		// initialization
		int[] ix = new int[1];
		for (int i = 0; i < sz1; i++) {
			ix[0] = i;
			// signal
			layer1.get(ix).setVal(2*i);
		}
		
		// layer1.iterator()
		Iterator<Unit1.SignalInt> itUnit1Elem = layer1.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().val; }
		assertEquals(counter, 2*45);
	}
	
	@Test
	public void testLayer2dim() {
		Layer<Unit1.SignalInt,Unit1.SignalInt> layer2 = new LayerBase<Unit1.SignalInt>(array2);
		int counter;
		
		int[] ix = new int[2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				ix[0] = i; ix[1] = j;
				// signal
				layer2.get(ix).setVal(2*(i+j));
			}
		}

		// layer2.iterator()
		Iterator<Unit1.SignalInt> itUnit1Elem = layer2.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().val; }
		assertEquals(counter, 2*18);
	}
	
	@Test
	public void testLayer3dim() {
		Layer<Unit1.SignalInt,Unit1.SignalInt> layer3 = new LayerBase<Unit1.SignalInt>(array3);
		int counter;
		
		int[] ix = new int[3];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					ix[0] = i; ix[1] = j; ix[2] = k;
					// signal
					layer3.get(ix).setVal(2*(i+j+k));
				}
			}
		}
		
		// layer3.iterator()
		Iterator<Unit1.SignalInt> itUnit1Elem = layer3.iterator();
		counter = 0;
		while (itUnit1Elem.hasNext()){ counter += itUnit1Elem.next().val; }
		assertEquals(counter, 2*12);
	}
  
}

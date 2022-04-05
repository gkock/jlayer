package org.jlayer.util;

import java.util.Iterator;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

import org.jlayer.model.*;

import basic.units.*;

/**
 * tests the generation of layer methods.
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Method_Test {
	
	final int sz1 = 10;
	final int sz2 = 3;
	final int sz3 = 2;
	
	Integer[] intArr1;
	Integer[][] intArr2;
	Integer[][][] intArr3;
	Unit1[] unit1Arr1;
	Unit1[][] unit1Arr2;
	Unit1[][][] unit1Arr3;
	Unit2[] unit2Arr1;
	Unit2[][] unit2Arr2;
	Unit2[][][] unit2Arr3;
	
	
	@BeforeClass
    public void beforeClass() {
		intArr1 = new Integer[sz1];
		unit1Arr1 = new Unit1[sz1];
		unit2Arr1 = new Unit2[sz1];
		for (int i = 0; i < sz1; i++) {	
			intArr1[i] = i; 
			unit1Arr1[i] = new Unit1(4711); 
			unit2Arr1[i] = new Unit2(i); 
		}
		intArr2 = new Integer[sz2][sz2];
		unit1Arr2 = new Unit1[sz2][sz2];
		unit2Arr2 = new Unit2[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				intArr2[i][j] = i+j; 
				unit1Arr2[i][j] = new Unit1(4711); 
				unit2Arr2[i][j] = new Unit2(i+j); 
			}
		}
		intArr3 = new Integer[sz3][sz3][sz3];
		unit1Arr3 = new Unit1[sz3][sz3][sz3];
		unit2Arr3 = new Unit2[sz3][sz3][sz3];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					intArr3[i][j][k] = i+j+k; 
					unit1Arr3[i][j][k] = new Unit1(4711); 
					unit2Arr3[i][j][k] = new Unit2(i+j+k); 
				}
			}
		}
	}
	
	@Test
	public void testSimpleMethod() {
		Layer_Unit1_ layerD1 = new Layer_Unit1_(unit1Arr1);
		Layer_Unit1_ layerD2 = new Layer_Unit1_(unit1Arr2);
		Layer_Unit1_ layerD3 = new Layer_Unit1_(unit1Arr3);
		Unit1.cntUnit1 = 0;
		layerD1.simpleMethod();
		assertEquals(Unit1.cntUnit1, sz1);
		Unit1.cntUnit1 = 0;
		layerD2.simpleMethod();
		assertEquals(Unit1.cntUnit1, sz2*sz2);
		Unit1.cntUnit1 = 0;
		layerD3.simpleMethod();
		assertEquals(Unit1.cntUnit1, sz3*sz3*sz3);
		
	}
	
	@Test
	public void testSetIntItem_1() {
		Layer_Unit1_ layerD1 = new Layer_Unit1_(unit1Arr1);
		Layer_Unit1_ layerD2 = new Layer_Unit1_(unit1Arr2);
		Layer_Unit1_ layerD3 = new Layer_Unit1_(unit1Arr3);
		layerD1.setIntItem_1(2);
		layerD2.setIntItem_1(2);
		layerD3.setIntItem_1(2);
		Layer<Integer,Integer> ilayerD1 = layerD1.returnIntItem();
		Layer<Integer,Integer> ilayerD2 = layerD2.returnIntItem();
		Layer<Integer,Integer> ilayerD3 = layerD3.returnIntItem();
		Iterator<Integer> iterD1 = ilayerD1.iterator();
		Iterator<Integer> iterD2 = ilayerD2.iterator();
		Iterator<Integer> iterD3 = ilayerD3.iterator();
		int counter = 0;
		while (iterD1.hasNext()){ counter += iterD1.next(); }
		assertEquals(counter, 2*sz1);
		counter = 0;
		while (iterD2.hasNext()){ counter += iterD2.next(); }
		assertEquals(counter, 2*sz2*sz2);
		counter = 0;
		while (iterD3.hasNext()){ counter += iterD3.next(); }
		assertEquals(counter, 2*sz3*sz3*sz3);
	}
	
	@Test
	public void testSetIntItem_2() {
		Layer_Unit1_ layerD1 = new Layer_Unit1_(unit1Arr1);
		Layer_Unit1_ layerD2 = new Layer_Unit1_(unit1Arr2);
		Layer_Unit1_ layerD3 = new Layer_Unit1_(unit1Arr3);
		Layer<Integer,Integer> ilayerD1 = new LayerBase<Integer>(intArr1);
		Layer<Integer,Integer> ilayerD2 = new LayerBase<Integer>(intArr2);
		Layer<Integer,Integer> ilayerD3 = new LayerBase<Integer>(intArr3);
		layerD1.setIntItem_2(ilayerD1);
		layerD2.setIntItem_2(ilayerD2);
		layerD3.setIntItem_2(ilayerD3);
		Iterator<Integer> iterD1 = layerD1.intItem.iterator();
		Iterator<Integer> iterD2 = layerD2.intItem.iterator();
		Iterator<Integer> iterD3 = layerD3.intItem.iterator();
		int counter = 0;
		while (iterD1.hasNext()){ counter += iterD1.next(); }
		assertEquals(counter, 45);
		counter = 0;
		while (iterD2.hasNext()){ counter += iterD2.next(); }
		assertEquals(counter, 18);
		counter = 0;
		while (iterD3.hasNext()){ counter += iterD3.next(); }
		assertEquals(counter, 12);
	}
	
	@Test
	public void testSetUnit2Item_1() {
		Layer_Unit1_ layerD1 = new Layer_Unit1_(unit1Arr1);
		Layer_Unit1_ layerD2 = new Layer_Unit1_(unit1Arr2);
		Layer_Unit1_ layerD3 = new Layer_Unit1_(unit1Arr3);
		Unit2 u = new Unit2(2);
		layerD1.setUnit2Item_1(u);
		layerD2.setUnit2Item_1(u);
		layerD3.setUnit2Item_1(u);
		ILayer_Unit2_ ulayerD1 = layerD1.returnUnit2Item();
		ILayer_Unit2_ ulayerD2 = layerD2.returnUnit2Item();
		ILayer_Unit2_ ulayerD3 = layerD3.returnUnit2Item();
		Iterator<Unit2> iterD1 = ulayerD1.iterator();
		Iterator<Unit2> iterD2 = ulayerD2.iterator();
		Iterator<Unit2> iterD3 = ulayerD3.iterator();
		int counter = 0;
		while (iterD1.hasNext()){ counter += iterD1.next().noItem; }
		assertEquals(counter, 2*sz1);
		counter = 0;
		while (iterD2.hasNext()){ counter += iterD2.next().noItem; }
		assertEquals(counter, 2*sz2*sz2);
		counter = 0;
		while (iterD3.hasNext()){ counter += iterD3.next().noItem; }
		assertEquals(counter, 2*sz3*sz3*sz3);
	}
	
	@Test
	public void testSetUnit2Item_2() {
		Layer_Unit1_ layerD1 = new Layer_Unit1_(unit1Arr1);
		Layer_Unit1_ layerD2 = new Layer_Unit1_(unit1Arr2);
		Layer_Unit1_ layerD3 = new Layer_Unit1_(unit1Arr3);
		Layer_Unit2_ ulayerD1 = new Layer_Unit2_(unit2Arr1);
		Layer_Unit2_ ulayerD2 = new Layer_Unit2_(unit2Arr2);
		Layer_Unit2_ ulayerD3 = new Layer_Unit2_(unit2Arr3);
		layerD1.setUnit2Item_2(ulayerD1);
		layerD2.setUnit2Item_2(ulayerD2);
		layerD3.setUnit2Item_2(ulayerD3);
		Iterator<Unit2> iterD1 = layerD1.unit2Item.iterator();
		Iterator<Unit2> iterD2 = layerD2.unit2Item.iterator();
		Iterator<Unit2> iterD3 = layerD3.unit2Item.iterator();
		int counter = 0;
		while (iterD1.hasNext()){ counter += iterD1.next().noItem; }
		assertEquals(counter, 45);
		counter = 0;
		while (iterD2.hasNext()){ counter += iterD2.next().noItem; }
		assertEquals(counter, 18);
		counter = 0;
		while (iterD3.hasNext()){ counter += iterD3.next().noItem; }
		assertEquals(counter, 12);
	}
	
	@Test
	public void testSetBothByInt_1() {
		Layer_Unit1_ layerUnit1;
		Layer<Unit2,Unit2> layerUnit2; 
		Layer<Integer,Integer> layerInt;
		Iterator<Unit2> iterUnit2;
		Iterator<Integer> iterInt;
		int counter;
		// dimension == 1
		layerUnit1 = new Layer_Unit1_(unit1Arr1);
		layerUnit1.setBothByInt_1(1, 2);
		layerInt = layerUnit1.returnNoItem();
		iterInt = layerInt.iterator();
		counter = 0;
		while (iterInt.hasNext()){ counter += iterInt.next(); }
		assertEquals(counter, sz1);
		layerUnit2 = layerUnit1.returnUnit2Item();
		iterUnit2 = layerUnit2.iterator();
		counter = 0;
		while (iterUnit2.hasNext()){ counter += iterUnit2.next().noItem; }
		assertEquals(counter, 2*sz1);
		// dimension == 2
		layerUnit1 = new Layer_Unit1_(unit1Arr2);
		layerUnit1.setBothByInt_1(1, 2);
		layerInt = layerUnit1.returnNoItem();
		iterInt = layerInt.iterator();
		counter = 0;
		while (iterInt.hasNext()){ counter += iterInt.next(); }
		assertEquals(counter, sz2*sz2);
		layerUnit2 = layerUnit1.returnUnit2Item();
		iterUnit2 = layerUnit2.iterator();
		counter = 0;
		while (iterUnit2.hasNext()){ counter += iterUnit2.next().noItem; }
		assertEquals(counter, 2*sz2*sz2);
		// dimension == 3
		layerUnit1 = new Layer_Unit1_(unit1Arr3);
		layerUnit1.setBothByInt_1(1, 2);
		layerInt = layerUnit1.returnNoItem();
		iterInt = layerInt.iterator();
		counter = 0;
		while (iterInt.hasNext()){ counter += iterInt.next(); }
		assertEquals(counter, sz3*sz3*sz3);
		layerUnit2 = layerUnit1.returnUnit2Item();
		iterUnit2 = layerUnit2.iterator();
		counter = 0;
		while (iterUnit2.hasNext()){ counter += iterUnit2.next().noItem; }
		assertEquals(counter, 2*sz3*sz3*sz3);
	}
	
	@Test
	public void testSetBothByInt_2() {
		Layer_Unit1_ layerUnit1;
		Layer<Unit2,Unit2> layerUnit2; 
		Layer<Integer,Integer> layerInt;
		Iterator<Unit2> iterUnit2;
		Iterator<Integer> iterInt;
		int counter;
		// dimension == 1
		layerUnit1 = new Layer_Unit1_(unit1Arr1);
		int[][] parArr1;
		Layer<?,int[]> parLayer1;
		parArr1 = new int[sz1][2];
		for (int i = 0; i < sz1; i++){
			parArr1[i][0] = i;
			parArr1[i][1] = 2*i;
		}
		parLayer1 = new LayerBase<int[]>(parArr1);
		layerUnit1.setBothByInt_2(parLayer1);
		layerInt = layerUnit1.returnNoItem();
		iterInt = layerInt.iterator();
		counter = 0;
		while (iterInt.hasNext()){ counter += iterInt.next(); }
		assertEquals(counter, 45);
		layerUnit2 = layerUnit1.returnUnit2Item();
		iterUnit2 = layerUnit2.iterator();
		counter = 0;
		while (iterUnit2.hasNext()){ counter += iterUnit2.next().noItem; }
		assertEquals(counter, 2*45);
		// dimension == 2
		layerUnit1 = new Layer_Unit1_(unit1Arr2);
		int[][][] parArr2;
		Layer<?,int[]> parLayer2;
		parArr2 = new int[sz2][sz2][2];
		for (int i = 0; i < sz2; i++){
			for (int j = 0; j < sz2; j++){
				parArr2[i][j][0] = i+j;
				parArr2[i][j][1] = 2*i+2*j;
			}
		}
		parLayer2 = new LayerBase<int[]>(parArr2);
		layerUnit1.setBothByInt_2(parLayer2);
		layerInt = layerUnit1.returnNoItem();
		iterInt = layerInt.iterator();
		counter = 0;
		while (iterInt.hasNext()){ counter += iterInt.next(); }
		assertEquals(counter, 18);
		layerUnit2 = layerUnit1.returnUnit2Item();
		iterUnit2 = layerUnit2.iterator();
		counter = 0;
		while (iterUnit2.hasNext()){ counter += iterUnit2.next().noItem; }
		assertEquals(counter, 2*18);
		// dimension == 3
		layerUnit1 = new Layer_Unit1_(unit1Arr3);
		int[][][][] parArr3;
		Layer<?,int[]> parLayer3;
		parArr3 = new int[sz3][sz3][sz3][2];
		for (int i = 0; i < sz3; i++){
			for (int j = 0; j < sz3; j++){
				for (int k = 0; k < sz3; k++){
					parArr3[i][j][k][0] = i+j+k;
					parArr3[i][j][k][1] = 2*i+2*j+2*k;
				}
			}
		}
		parLayer3 = new LayerBase<int[]>(parArr3);
		layerUnit1.setBothByInt_2(parLayer3);
		layerInt = layerUnit1.returnNoItem();
		iterInt = layerInt.iterator();
		counter = 0;
		while (iterInt.hasNext()){ counter += iterInt.next(); }
		assertEquals(counter, 12);
		layerUnit2 = layerUnit1.returnUnit2Item();
		iterUnit2 = layerUnit2.iterator();
		counter = 0;
		while (iterUnit2.hasNext()){ counter += iterUnit2.next().noItem; }
		assertEquals(counter, 2*12);
	}
	
	@Test
	public void testSetBothByUnit2_1() {
		Layer_Unit1_ layerUnit1;
		Layer<Unit2,Unit2> layerUnit2; 
		Layer<Integer,Integer> layerInt;
		Iterator<Unit2> iterUnit2;
		Iterator<Integer> iterInt;
		Unit2 u1 = new Unit2(1);
		Unit2 u2 = new Unit2(2);
		int counter;
		// dimension == 1
		layerUnit1 = new Layer_Unit1_(unit1Arr1);
		layerUnit1.setBothByUnit2_1(u1, u2);
		layerInt = layerUnit1.returnNoItem();
		iterInt = layerInt.iterator();
		counter = 0;
		while (iterInt.hasNext()){ counter += iterInt.next(); }
		assertEquals(counter, sz1);
		layerUnit2 = layerUnit1.returnUnit2Item();
		iterUnit2 = layerUnit2.iterator();
		counter = 0;
		while (iterUnit2.hasNext()){ counter += iterUnit2.next().noItem; }
		assertEquals(counter, 2*sz1);
		// dimension == 2
		layerUnit1 = new Layer_Unit1_(unit1Arr2);
		layerUnit1.setBothByUnit2_1(u1, u2);
		layerInt = layerUnit1.returnNoItem();
		iterInt = layerInt.iterator();
		counter = 0;
		while (iterInt.hasNext()){ counter += iterInt.next(); }
		assertEquals(counter, sz2*sz2);
		layerUnit2 = layerUnit1.returnUnit2Item();
		iterUnit2 = layerUnit2.iterator();
		counter = 0;
		while (iterUnit2.hasNext()){ counter += iterUnit2.next().noItem; }
		assertEquals(counter, 2*sz2*sz2);
		// dimension == 3
		layerUnit1 = new Layer_Unit1_(unit1Arr3);
		layerUnit1.setBothByUnit2_1(u1, u2);
		layerInt = layerUnit1.returnNoItem();
		iterInt = layerInt.iterator();
		counter = 0;
		while (iterInt.hasNext()){ counter += iterInt.next(); }
		assertEquals(counter, sz3*sz3*sz3);
		layerUnit2 = layerUnit1.returnUnit2Item();
		iterUnit2 = layerUnit2.iterator();
		counter = 0;
		while (iterUnit2.hasNext()){ counter += iterUnit2.next().noItem; }
		assertEquals(counter, 2*sz3*sz3*sz3);
	}
	
	@Test
	public void testSetBothByUnit2_2() {
		Layer_Unit1_ layerUnit1;
		Layer<Unit2,Unit2> layerUnit2; 
		Layer<Integer,Integer> layerInt;
		Iterator<Unit2> iterUnit2;
		Iterator<Integer> iterInt;
		int counter;
		// dimension == 1
		layerUnit1 = new Layer_Unit1_(unit1Arr1);
		Unit2[][] parArr1;
		VectorLayer<?,Unit2> parLayer1;
		parArr1 = new Unit2[sz1][2];
		for (int i = 0; i < sz1; i++){
			parArr1[i][0] = new Unit2(i);
			parArr1[i][1] = new Unit2(2*i);
		}
		parLayer1 = new VectorLayerBase<Unit2>(parArr1){
			@Override
			protected Unit2 newObject(){ return new Unit2(); }
			@Override
			protected Unit2[] newArray(int len) { return new Unit2[len];}
		};
		layerUnit1.setBothByUnit2_2(parLayer1);
		layerInt = layerUnit1.returnNoItem();
		iterInt = layerInt.iterator();
		counter = 0;
		while (iterInt.hasNext()){ counter += iterInt.next(); }
		assertEquals(counter, 45);
		layerUnit2 = layerUnit1.returnUnit2Item();
		iterUnit2 = layerUnit2.iterator();
		counter = 0;
		while (iterUnit2.hasNext()){ counter += iterUnit2.next().noItem; }
		assertEquals(counter, 2*45);
		// dimension == 2
		layerUnit1 = new Layer_Unit1_(unit1Arr2);
		Unit2[][][] parArr2;
		VectorLayer<?,Unit2> parLayer2;
		parArr2 = new Unit2[sz2][sz2][2];
		for (int i = 0; i < sz2; i++){
			for (int j = 0; j < sz2; j++){
				parArr2[i][j][0] = new Unit2(i+j);
				parArr2[i][j][1] = new Unit2(2*i+2*j);
			}
		}
		parLayer2 = new VectorLayerBase<Unit2>(parArr2){
			@Override
			protected Unit2 newObject(){ return new Unit2(); }
			@Override
			protected Unit2[] newArray(int len) { return new Unit2[len];}
		};
		layerUnit1.setBothByUnit2_2(parLayer2);
		layerInt = layerUnit1.returnNoItem();
		iterInt = layerInt.iterator();
		counter = 0;
		while (iterInt.hasNext()){ counter += iterInt.next(); }
		assertEquals(counter, 18);
		layerUnit2 = layerUnit1.returnUnit2Item();
		iterUnit2 = layerUnit2.iterator();
		counter = 0;
		while (iterUnit2.hasNext()){ counter += iterUnit2.next().noItem; }
		assertEquals(counter, 2*18);
		// dimension == 3
		layerUnit1 = new Layer_Unit1_(unit1Arr3);
		Unit2[][][][] parArr3;
		VectorLayer<?,Unit2> parLayer3;
		parArr3 = new Unit2[sz3][sz3][sz3][2];
		for (int i = 0; i < sz3; i++){
			for (int j = 0; j < sz3; j++){
				for (int k = 0; k < sz3; k++){
					parArr3[i][j][k][0] = new Unit2(i+j+k);
					parArr3[i][j][k][1] = new Unit2(2*i+2*j+2*k);
				}
			}
		}
		parLayer3 = new VectorLayerBase<Unit2>(parArr3){
			@Override
			protected Unit2 newObject(){ return new Unit2(); }
			@Override
			protected Unit2[] newArray(int len) { return new Unit2[len];}
		};
		layerUnit1.setBothByUnit2_2(parLayer3);
		layerInt = layerUnit1.returnNoItem();
		iterInt = layerInt.iterator();
		counter = 0;
		while (iterInt.hasNext()){ counter += iterInt.next(); }
		assertEquals(counter, 12);
		layerUnit2 = layerUnit1.returnUnit2Item();
		iterUnit2 = layerUnit2.iterator();
		counter = 0;
		while (iterUnit2.hasNext()){ counter += iterUnit2.next().noItem; }
		assertEquals(counter, 2*12);
	}
  
}

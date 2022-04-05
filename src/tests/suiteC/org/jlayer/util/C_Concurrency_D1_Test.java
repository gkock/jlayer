package org.jlayer.util;

import org.jlayer.model.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

import java.util.Iterator;
import java.util.Random;

import basic.units.*;

/**
 * testing concurrency for dimensionality D1
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Concurrency_D1_Test {
  
	final int sz1 = 1001;
//	final int sz1 = 10; // for test purposes
	final int no  = 27;
	
	boolean[][] random1 = new boolean[sz1][sz1];
	int         trueno1 = 0;
	
	C_Concurrency_D1_Test() {
		Random generator = new Random(System.nanoTime());
		for (int x = 0; x < sz1; x++){
			for (int y = 0; y < sz1; y++){
				if (generator.nextBoolean()) {
					random1[x][y] = true;
					trueno1++;
				} else {
					random1[x][y] = false;
				}
				
			}
		}
	}
	
	Relation rand = new IndexTools.D1D1(){
		@Override
		public boolean contains(int x, int y){ return random1[x][y]; }
	};
	Relation equality = new IndexTools.D1D1(){  // for test purposes
		@Override
		public boolean contains(int x, int y){ return (x==y); }
	};
	
	Unit3[] array1;
	Layer_Unit3_ layer1;
	
	int check_void_fork = 0;
	int check_void_sequ = 0;
	int check_voidInt_fork = 0;
	int check_voidInt_sequ = 0;
	int check_voidLPInt_fork = 0;
	int check_voidLPInt_sequ = 0;
	int check_voidUnit2LPUnit2_fork = 0;
	int check_voidUnit2LPUnit2_sequ = 0;
	
	int check_int_fork = 0;
	int check_int_sequ = 0;
	int check_intInt_fork = 0;
	int check_intInt_sequ = 0;
	int check_intLPInt_fork = 0;
	int check_intLPInt_sequ = 0;
	int check_intUnit2LPUnit2_fork = 0;
	int check_intUnit2LPUnit2_sequ = 0;
	
	int check_voidIntDot_fork = 0;
	int check_voidIntDot_sequ = 0;
	int check_voidLPIntDot_fork = 0;
	int check_voidLPIntDot_sequ = 0;
	int check_voidUnit2Dot_fork = 0;
	int check_voidUnit2Dot_sequ = 0;
	int check_voidLPUnit2Dot_fork = 0;
	int check_voidLPUnit2Dot_sequ = 0;
	
	@BeforeClass
    public void beforeClass() {
		array1 = new Unit3[sz1];
		for (int i = 0; i < sz1; i++) {	
			array1[i] = new Unit3(i); 
		}
		layer1 = new Layer_Unit3_(array1);
		XIterator<Unit3> xIter1 = layer1.xIterator();
		while (xIter1.hasNext()) {
			int x1 = xIter1.getXX()[0];
			xIter1.next().x1 = x1;
		}
		layer1.unit3Vector.connect(layer1, rand);
//		layer1.unit3Vector.connect(layer1, equality);  // for test purposes
	}
	
	/**********************************************************************************/
	/**    void(), void(int), void(@LayerParam int), void(Unit2, @LayerParam Unit2)  **/
	/**********************************************************************************/
	
	// void()
	
	@Test
	public void voidMethod_fork() {
		layer1.voidMethod_fork();
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_void_fork += iter1.next().cnt;
		}
	}
	
	@Test
	public void voidMethod_sequ() {
		layer1.voidMethod_sequ();
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_void_sequ += iter1.next().cnt;
		}
	}
	
	// void(int)
	
	@Test
	public void voidInt_fork() {
		layer1.voidInt_fork(no);
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_voidInt_fork += iter1.next().cnt;
		}
	}
	
	@Test
	public void voidInt_sequ() {
		layer1.voidInt_sequ(no);
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_voidInt_sequ += iter1.next().cnt;
		}
	}	
	
	// void(@LayerParam int)
	
	@Test
	public void voidLPInt_fork() {
		Integer[] array = new Integer[sz1];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		BasedLayer<Integer> lint = new LayerBase<Integer>(array);
		layer1.voidLPInt_fork(lint);
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_voidLPInt_fork += iter1.next().cnt;
		}
	}
	
	@Test
	public void voidLPInt_sequ() {
		Integer[] array = new Integer[sz1];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		BasedLayer<Integer> lint = new LayerBase<Integer>(array);
		layer1.voidLPInt_sequ(lint);
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_voidLPInt_sequ += iter1.next().cnt;
		}
	}	
	
	// void(Unit2, @LayerParam Unit2)
	
	@Test
	public void  voidUnit2LPUnit2_fork() {
		Unit2 unit2 = new Unit2(no);
		Unit2[] array = new Unit2[sz1];
		for (int i = 0; i < array.length; i++) {
			array[i] = new Unit2(i);
		}
		BasedLayer<Unit2> lunit2 = new LayerBase<Unit2>(array);
		layer1.voidUnit2LPUnit2_fork(unit2, lunit2);
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_voidUnit2LPUnit2_fork += iter1.next().cnt;
		}
	}
	
	@Test
	public void  voidUnit2LPUnit2_sequ() {
		Unit2 unit2 = new Unit2(no);
		Unit2[] array = new Unit2[sz1];
		for (int i = 0; i < array.length; i++) {
			array[i] = new Unit2(i);
		}
		BasedLayer<Unit2> lunit2 = new LayerBase<Unit2>(array);
		layer1.voidUnit2LPUnit2_sequ(unit2, lunit2);
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_voidUnit2LPUnit2_sequ += iter1.next().cnt;
		}
	}
	
	/**********************************************************************************/
	/**    int(), int(int), int(@LayerParam int), Unit2(Unit2, @LayerParam Unit2)    **/
	/**********************************************************************************/
	
	// int()
	
	@Test
	public void  intMethod_fork() {
		BasedLayer<Integer> res = layer1.intMethod_fork();
		Iterator<Integer> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_int_fork += iter1.next();
		}
	}
	
	@Test
	public void  intMethod_sequ() {
		BasedLayer<Integer> res = layer1.intMethod_sequ();
		Iterator<Integer> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_int_sequ += iter1.next();
		}
	}
	
	// int(int)
	
	@Test
	public void intInt_fork() {
		BasedLayer<Integer> res = layer1.intInt_fork(no);
		Iterator<Integer> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intInt_fork += iter1.next();
		}
	}
	
	@Test
	public void intInt_sequ() {
		BasedLayer<Integer> res = layer1.intInt_sequ(no);
		Iterator<Integer> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intInt_sequ += iter1.next();
		}
	}
	
	// int(@LayerParam int)  // AKTUELLE BAUSTELLE
	
	@Test
	public void intLPInt_fork() {
		Integer[] array = new Integer[sz1];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		BasedLayer<Integer> lint = new LayerBase<Integer>(array);
		BasedLayer<Integer> res = layer1.intLPInt_fork(lint);
		Iterator<Integer> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intLPInt_fork += iter1.next();
		}
	}
	
	@Test
	public void intLPInt_sequ() {
		Integer[] array = new Integer[sz1];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		BasedLayer<Integer> lint = new LayerBase<Integer>(array);
		BasedLayer<Integer> res = layer1.intLPInt_sequ(lint);
		Iterator<Integer> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intLPInt_sequ += iter1.next();
		}
	}	
	
	// Unit2(Unit2, @LayerParam Unit2)
	
	@Test
	public void  unit2Unit2LPUnit2_fork() {
		Unit2 unit2 = new Unit2(no);
		Unit2[] array = new Unit2[sz1];
		for (int i = 0; i < array.length; i++) {
			array[i] = new Unit2(i);
		}
		BasedLayer<Unit2> lunit2 = new LayerBase<Unit2>(array);
		BasedLayer<Unit2> res = layer1.unit2Unit2LPUnit2_fork(unit2, lunit2);
		Iterator<Unit2> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intUnit2LPUnit2_fork += iter1.next().noItem;
		}
	}
	
	@Test
	public void  unit2Unit2LPUnit2_sequ() {
		Unit2 unit2 = new Unit2(no);
		Unit2[] array = new Unit2[sz1];
		for (int i = 0; i < array.length; i++) {
			array[i] = new Unit2(i);
		}
		BasedLayer<Unit2> lunit2 = new LayerBase<Unit2>(array);
		BasedLayer<Unit2> res = layer1.unit2Unit2LPUnnit2_sequ(unit2, lunit2);
		Iterator<Unit2> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intUnit2LPUnit2_sequ += iter1.next().noItem;
		}
	}
	
	/*******************************************************************************************/
	/**   void(int...), void(@LayerParam int...), void(Unit2...), void(@LayerParam Unit2...)  **/
	/*******************************************************************************************/
	
	// void(int...)
	
	@Test
	public void voidIntDot_fork() {
		layer1.voidIntDot_fork(no, no);
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_voidIntDot_fork += iter1.next().cnt;
		}
	}
	
	@Test
	public void voidIntDot_sequ() {
		layer1.voidIntDot_sequ(no, no);
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_voidIntDot_sequ += iter1.next().cnt;
		}
	}	
	
	// void(@LayerParam int...)
	
	@Test
	public void voidLPIntDot_fork() {
		int[][] array = new int[sz1][];
		for (int i = 0; i < array.length; i++) {
			array[i] = new int[]{i, i};
		}
		BasedLayer<int[]> lint  = new LayerBase<int[]>(array);
		layer1.voidLPIntDot_fork(lint);
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_voidLPIntDot_fork += iter1.next().cnt;
		}
	}
	
	@Test
	public void voidLPIntDot_sequ() {
		int[][] array = new int[sz1][];
		for (int i = 0; i < array.length; i++) {
			array[i] = new int[]{i, i};
		}
		BasedLayer<int[]> lint  = new LayerBase<int[]>(array);
		layer1.voidLPIntDot_sequ(lint);
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_voidLPIntDot_sequ += iter1.next().cnt;
		}
	}	
		
	// void(Unit2...)
	
	@Test
	public void  voidUnit2Dot_fork() {
		Unit2 unit21 = new Unit2(no);
		Unit2 unit22 = new Unit2(no);
		layer1.voidUnit2Dot_fork(unit21, unit22);
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_voidUnit2Dot_fork += iter1.next().cnt;
		}
	}
	
	@Test
	public void  voidUnit2Dot_sequ() {
		Unit2 unit21 = new Unit2(no);
		Unit2 unit22 = new Unit2(no);
		layer1.voidUnit2Dot_sequ(unit21, unit22);
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_voidUnit2Dot_sequ += iter1.next().cnt;
		}
	}
		
	// void(@LayerParam Unit2...)
	
	public void voidLPUnit2Dot_fork() {
		Unit2[][] array = new Unit2[sz1][];
		for (int i = 0; i < array.length; i++) {
			array[i] = new Unit2[]{new Unit2(i), new Unit2(i)};
		}
		VectorLayer<?,Unit2> vlayer = new VectorLayerBase<Unit2>(array){
			@Override
			protected Unit2 newObject(){ return new Unit2(); }
			@Override
			protected Unit2[] newArray(int len) { return new Unit2[len];}
		};
		layer1.voidLPUnit2Dot_fork(vlayer);
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_voidLPUnit2Dot_fork += iter1.next().cnt;
		}
	}	
	
	public void voidLPUnit2Dot_sequ() {
		Unit2[][] array = new Unit2[sz1][];
		for (int i = 0; i < array.length; i++) {
			array[i] = new Unit2[]{new Unit2(i), new Unit2(i)};
		}
		VectorLayer<?,Unit2> vlayer = new VectorLayerBase<Unit2>(array){
			@Override
			protected Unit2 newObject(){ return new Unit2(); }
			@Override
			protected Unit2[] newArray(int len) { return new Unit2[len];}
		};
		layer1.voidLPUnit2Dot_sequ(vlayer);
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			check_voidLPUnit2Dot_sequ += iter1.next().cnt;
		}
	}	
	
	@AfterClass
    public void afterClass() {
		int checksum = check_void_fork;
		// TEST OUTPUT
//		System.out.printf("D1: checksum = %d%n", checksum);
		
		assertEquals(check_void_fork, checksum);
		assertEquals(check_void_sequ, checksum);
		assertEquals(check_voidInt_fork, checksum + sz1*no);
		assertEquals(check_voidInt_sequ, checksum + sz1*no);
		assertEquals(check_voidLPInt_fork, checksum + (sz1-1)*sz1/2);
		assertEquals(check_voidLPInt_sequ, checksum + (sz1-1)*sz1/2);
		assertEquals(check_voidUnit2LPUnit2_fork, checksum + (sz1-1)*sz1/2 + sz1*no);
		assertEquals(check_voidUnit2LPUnit2_sequ, checksum + (sz1-1)*sz1/2 + sz1*no);
		
		assertEquals(check_int_fork, checksum);
		assertEquals(check_int_sequ, checksum);
		assertEquals(check_intInt_fork, checksum + sz1*no);
		assertEquals(check_intInt_sequ, checksum + sz1*no);
		assertEquals(check_intLPInt_fork, checksum + (sz1-1)*sz1/2);
		assertEquals(check_intLPInt_sequ, checksum + (sz1-1)*sz1/2);
		assertEquals(check_intUnit2LPUnit2_fork, checksum + (sz1-1)*sz1/2 + sz1*no);
		assertEquals(check_intUnit2LPUnit2_sequ, checksum + (sz1-1)*sz1/2 + sz1*no);
		
		assertEquals(check_voidIntDot_fork, checksum + 2*sz1*no);
		assertEquals(check_voidIntDot_sequ, checksum + 2*sz1*no);
		assertEquals(check_voidLPIntDot_fork, checksum + (sz1-1)*sz1);
		assertEquals(check_voidLPIntDot_sequ, checksum + (sz1-1)*sz1);
		assertEquals(check_voidUnit2Dot_fork, checksum + 2*sz1*no);
		assertEquals(check_voidUnit2Dot_sequ, checksum + 2*sz1*no);
		assertEquals(check_voidLPUnit2Dot_fork, checksum + (sz1-1)*sz1);
		assertEquals(check_voidLPUnit2Dot_sequ, checksum + (sz1-1)*sz1);
		
	}
  
}

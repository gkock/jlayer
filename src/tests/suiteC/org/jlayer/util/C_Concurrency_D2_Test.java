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
 * testing concurrency for dimensionality D2
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Concurrency_D2_Test {
  
	final int sz2 = 30;
//	final int sz2 = 10; // for test purposes
	final int no  = 27;
	
	boolean[][][][] random1 = new boolean[sz2][sz2][sz2][sz2];
	int             trueno1 = 0;
	
	C_Concurrency_D2_Test() {
		Random generator = new Random(System.nanoTime());
		for (int x1 = 0; x1 < sz2; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int y1 = 0; y1 < sz2; y1++){
					for (int y2 = 0; y2 < sz2; y2++){
						if (generator.nextBoolean()) {
							random1[x1][x2][y1][y2] = true;
							trueno1++;
						} else {
							random1[x1][x2][y1][y2] = false;
						}
					}
				}
			}
		}
	}
	
	Relation rand = new IndexTools.D2D2(){
		@Override
		public boolean contains(int x1, int x2, int y1, int y2){ 
			return random1[x1][x2][y1][y2]; 
		}
	};
	Relation equality = new IndexTools.D2D2(){  // for test purposes
		@Override
		public boolean contains(int x1, int x2, int y1, int y2){ return ( (x1==y1) && (x2==y2) ); }
	};
	
	Unit3[][] array2;
	Layer_Unit3_ layer2;
	
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
	
	@BeforeClass
    public void beforeClass() {
		array2 = new Unit3[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array2[i][j] = new Unit3(i+j); 
			}
		}
		layer2 = new Layer_Unit3_(array2);
		XIterator<Unit3> xIter2 = layer2.xIterator();
		while (xIter2.hasNext()) {
			int x1 = xIter2.getXX()[0];
			int x2 = xIter2.getXX()[1];
			Unit3 unit3 = xIter2.next();
			unit3.x1 = x1;
			unit3.x2 = x2;
		}
		layer2.unit3Vector.connect(layer2, rand);
//		layer2.unit3Vector.connect(layer2, equality);  // for test purposes
	}
	
	/**********************************************************************************/
	/**    void(), void(int), void(@LayerParam int), void(Unit2, @LayerParam Unit2)  **/
	/**********************************************************************************/
	
	// void()
	
	@Test
	public void  voidMethod_fork() {
		layer2.voidMethod_fork();
		Iterator<Unit3> iter2 = layer2.iterator();
		while (iter2.hasNext()) {
			check_void_fork += iter2.next().cnt;
		}
	}
	
	@Test
	public void  voidMethod_sequ() {
		layer2.voidMethod_sequ();
		Iterator<Unit3> iter2 = layer2.iterator();
		while (iter2.hasNext()) {
			check_void_sequ += iter2.next().cnt;
		}
	}
	
	// void(int)
	
	@Test
	public void voidInt_fork() {
		layer2.voidInt_fork(no);
		Iterator<Unit3> iter1 = layer2.iterator();
		while (iter1.hasNext()) {
			check_voidInt_fork += iter1.next().cnt;
		}
	}
	
	@Test
	public void voidInt_sequ() {
		layer2.voidInt_sequ(no);
		Iterator<Unit3> iter1 = layer2.iterator();
		while (iter1.hasNext()) {
			check_voidInt_sequ += iter1.next().cnt;
		}
	}	
	
	// void(@LayerParam int)
	
	@Test
	public void voidLPInt_fork() {
		Integer[][] array = new Integer[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array[i][j] = i+j; 
			}
		}
		BasedLayer<Integer> lint = new LayerBase<Integer>(array);
		layer2.voidLPInt_fork(lint);
		Iterator<Unit3> iter1 = layer2.iterator();
		while (iter1.hasNext()) {
			check_voidLPInt_fork += iter1.next().cnt;
		}
	}
	
	@Test
	public void voidLPInt_sequ() {
		Integer[][] array = new Integer[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array[i][j] = i+j; 
			}
		}
		BasedLayer<Integer> lint = new LayerBase<Integer>(array);
		layer2.voidLPInt_sequ(lint);
		Iterator<Unit3> iter1 = layer2.iterator();
		while (iter1.hasNext()) {
			check_voidLPInt_sequ += iter1.next().cnt;
		}
	}	
	
	// void(Unit2, @LayerParam Unit2)
	
	@Test
	public void  voidUnit2LPUnit2_fork() {
		Unit2 unit2 = new Unit2(no);
		Unit2[][] array = new Unit2[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array[i][j] = new Unit2(i+j);
			}
		}
		BasedLayer<Unit2> lunit2 = new LayerBase<Unit2>(array);
		layer2.voidUnit2LPUnit2_fork(unit2, lunit2);
		Iterator<Unit3> iter = layer2.iterator();
		while (iter.hasNext()) {
			check_voidUnit2LPUnit2_fork += iter.next().cnt;
		}
	}
	
	@Test
	public void  voidUnit2LPUnit2_sequ() {
		Unit2 unit2 = new Unit2(no);
		Unit2[][] array = new Unit2[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array[i][j] = new Unit2(i+j);
			}
		}
		BasedLayer<Unit2> lunit2 = new LayerBase<Unit2>(array);
		layer2.voidUnit2LPUnit2_sequ(unit2, lunit2);
		Iterator<Unit3> iter = layer2.iterator();
		while (iter.hasNext()) {
			check_voidUnit2LPUnit2_sequ += iter.next().cnt;
		}
	}
	
	/**********************************************************************************/
	/**    int(), int(int), int(@LayerParam int), Unit2(Unit2, @LayerParam Unit2)    **/
	/**********************************************************************************/
	
	// int()
	
	@Test
	public void  intMethod_fork() {
		BasedLayer<Integer> res = layer2.intMethod_fork();
		Iterator<Integer> iter2 = res.iterator();
		while (iter2.hasNext()) {
			check_int_fork += iter2.next();
		}
	}
	
	@Test
	public void  intMethod_sequ() {
		BasedLayer<Integer> res = layer2.intMethod_sequ();
		Iterator<Integer> iter2 = res.iterator();
		while (iter2.hasNext()) {
			check_int_sequ += iter2.next();
		}
	}

	// int(int)
	
	@Test
	public void intInt_fork() {
		BasedLayer<Integer> res = layer2.intInt_fork(no);
		Iterator<Integer> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intInt_fork += iter1.next();
		}
	}
	
	@Test
	public void intInt_sequ() {
		BasedLayer<Integer> res = layer2.intInt_sequ(no);
		Iterator<Integer> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intInt_sequ += iter1.next();
		}
	}
	
	// int(@LayerParam int)
	
	@Test
	public void intLPInt_fork() {
		Integer[][] array = new Integer[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array[i][j] = i+j; 
			}
		}
		BasedLayer<Integer> lint = new LayerBase<Integer>(array);
		BasedLayer<Integer> res = layer2.intLPInt_fork(lint);
		Iterator<Integer> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intLPInt_fork += iter1.next();
		}
	}
	
	@Test
	public void intLPInt_sequ() {
		Integer[][] array = new Integer[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array[i][j] = i+j; 
			}
		}
		BasedLayer<Integer> lint = new LayerBase<Integer>(array);
		BasedLayer<Integer> res = layer2.intLPInt_sequ(lint);
		Iterator<Integer> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intLPInt_sequ += iter1.next();
		}
	}	
	
	// Unit2(Unit2, @LayerParam Unit2)
	
	@Test
	public void  unit2Unit2LPUnit2_fork() {
		Unit2 unit2 = new Unit2(no);
		Unit2[][] array = new Unit2[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array[i][j] = new Unit2(i+j);
			}
		}
		BasedLayer<Unit2> lunit2 = new LayerBase<Unit2>(array);
		BasedLayer<Unit2> res = layer2.unit2Unit2LPUnit2_fork(unit2, lunit2);
		Iterator<Unit2> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intUnit2LPUnit2_fork += iter1.next().noItem;
		}
	}
	
	@Test
	public void  unit2Unit2LPUnit2_sequ() {
		Unit2 unit2 = new Unit2(no);
		Unit2[][] array = new Unit2[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array[i][j] = new Unit2(i+j);
			}
		}
		BasedLayer<Unit2> lunit2 = new LayerBase<Unit2>(array);
		BasedLayer<Unit2> res = layer2.unit2Unit2LPUnnit2_sequ(unit2, lunit2);
		Iterator<Unit2> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intUnit2LPUnit2_sequ += iter1.next().noItem;
		}
	}
	
	/*******************************************************************************************/
	/**   void(int...), void(@LayerParam int...), void(Unit2...), void(@LayerParam Unit2...)  **/
	/*******************************************************************************************/
	
	// void(int...)
	
	// void(@LayerParam int...)
		
	// void(Unit2...)
		
	// void(@LayerParam Unit2...)
	
	@AfterClass
    public void afterClass() {
		int checksum = check_void_fork;
		// TEST OUTPUT
//		System.out.printf("D1: checksum = %d%n", checksum);
		
		assertEquals(check_void_fork, checksum);
		assertEquals(check_void_sequ, checksum);
		assertEquals(check_voidInt_fork, checksum + sz2*sz2*no);
		assertEquals(check_voidInt_sequ, checksum + sz2*sz2*no);
		assertEquals(check_voidLPInt_fork, checksum + 2*sz2*(sz2-1)*sz2/2);
		assertEquals(check_voidLPInt_sequ, checksum + 2*sz2*(sz2-1)*sz2/2);
		assertEquals(check_voidUnit2LPUnit2_fork, checksum + 2*sz2*(sz2-1)*sz2/2 + sz2*sz2*no);
		assertEquals(check_voidUnit2LPUnit2_sequ, checksum + 2*sz2*(sz2-1)*sz2/2 + sz2*sz2*no);
		
		assertEquals(check_int_fork, checksum);
		assertEquals(check_int_sequ, checksum);
		assertEquals(check_intInt_fork, checksum + sz2*sz2*no);
		assertEquals(check_intInt_sequ, checksum + sz2*sz2*no);
		assertEquals(check_intLPInt_fork, checksum + 2*sz2*(sz2-1)*sz2/2);
		assertEquals(check_intLPInt_sequ, checksum + 2*sz2*(sz2-1)*sz2/2);
		assertEquals(check_intUnit2LPUnit2_fork, checksum + 2*sz2*(sz2-1)*sz2/2 + sz2*sz2*no);
		assertEquals(check_intUnit2LPUnit2_sequ, checksum + 2*sz2*(sz2-1)*sz2/2 + sz2*sz2*no);

	}
  
}

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
 * testing concurrency for dimensionality D3
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Concurrency_D3_Test {
  
	final int sz1 = 10;
//	final int sz1 = 3;  // for test purposes
	final int sz2 = 11;
//	final int sz2 = 3;  // for test purposes
	final int sz3 = 12;
//	final int sz3 = 3; // for test purposes
	final int no  = 27;
	
	boolean[][][][][][] random1 = new boolean[sz1][sz2][sz3][sz1][sz2][sz3];
	int         	    trueno1 = 0;
	
	C_Concurrency_D3_Test() {
		Random generator = new Random(System.nanoTime());
		for (int x1 = 0; x1 < sz1; x1++){
			for (int x2 = 0; x2 < sz2; x2++){
				for (int x3 = 0; x3 < sz3; x3++){
					for (int y1 = 0; y1 < sz1; y1++){
						for (int y2 = 0; y2 < sz2; y2++){
							for (int y3 = 0; y3 < sz3; y3++){
								if (generator.nextBoolean()) {
									random1[x1][x2][x3][y1][y2][y3] = true;
									trueno1++;
								} else {
									random1[x1][x2][x3][y1][y2][y3] = false;
								}
							}
						}
					}
				}
			}
		}
	}
	
	Relation rand = new IndexTools.D3D3(){
		@Override
		public boolean contains(int x1, int x2, int x3, int y1, int y2, int y3){ 
			return random1[x1][x2][x3][y1][y2][y3]; 
		}
	};
	Relation equality = new IndexTools.D3D3(){  // for test purposes
		@Override
		public boolean contains(int x1, int x2, int x3, int y1, int y2, int y3){ 
			return ( (x1==y1) && (x2==y2) && (x3==y3) ); 
		}
	};
	
	Unit3[][][] array3;
	Layer_Unit3_ layer3;
	
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
		array3 = new Unit3[sz1][sz2][sz3];
		for (int i = 0; i < sz1; i++) {	
			for (int j = 0; j < sz2; j++) {	
				for (int k = 0; k < sz3; k++) {	
					array3[i][j][k] = new Unit3(i+j+k); 
				}
			}
		}
		layer3 = new Layer_Unit3_(array3);
		XIterator<Unit3> xIter3 = layer3.xIterator();
		while (xIter3.hasNext()) {
			int x1 = xIter3.getXX()[0];
			int x2 = xIter3.getXX()[1];
			int x3 = xIter3.getXX()[2];
			Unit3 unit3 = xIter3.next();
			unit3.x1 = x1;
			unit3.x2 = x2;
			unit3.x3 = x3;
		}
		layer3.unit3Vector.connect(layer3, rand);
//		layer3.unit3Vector.connect(layer3, equality);  // for test purposes
	}

	/**********************************************************************************/
	/**    void(), void(int), void(@LayerParam int), void(Unit2, @LayerParam Unit2)  **/
	/**********************************************************************************/
	
	// void()
	
	@Test
	public void  voidMethod_fork() {
		layer3.voidMethod_fork();
		Iterator<Unit3>iter3 = layer3.iterator();
		while (iter3.hasNext()) {
			check_void_fork += iter3.next().cnt;
		}
	}
	
	@Test
	public void  voidMethod_sequ() {
		layer3.voidMethod_sequ();
		Iterator<Unit3>iter3 = layer3.iterator();
		while (iter3.hasNext()) {
			check_void_sequ += iter3.next().cnt;
		}
	}

	// void(int)
	
	@Test
	public void voidInt_fork() {
		layer3.voidInt_fork(no);
		Iterator<Unit3> iter1 = layer3.iterator();
		while (iter1.hasNext()) {
			check_voidInt_fork += iter1.next().cnt;
		}
	}
	
	@Test
	public void voidInt_sequ() {
		layer3.voidInt_sequ(no);
		Iterator<Unit3> iter1 = layer3.iterator();
		while (iter1.hasNext()) {
			check_voidInt_sequ += iter1.next().cnt;
		}
	}		
	
	// void(@LayerParam int)
	
	@Test
	public void voidLPInt_fork() {
		Integer[][][] array = new Integer[sz1][sz2][sz3];
		for (int i = 0; i < sz1; i++) {	
			for (int j = 0; j < sz2; j++) {	
				for (int k = 0; k < sz3; k++) {	
					array[i][j][k] = i+j+k; 
				}
			}
		}
		BasedLayer<Integer> lint = new LayerBase<Integer>(array);
		layer3.voidLPInt_fork(lint);
		Iterator<Unit3> iter1 = layer3.iterator();
		while (iter1.hasNext()) {
			check_voidLPInt_fork += iter1.next().cnt;
		}
	}
	
	@Test
	public void voidLPInt_sequ() {
		Integer[][][] array = new Integer[sz1][sz2][sz3];
		for (int i = 0; i < sz1; i++) {	
			for (int j = 0; j < sz2; j++) {	
				for (int k = 0; k < sz3; k++) {	
					array[i][j][k] = i+j+k; 
				}
			}
		}
		BasedLayer<Integer> lint = new LayerBase<Integer>(array);
		layer3.voidLPInt_sequ(lint);
		Iterator<Unit3> iter1 = layer3.iterator();
		while (iter1.hasNext()) {
			check_voidLPInt_sequ += iter1.next().cnt;
		}
	}	
	
	// void(Unit2, @LayerParam Unit2)
	
	@Test
	public void  voidUnit2LPUnit2_fork() {
		Unit2 unit2 = new Unit2(no);
		Unit2[][][] array = new Unit2[sz1][sz2][sz3];
		for (int i = 0; i < sz1; i++) {	
			for (int j = 0; j < sz2; j++) {	
				for (int k = 0; k < sz3; k++) {	
					array[i][j][k] = new Unit2(i+j+k);
				}
			}
		}
		BasedLayer<Unit2> lunit2 = new LayerBase<Unit2>(array);
		layer3.voidUnit2LPUnit2_fork(unit2, lunit2);
		Iterator<Unit3> iter = layer3.iterator();
		while (iter.hasNext()) {
			check_voidUnit2LPUnit2_fork += iter.next().cnt;
		}
	}
	
	@Test
	public void  voidUnit2LPUnit2_sequ() {
		Unit2 unit2 = new Unit2(no);
		Unit2[][][] array = new Unit2[sz1][sz2][sz3];
		for (int i = 0; i < sz1; i++) {	
			for (int j = 0; j < sz2; j++) {	
				for (int k = 0; k < sz3; k++) {	
					array[i][j][k] = new Unit2(i+j+k);
				}
			}
		}
		BasedLayer<Unit2> lunit2 = new LayerBase<Unit2>(array);
		layer3.voidUnit2LPUnit2_sequ(unit2, lunit2);
		Iterator<Unit3> iter = layer3.iterator();
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
		BasedLayer<Integer> res = layer3.intMethod_fork();
		Iterator<Integer> iter3 = res.iterator();
		while (iter3.hasNext()) {
			check_int_fork += iter3.next();
		}
	}
	
	@Test
	public void  intMethod_sequ() {
		BasedLayer<Integer> res = layer3.intMethod_sequ();
		Iterator<Integer> iter3 = res.iterator();
		while (iter3.hasNext()) {
			check_int_sequ += iter3.next();
		}
	}

	// int(int)
	
	@Test
	public void intInt_fork() {
		BasedLayer<Integer> res = layer3.intInt_fork(no);
		Iterator<Integer> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intInt_fork += iter1.next();
		}
	}
	
	@Test
	public void intInt_sequ() {
		BasedLayer<Integer> res = layer3.intInt_sequ(no);
		Iterator<Integer> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intInt_sequ += iter1.next();
		}
	}
	
	// int(@LayerParam int)
	
	@Test
	public void intLPInt_fork() {
		Integer[][][] array = new Integer[sz1][sz2][sz3];
		for (int i = 0; i < sz1; i++) {	
			for (int j = 0; j < sz2; j++) {	
				for (int k = 0; k < sz3; k++) {	
					array[i][j][k] = i+j+k; 
				}
			}
		}
		BasedLayer<Integer> lint = new LayerBase<Integer>(array);
		BasedLayer<Integer> res = layer3.intLPInt_fork(lint);
		Iterator<Integer> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intLPInt_fork += iter1.next();
		}
	}
	
	@Test
	public void intLPInt_sequ() {
		Integer[][][] array = new Integer[sz1][sz2][sz3];
		for (int i = 0; i < sz1; i++) {	
			for (int j = 0; j < sz2; j++) {	
				for (int k = 0; k < sz3; k++) {	
					array[i][j][k] = i+j+k; 
				}
			}
		}
		BasedLayer<Integer> lint = new LayerBase<Integer>(array);
		BasedLayer<Integer> res = layer3.intLPInt_sequ(lint);
		Iterator<Integer> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intLPInt_sequ += iter1.next();
		}
	}	
	
	// void(Unit2, @LayerParam Unit2)
	
	@Test
	public void  unit2Unit2LPUnit2_fork() {
		Unit2 unit2 = new Unit2(no);
		Unit2[][][] array = new Unit2[sz1][sz2][sz3];
		for (int i = 0; i < sz1; i++) {	
			for (int j = 0; j < sz2; j++) {	
				for (int k = 0; k < sz3; k++) {	
					array[i][j][k] = new Unit2(i+j+k);
				}
			}
		}
		BasedLayer<Unit2> lunit2 = new LayerBase<Unit2>(array);
		BasedLayer<Unit2> res = layer3.unit2Unit2LPUnit2_fork(unit2, lunit2);
		Iterator<Unit2> iter1 = res.iterator();
		while (iter1.hasNext()) {
			check_intUnit2LPUnit2_fork += iter1.next().noItem;
		}
	}
	
	@Test
	public void  unit2Unit2LPUnit2_sequ() {
		Unit2 unit2 = new Unit2(no);
		Unit2[][][] array = new Unit2[sz1][sz2][sz3];
		for (int i = 0; i < sz1; i++) {	
			for (int j = 0; j < sz2; j++) {	
				for (int k = 0; k < sz3; k++) {	
					array[i][j][k] = new Unit2(i+j+k);
				}
			}
		}
		BasedLayer<Unit2> lunit2 = new LayerBase<Unit2>(array);
		BasedLayer<Unit2> res = layer3.unit2Unit2LPUnnit2_sequ(unit2, lunit2);
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
		
		final int chk1 = (sz2*sz3)*(sz1-1)*sz1/2;
		final int chk2 = (sz1*sz3)*(sz2-1)*sz2/2;
		final int chk3 = (sz1*sz2)*(sz3-1)*sz3/2;
		
		assertEquals(check_void_fork, checksum);
		assertEquals(check_void_sequ, checksum);
		assertEquals(check_voidInt_fork, checksum + sz1*sz2*sz3*no);
		assertEquals(check_voidInt_sequ, checksum + sz1*sz2*sz3*no);
		assertEquals(check_voidLPInt_fork, checksum + chk1 + chk2 + chk3);
		assertEquals(check_voidLPInt_sequ, checksum + chk1 + chk2 + chk3);
		assertEquals(check_voidUnit2LPUnit2_fork, checksum + chk1 + chk2 + chk3 + sz1*sz2*sz3*no);
		assertEquals(check_voidUnit2LPUnit2_sequ, checksum + chk1 + chk2 + chk3 + sz1*sz2*sz3*no);
		
		assertEquals(check_int_fork, checksum);
		assertEquals(check_int_sequ, checksum);
		assertEquals(check_intInt_fork, checksum + sz1*sz2*sz3*no);
		assertEquals(check_intInt_sequ, checksum + sz1*sz2*sz3*no);
		assertEquals(check_intLPInt_fork, checksum + chk1 + chk2 + chk3);
		assertEquals(check_intLPInt_sequ, checksum + chk1 + chk2 + chk3);
		assertEquals(check_intUnit2LPUnit2_fork, checksum + chk1 + chk2 + chk3 + sz1*sz2*sz3*no);
		assertEquals(check_intUnit2LPUnit2_sequ, checksum + chk1 + chk2 + chk3 + sz1*sz2*sz3*no);

	}
  
}

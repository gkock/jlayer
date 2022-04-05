package sandbox;

import static org.testng.Assert.assertEquals;

import org.jlayer.model.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * tests the code generated from package sandbox
 * 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Sandbox_Test {
	
	Unit1[] testArray1;
	Layer_Unit1_ testLayer1;
	
	Unit2[] testArray2;
	Layer_Unit2_ testLayer2;
	
	Relation full = new Relation() {
		@Override
		public boolean contains(int[] x, int[] y) { 
			return true;
		}
	};
	
	
	@BeforeClass
    public void beforeClass() {
		testArray1 = new Unit1[10];
		for (int i=0; i < 10; i++) {
			testArray1[i] = new Unit1();
			testArray1[i].noItem = i;
		}
		testLayer1 = new Layer_Unit1_(testArray1);
		
		testArray2 = new Unit2[10];
		for (int i=0; i < 10; i++) {
			testArray2[i] = new Unit2();
			testArray2[i].noItem = i;
		}
		testLayer2 = new Layer_Unit2_(testArray2);
		
		testLayer1.signalVector.associate(testLayer2.signalVector, full);
//		testLayer1.unit1Vector.connect(testLayer1.unit1Vector, full);
//		
//		VectorLayer<?, Unit1> vLayer = testLayer1.unit1Vector;
//		vLayer.connect(testLayer1.unit1Vector, full);
//		testLayer1.unit1Vector.connect(vLayer, full);
//		vLayer.connect(vLayer, full);
	}
	
	int sum(Signal[] vector){
		int sum = 0;
		for (int i=0; i < vector.length; i++) {
			sum += vector[i].val;
		}
		return sum;
	}
	
//	@Test
//	public void testUnit1Vector() {
//		for (int i=0; i < 10; i++) {
//			assertEquals(testLayer1.get(i).unit1Vector.length, 10);
//		}
//	}
	
	@Test
	public void testSignalVectors1() {
		testLayer1.setVector(0);
		testLayer2.setVector(0);
		for (int i=0; i < 10; i++) {
			assertEquals(testLayer1.get(i).signalVector.length, 10);
			assertEquals(sum(testLayer1.get(i).signalVector), 0);
			assertEquals(testLayer2.get(i).signalVector.length, 10);
			assertEquals(sum(testLayer2.get(i).signalVector), 0);
		}
	}
	
	@Test
	public void testSignalVectors2() {
		testLayer2.setVector();
		for (int i=0; i < 10; i++) {
			assertEquals(sum(testLayer1.get(i).signalVector), 45);
			assertEquals(sum(testLayer2.get(i).signalVector), 10*i);
		}
	}
	
	@Test
	public void testNoItems() {
		for (int i=0; i < 10; i++) {
			assertEquals(testLayer1.get(i).noItem, i);
			assertEquals(testLayer2.get(i).noItem, i);
		}
	}
	
	@Test
	public void testIntItems() {
		testLayer1.methodItem();
		testLayer2.methodItem();
		for (int i=0; i < 10; i++) {
			assertEquals(testLayer1.get(i).intItem, i);
			assertEquals(testLayer2.get(i).intItem, i);
		}
		assertEquals(true, true);
	}
  
}

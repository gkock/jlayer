package org.jlayer.util;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import org.jlayer.model.*;
import basic.units.*;

/**
 * tests the generated {@code Layer_AbstractUnit_}, 
 * UNDER DEVELOPMENT.
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_AbstractUnit_Test {
	
	final int sz1 = 1000;
	final int sz2 = 200;
	final int sz3 = 40;
	
	AbstractUnit[] array1;
	AbstractUnit[][] array2;
	AbstractUnit[][][] array3;
	Layer_AbstractUnit_ layer1, layer2, layer3;
	
	Integer[] intArr1 = new Integer[sz1];
	Integer[][] intArr2 = new Integer[sz2][sz2];
	Integer[][][] intArr3 = new Integer[sz3][sz3][sz3];
	
	BasedLayer<Integer> intLayer1 = new LayerBase<Integer>(intArr1);
	BasedLayer<Integer> intLayer2 = new LayerBase<Integer>(intArr2);
	BasedLayer<Integer> intLayer3 = new LayerBase<Integer>(intArr3);
	
	class ConcreteUnit extends AbstractUnit {
		ConcreteUnit(int i){
			intItem = i;
		}
		@Override
		public int myFun(){
			return intItem;
		}
	}
	
	@BeforeClass
    public void beforeClass() {
		array1 = new ConcreteUnit[sz1];
		for (int i = 0; i < sz1; i++) {	
			array1[i] = new ConcreteUnit(i); 
		}
		array2 = new ConcreteUnit[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array2[i][j] = new ConcreteUnit(i+j); 
			}
		}
		array3 = new ConcreteUnit[sz3][sz3][sz3];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					array3[i][j][k] = new ConcreteUnit(i+j+k); 
				}
			}
		}
		layer1 = new Layer_AbstractUnit_(array1);
		layer2 = new Layer_AbstractUnit_(array2);
		layer3 = new Layer_AbstractUnit_(array3);
	}
	
	@Test
	public void  testLayer1() {
		intLayer1 = layer1.myFun();
		for (int i = 0; i < sz1; i++) {	
			assertEquals(intLayer1.get(i).intValue(), i);
		}
	}
	
	@Test
	public void  testLayer2() {
		intLayer2 = layer2.myFun();
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				assertEquals(intLayer2.get(i, j).intValue(), i+j);
			}
		}
	}
	
	@Test
	public void  testLayer3() {
		intLayer3 = layer3.myFun();
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					assertEquals(intLayer3.get(i, j, k).intValue(), i+j+k);
				}
			}
		}
	}
  
}

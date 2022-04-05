package org.jlayer.util;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.jlayer.model.*;

import basic.units.*;

/**
 * tests LifeUnit.
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Life_Test {
	
	final int sz1 = 10, sz2 = 3;
	LifeUnit[] lifeArray1 = new LifeUnit[sz1];
	LifeUnit[] lifeArray2 = new LifeUnit[sz2];
	
	Relation rel = new IndexTools.D1D1(){
		@Override
		public boolean contains(int i, int j) { return true; }
	};
	
	@BeforeClass
    public void beforeClass() {
		int i;
		for (i = 0; i < sz1; i++) {	
			lifeArray1[i] = new LifeUnit(); 
		}
		for (i = 0; i < sz2; i++) {	
			lifeArray2[i] = new LifeUnit(); 
		}
	}
	
	@Test
	public void test1() {
		Layer_LifeUnit_ lifeLayer1 = new Layer_LifeUnit_(lifeArray1);
		Layer_LifeUnit_ lifeLayer2 = new Layer_LifeUnit_(lifeArray2);
		Layer_LifeUnit_.D1 layer1 = lifeLayer1.getD1();
		Layer_LifeUnit_.D1 layer2 = lifeLayer2.getD1();
		layer1.vector.connect(layer2, rel);
		layer1.nextState();
		layer2.vector.connect(layer1, rel);
		layer2.nextState();
		
	}
  
}

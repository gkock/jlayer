package more.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * 
 * 
 * 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
//@Test(groups = {"suiteC", "choice"})
public class C_ValUnit_Test {
	
	final int sz1 = 10;
	final int sz2 = 4;
	final int sz3 = 3;
	
	ValUnit[] valUnitArray1;
	ValUnit[][] valUnitArray2;
	ValUnit[][][] valUnitArray3;
	
	ALayer_ValUnit_ valUnitLayer1, valUnitLayer1R;
	ALayer_ValUnit_ valUnitLayer2, valUnitLayer2R;
	ALayer_ValUnit_ valUnitLayer3, valUnitLayer3R;

	@BeforeClass
	public void beforeClass() {
		
		// dim == 1
		valUnitArray1 = new ValUnit[sz1];
		
		for (int i = 0; i < sz1; i++) {
			valUnitArray1[i] = new ValUnit();
			valUnitArray1[i].val = 0;
		}
		
		valUnitLayer1 = new Layer_ValUnit_(valUnitArray1);
		valUnitLayer1R = valUnitLayer1.getValUnit();
		
		// dim == 2
		valUnitArray2 = new ValUnit[sz2][sz2];
		
		for (int i = 0; i < sz2; i++) {
			for (int j = 0; j < sz2; j++) {
				valUnitArray2[i][j] = new ValUnit();
				valUnitArray2[i][j].val = 0;
			}
		}
		
		valUnitLayer2 = new Layer_ValUnit_(valUnitArray2);
		valUnitLayer2R = valUnitLayer2.getValUnit();
		
		// dim == 3
		valUnitArray3 = new ValUnit[sz3][sz3][sz3];
		
		for (int i = 0; i < sz3; i++) {
			for (int j = 0; j < sz3; j++) {
				for (int k = 0; k < sz3; k++) {
					valUnitArray3[i][j][k] = new ValUnit();
					valUnitArray3[i][j][k].val = 0;
				}
			}
		}
		
		valUnitLayer3 = new Layer_ValUnit_(valUnitArray3);
		valUnitLayer3R = valUnitLayer3.getValUnit();
		
	}
	
	@Test
	public void testLayer1() {
		for (int i = 0; i < sz1; i++) {
			valUnitLayer1.val.set(i,  i);
			assertEquals(valUnitLayer1R.val.get(i), (Integer)i);
			valUnitLayer1R.val.set(i,  2*i);
			assertEquals(valUnitLayer1.val.get(i), (Integer)(2*i));
		}
		valUnitLayer1.setVal(3);
		for (int i = 0; i < sz1; i++) {
			assertEquals(valUnitLayer1R.get(i).getVal(), 3);
		}
		valUnitLayer1R.setVal(4);
		for (int i = 0; i < sz1; i++) {
			assertEquals(valUnitLayer1.get(i).getVal(), 4);
		}
	}
	
	@Test
	public void testLayer2() {
		for (int i = 0; i < sz2; i++) {
			for (int j = 0; j < sz2; j++) {
				valUnitLayer2.val.set(i, j,  i+j);
				assertEquals(valUnitLayer2R.val.get(i, j), (Integer)(i+j));
				valUnitLayer2R.val.set(i, j,  2*(i+j));
				assertEquals(valUnitLayer2.val.get(i, j), (Integer)(2*(i+j)));
			}
		}
		valUnitLayer2.setVal(3);
		for (int i = 0; i < sz2; i++) {
			for (int j = 0; j < sz2; j++) {
				assertEquals(valUnitLayer2R.get(i, j).getVal(), 3);
			}
		}
		valUnitLayer2R.setVal(4);
		for (int i = 0; i < sz2; i++) {
			for (int j = 0; j < sz2; j++) {
				assertEquals(valUnitLayer2.get(i, j).getVal(), 4);
			}
		}
	}
	
	@Test
	public void testLayer3() {
		for (int i = 0; i < sz3; i++) {
			for (int j = 0; j < sz3; j++) {
				for (int k = 0; k < sz3; k++) {
					valUnitLayer3.val.set(i, j, k,  i+j+k);
					assertEquals(valUnitLayer3R.val.get(i, j, k), (Integer)(i+j+k));
					valUnitLayer3R.val.set(i, j, k,  2*(i+j+k));
					assertEquals(valUnitLayer3.val.get(i, j, k), (Integer)(2*(i+j+k)));
				}
			}
		}
		valUnitLayer3.setVal(3);
		for (int i = 0; i < sz3; i++) {
			for (int j = 0; j < sz3; j++) {
				for (int k = 0; k < sz3; k++) {
					assertEquals(valUnitLayer3R.get(i, j, k).getVal(), 3);
				}
			}
		}
		valUnitLayer3R.setVal(4);
		for (int i = 0; i < sz3; i++) {
			for (int j = 0; j < sz3; j++) {
				for (int k = 0; k < sz3; k++) {
					assertEquals(valUnitLayer3.get(i, j, k).getVal(), 4);
				}
			}
		}
	}
  
}

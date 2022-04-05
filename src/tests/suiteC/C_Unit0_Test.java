
//import java.lang.Integer;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import org.jlayer.model.*;

/**
 * tests the generated {@code Layer_Unit0_}, 
 * UNDER DEVELOPMENT.
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Unit0_Test {
	
	final int sz1 = 1000;
	final int sz2 = 200;
	final int sz3 = 40;
	
	Unit0[] array1;
	Unit0[][] array2;
	Unit0[][][] array3;
	Layer_Unit0_ layer1, layer2, layer3;
	
	@BeforeClass
    public void beforeClass() {
		array1 = new Unit0[sz1];
		for (int i = 0; i < sz1; i++) {	
			array1[i] = new Unit0(i); 
		}
		array2 = new Unit0[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array2[i][j] = new Unit0(i+j); 
			}
		}
		array3 = new Unit0[sz3][sz3][sz3];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					array3[i][j][k] = new Unit0(i+j+k); 
				}
			}
		}
		layer1 = new Layer_Unit0_(array1);
		layer2 = new Layer_Unit0_(array2);
		layer3 = new Layer_Unit0_(array3);
	}
	
	@Test
	public void  testLayer1() {
		Layer<?, Integer> basedLayer = layer1.returnNoItem();
		for (int i = 0; i < sz1; i++) {	
			 assertEquals(basedLayer.get(i), (Integer)i);
		}
	}
	
	@Test
	public void  testLayer2() {
		BasedLayer<Integer> basedLayer = layer2.returnNoItem();
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				assertEquals(basedLayer.get(i, j), (Integer)(i+j));
			}
		}
	}
	
	@Test
	public void  testLayer3() {
		BasedLayer<Integer> basedLayer = layer3.returnNoItem();
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					assertEquals(basedLayer.get(i, j, k), (Integer)(i+j+k));
				}
			}
		}
	}
  
}

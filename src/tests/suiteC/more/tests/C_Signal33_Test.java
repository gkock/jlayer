package more.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import org.jlayer.model.*;
import org.jlayer.util.JLayerException;


/**
 * 
 * Classes Signal, InUnit, OutUnit are used for connecting and associating "signal units"
 * here layers of dim == 3
 * 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Signal33_Test {
	
	final int sz3 = 2;
	
	InUnit[][][] inArray;
	Layer_InUnit_ inLayer;
	
	OutUnit[][][] outArray;
	Layer_OutUnit_ outLayer;
	
	BasedVectorLayer<Signal> connectedVectorLayer;
	BasedVectorLayer<Signal> associatedVectorLayer;
	
	Relation full = new Relation() {
		@Override
		public boolean contains(int[] x, int[] y) { 
			return true;
		}
	};
	
	
	@BeforeClass
    public void beforeClass() {
		inArray = new InUnit[sz3][sz3][sz3];
		for (int i=0; i < sz3; i++) {
			for (int j=0; j < sz3; j++) {
				for (int k=0; k < sz3; k++) {
					inArray[i][j][k] = new InUnit();
					inArray[i][j][k].unitId = i+j+k;
				}
			}
		}
		inLayer = new Layer_InUnit_(inArray);
		connectedVectorLayer = inLayer.returnConnectedVector();
		associatedVectorLayer = inLayer.returnAssociatedVector();
		
		outArray = new OutUnit[sz3][sz3][sz3];
		for (int i=0; i < sz3; i++) {
			for (int j=0; j < sz3; j++) {
				for (int k=0; k < sz3; k++) {
					outArray[i][j][k] = new OutUnit();
					outArray[i][j][k].unitId = i+j+k;
				}
			}
		}
		outLayer = new Layer_OutUnit_(outArray);
	}
	
	private int sum(Signal[] vector){
		int sum = 0;
		for (int i=0; i < vector.length; i++) {
			sum += vector[i].getVal();
		}
		return sum;
	}
	
	@Test
	public void testConnect() {
		inLayer.connectedVector.connect(outLayer.fanoutSignal, full);
		connectedVectorLayer = inLayer.returnConnectedVector();
		outLayer.setSignal(1);
		for (int i=0; i < sz3; i++) {
			for (int j=0; j < sz3; j++) {
				for (int k=0; k < sz3; k++) {
					assertEquals(outLayer.fanoutSignal.get(i, j, k).getVal(), i+j+k);
					assertEquals(sum(inLayer.connectedVector.get(i, j, k)), 12);
					assertEquals(sum(connectedVectorLayer.get(i, j, k)), 12);
				}
			}
		}
		connectedVectorLayer.connect(outLayer.fanoutSignal, full);
		outLayer.setSignal(2);
		for (int i=0; i < sz3; i++) {
			for (int j=0; j < sz3; j++) {
				for (int k=0; k < sz3; k++) {
					assertEquals(outLayer.fanoutSignal.get(i, j, k).getVal(), (i+j+k) * 2);
					assertEquals(sum(inLayer.connectedVector.get(i, j, k)), 12 * 2);
					assertEquals(sum(connectedVectorLayer.get(i, j, k)), 12 * 2);
				}
			}
		}
		inLayer.incrementSignalVector(inLayer.connectedVector);
		inLayer.incrementSignalVector(connectedVectorLayer);
		for (int i=0; i < sz3; i++) {
			for (int j=0; j < sz3; j++) {
				for (int k=0; k < sz3; k++) {
					assertEquals(outLayer.fanoutSignal.get(i, j, k).getVal(), 16 + (i+j+k) * 2);
					assertEquals(sum(inLayer.connectedVector.get(i, j, k)), 16 * 8 + 12 * 2);
					assertEquals(sum(connectedVectorLayer.get(i, j, k)), 16 * 8 + 12 * 2);
				}
			}
		}
	}
	
	@Test
	public void testAssociate() {
		inLayer.associatedVector.associate(outLayer.signalVector, full);
		associatedVectorLayer = inLayer.returnAssociatedVector();
		outLayer.setSignalVector(1);
		for (int i=0; i < sz3; i++) {
			for (int j=0; j < sz3; j++) {
				for (int k=0; k < sz3; k++) {
				assertEquals(sum(outLayer.signalVector.get(i, j, k)), 8 * (i+j+k));
				assertEquals(sum(inLayer.associatedVector.get(i, j, k)), 12);
				assertEquals(sum(associatedVectorLayer.get(i, j, k)), 12);
				}
			}
		}
		associatedVectorLayer.associate(outLayer.signalVector, full);
		outLayer.setSignalVector(2);
		for (int i=0; i < sz3; i++) {
			for (int j=0; j < sz3; j++) {
				for (int k=0; k < sz3; k++) {
					assertEquals(sum(outLayer.signalVector.get(i, j, k)), 8 * (i+j+k) * 2);
					assertEquals(sum(inLayer.associatedVector.get(i, j, k)), 12);
					assertEquals(sum(associatedVectorLayer.get(i, j, k)), 12 * 2);
				}
			}
		}
		inLayer.incrementSignalVector(inLayer.associatedVector);
		inLayer.incrementSignalVector(associatedVectorLayer);
		for (int i=0; i < sz3; i++) {
			for (int j=0; j < sz3; j++) {
				for (int k=0; k < sz3; k++) {
				assertEquals(sum(outLayer.signalVector.get(i, j, k)), 8 + 8 * (i+j+k) * 2);
				assertEquals(sum(inLayer.associatedVector.get(i, j, k)), 8 + 12);
				assertEquals(sum(associatedVectorLayer.get(i, j, k)), 8 + 12 * 2);
				}
			}
		}
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testAdapterException1() {
		inLayer.associatedVector.associate(inLayer.associatedVector, full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testAdapterException2() {
		inLayer.associatedVector.associate(inLayer.associatedVector.getD3(), full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testAdapterException3() {
		inLayer.associatedVector.getD3().associate(inLayer.associatedVector, full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testAdapterException4() {
		inLayer.associatedVector.getD3().associate(inLayer.associatedVector.getD3(), full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testBaseException1() {
		associatedVectorLayer.associate(associatedVectorLayer, full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testBaseException2() {
		associatedVectorLayer.associate(associatedVectorLayer.getD3(), full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testBaseException3() {
		associatedVectorLayer.getD3().associate(associatedVectorLayer, full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testBaseException4() {
		associatedVectorLayer.getD3().associate(associatedVectorLayer.getD3(), full);
	}
	
  
}

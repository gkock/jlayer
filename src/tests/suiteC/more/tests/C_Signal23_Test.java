package more.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import org.jlayer.model.*;


/**
 * 
 * Classes Signal, InUnit, OutUnit are used for connecting and associating "signal units"
 * here layers of dims == 2 and 3
 * 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Signal23_Test {
	
	final int sz2 = 3;
	final int sz3 = 2;
	
	InUnit[][] inArray;
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
		inArray = new InUnit[sz2][sz2];
		for (int i=0; i < sz2; i++) {
			for (int j=0; j < sz2; j++) {
				inArray[i][j] = new InUnit();
				inArray[i][j].unitId = i+j;
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
				}
				assertEquals(sum(inLayer.connectedVector.get(i, j)), 12);
				assertEquals(sum(connectedVectorLayer.get(i, j)), 12);
			}
		}
		connectedVectorLayer.connect(outLayer.fanoutSignal, full);
		outLayer.setSignal(2);
		for (int i=0; i < sz3; i++) {
			for (int j=0; j < sz3; j++) {
				for (int k=0; k < sz3; k++) {
					assertEquals(outLayer.fanoutSignal.get(i, j, k).getVal(), (i+j+k) * 2);
				}
				assertEquals(sum(inLayer.connectedVector.get(i, j)), 12 * 2);
				assertEquals(sum(connectedVectorLayer.get(i, j)), 12 * 2);
			}
		}
		inLayer.incrementSignalVector(inLayer.connectedVector);
		inLayer.incrementSignalVector(connectedVectorLayer);
		for (int i=0; i < sz3; i++) {
			for (int j=0; j < sz3; j++) {
				for (int k=0; k < sz3; k++) {
					assertEquals(outLayer.fanoutSignal.get(i, j, k).getVal(), 18 + (i+j+k) * 2);
				}
//				assertEquals(sum(inLayer.connectedVector.get(i, j)), 18 * 9 + 12 * 2);
//				assertEquals(sum(connectedVectorLayer.get(i, j)), 16 * 8 + 12 * 2);
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
					assertEquals(sum(outLayer.signalVector.get(i, j, k)), 9 * (i+j+k));
				}
				assertEquals(sum(inLayer.associatedVector.get(i, j)), 12);
				assertEquals(sum(associatedVectorLayer.get(i, j)), 12);
			}
		}
		associatedVectorLayer.associate(outLayer.signalVector, full);
		outLayer.setSignalVector(2);
		for (int i=0; i < sz3; i++) {
			for (int j=0; j < sz3; j++) {
				for (int k=0; k < sz3; k++) {
					assertEquals(sum(outLayer.signalVector.get(i, j, k)), 9 * (i+j+k) * 2);
				}
				assertEquals(sum(inLayer.associatedVector.get(i, j)), 12);
				assertEquals(sum(associatedVectorLayer.get(i, j)), 12 * 2);
			}
		}
		inLayer.incrementSignalVector(inLayer.associatedVector);
		inLayer.incrementSignalVector(associatedVectorLayer);
		for (int i=0; i < sz3; i++) {
			for (int j=0; j < sz3; j++) {
				for (int k=0; k < sz3; k++) {
				assertEquals(sum(outLayer.signalVector.get(i, j, k)), 9 + 9 * (i+j+k) * 2);
				}
//				assertEquals(sum(inLayer.associatedVector.get(i, j)), 8 + 12);
//				assertEquals(sum(associatedVectorLayer.get(i, j)), 8 + 12 * 2);
			}
		}
	}
  
}

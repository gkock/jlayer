package more.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import org.jlayer.model.*;
import org.jlayer.util.JLayerException;


/**
 * 
 * Classes Signal, InUnit, OutUnit are used for connecting and associating "signal units"
 * here layers of dim == 2
 * 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Signal22_Test {
	
	final int sz2 = 3;
	
	InUnit[][] inArray;
	Layer_InUnit_ inLayer;
	
	OutUnit[][] outArray;
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
		
		outArray = new OutUnit[sz2][sz2];
		for (int i=0; i < sz2; i++) {
			for (int j=0; j < sz2; j++) {
				outArray[i][j] = new OutUnit();
				outArray[i][j].unitId = i+j;
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
		for (int i=0; i < sz2; i++) {
			for (int j=0; j < sz2; j++) {
				assertEquals(outLayer.fanoutSignal.get(i, j).getVal(), i+j);
				assertEquals(sum(inLayer.connectedVector.get(i, j)), 18);
				assertEquals(sum(connectedVectorLayer.get(i, j)), 18);
			}
		}
		connectedVectorLayer.connect(outLayer.fanoutSignal, full);
		outLayer.setSignal(2);
		for (int i=0; i < sz2; i++) {
			for (int j=0; j < sz2; j++) {
				assertEquals(outLayer.fanoutSignal.get(i, j).getVal(), (i+j) * 2);
				assertEquals(sum(inLayer.connectedVector.get(i, j)), 18 * 2);
				assertEquals(sum(connectedVectorLayer.get(i, j)), 18 * 2);
			}
		}
		inLayer.incrementSignalVector(inLayer.connectedVector);
		inLayer.incrementSignalVector(connectedVectorLayer);
		for (int i=0; i < sz2; i++) {
			for (int j=0; j < sz2; j++) {
				assertEquals(outLayer.fanoutSignal.get(i, j).getVal(), 18 + (i+j) * 2);
				assertEquals(sum(inLayer.connectedVector.get(i, j)), 18 * sz2 * sz2 + 18 * 2);
				assertEquals(sum(connectedVectorLayer.get(i, j)), 18 * sz2 * sz2 + 18 * 2);
			}
		}
	}
	
	@Test
	public void testAssociate() {
		inLayer.associatedVector.associate(outLayer.signalVector, full);
		associatedVectorLayer = inLayer.returnAssociatedVector();
		outLayer.setSignalVector(1);
		for (int i=0; i < sz2; i++) {
			for (int j=0; j < sz2; j++) {
				assertEquals(sum(outLayer.signalVector.get(i, j)), sz2*sz2*(i+j));
				assertEquals(sum(inLayer.associatedVector.get(i, j)), 18);
				assertEquals(sum(associatedVectorLayer.get(i, j)), 18);
			}
		}
		associatedVectorLayer.associate(outLayer.signalVector, full);
		outLayer.setSignalVector(2);
		for (int i=0; i < sz2; i++) {
			for (int j=0; j < sz2; j++) {
				assertEquals(sum(outLayer.signalVector.get(i, j)), sz2*sz2*(i+j) * 2);
				assertEquals(sum(inLayer.associatedVector.get(i, j)), 18);
				assertEquals(sum(associatedVectorLayer.get(i, j)), 18 * 2);
			}
		}
		inLayer.incrementSignalVector(inLayer.associatedVector);
		inLayer.incrementSignalVector(associatedVectorLayer);
		for (int i=0; i < sz2; i++) {
			for (int j=0; j < sz2; j++) {
				assertEquals(sum(outLayer.signalVector.get(i, j)), sz2*sz2 + sz2*sz2*(i+j) * 2);
				assertEquals(sum(inLayer.associatedVector.get(i, j)), sz2*sz2 + 18);
				assertEquals(sum(associatedVectorLayer.get(i, j)), sz2*sz2 + 18 * 2);
			}
		}
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testAdapterException1() {
		inLayer.associatedVector.associate(inLayer.associatedVector, full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testAdapterException2() {
		inLayer.associatedVector.associate(inLayer.associatedVector.getD2(), full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testAdapterException3() {
		inLayer.associatedVector.getD2().associate(inLayer.associatedVector, full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testAdapterException4() {
		inLayer.associatedVector.getD2().associate(inLayer.associatedVector.getD2(), full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testBaseException1() {
		associatedVectorLayer.associate(associatedVectorLayer, full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testBaseException2() {
		associatedVectorLayer.associate(associatedVectorLayer.getD2(), full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testBaseException3() {
		associatedVectorLayer.getD2().associate(associatedVectorLayer, full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testBaseException4() {
		associatedVectorLayer.getD2().associate(associatedVectorLayer.getD2(), full);
	}
	
  
}

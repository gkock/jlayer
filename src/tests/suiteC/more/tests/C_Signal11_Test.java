package more.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import org.jlayer.model.*;
import org.jlayer.util.JLayerException;


/**
 * 
 * Classes Signal, InUnit, OutUnit are used for connecting and associating "signal units"
 * here layers of dim == 1
 * 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Signal11_Test {
	
	final int sz1 = 10;
	
	InUnit[] inArray;
	Layer_InUnit_ inLayer;
	
	OutUnit[] outArray;
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
		inArray = new InUnit[sz1];
		for (int i=0; i < sz1; i++) {
			inArray[i] = new InUnit();
			inArray[i].unitId = i;
		}
		inLayer = new Layer_InUnit_(inArray);
		connectedVectorLayer = inLayer.returnConnectedVector();
		associatedVectorLayer = inLayer.returnAssociatedVector();
		
		outArray = new OutUnit[sz1];
		for (int i=0; i < sz1; i++) {
			outArray[i] = new OutUnit();
			outArray[i].unitId = i;
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
		for (int i=0; i < sz1; i++) {
			assertEquals(outLayer.fanoutSignal.get(i).getVal(), i);
			assertEquals(sum(inLayer.connectedVector.get(i)), 45);
			assertEquals(sum(connectedVectorLayer.get(i)), 45);
		}
		connectedVectorLayer.connect(outLayer.fanoutSignal, full);
		outLayer.setSignal(2);
		for (int i=0; i < sz1; i++) {
			assertEquals(outLayer.fanoutSignal.get(i).getVal(), i * 2);
			assertEquals(sum(inLayer.connectedVector.get(i)), 45 * 2);
			assertEquals(sum(connectedVectorLayer.get(i)), 45 * 2);
		}
		inLayer.incrementSignalVector(inLayer.connectedVector);
		inLayer.incrementSignalVector(connectedVectorLayer);
		for (int i=0; i < sz1; i++) {
			assertEquals(outLayer.fanoutSignal.get(i).getVal(), 20 + i * 2);
			assertEquals(sum(inLayer.connectedVector.get(i)), 20 * sz1 + 45 * 2);
			assertEquals(sum(connectedVectorLayer.get(i)), 20 * sz1 + 45 * 2);
		}
	}
	
	@Test
	public void testAssociate() {
		inLayer.associatedVector.associate(outLayer.signalVector, full);
		associatedVectorLayer = inLayer.returnAssociatedVector();
		outLayer.setSignalVector(1);
		for (int i=0; i < sz1; i++) {
			assertEquals(sum(outLayer.signalVector.get(i)), sz1*i);
			assertEquals(sum(inLayer.associatedVector.get(i)), 45);
			assertEquals(sum(associatedVectorLayer.get(i)), 45);
		}
		associatedVectorLayer.associate(outLayer.signalVector, full);
		outLayer.setSignalVector(2);
		for (int i=0; i < sz1; i++) {
			assertEquals(sum(outLayer.signalVector.get(i)), sz1*i * 2);
			assertEquals(sum(inLayer.associatedVector.get(i)), 45);
			assertEquals(sum(associatedVectorLayer.get(i)), 45 * 2);
		}
		inLayer.incrementSignalVector(inLayer.associatedVector);
		inLayer.incrementSignalVector(associatedVectorLayer);
		for (int i=0; i < sz1; i++) {
			assertEquals(sum(outLayer.signalVector.get(i)), sz1 + sz1*i * 2);
			assertEquals(sum(inLayer.associatedVector.get(i)), sz1 + 45);
			assertEquals(sum(associatedVectorLayer.get(i)), sz1 + 45 * 2);
		}
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testAdapterException1() {
		inLayer.associatedVector.associate(inLayer.associatedVector, full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testAdapterException2() {
		inLayer.associatedVector.associate(inLayer.associatedVector.getD1(), full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testAdapterException3() {
		inLayer.associatedVector.getD1().associate(inLayer.associatedVector, full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testAdapterException4() {
		inLayer.associatedVector.getD1().associate(inLayer.associatedVector.getD1(), full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testBaseException1() {
		associatedVectorLayer.associate(associatedVectorLayer, full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testBaseException2() {
		associatedVectorLayer.associate(associatedVectorLayer.getD1(), full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testBaseException3() {
		associatedVectorLayer.getD1().associate(associatedVectorLayer, full);
	}
	
	@Test(expectedExceptions = JLayerException.class)
	public void testBaseException4() {
		associatedVectorLayer.getD1().associate(associatedVectorLayer.getD1(), full);
	}
  
}

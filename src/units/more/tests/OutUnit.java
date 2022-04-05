package more.tests;

import org.jlayer.annotations.*;

/**
 * 
 * Classes Signal, InUnit, OutUnit are used for connecting and associating "signal units"
 *
 */
@LayerUnit 
public class OutUnit {
	
	int unitId;
	
	@LayerField Signal   fanoutSignal;
	@LayerField	Signal[] signalVector;
	
	@LayerMethod
	public void setSignal(int factor){
		fanoutSignal.setVal(unitId * factor);
	}
	
	@LayerMethod
	public void setSignalVector(int factor){
		for (int i=0; i < signalVector.length; i++) {
			signalVector[i].setVal(unitId * factor);
		}
	}

}

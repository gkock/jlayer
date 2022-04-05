package more.tests;

import org.jlayer.annotations.*;

/**
 * 
 * Classes Signal, InUnit, OutUnit are used for connecting and associating "signal units"
 *
 */
@LayerUnit 
public class InUnit {
	
	int unitId;
	
	@LayerField	Signal[] connectedVector;
	@LayerField	Signal[] associatedVector;
	
	@LayerMethod
	public Signal[] returnConnectedVector(){
		return connectedVector;
	}
	@LayerMethod
	public Signal[] returnAssociatedVector(){
		return associatedVector;
	}
	@LayerMethod
	public void incrementSignalVector(@LayerParam Signal[] vector){
		for (Signal signal : vector) {
			signal.setVal(signal.getVal()+1);
		}
	}

}

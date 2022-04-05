package sandbox;

import org.jlayer.annotations.*;

@LayerUnit 
public class Unit2 {
	
	int noItem;
	
	@LayerField	int intItem;
	
	@LayerField	Signal[] signalVector;
	
	@LayerMethod
	void methodItem(){
		intItem = noItem;
	}
	
	@LayerMethod
	void setVector(){
		for (int i=0; i < signalVector.length; i++) {
			signalVector[i].val = noItem;
		}
	}
	
	@LayerMethod
	void setVector(int val){
		for (int i=0; i < signalVector.length; i++) {
			signalVector[i].val = val;
		}
	}

}

package sandbox;

import org.jlayer.annotations.*;

@LayerUnit 
public class Unit1 {
	
	int noItem;
	
	@LayerField int intItem;
	
	@LayerField	Unit1[] unit1Vector;
	@LayerField	Signal[] signalVector;
	
	@LayerMethod
	public void methodItem(){
		intItem = noItem;
	}
	
	@LayerMethod
	void setVector(int val){
		for (int i=0; i < signalVector.length; i++) {
			signalVector[i].val = val;
		}
	}

}

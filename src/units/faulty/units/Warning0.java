package faulty.units;

import org.jlayer.annotations.*;

class Warning0 {
	
	Warning0(int par){
		noItem = par;
	}
	
	int noItem;
	
	@LayerField    // warning:   <<< THIS IS TO BE DONE
	int intItem;   // @LayerField is not processed: missing @LayerUnit

}

package more.tests;

import org.jlayer.annotations.*;

/**
 * 
 * Class ValUnit is used for testing returning layer units
 *
 */
@LayerUnit 
public class ValUnit {
	
	@LayerField 
	int val;
	
	@LayerMethod
	void setVal(int val){
		this.val = val;
	}
	
	@LayerMethod
	int getVal() {
		return val;
	}
	
	@LayerMethod
	ValUnit getValUnit() {
		return this;
	}
}

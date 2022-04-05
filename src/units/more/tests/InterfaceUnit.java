package more.tests;

import org.jlayer.annotations.*;

@LayerUnit
public interface InterfaceUnit {
	
	@LayerMethod
	void init(@LayerParam int par);
	
	@LayerMethod
	void add1(@LayerParam int par);
	
	@LayerMethod(runtimeMode=RuntimeMode.FORKJOIN)
	void add2(@LayerParam int par);
	
	@LayerMethod
	int getVal();
}

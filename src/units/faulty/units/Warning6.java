package faulty.units;

import org.jlayer.annotations.*;

@LayerUnit 
class Warning6 {
	
	Warning6(int par){
		noItem = par;
	}
	
	int noItem;
	
	@LayerField
	int intItem;
	
	@LayerMethod
	void methodItem(){
		intItem = noItem;
	}
	
	@LayerMethod                        
	void length(){             // warning:
		intItem = noItem;   // a layer method must not have this identifier:
	}
	
	@LayerMethod                        
	void get(){             // warning:
		intItem = noItem;   // a layer method must not have this identifier:
	}
	
	@LayerMethod                        
	void set(){             // warning:
		intItem = noItem;   // a layer method must not have this identifier:
	}
	
	@LayerMethod                        
	void isAdapter(){       // warning:
		intItem = noItem;   // a layer method must not have this identifier:
	}
	
	@LayerMethod                        
	void getUnit(){         // warning:
		intItem = noItem;   // a layer method must not have this identifier:
	}
	
	@LayerMethod                        
	void setUnit(){         // warning:
		intItem = noItem;   // a layer method must not have this identifier:
	}
	
	@LayerMethod                        
	void iterator(){        // warning:
		intItem = noItem;   // a layer method must not have this identifier:
	}
	
	@LayerMethod                      
	void xIterator(){       // warning:
		intItem = noItem;   // a layer method must not have this identifier:
	}
	
	@LayerMethod                        
	void dims(){            // warning:
		intItem = noItem;   // a layer method must not have this identifier:
	}
	
	@LayerMethod                        
	void getD1(){           // warning:
		intItem = noItem;   // a layer method must not have this identifier:
	}
	
	@LayerMethod                        
	void getD2(){           // warning:
		intItem = noItem;   // a layer method must not have this identifier:
	}
	
	@LayerMethod                        
	void getD3(){           // warning:
		intItem = noItem;   // a layer method must not have this identifier:
	}
	
	@LayerMethod                        
	void getBase(){         // warning:
		intItem = noItem;   // a layer method must not have this identifier:
	}

}

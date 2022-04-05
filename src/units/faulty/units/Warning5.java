package faulty.units;


import java.util.List;
import java.util.ArrayList;

import org.jlayer.annotations.*;

@LayerUnit 
class Warning5 {
	
	Warning5(int par){
		noItem = par;
	}
	
	int noItem;
	
	@LayerField
	int intItem;
	
	@LayerField                         // warning:
	private int privateItem;            // a layer field must not be private
	
	@LayerMethod
	void methodItem(){
		intItem = noItem;
	}
	
	@LayerMethod                        // warning:
	private void privateMethod(){       // a layer method must not be private
		intItem = noItem;
	}
	
	@LayerMethod 
	public ArrayList<Integer> returnMethod1() {               // warning:
		ArrayList<Integer> list = new ArrayList<Integer>();   // the return type of a layer method must not be a concrete parameterized type
		return list; 
	}
	
	@LayerMethod 
	public List<Integer> returnMethod2() {                    // warning:
		List<Integer> list = new ArrayList<Integer>();        // the return type of a layer method must not be a concrete parameterized type
		return list; 
	}
	
	@LayerMethod 
	public void paramMethod(@LayerParam List<Integer> list) {
		intItem = noItem;
	}

}

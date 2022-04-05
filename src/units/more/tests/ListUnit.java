package more.tests;

import org.jlayer.annotations.*;

import java.util.List;
import java.util.ArrayList;


/**
 * 
 * layer fields and layer parameters with interface type
 * {@code List<Integer>} and concrete parametrized type {@code ArrayList<Integer>}
 * 
 * @author Gerd Kock
 */
@LayerUnit
public class ListUnit {
	
	// constructors
	
	ListUnit(){
		intListField = null;
		intArrayListField = null;
	}
	
	ListUnit(List<Integer> intList1, ArrayList<Integer> intList2){
		intListField = intList1;
		intArrayListField = intList2;
	}
	
	// layer fields
	
	@LayerField
	List<Integer> intListField;
	
	@LayerField
	ArrayList<Integer> intArrayListField;
	
	// layer methods with layer parameters
	
	@LayerMethod
	public void setIntListField(@LayerParam ArrayList<Integer> ListParam){
		intListField = new ArrayList<Integer>();
		for (int i : ListParam) {
			intListField.add(i);
		}
	}
	
	@LayerMethod
	public void setIntArrayListField(@LayerParam List<Integer> intListParam){
		intArrayListField = new ArrayList<Integer>();
		for (int i : intListParam) {
			intArrayListField.add(i);
		}
	}

}

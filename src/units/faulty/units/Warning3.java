package faulty.units;

import java.util.Stack;

import org.jlayer.annotations.LayerUnit;
import org.jlayer.annotations.LayerField;
import org.jlayer.annotations.LayerMethod;
import org.jlayer.annotations.LayerParam;

import basic.units.NoUnit;

@LayerUnit
public class Warning3 {
	
	@LayerField public NoUnit[] noUnitItem1;
	
	@LayerField static NoUnit noUnitItem;		    // warning: a layer field must not be static
	@LayerField public Stack<Integer> stackItem;
	@LayerField public double doubleItem;
	
	@LayerField(isIndex = true) Integer[] indexTest1; // warning: the type of a layer index must be int[]
	@LayerField(isIndex = true) NoUnit[] indexTest2; // warning: the type of a layer index must be int[]
	@LayerField(isIndex = true) int indexTest; // warning: the type of a layer index must be int[]
	@LayerField(isIndex = true) int[][] indexTest4; // warning: the type of a layer index must be int[]
	
	@LayerMethod void mok1() {}
	@LayerMethod int mok2(int par) { return 0; }
	
	@LayerMethod static int staticMethod(int par) { return 0; }  // warning: a layer method must not be static
	
	@LayerMethod void mwarn1(@LayerParam double par) { }
	@LayerMethod double mwarn2() {return 0.0;}
	
}

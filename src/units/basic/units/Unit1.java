package basic.units;

import org.jlayer.annotations.LayerUnit;
import org.jlayer.annotations.LayerField;
import org.jlayer.annotations.LayerMethod;
import org.jlayer.annotations.LayerParam;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Random;

@LayerUnit
public class Unit1 {
	
	public Unit1(){ noItem = 3; }
	public Unit1(int i){ noItem = i; }
	
	@LayerUnit
	public static class SignalInt {
		private BigInteger big = new BigInteger("7");
		@LayerField public int val = big.intValue();
		@LayerMethod public int getVal() { return val; }
		@LayerMethod public void setVal(int value) { val = value; }
		// imported return and parameter type
		@LayerMethod public BigDecimal initState(Random r) { 
			return BigDecimal.valueOf(r.nextInt(2)); 
		}
	}
	
	@LayerUnit
	public static class SignalDouble {
		public double val = 5.0;
		@LayerMethod public double getVal() { return val; }
		@LayerMethod public void setVal(double value) { val = value; }
	}
	
	public int noItem;
	static public int cntUnit1;
	
	// field items
	
	@LayerField public int intItem;
	@LayerField public int[] intLink;
	
	@LayerField public NoUnit noUnitItem;
	@LayerField public NoUnit[] noUnitLink;
	
	@LayerField public SignalInt intSignal;
	@LayerField public SignalInt[] intVector;
	
	@LayerField public SignalDouble doubleSignal;
	@LayerField public SignalDouble[] doubleVector;
	
	@LayerField public Unit1 unit1Item;
	@LayerField public Unit1[] unit1Link;
	
	@LayerField public Unit2 unit2Item;
	@LayerField public Unit2[] unit2Link;
	
	@LayerMethod
	public void simpleMethod() {
		++cntUnit1;
	}
	
	// returning items
	
	@LayerMethod
	public Unit1 returnThis() { return this; }
	@LayerMethod
	public int returnNoItem() { return noItem; }
	@LayerMethod
	public int returnIntItem() { return intItem; }
	
	@LayerMethod
	public SignalInt returnIntSignal() { return intSignal; }
	@LayerMethod
	public SignalInt[] returnIntVector() { return intVector; }
	
	@LayerMethod
	public SignalDouble returnDoubleSignal() { return doubleSignal; }
	@LayerMethod
	public SignalDouble[] returnDoubleVector() { return doubleVector; }
	
	@LayerMethod
	public Unit1 returnUnit1Item() { return unit1Item; }
	@LayerMethod
	public Unit1[] returnUnit1Link() { return unit1Link; }
	
	@LayerMethod
	public Unit2 returnUnit2Item() { return unit2Item; }
	@LayerMethod
	public Unit2[] returnUnit2Link() { return unit2Link; }
	
	// setting items
	
	@LayerMethod
	public void setNoItem_1(int p) { noItem = p; }
	@LayerMethod
	public void setNoItem_2(@LayerParam int i) { noItem = i; }
	
	@LayerMethod
	public void setIntItem_1(int p) { intItem = p; }
	@LayerMethod
	public void setIntItem_2(@LayerParam int i) { intItem = i; }
	
	@LayerMethod
	public void setUnit1Item() { unit1Item = this;}
	
	@LayerMethod
	public void setUnit2Item_1(Unit2 u) { unit2Item = u; }
	@LayerMethod
	public void setUnit2Item_2(@LayerParam Unit2 u) { unit2Item = u; }
	
	// setting items with varargs parameters
	
	@LayerMethod
	public void setBothByInt_1(int... ps) {
		noItem = ps[0]; unit2Item = new Unit2(ps[1]);
	}
	@LayerMethod
	public void setBothByInt_2(@LayerParam int... ps) {
		noItem = ps[0]; unit2Item = new Unit2(ps[1]);
	}
	
	@LayerMethod
	public void setBothByUnit2_1(Unit2... ps) {
		noItem = ps[0].noItem; unit2Item = ps[1];
	}
	@LayerMethod
	public void setBothByUnit2_2(@LayerParam Unit2... ps) { 
		noItem = ps[0].noItem; unit2Item = ps[1];
	}
	
	// imported return and parameter type
	
	@LayerMethod public BigInteger initState(Random r) { 
		return BigInteger.valueOf(r.nextInt(2)); 
	}
	
}

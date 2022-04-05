package basic.units;

import org.jlayer.annotations.*;

import basic.units.Unit1.SignalInt;
import basic.units.Unit1.SignalDouble;

@LayerUnit
public class Unit2 {
	
	public Unit2(){ noItem = 7; }
	public Unit2(int i){ noItem = i; }
	
	public int noItem;
	
	@LayerField public int intItem;
	
	@LayerField public SignalInt intSignal;
	@LayerField public SignalInt[] intVector;
	
	@LayerField public SignalDouble doubleSignal;
	@LayerField public SignalDouble[] doubleVector;
	
	@LayerField public Unit1 unit1Item;
	@LayerField public Unit1[] unit1Link;
	
	@LayerField public Unit2 unit2Item;
	@LayerField public Unit2[] unit2Link;
	
	// returning items
	
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
	
}

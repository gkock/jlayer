package faulty.units;

import org.jlayer.annotations.LayerUnit;
import org.jlayer.annotations.LayerField;

@LayerUnit
public class Warning1 {
	@LayerField public int intItem = 10;
	
	@LayerUnit // warning: an enum type must not be a layer unit
	public enum Day {
	    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
	    THURSDAY, FRIDAY, SATURDAY 
	}
}
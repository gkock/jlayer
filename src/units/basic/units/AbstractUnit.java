package basic.units;

import org.jlayer.annotations.LayerUnit;
import org.jlayer.annotations.LayerField;
import org.jlayer.annotations.LayerMethod;

/**
 * this unit type is used for tests related to modifiers.
 * @author Gerd Kock
 */
@LayerUnit
abstract public class AbstractUnit {
	
	@LayerField protected int intItem;
	@LayerField protected NoUnit noUnitItem;
	@LayerField protected Unit2 unit2Item;
	@LayerMethod public abstract int myFun();
	
}

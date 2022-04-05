package basic.units;

import org.jlayer.annotations.LayerUnit;
import org.jlayer.annotations.LayerField;

/**
 * this unit type is used for tests related to modifiers.
 * @author Gerd Kock
 */
@LayerUnit
class UnitModif1 {
	
	@LayerField int intItem;
	@LayerField NoUnit noUnitItem;
	@LayerField Unit2 unit2Item;
	
}

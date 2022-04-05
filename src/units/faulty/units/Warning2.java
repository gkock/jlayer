package faulty.units;

import org.jlayer.annotations.LayerUnit;
import org.jlayer.annotations.LayerField;

@LayerUnit
class Warning2<T> { // warning: a generic class must not be a layer unit
	@LayerField public int intItem = 10;
}
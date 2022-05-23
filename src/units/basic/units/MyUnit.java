package basic.units;

import org.jlayer.annotations.*;

/**
 * 
 * item method test
 * 
 * @author gerd 
 *
 */
@LayerUnit
public class MyUnit {
	
	@LayerUnit
	static class InnerUnit {
		
	}
	
	public int noItem;
	
	@LayerField(isIndex = true)
	public int[] index;
	
	@LayerField int intField = 1;
	@LayerField int[] intVector = new int[]{1};
	@LayerField InnerUnit unitField = new InnerUnit();
	@LayerField InnerUnit[] unitVector = new InnerUnit[]{};
	
	@LayerMethod public void myMethod(@LayerParam InnerUnit[] i) {}
	
	@LayerMethod public int getInt() { return intField; }
	
	@LayerMethod public int[] getIntVector() { return intVector; }
	
	@LayerMethod public InnerUnit getUnitField() { return unitField; }
	
	@LayerMethod public InnerUnit[] getUnitVector() { return unitVector; }

}

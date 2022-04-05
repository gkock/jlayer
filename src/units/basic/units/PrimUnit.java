package basic.units;

import org.jlayer.annotations.LayerUnit;
import org.jlayer.annotations.LayerField;
import org.jlayer.annotations.LayerMethod;
import org.jlayer.annotations.LayerParam;


@LayerUnit
public class PrimUnit {
	
	public PrimUnit(){}
	public PrimUnit(int i){ noItem = i; }
	
	public int noItem;
	
	@LayerField public boolean booleanItem;
	@LayerField public byte byteItem;
	@LayerField public char charItem;
	@LayerField public double doubleItem;
	@LayerField public float floatItem;
	@LayerField public int intItem;
	@LayerField public long longItem;
	@LayerField public short shortItem;
	
	@LayerMethod
	public void setBooleanItem(@LayerParam boolean p) { booleanItem = p; }
	@LayerMethod
	public void setByteItem(@LayerParam byte p) { byteItem = p; }
	@LayerMethod
	public void setCharItem(@LayerParam char p) { charItem = p; }
	@LayerMethod
	public void setDoubleItem(@LayerParam double p) { doubleItem = p; }
	@LayerMethod
	public void setFloatItem(@LayerParam float p) { floatItem = p; }
	@LayerMethod
	public void setIntItem(@LayerParam int p) { intItem = p; }
	@LayerMethod
	public void setLongItem(@LayerParam long p) { longItem = p; }
	@LayerMethod
	public void setShortItem(@LayerParam short p) { shortItem = p; }
	
}

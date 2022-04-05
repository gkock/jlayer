
import java.util.Date;
import org.jlayer.annotations.*;

/**
 * 
 * testing a unit in the default package
 * 
 * @author gerd 
 *
 */
@LayerUnit
public class Unit0 {
	
	public Unit0(){}
	public Unit0(int i){ noItem = i; }
	
	public int noItem;
	
	@LayerField int intField = 1;
	@LayerField int[] intVector = new int[]{1};
	@LayerField Date date;
	
	@LayerMethod
	public int returnNoItem() {
		return noItem;
	}

}

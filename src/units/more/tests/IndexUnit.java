package more.tests;

import org.jlayer.annotations.*;

@LayerUnit 
public class IndexUnit {
	
	@LayerField(isIndex = true)
	int[] index;
	
	@LayerField(isIndex = true)
	int[] fullIndex;
	
	@LayerField(isIndex = true)
	int[] partialIndex;
	
	@LayerField
	IndexUnit[] neighbours;
	
}

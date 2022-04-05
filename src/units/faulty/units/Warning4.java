package faulty.units;

import org.jlayer.annotations.LayerField;
import org.jlayer.annotations.LayerUnit;

@LayerUnit
public class Warning4 {
	
	@LayerUnit
	public class MemberClass {                // warning:
		int dummy;                            // a layer unit must be a top-level class
	}                                         // or a static member class immediately nested in a top-level layer unit
	
	@LayerUnit
	private static class PrivateClass {       // warning:
		// int dummy;                         // a nested layer unit must not be private
	}
	
	@LayerUnit
	public static class NestedClass { 
		int dummy;
		@LayerUnit
		public static class Next1Class {       // warning:
			int dummy;                         // a layer unit must be a top-level class
		}                                      // or a static member class immediately nested in a top-level layer unit
		@LayerUnit
		private static class Next2Class {      // warning:
			// int dummy;                      // a layer unit must be a top-level class
		}                                      // or a static member class immediately nested in a top-level layer unit
	}
	
	@LayerField MemberClass memberItem;        // 3 warnings:
	@LayerField MemberClass[] memberArr1;      // if the (array element) type of a layer field is a member class,
	@LayerField MemberClass[][] memberArr2;    // this class must be static
	
	@LayerField NestedClass nestedItem;
	@LayerField NestedClass[] nestedVector;
	
	@LayerField PrivateClass privateItem;      // 3 warnings:
	@LayerField PrivateClass[] privateArr1;    // if the (array element) type of a layer field is a member class,
	@LayerField PrivateClass[][] privateArr2;  // this class must not be private
	
}

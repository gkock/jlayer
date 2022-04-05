package more.tests;

import org.jlayer.util.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * 
 * layer fields and layer parameters with interface type
 * {@code List<Integer>} and concrete parametrized type {@code ArrayList<Integer>}
 * 
 * @author Gerd Kock
 */
//@Test(groups = {"suiteC"})
@Test(groups = {"suiteC", "choice"})
public class C_InterfaceUnit_Test {
	
	// the implementation of InterfaceUnit
	
	static class InterfaceUnitImpl implements InterfaceUnit {
		
		int val;
		
		@Override
		public void init(int par) {
			val = par;
		}
		
		@Override
		public void add1(int par) {
			val += par;
		}
		
		@Override
		public void add2(int par) {
			val += par;
		}
		
		@Override
		public int getVal() {
			return val;
		}
		
	}
	
	final int sz1 = 10;
	
	
	// further items
	
	Integer[] initArray, add1Array, add2Array;
	LayerBase<Integer> initLayer, add1Layer, add2Layer;
	
	InterfaceUnit[] interfaceUnitArray;
	Layer_InterfaceUnit_ interfaceUnitLayer;

	@BeforeClass
	public void beforeClass() {
		initArray = new Integer[sz1];
		add1Array = new Integer[sz1];
		add2Array = new Integer[sz1];
		
		interfaceUnitArray = new InterfaceUnitImpl[sz1];
		
		for (int i = 0; i < sz1; i++) {
			initArray[i] = i;
			add1Array[i] = -i;
			add2Array[i] = i;
			interfaceUnitArray[i] = new InterfaceUnitImpl();
		}
		
		initLayer = new LayerBase<Integer>(initArray);
		add1Layer = new LayerBase<Integer>(add1Array);
		add2Layer = new LayerBase<Integer>(add2Array);
		
		interfaceUnitLayer = new Layer_InterfaceUnit_(interfaceUnitArray);
	}
	
	private int sum(Layer_InterfaceUnit_ layer){
		int sum = 0;
		for (int i = 0; i < layer.length(); i++) {
			sum += layer.get(i).getVal();
		}
		return sum;
	}
	
	@Test
	public void testLayer() {
		assertEquals(interfaceUnitArray.length, sz1);
		assertEquals(interfaceUnitLayer.length(), sz1);
		assertEquals(sum(interfaceUnitLayer), 0);
		interfaceUnitLayer.init(initLayer);
		assertEquals(sum(interfaceUnitLayer), sz1*(sz1-1)/2);
		interfaceUnitLayer.add1(add1Layer);
		assertEquals(sum(interfaceUnitLayer), 0);
		interfaceUnitLayer.add2(add2Layer);
		assertEquals(sum(interfaceUnitLayer), sz1*(sz1-1)/2);
	}
  
}

package more.tests;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@Test(groups = {"suiteC"})
public class C_ListUnit_Test {
	
	final int sz1 = 10;
	final int sz2 = 3;
	final int sz3 = 2;
	
	ListUnit[] listUnitArray1, listUnitArray12;
	Layer_ListUnit_ listUnitLayer1, listUnitLayer12;
	
	ListUnit[][] listUnitArray2, listUnitArray22;
	Layer_ListUnit_ listUnitLayer2, listUnitLayer22;

	ListUnit[][][] listUnitArray3, listUnitArray32;
	Layer_ListUnit_ listUnitLayer3, listUnitLayer32;
	
	private void initLayers1() {
		listUnitArray1 = new ListUnit[sz1];
		listUnitArray12 = new ListUnit[sz1];
		for (int i=0; i < sz1; i++) {
			List<Integer> list = new ArrayList<Integer>();
			ArrayList<Integer> arrayList = new ArrayList<Integer>();
			for (int j=10*i; j<10*i+sz1; j++) {
				list.add(j);
				arrayList.add(-j);
			}
			listUnitArray1[i] = new ListUnit(list, arrayList);
			listUnitArray12[i] = new ListUnit();
		}
		listUnitLayer1 = new Layer_ListUnit_(listUnitArray1);
		listUnitLayer12 = new Layer_ListUnit_(listUnitArray12);
	}
	
	private void initLayers2() {
		listUnitArray2 = new ListUnit[sz2][sz2];
		listUnitArray22 = new ListUnit[sz2][sz2];
		for (int i=0; i < sz2; i++) {
			for (int j=0; j < sz2; j++) {
				List<Integer> list = new ArrayList<Integer>();
				ArrayList<Integer> arrayList = new ArrayList<Integer>();
				for (int x=10*(i+j); x<10*(i+j)+sz1; x++) {
					list.add(x);
					arrayList.add(-x);
				}
				listUnitArray2[i][j] = new ListUnit(list, arrayList);
				listUnitArray22[i][j] = new ListUnit();
			}
		}
		listUnitLayer2 = new Layer_ListUnit_(listUnitArray2);
		listUnitLayer22 = new Layer_ListUnit_(listUnitArray22);
	}
	
	private void initLayers3() {
		listUnitArray3 = new ListUnit[sz3][sz3][sz3];
		listUnitArray32 = new ListUnit[sz3][sz3][sz3];
		for (int i=0; i < sz3; i++) {
			for (int j=0; j < sz3; j++) {
				for (int k=0; k < sz3; k++) {
					List<Integer> list = new ArrayList<Integer>();
					ArrayList<Integer> arrayList = new ArrayList<Integer>();
					for (int x=10*(i+j+k); x<10*(i+j+k)+sz1; x++) {
						list.add(x);
						arrayList.add(-x);
					}
					listUnitArray3[i][j][k] = new ListUnit(list, arrayList);
					listUnitArray32[i][j][k] = new ListUnit();
				}
			}
		}
		listUnitLayer3 = new Layer_ListUnit_(listUnitArray3);
		listUnitLayer32 = new Layer_ListUnit_(listUnitArray32);
	}
	
	private int sum(List<Integer> list){
		int sum = 0;
		for (int i : list) {
			sum += i;
		}
		return sum;
	}
	
	@Test
	public void test1() {
		initLayers1();
		for (int i=0; i < sz1; i++) {
			assertEquals(listUnitLayer1.get(i).intListField.size(), sz1);
			assertEquals(sum(listUnitLayer1.get(i).intListField), i*100 + 45);
			assertEquals(listUnitLayer1.get(i).intArrayListField.size(), sz1);
			assertEquals(sum(listUnitLayer1.get(i).intArrayListField), -i*100 - 45);
			assertNull(listUnitLayer12.get(i).intListField);
			assertNull(listUnitLayer12.get(i).intArrayListField);
		}
		listUnitLayer12.setIntListField(listUnitLayer1.intArrayListField);
		listUnitLayer12.setIntArrayListField(listUnitLayer1.intListField);
		for (int i=0; i < sz1; i++) {
			assertEquals(listUnitLayer12.get(i).intListField.size(), sz1);
			assertEquals(sum(listUnitLayer12.get(i).intListField), -i*100 - 45);
			assertEquals(listUnitLayer12.get(i).intArrayListField.size(), sz1);
			assertEquals(sum(listUnitLayer12.get(i).intArrayListField), i*100 + 45);
		}
	}
	
	@Test
	public void test2() {
		initLayers2();
		for (int i=0; i < sz2; i++) {
			for (int j=0; j < sz2; j++) {
				assertEquals(listUnitLayer2.get(i,j).intListField.size(), sz1);
				assertEquals(sum(listUnitLayer2.get(i,j).intListField), (i+j)*100 + 45);
				assertEquals(listUnitLayer2.get(i,j).intArrayListField.size(), sz1);
				assertEquals(sum(listUnitLayer2.get(i,j).intArrayListField), -(i+j)*100 - 45);
				assertNull(listUnitLayer22.get(i,j).intListField);
				assertNull(listUnitLayer22.get(i,j).intArrayListField);
			}
		}
		listUnitLayer22.setIntListField(listUnitLayer2.intArrayListField);
		listUnitLayer22.setIntArrayListField(listUnitLayer2.intListField);
		for (int i=0; i < sz2; i++) {
			for (int j=0; j < sz2; j++) {
				assertEquals(listUnitLayer22.get(i,j).intListField.size(), sz1);
				assertEquals(sum(listUnitLayer22.get(i,j).intListField), -(i+j)*100 - 45);
				assertEquals(listUnitLayer22.get(i,j).intArrayListField.size(), sz1);
				assertEquals(sum(listUnitLayer22.get(i,j).intArrayListField), (i+j)*100 + 45);
			}
		}
	}
	
	@Test
	public void test3() {
		initLayers3();
		for (int i=0; i < sz3; i++) {
			for (int j=0; j < sz3; j++) {
				for (int k=0; k < sz3; k++) {
					assertEquals(listUnitLayer3.get(i,j,k).intListField.size(), sz1);
					assertEquals(sum(listUnitLayer3.get(i,j,k).intListField), (i+j+k)*100 + 45);
					assertEquals(listUnitLayer3.get(i,j,k).intArrayListField.size(), sz1);
					assertEquals(sum(listUnitLayer3.get(i,j,k).intArrayListField), -(i+j+k)*100 - 45);
					assertNull(listUnitLayer32.get(i,j,k).intListField);
					assertNull(listUnitLayer32.get(i,j,k).intArrayListField);
				}
			}
		}
		listUnitLayer32.setIntListField(listUnitLayer3.intArrayListField);
		listUnitLayer32.setIntArrayListField(listUnitLayer3.intListField);
		for (int i=0; i < sz3; i++) {
			for (int j=0; j < sz3; j++) {
				for (int k=0; k < sz3; k++) {
					assertEquals(listUnitLayer32.get(i,j,k).intListField.size(), sz1);
					assertEquals(sum(listUnitLayer32.get(i,j,k).intListField), -(i+j+k)*100 - 45);
					assertEquals(listUnitLayer32.get(i,j,k).intArrayListField.size(), sz1);
					assertEquals(sum(listUnitLayer32.get(i,j,k).intArrayListField), (i+j+k)*100 + 45);
				}
			}
		}
	}
  
}

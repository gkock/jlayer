package more.tests;

import org.jlayer.util.IndexTools;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.Iterator;

/**
 * 
 * tests IndexTool.Orientation and IndexTools.getOrientation()
 * as well as the fact, that Relation "isNeighbour" connects units regularly
 *
 */
@Test(groups = {"suiteC"})
public class C_IndexUnit_Test {
	
	final int sz = 10;
	
	IndexUnit[][] indexArray;
	Layer_IndexUnit_ indexLayer;
    
    private void checkInnerUnit(IndexUnit posUnit) {
    	assertEquals(posUnit.neighbours.length, 8);
    	
    	IndexUnit neighbour_0 = posUnit.neighbours[0];
		IndexUnit neighbour_1 = posUnit.neighbours[1];
		IndexUnit neighbour_2 = posUnit.neighbours[2];
		IndexUnit neighbour_3 = posUnit.neighbours[3];
		IndexUnit neighbour_4 = posUnit.neighbours[4];
		IndexUnit neighbour_5 = posUnit.neighbours[5];
		IndexUnit neighbour_6 = posUnit.neighbours[6];
		IndexUnit neighbour_7 = posUnit.neighbours[7];
		
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_0.index), IndexTools.Orientation.NW);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_1.index), IndexTools.Orientation.N);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_2.index), IndexTools.Orientation.NE);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_3.index), IndexTools.Orientation.W);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_4.index), IndexTools.Orientation.E);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_5.index), IndexTools.Orientation.SW);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_6.index), IndexTools.Orientation.S);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_7.index), IndexTools.Orientation.SE);
    }
    
	private void checkLeftUnit(IndexUnit posUnit) {
		assertEquals(posUnit.neighbours.length, 5);
		
		IndexUnit neighbour_0 = posUnit.neighbours[0];
		IndexUnit neighbour_1 = posUnit.neighbours[1];
		IndexUnit neighbour_2 = posUnit.neighbours[2];
		IndexUnit neighbour_3 = posUnit.neighbours[3];
		IndexUnit neighbour_4 = posUnit.neighbours[4];
		
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_0.index), IndexTools.Orientation.N);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_1.index), IndexTools.Orientation.NE);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_2.index), IndexTools.Orientation.E);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_3.index), IndexTools.Orientation.S);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_4.index), IndexTools.Orientation.SE);
	}
	
	private void checkRightUnit(IndexUnit posUnit) {
		assertEquals(posUnit.neighbours.length, 5);
		
		IndexUnit neighbour_0 = posUnit.neighbours[0];
		IndexUnit neighbour_1 = posUnit.neighbours[1];
		IndexUnit neighbour_2 = posUnit.neighbours[2];
		IndexUnit neighbour_3 = posUnit.neighbours[3];
		IndexUnit neighbour_4 = posUnit.neighbours[4];
		
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_0.index), IndexTools.Orientation.NW);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_1.index), IndexTools.Orientation.N);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_2.index), IndexTools.Orientation.W);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_3.index), IndexTools.Orientation.SW);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_4.index), IndexTools.Orientation.S);
	}
	
	private void checkUpperUnit(IndexUnit posUnit) {
		assertEquals(posUnit.neighbours.length, 5);
		
		IndexUnit neighbour_0 = posUnit.neighbours[0];
		IndexUnit neighbour_1 = posUnit.neighbours[1];
		IndexUnit neighbour_2 = posUnit.neighbours[2];
		IndexUnit neighbour_3 = posUnit.neighbours[3];
		IndexUnit neighbour_4 = posUnit.neighbours[4];
		
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_0.index), IndexTools.Orientation.W);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_1.index), IndexTools.Orientation.E);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_2.index), IndexTools.Orientation.SW);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_3.index), IndexTools.Orientation.S);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_4.index), IndexTools.Orientation.SE);
	}
	
	private void checkLowerUnit(IndexUnit posUnit) {
		assertEquals(posUnit.neighbours.length, 5);
		
		IndexUnit neighbour_0 = posUnit.neighbours[0];
		IndexUnit neighbour_1 = posUnit.neighbours[1];
		IndexUnit neighbour_2 = posUnit.neighbours[2];
		IndexUnit neighbour_3 = posUnit.neighbours[3];
		IndexUnit neighbour_4 = posUnit.neighbours[4];
		
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_0.index), IndexTools.Orientation.NW);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_1.index), IndexTools.Orientation.N);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_2.index), IndexTools.Orientation.NE);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_3.index), IndexTools.Orientation.W);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_4.index), IndexTools.Orientation.E);
	}
	
	private void checkUnit_00(IndexUnit posUnit) {
		assertEquals(posUnit.neighbours.length, 3);
		
		IndexUnit neighbour_0 = posUnit.neighbours[0];
		IndexUnit neighbour_1 = posUnit.neighbours[1];
		IndexUnit neighbour_2 = posUnit.neighbours[2];
		
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_0.index), IndexTools.Orientation.E);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_1.index), IndexTools.Orientation.S);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_2.index), IndexTools.Orientation.SE);
	}
	
	private void checkUnit_0N(IndexUnit posUnit) {
		assertEquals(posUnit.neighbours.length, 3);

		IndexUnit neighbour_0 = posUnit.neighbours[0];
		IndexUnit neighbour_1 = posUnit.neighbours[1];
		IndexUnit neighbour_2 = posUnit.neighbours[2];
		
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_0.index), IndexTools.Orientation.W);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_1.index), IndexTools.Orientation.SW);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_2.index), IndexTools.Orientation.S);
	}
	
	private void checkUnit_N0(IndexUnit posUnit) {
		assertEquals(posUnit.neighbours.length, 3);
		
		IndexUnit neighbour_0 = posUnit.neighbours[0];
		IndexUnit neighbour_1 = posUnit.neighbours[1];
		IndexUnit neighbour_2 = posUnit.neighbours[2];
		
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_0.index), IndexTools.Orientation.N);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_1.index), IndexTools.Orientation.NE);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_2.index), IndexTools.Orientation.E);
	}
	
	private void checkUnit_NN(IndexUnit posUnit) {
		assertEquals(posUnit.neighbours.length, 3);
		
		IndexUnit neighbour_0 = posUnit.neighbours[0];
		IndexUnit neighbour_1 = posUnit.neighbours[1];
		IndexUnit neighbour_2 = posUnit.neighbours[2];
		
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_0.index), IndexTools.Orientation.NW);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_1.index), IndexTools.Orientation.N);
		assertEquals(IndexTools.getOrientation(posUnit.index, neighbour_2.index), IndexTools.Orientation.W);
	}
	
	@BeforeClass
	public void beforeClass() {
		indexArray = new IndexUnit[sz][sz];
		for(int i = 0; i < sz; i++) {
			for(int j = 0; j < sz; j++) {
				indexArray[i][j] = new IndexUnit();
			}
		} 
		indexLayer = new Layer_IndexUnit_(indexArray);
		indexLayer.neighbours.connect(indexLayer, IndexTools.isNeighbour);
	}

	@Test
	void testConnectedNeighbours() {
		for(int i = 0; i < sz; i++) {
			for(int j = 0; j < sz; j++) {
				
				IndexUnit posUnit = indexLayer.get(i, j);
				assertNull( IndexTools.getOrientation(posUnit.index, posUnit.index) );
				
				if ( (i == 0) && (j == 0) ) {
					checkUnit_00(posUnit);
				} else if ( (i == 0) && (j == sz-1) ) {
					checkUnit_0N(posUnit);
				} else if ( (i == sz-1) && (j == 0) ) {
					checkUnit_N0(posUnit);
				} else if ( (i == sz-1) && (j == sz-1) ) {
					checkUnit_NN(posUnit);
				} else if (i == 0) {
					checkUpperUnit(posUnit);
				} else if (i == sz-1) {
					checkLowerUnit(posUnit);
				} else if (j == 0) {
					checkLeftUnit(posUnit);
				} else if (j == sz-1) {
					checkRightUnit(posUnit);
				} else {
					checkInnerUnit(posUnit);
				}
				
			}
		}    
	}
	
	@Test
	void testVirtualNeighbours() {
		IndexUnit neighbour_0 = indexLayer.get(0, 0);
		IndexUnit neighbour_1 = indexLayer.get(0, sz-1);
		IndexUnit neighbour_2 = indexLayer.get(sz-1, 0);
		IndexUnit neighbour_3 = indexLayer.get(sz-1, sz-1);
		IndexUnit neighbour_4 = indexLayer.get(5, 4);
		
		int[] virtualIndex0 = new int[]{-3,-4};
		int[] virtualIndex1 = new int[]{-3,6};
		int[] virtualIndex2 = new int[]{13,2};
		int[] virtualIndex3 = new int[]{3,-3};
		
		assertEquals(IndexTools.getOrientation(neighbour_0.index, virtualIndex0), IndexTools.Orientation.NWW);
		assertEquals(IndexTools.getOrientation(neighbour_1.index, virtualIndex0), IndexTools.Orientation.NWW);
		assertEquals(IndexTools.getOrientation(neighbour_2.index, virtualIndex0), IndexTools.Orientation.NNW);
		assertEquals(IndexTools.getOrientation(neighbour_3.index, virtualIndex0), IndexTools.Orientation.NWW);
		assertEquals(IndexTools.getOrientation(neighbour_4.index, virtualIndex0), IndexTools.Orientation.NW);
		
		assertEquals(IndexTools.getOrientation(neighbour_0.index, virtualIndex1), IndexTools.Orientation.NEE);
		assertEquals(IndexTools.getOrientation(neighbour_1.index, virtualIndex1), IndexTools.Orientation.NW);
		assertEquals(IndexTools.getOrientation(neighbour_2.index, virtualIndex1), IndexTools.Orientation.NNE);
		assertEquals(IndexTools.getOrientation(neighbour_3.index, virtualIndex1), IndexTools.Orientation.NNW);
		assertEquals(IndexTools.getOrientation(neighbour_4.index, virtualIndex1), IndexTools.Orientation.NNE);
		
		assertEquals(IndexTools.getOrientation(neighbour_0.index, virtualIndex2), IndexTools.Orientation.SSE);
		assertEquals(IndexTools.getOrientation(neighbour_1.index, virtualIndex2), IndexTools.Orientation.SSW);
		assertEquals(IndexTools.getOrientation(neighbour_2.index, virtualIndex2), IndexTools.Orientation.SSE);
		assertEquals(IndexTools.getOrientation(neighbour_3.index, virtualIndex2), IndexTools.Orientation.SWW);
		assertEquals(IndexTools.getOrientation(neighbour_4.index, virtualIndex2), IndexTools.Orientation.SSW);
		
		assertEquals(IndexTools.getOrientation(neighbour_0.index, virtualIndex3), IndexTools.Orientation.SW);
		assertEquals(IndexTools.getOrientation(neighbour_1.index, virtualIndex3), IndexTools.Orientation.SWW);
		assertEquals(IndexTools.getOrientation(neighbour_2.index, virtualIndex3), IndexTools.Orientation.NNW);
		assertEquals(IndexTools.getOrientation(neighbour_3.index, virtualIndex3), IndexTools.Orientation.NWW);
		assertEquals(IndexTools.getOrientation(neighbour_4.index, virtualIndex3), IndexTools.Orientation.NWW);
	}
	
	@Test
	void testXOrientations() {
		int[] fromIndex = new int[]{2, 2};
		
		int[] xNEIndex = new int[]{0, 3};
		int[] NExIndex = new int[]{1, 4};
		int[] xNWIndex = new int[]{-1, 1};
		int[] NWxIndex = new int[]{1, 0};
		
		int[] xSEIndex = new int[]{5, 4};
		int[] SExIndex = new int[]{4, 5};
		int[] xSWIndex = new int[]{5, 1};
		int[] SWxIndex = new int[]{3, -1};
		
		assertEquals(IndexTools.getOrientation(fromIndex, xNEIndex), IndexTools.Orientation.NNE);
		assertEquals(IndexTools.getOrientation(fromIndex, NExIndex), IndexTools.Orientation.NEE);
		assertEquals(IndexTools.getOrientation(fromIndex, xNWIndex), IndexTools.Orientation.NNW);
		assertEquals(IndexTools.getOrientation(fromIndex, NWxIndex), IndexTools.Orientation.NWW);
		
		assertEquals(IndexTools.getOrientation(fromIndex, xSEIndex), IndexTools.Orientation.SSE);
		assertEquals(IndexTools.getOrientation(fromIndex, SExIndex), IndexTools.Orientation.SEE);
		assertEquals(IndexTools.getOrientation(fromIndex, xSWIndex), IndexTools.Orientation.SSW);
		assertEquals(IndexTools.getOrientation(fromIndex, SWxIndex), IndexTools.Orientation.SWW);
	}
	
	@Test
	void testFullIndex() {
		final int sz1 = 10;
		final int sz2 = 3;
		final int sz3 = 2;
		
		IndexUnit[] indexArray1;
		IndexUnit[][] indexArray2;
		IndexUnit[][][] indexArray3;
		
		Layer_IndexUnit_ indexLayer1;
		Layer_IndexUnit_ indexLayer2;
		Layer_IndexUnit_ indexLayer3;
		
		indexArray1 = new IndexUnit[sz1];
		indexArray2 = new IndexUnit[sz2][sz2];
		indexArray3 = new IndexUnit[sz3][sz3][sz3];
		
		for(int i = 0; i < sz1; i++) {
			indexArray1[i] = new IndexUnit();
		} 
		for(int i = 0; i < sz2; i++) {
			for(int j = 0; j < sz2; j++) {
				indexArray2[i][j] = new IndexUnit();
			}
		} 
		for(int i = 0; i < sz3; i++) {
			for(int j = 0; j < sz3; j++) {
				for(int k = 0; k < sz3; k++) {
					indexArray3[i][j][k] = new IndexUnit();
				}
			}
		} 
		
		indexLayer1 = new Layer_IndexUnit_(indexArray1);
		indexLayer2 = new Layer_IndexUnit_(indexArray2);
		indexLayer3 = new Layer_IndexUnit_(indexArray3);
		
		int cnt, checksum;
		Iterator<IndexUnit> it;
		IndexUnit unit;
		
		cnt = 0;
		checksum = 0;
		it = indexLayer1.iterator();
		while (it.hasNext()) {
			unit = it.next();
			cnt += unit.fullIndex[0];
		}
		checksum = sz1 * (sz1-1) / 2;
		assertEquals(cnt, checksum);
		
		cnt = 0;
		checksum = 0;
		it = indexLayer2.iterator();
		while (it.hasNext()) {
			unit = it.next();
			cnt += unit.fullIndex[0];
			cnt += unit.fullIndex[1];
		}
		checksum = sz2 * (sz2-1) / 2;
		assertEquals(cnt, 2 * checksum * checksum);
		
		cnt = 0;
		checksum = 0;
		it = indexLayer3.iterator();
		while (it.hasNext()) {
			unit = it.next();
			cnt += unit.fullIndex[0];
			cnt += unit.fullIndex[1];
			cnt += unit.fullIndex[2];
		}
		checksum = 12;
		assertEquals(cnt, checksum);
	}
	
	@Test
	void testPartialIndex() {
		final int sz1 = 10;
		final int sz2 = 3;
		final int sz3 = 2;
		
		IndexUnit[] indexArray1;
		IndexUnit[][] indexArray2;
		IndexUnit[][][] indexArray3;
		
		Layer_IndexUnit_ indexLayer1;
		Layer_IndexUnit_ indexLayer2;
		Layer_IndexUnit_ indexLayer3;
		
		indexArray1 = new IndexUnit[sz1];
		indexArray2 = new IndexUnit[sz2][sz2];
		indexArray3 = new IndexUnit[sz3][sz3][sz3];
		
		for(int i = 0; i < sz1; i++) {
			if (i%2 == 0) indexArray1[i] = new IndexUnit();
		} 
		for(int i = 0; i < sz2; i++) {
			for(int j = 0; j < sz2; j++) {
				if (i != 2) indexArray2[i][j] = new IndexUnit();
			}
		} 
		for(int i = 0; i < sz3; i++) {
			for(int j = 0; j < sz3; j++) {
				for(int k = 0; k < sz3; k++) {
					if (j != 0) indexArray3[i][j][k] = new IndexUnit();
				}
			}
		} 
		
		indexLayer1 = new Layer_IndexUnit_(indexArray1);
		indexLayer2 = new Layer_IndexUnit_(indexArray2);
		indexLayer3 = new Layer_IndexUnit_(indexArray3);
		
		int cnt, checksum;
		Iterator<IndexUnit> it;
		IndexUnit unit;
		
		cnt = 0;
		checksum = 0;
		it = indexLayer1.iterator();
		while (it.hasNext()) {
			unit = it.next();
			cnt += unit.partialIndex[0];
		}
		checksum = 20;
		assertEquals(cnt, checksum);
		
		cnt = 0;
		checksum = 0;
		it = indexLayer2.iterator();
		while (it.hasNext()) {
			unit = it.next();
			cnt += unit.partialIndex[0];
			cnt += unit.partialIndex[1];
		}
		checksum = 9;
		assertEquals(cnt, checksum);
		
		cnt = 0;
		checksum = 0;
		it = indexLayer3.iterator();
		while (it.hasNext()) {
			unit = it.next();
			cnt += unit.partialIndex[0];
			cnt += unit.partialIndex[1];
			cnt += unit.partialIndex[2];
		}
		checksum = 8;
		assertEquals(cnt, checksum);
	}
	
}

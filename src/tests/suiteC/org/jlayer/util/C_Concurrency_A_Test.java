package org.jlayer.util;

import org.jlayer.model.Relation;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import basic.units.*;

/**
 * 
 * testing concurrency, basic figures for void and parameterless methods
 * 
 * @author Gerd Kock
 */
@Test(groups = {"suiteC"})
public class C_Concurrency_A_Test {
  
	final int sz1 = 999;
	final int sz2 = 29;
	final int sz3 = 23;
	
	Unit3[] array1;
	Unit3[][] array2;
	Unit3[][][] array3;
	Layer_Unit3_ layer1, layer2, layer3;
	
	Relation full = new Relation(){
		@Override
		public boolean contains(int[] x, int[] y){ return true; }
	};
	
	int procNo = Runtime.getRuntime().availableProcessors();
	Map<Long, Integer> proc2Units = new HashMap<Long, Integer>();
	
	long startTime, endTime;
	long key;
	int value;
	
	@BeforeClass
    public void beforeClass() {
		array1 = new Unit3[sz1];
		for (int i = 0; i < sz1; i++) {	
			array1[i] = new Unit3(i); 
		}
		array2 = new Unit3[sz2][sz2];
		for (int i = 0; i < sz2; i++) {	
			for (int j = 0; j < sz2; j++) {	
				array2[i][j] = new Unit3(i+j); 
			}
		}
		array3 = new Unit3[sz3][sz3][sz3];
		for (int i = 0; i < sz3; i++) {	
			for (int j = 0; j < sz3; j++) {	
				for (int k = 0; k < sz3; k++) {	
					array3[i][j][k] = new Unit3(i+j+k); 
				}
			}
		}
		layer1 = new Layer_Unit3_(array1);
		layer2 = new Layer_Unit3_(array2);
		layer3 = new Layer_Unit3_(array3);
		layer1.unit3Vector.connect(layer3, full);
		layer2.unit3Vector.connect(layer2, full);
		layer3.unit3Vector.connect(layer1, full);
	}
	
	@Test
	public void  voidMethod_fork() {
		// TEST OUTPUT
//		System.out.println("TEST OUTPUT voidMethod_fork()");
		
		// layer1
		startTime = System.currentTimeMillis();
		layer1.voidMethod_fork();
		endTime = System.currentTimeMillis();
		// TEST OUTPUT
//		System.out.println("   layer1.voidMethod_fork() took " + (endTime - startTime) + " milliseconds.");
		proc2Units.clear();
		Iterator<Unit3> iter1 = layer1.iterator();
		while (iter1.hasNext()) {
			key = iter1.next().threadId;
			if (!proc2Units.containsKey(key)) {
				proc2Units.put(key, 1);
			} else {
				value = proc2Units.get(key);
				proc2Units.put(key, ++value);
			}
		}
		if (proc2Units.size() == procNo) {
			for (Map.Entry<Long, Integer> entry : proc2Units.entrySet()) {
				value = entry.getValue();
				assertTrue( (value==sz1/procNo) || (value==sz1/procNo+1) );
			}
		} else {
			System.out.printf("   C_Concurrency_Test: D1 NO OF (USED) PROCESSORS: (%d) %d %n", proc2Units.size(), procNo);
		}
		
		// layer2
		startTime = System.currentTimeMillis();
		layer2.voidMethod_fork();
		endTime = System.currentTimeMillis();
		// TEST OUTPUT
//		System.out.println("   layer2.voidMethod_fork() took " + (endTime - startTime) + " milliseconds.");
		proc2Units.clear();
		Iterator<Unit3> iter2 = layer2.iterator();
		while (iter2.hasNext()) {
			key = iter2.next().threadId;
			if (!proc2Units.containsKey(key)) {
				proc2Units.put(key, 1);
			} else {
				value = proc2Units.get(key);
				proc2Units.put(key, ++value);
			}
		}
		if (proc2Units.size() == procNo) {
			for (Map.Entry<Long, Integer> entry : proc2Units.entrySet()) {
				value = entry.getValue();
				assertTrue( (value==sz2/procNo*sz2) || (value==(sz2/procNo+1)*sz2) );
			}
		} else {
			System.out.printf("   C_Concurrency_Test: D2 NO OF (USED) PROCESSORS: (%d) %d %n", proc2Units.size(), procNo);
		}
		
		// layer3
		startTime = System.currentTimeMillis();
		layer3.voidMethod_fork();
		endTime = System.currentTimeMillis();
		// TEST OUTPUT
//		System.out.println("   layer3.voidMethod_fork() took " + (endTime - startTime) + " milliseconds.");
		proc2Units.clear();
		Iterator<Unit3> iter3 = layer3.iterator();
		while (iter3.hasNext()) {
			key = iter3.next().threadId;
			if (!proc2Units.containsKey(key)) {
				proc2Units.put(key, 1);
			} else {
				value = proc2Units.get(key);
				proc2Units.put(key, ++value);
			}
		}
		if (proc2Units.size() == procNo) {
			for (Map.Entry<Long, Integer> entry : proc2Units.entrySet()) {
				value = entry.getValue();
				assertTrue( (value==sz3/procNo*sz3*sz3) || (value==(sz3/procNo+1)*sz3*sz3) );
			}
		} else {
			System.out.printf("   C_Concurrency_Test: D3 NO OF (USED) PROCESSORS: (%d) %d %n", proc2Units.size(), procNo);
		}
	}
	
	@Test
	public void  voidMethod_sequ() {
		// TEST OUTPUT
//		System.out.println("TEST OUTPUT voidMethod_sequ()");
		
		// layer1
		startTime = System.currentTimeMillis();
		layer1.voidMethod_sequ();
		endTime = System.currentTimeMillis();
		// TEST OUTPUT
//		System.out.println("   layer1.voidMethod_sequ() took " + (endTime - startTime) + " milliseconds.");
		
		// layer2
		startTime = System.currentTimeMillis();
		layer2.voidMethod_sequ();
		endTime = System.currentTimeMillis();
		// TEST OUTPUT
//		System.out.println("   layer2.voidMethod_sequ() took " + (endTime - startTime) + " milliseconds.");
		
		// layer3
		startTime = System.currentTimeMillis();
		layer3.voidMethod_sequ();
		endTime = System.currentTimeMillis();
		// TEST OUTPUT
//		System.out.println("   layer3.voidMethod_sequ() took " + (endTime - startTime) + " milliseconds.");
	}
  
}

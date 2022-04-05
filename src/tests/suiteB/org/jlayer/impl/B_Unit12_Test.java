package org.jlayer.impl;

import java.util.Map;
import java.util.HashMap;

import javax.tools.JavaFileObject;
import javax.tools.JavaCompiler.CompilationTask;
import javax.lang.model.element.Modifier;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jlayer.impl.Unit.FieldDescriptor;

import static org.jlayer.impl.B_Config_Test.compiler;
import static org.jlayer.impl.B_Config_Test.diagnosticCollector;
import static org.jlayer.impl.B_Config_Test.fileManager;
import static org.jlayer.impl.B_Config_Test.optionList;
import static org.jlayer.impl.B_Config_Test.processors;
import static org.jlayer.impl.B_Config_Test.userDir;
import static org.jlayer.impl.Processor.processedUnits;

/**
 * checks the {@code org.jlayer.impl.Unit} class objects generated from 
 * {@link basic.units.Unit1} and {@link basic.units.Unit2}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteB"})
public class B_Unit12_Test {
	
	// Unit1 and Unit2 have to be processed both because of mutual references 
	String unit1 = userDir + "/src/units/basic/units/Unit1.java";
	String unit2 = userDir + "/src/units/basic/units/Unit2.java";
	Unit processedUnit1;
	Unit processedUnit2;
	
    @BeforeClass
    public void beforeClass() {
    	// clear the list of processedUnits and compile next unit
    	processedUnits.clear();
		Iterable<? extends JavaFileObject> compilationUnits = 
				fileManager.getJavaFileObjects(unit1, unit2);
		CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector,
												optionList, null, compilationUnits);
		task.setProcessors(processors);
		boolean ok = task.call();
		assertEquals(ok, true);
    }
    
    @Test
    public void testBasics() {
		assertEquals(processedUnits.size(), 2);
		processedUnit1 = processedUnits.get(0);
		processedUnit2 = processedUnits.get(1);
    	assertEquals(processedUnit1.typeDescr.packageElement.toString(), "basic.units");
    	assertEquals(processedUnit1.typeDescr.typeName.toString(), "Unit1");
    	assertEquals(processedUnit1.typeDescr.modifiers.size(), 1);
    	assertTrue(processedUnit1.typeDescr.modifiers.contains(Modifier.PUBLIC));
    	assertEquals(processedUnit2.typeDescr.packageElement.toString(), "basic.units");
    	assertEquals(processedUnit2.typeDescr.typeName.toString(), "Unit2");
    	assertEquals(processedUnit2.typeDescr.modifiers.size(), 1);
    	assertTrue(processedUnit2.typeDescr.modifiers.contains(Modifier.PUBLIC));
//    	assertTrue(processedUnit2.modifiers.contains(Modifier.FINAL));
    }
    
    @Test
    public void testFieldsOfUnit1() {
    	// check field attributes
    	final int noOfFieldItems = 12;
    	Map<String, String> fieldMap = new HashMap<String, String>(noOfFieldItems);
    	
    	fieldMap.put("intSignal", "basic.units.Unit1.SignalInt");
    	fieldMap.put("intVector", "basic.units.Unit1.SignalInt");
    	
    	fieldMap.put("doubleSignal", "basic.units.Unit1.SignalDouble");
    	fieldMap.put("doubleVector", "basic.units.Unit1.SignalDouble");
    	
    	fieldMap.put("intItem", "int");
    	fieldMap.put("noUnitItem", "basic.units.NoUnit");
    	fieldMap.put("unit1Item", "basic.units.Unit1");
    	fieldMap.put("unit2Item", "basic.units.Unit2");
    	
    	fieldMap.put("intLink", "int");
    	fieldMap.put("noUnitLink", "basic.units.NoUnit");
    	fieldMap.put("unit1Link", "basic.units.Unit1");
    	fieldMap.put("unit2Link", "basic.units.Unit2");
    	
//    	for (FieldDescriptor fDescr : processedUnit1.fields) {
//    		System.out.format("name = %s, type = %s\n", fDescr.itemName.toString(), fDescr.typeDescr.typeMirror.toString() );
//		}
    	
    	assertEquals(processedUnit1.fields.size(), noOfFieldItems);
    	for (FieldDescriptor fDescr : processedUnit1.fields) {
    		assertTrue(fieldMap.get(fDescr.itemName.toString()).equals(fDescr.typeDescr.typeMirror.toString()));
    		assertEquals(fDescr.modifiers.size(), 1);
        	assertTrue(fDescr.modifiers.contains(Modifier.PUBLIC));
		}
    }
    
    @Test
    public void testFieldsOfUnit2() {
    	// check field attributes
    	final int noOfFieldItems = 9;
    	Map<String, String> fieldMap = new HashMap<String, String>(noOfFieldItems);
    	
    	fieldMap.put("intItem", "int");
    	fieldMap.put("intSignal", "basic.units.Unit1.SignalInt");
    	fieldMap.put("intVector", "basic.units.Unit1.SignalInt");
    	fieldMap.put("doubleSignal", "basic.units.Unit1.SignalDouble");
    	fieldMap.put("doubleVector", "basic.units.Unit1.SignalDouble");
    	fieldMap.put("unit1Item", "basic.units.Unit1");
    	fieldMap.put("unit1Link", "basic.units.Unit1");
    	fieldMap.put("unit2Item", "basic.units.Unit2");
    	fieldMap.put("unit2Link", "basic.units.Unit2");
    	
    	assertEquals(processedUnit2.fields.size(), noOfFieldItems);
    	for (FieldDescriptor fDescr : processedUnit2.fields) {
    		assertTrue(fieldMap.get(fDescr.itemName.toString()).equals(fDescr.typeDescr.typeMirror.toString()));
    		assertEquals(fDescr.modifiers.size(), 1);
        	assertTrue(fDescr.modifiers.contains(Modifier.PUBLIC));
		}
    }

}

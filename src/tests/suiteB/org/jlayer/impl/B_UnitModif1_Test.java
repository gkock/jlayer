package org.jlayer.impl;

import java.util.Map;
import java.util.HashMap;

import javax.tools.JavaFileObject;
import javax.tools.JavaCompiler.CompilationTask;

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
 * checks the {@code org.jlayer.impl.Unit} class object generated from {@code basic.units.UnitModif1}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteB"})
public class B_UnitModif1_Test {
	
	String unit = userDir + "/src/units/basic/units/UnitModif1.java";
	Unit processedUnit;
	
    @BeforeClass
    public void beforeClass() {
    	// clear the list of processedUnits and compile next unit
    	processedUnits.clear();
		Iterable<? extends JavaFileObject> compilationUnits = 
				fileManager.getJavaFileObjects(unit);
		CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector,
												optionList, null, compilationUnits);
		task.setProcessors(processors);
		boolean ok = task.call();
		assertEquals(ok, true);
    }
    
    @Test
    public void testBasics() {
		assertEquals(processedUnits.size(), 1);
		processedUnit = processedUnits.get(0);
    	assertEquals(processedUnit.typeDescr.packageElement.toString(), "basic.units");
    	assertEquals(processedUnit.typeDescr.typeName.toString(), "UnitModif1");
    	assertEquals(processedUnit.typeDescr.modifiers.size(), 0);
    }
    
    @Test
    public void testFieldsOfUnit() {
    	// check field attributes
    	final int noOfFieldItems = 3;
    	Map<String, String> fieldMap = new HashMap<String, String>(noOfFieldItems);
    	
    	fieldMap.put("intItem", "int");
    	fieldMap.put("noUnitItem", "basic.units.NoUnit");
    	fieldMap.put("unit2Item", "basic.units.Unit2");
    	
    	assertEquals(processedUnit.fields.size(), noOfFieldItems);
    	for (FieldDescriptor fDescr : processedUnit.fields) {
    		assertTrue(fieldMap.get(fDescr.itemName.toString()).equals(fDescr.typeDescr.typeMirror.toString()));
    		assertEquals(fDescr.modifiers.size(), 0);
		}
    }

}

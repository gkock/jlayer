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
import org.jlayer.impl.Unit.MethodDescriptor;

import static org.jlayer.impl.B_Config_Test.compiler;
import static org.jlayer.impl.B_Config_Test.diagnosticCollector;
import static org.jlayer.impl.B_Config_Test.fileManager;
import static org.jlayer.impl.B_Config_Test.optionList;
import static org.jlayer.impl.B_Config_Test.processors;
import static org.jlayer.impl.B_Config_Test.userDir;
import static org.jlayer.impl.Processor.processedUnits;

/**
 * checks the {@code org.jlayer.impl.Unit} class object generated from {@link basic.units.LifeUnit}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteB"})
public class B_LifeUnit_Test {
	
	// the compilation unit
	String nextUnit = userDir + "/src/units/basic/units/LifeUnit.java";
	Unit processedUnit;
	
    @BeforeClass
    public void beforeClass() {
		// clear the list of processedUnits and compile next unit
    	processedUnits.clear();
		Iterable<? extends JavaFileObject> compilationUnits = 
				fileManager.getJavaFileObjects(nextUnit);
		CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector,
												optionList, null, compilationUnits);
		task.setProcessors(processors);
		boolean ok = task.call();
		assertEquals(ok, true);
    }
    
    @Test
    public void testBasics() {
		// check basics
		assertEquals(processedUnits.size(), 1);
		processedUnit = processedUnits.get(0);
    	assertEquals(processedUnit.typeDescr.packageElement.toString(), "basic.units");
    	assertEquals(processedUnit.typeDescr.typeName.toString(), "LifeUnit");
    }
    
    @Test
    public void testFields() {
    	// check field attributes
    	final int noOfFieldItems = 2;
    	Map<String, String> fieldMap = new HashMap<String, String>(noOfFieldItems);
    	fieldMap.put("state", "int");
    	fieldMap.put("vector", "basic.units.LifeUnit");
    	for (FieldDescriptor fDescr : processedUnit.fields) {
    		assertTrue(fieldMap.get(fDescr.itemName.toString()).equals(fDescr.typeDescr.typeMirror.toString()));
		}
    }
    
    @Test
    public void testMethods() {
    	// check method attributes
    	final int noOfMethodItems = 3;
    	Map<String, String> methodMap = new HashMap<String, String>(noOfMethodItems);
    	methodMap.put("initState", "(java.util.Random)void");
    	methodMap.put("nextState", "()void");
    	methodMap.put("updateState", "()void");
    	for (MethodDescriptor mDescr : processedUnit.methods) {
    		assertTrue(methodMap.get(mDescr.itemName.toString()).equals(mDescr.itemType.toString()));
		}
    	
    	
    }

}

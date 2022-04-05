package org.jlayer.impl;

import javax.tools.JavaFileObject;
import javax.tools.JavaCompiler.CompilationTask;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import static org.testng.Assert.assertEquals;

import static org.jlayer.impl.B_Config_Test.compiler;
import static org.jlayer.impl.B_Config_Test.diagnosticCollector;
import static org.jlayer.impl.B_Config_Test.fileManager;
import static org.jlayer.impl.B_Config_Test.optionList;
import static org.jlayer.impl.B_Config_Test.processors;
import static org.jlayer.impl.B_Config_Test.userDir;
import static org.jlayer.impl.Processor.processedUnits;

/**
 * checks the {@code org.jlayer.impl.Unit} class object generated from {@code Unit0}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteB"})
public class B_Unit0_Test {
	
	// the compilation unit
	String nextUnit = userDir + "/src/units/Unit0.java";
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
    	assertEquals(processedUnit.typeDescr.packageElement.isUnnamed(), true);
    	assertEquals(processedUnit.typeDescr.typeName.toString(), "Unit0");
    }
 

}

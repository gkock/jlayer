package org.jlayer.impl;

import java.util.Iterator;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.JavaCompiler.CompilationTask;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import static org.testng.Assert.assertEquals;

import static org.jlayer.impl.B_Config_Test.compiler;
import static org.jlayer.impl.B_Config_Test.fileManager;
import static org.jlayer.impl.B_Config_Test.optionList;
import static org.jlayer.impl.B_Config_Test.processors;
import static org.jlayer.impl.B_Config_Test.userDir;
import static org.jlayer.impl.Processor.processedUnits;

/**
 * checks the {@code org.jlayer.impl.Unit} class objects generated from {@code faulty.units.Warning3}.
 * @author Gerd Kock
 */
@Test(groups = {"suiteB"})
public class B_Warning3_Test {
	
	// All classes that evoke warnings 
	String unit = userDir + "/src/units/faulty/units/Warning3.java";
	
	// a "local" diagnostic collector to be used in this class
	private DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();
	
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
    }
    
    @Test
    public void testWarning() {
    	Unit processedUnit = processedUnits.get(0);
		assertEquals(processedUnit.typeDescr.packageElement.toString(), "faulty.units");
		assertEquals(processedUnit.typeDescr.typeName.toString(), "Warning3");
		assertEquals(processedUnit.fields.size(), 3);
		assertEquals(processedUnit.methods.size(), 4);
    }
    
    @AfterClass
    public void afterClass() {
    	List<Diagnostic<? extends JavaFileObject>> dList = diagnosticCollector.getDiagnostics();
    	assertEquals(dList.size(), 6);
    	// TEST OUTPUT
//    	for (Iterator<Diagnostic<? extends JavaFileObject>> iterator = dList.iterator(); iterator.hasNext();) {
//			Diagnostic<? extends JavaFileObject> diagnostic = iterator.next();
//			System.out.format(">>> DIAGNOSTICS of kind %s <<<\n%s\n", diagnostic.getKind(), diagnostic.getMessage(null));
//		}
    }
		
}
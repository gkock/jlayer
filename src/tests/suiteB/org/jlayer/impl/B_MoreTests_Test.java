package org.jlayer.impl;

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
 * checks the {@code org.jlayer.impl.Unit} class objects generated from package {@link more.tests},
 * 
 * @author Gerd Kock
 */
@Test(groups = {"suiteB"})
//@Test(groups = {"suiteB", "choice"})
public class B_MoreTests_Test {
	
	final int no = 6;
	
	// All classes that evoke warnings 
	String unit1 = userDir + "/src/units/more/tests/InUnit.java";
	String unit2 = userDir + "/src/units/more/tests/OutUnit.java";
	String unit3 = userDir + "/src/units/more/tests/ListUnit.java";
	String unit4 = userDir + "/src/units/more/tests/InterfaceUnit.java";
	String unit5 = userDir + "/src/units/more/tests/IndexUnit.java";
	String unit6 = userDir + "/src/units/more/tests/ValUnit.java";
	Unit processedUnit;
	
	// a "local" diagnostic collector to be used in this class
	private DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();
	
    @BeforeClass
    public void beforeClass() {
    	// clear the list of processedUnits and compile next unit
    	processedUnits.clear();
		Iterable<? extends JavaFileObject> compilationUnits = 
				fileManager.getJavaFileObjects(unit1, unit2, unit3, unit4, unit5, unit6);
		CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector,
												optionList, null, compilationUnits);
		task.setProcessors(processors);
		boolean ok = task.call();
		assertEquals(ok, true);
    }
    
    @Test
    public void testBasics() {
    	assertEquals(processedUnits.size(), no);
    	
    	processedUnit = processedUnits.get(0);
    	assertEquals(processedUnit.typeDescr.packageElement.toString(), "more.tests");
		assertEquals(processedUnit.typeDescr.typeName.toString(), "InUnit");
    	
    	processedUnit = processedUnits.get(1);
    	assertEquals(processedUnit.typeDescr.packageElement.toString(), "more.tests");
		assertEquals(processedUnit.typeDescr.typeName.toString(), "OutUnit");
		
		processedUnit = processedUnits.get(2);
    	assertEquals(processedUnit.typeDescr.packageElement.toString(), "more.tests");
		assertEquals(processedUnit.typeDescr.typeName.toString(), "ListUnit");

		processedUnit = processedUnits.get(3);
    	assertEquals(processedUnit.typeDescr.packageElement.toString(), "more.tests");
		assertEquals(processedUnit.typeDescr.typeName.toString(), "InterfaceUnit");
		
		processedUnit = processedUnits.get(4);
    	assertEquals(processedUnit.typeDescr.packageElement.toString(), "more.tests");
		assertEquals(processedUnit.typeDescr.typeName.toString(), "IndexUnit");
    }
    
    
    @AfterClass
    public void afterClass() {
    	List<Diagnostic<? extends JavaFileObject>> dList = diagnosticCollector.getDiagnostics();
    	assertEquals(dList.size(), 0);
    	// TEST OUTPUT
//    	for (Iterator<Diagnostic<? extends JavaFileObject>> iterator = dList.iterator(); iterator.hasNext();) {
//			Diagnostic<? extends JavaFileObject> diagnostic = iterator.next();
//			System.out.format(">>> DIAGNOSTICS of kind %s <<<\n%s\n", diagnostic.getKind(), diagnostic.getMessage(null));
//		}
    }
		
}
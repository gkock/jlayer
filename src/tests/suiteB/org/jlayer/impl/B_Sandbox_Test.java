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
 * Sandbox_Test
 * 
 * @author Gerd Kock
 */
@Test(groups = {"suiteB"})
//@Test(groups = {"suiteB", "choice"})
public class B_Sandbox_Test {
	
	// All classes that evoke warnings 
	String unit1 = userDir + "/src/units/sandbox/Unit1.java";
	String unit2 = userDir + "/src/units/sandbox/Unit2.java";
	String unit3 = userDir + "/src/units/sandbox/Signal.java";
	Unit processedUnit1;
	Unit processedUnit2;
	
	// a "local" diagnostic collector to be used in this class
	private DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();
	
    @BeforeClass
    public void beforeClass() {
    	// clear the list of processedUnits and compile next unit
    	processedUnits.clear();
		Iterable<? extends JavaFileObject> compilationUnits = 
				fileManager.getJavaFileObjects(unit1, unit2, unit3);
		CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector,
												optionList, null, compilationUnits);
		task.setProcessors(processors);
		boolean ok = task.call();
		assertEquals(ok, true);
    }
    
    @Test
    public void testBasics() {
    	assertEquals(processedUnits.size(), 3);
    	
    	processedUnit1 = processedUnits.get(0);
    	assertEquals(processedUnit1.typeDescr.packageElement.toString(), "sandbox");
		assertEquals(processedUnit1.typeDescr.typeName.toString(), "Unit1");
//		assertEquals(processedUnit1.fields.size(), 2);
//		assertEquals(processedUnit1.methods.size(), 7);
    	
    	processedUnit2 = processedUnits.get(1);
    	assertEquals(processedUnit2.typeDescr.packageElement.toString(), "sandbox");
		assertEquals(processedUnit2.typeDescr.typeName.toString(), "Unit2");
//		assertEquals(processedUnit2.fields.size(), 2);
//		assertEquals(processedUnit2.methods.size(), 1);
		
    	processedUnit2 = processedUnits.get(2);
    	assertEquals(processedUnit2.typeDescr.packageElement.toString(), "sandbox");
		assertEquals(processedUnit2.typeDescr.typeName.toString(), "Signal");
//		assertEquals(processedUnit2.fields.size(), 2);
//		assertEquals(processedUnit2.methods.size(), 1);
    }
    
//    @Test
//    public void testMore() {
//    	
//    	System.out.println("");
//    	
//    	System.out.println("MyUnit");
//    	System.out.println("======");
//    	for (Unit.FieldDescriptor field : processedUnit1.fields){
//    		System.out.printf("%s%n", field.toString());
//    		System.out.printf("%s is connectable: %b %n", field.itemName, field.typeDescr.isConnectable());
//    	}
//    	
//    	System.out.println("");
//    	
//    	System.out.println("TestUnit");
//    	System.out.println("========");
//    	for (Unit.FieldDescriptor field : processedUnit2.fields){
//    		System.out.printf("%s%n", field.toString());
//    		System.out.printf("%s is connectable: %b %n", field.itemName, field.typeDescr.isConnectable());
//    	}
//    	
//    }
    
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
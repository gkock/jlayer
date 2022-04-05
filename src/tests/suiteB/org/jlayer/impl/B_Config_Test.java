package org.jlayer.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

import javax.annotation.processing.AbstractProcessor;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

/**
 * is used for the configuration of test {@code suiteB}.
 * @author Gerd Kock
 */
//@Test(groups = "config")
@Test(groups = {"config", "choice"})
public class B_Config_Test {
	
	public static JavaCompiler compiler;
	public static DiagnosticCollector<JavaFileObject> diagnosticCollector;
	public static StandardJavaFileManager fileManager;
	public static List<AbstractProcessor> processors;
	public static String userDir = System.getProperty("user.dir");
	public static List<String> optionList = new ArrayList<String>(); // option list for the compilation tasks
  
    @BeforeSuite
    public void beforeSuite() {
    	//System.out.println("ConfigTest.beforeSuite(BEGIN)");
    	
    	// get the compiler, set the diagnostic collector, get the standard file manager
		compiler = ToolProvider.getSystemJavaCompiler();
		diagnosticCollector = new DiagnosticCollector<JavaFileObject>();
		fileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);
		
		// Set the SOURCE_PATH, SOURCE_OUTPUT, CLASS_OUTPUT of the fileManager
		Iterable<? extends File> sourcePath = Arrays.asList(new File(userDir, "src/classes"), new File(userDir, "src/tests"), new File(userDir, "src/units"));
		Iterable<? extends File> sourceOutput = Arrays.asList(new File(userDir, "src/layers"));
		Iterable<? extends File> classOutput = Arrays.asList(new File(userDir, "build"));
		try { 
			fileManager.setLocation(StandardLocation.SOURCE_PATH, sourcePath);
			fileManager.setLocation(StandardLocation.SOURCE_OUTPUT, sourceOutput);
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT, classOutput);
		} catch (IOException e){
			System.out.println("configSuiteB.B_Config_Test.beforeClass >>> IOException");
		}
		
		// create a list to hold annotation processors and add the JLayerProcessor
		processors = new LinkedList<AbstractProcessor>();
		processors.add(new Processor());
		
		// set the options for the compilation tasks
		optionList.addAll(Arrays.asList("-implicit:class"));
		// optionList.addAll(Arrays.asList("-verbose", "-implicit:class"));
		
		// TEST OUTPUT
		/* for (StandardLocation loc : StandardLocation.values()) {
			Iterable<? extends File> path = fileManager.getLocation(loc);
			System.out.println("StandardLocation " + loc + ": " + path);
		} */
    }

    @AfterSuite
    public void afterSuite() {
	    //System.out.println("ConfigTest.afterSuite(BEGIN)");
	    
	    // close the file manager
	    try {
	    	fileManager.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    
	    // in case of diagnostic messages, print them
    	for (Diagnostic<? extends JavaFileObject> diag : diagnosticCollector.getDiagnostics()) {
    		System.out.format(">>> DIAGNOSTICS of kind %s <<<\n%s\n", diag.getKind(), diag.getMessage(null));
    		System.out.println(">>> END OF MESSAGE <<<<<<<<<<<<<<");
		}
    }

}

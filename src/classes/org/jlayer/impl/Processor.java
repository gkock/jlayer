package org.jlayer.impl;

import java.util.LinkedList;
import java.util.Set;
import java.util.List;
import java.io.IOException;

import javax.lang.model.SourceVersion; 
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.AbstractProcessor;

@SupportedAnnotationTypes({"org.jlayer.annotations.*"})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class Processor extends AbstractProcessor {
	
	// A LIST OF UNITS USED FOR TEST PURPOSES ONLY
	static List<Unit> processedUnits = new LinkedList<Unit>();
	
	private ProcessingEnvironment pe = null;
	private UnitVisitor visitor = null;
	private LayerWriter writer = null;
	
	@Override
	public void init(ProcessingEnvironment processingEnv){
		pe = processingEnv;
		visitor = new UnitVisitor(pe);
		writer = new LayerWriter(pe);
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		//System.out.println("JLayerProcessor.process(BEGIN)");
		boolean visitRootOK = false;
		
		// round 0 ==> annotations.isEmpty() == false
		// round 1 ==> annotations.isEmpty() == true   (after layer generation)
		// round 2 ==> annotations.isEmpty() == true   (final round)
		if ( annotations.isEmpty() ) return true;
		
		for (Element rootElem : roundEnv.getRootElements()) {
			
			// for the given rootElem:
			// ==> create a rootUnit and collect attributes for that rootUnit
			Unit rootUnit = new Unit();
			visitRootOK = visitor.visit(rootElem, rootUnit);
			
			// for the created rootUnit:
			// ==> write the corresponding layer interface and class files
			try { 
				writer.writeFiles(rootElem, rootUnit);
			} catch (IOException e){
				System.out.printf("org.jlayer.impl.Processor >>> %s%n", e.toString());
			}
			
			// COLLECT THE UNIT OBJECTS FOR TEST PURPOSES
			processedUnits.add(rootUnit);		
			
			// TEST OUTPUT
//			System.out.println("JLayerProcessor.process(END):\n" + rootUnit);
			
		}
		return visitRootOK;
	}

}

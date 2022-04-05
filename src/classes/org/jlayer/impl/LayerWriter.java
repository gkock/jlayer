package org.jlayer.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Element;
import javax.tools.FileObject;

import org.jlayer.annotations.*;
import org.jlayer.impl.Unit.TypeDescriptor;
import org.jlayer.impl.Unit.FieldDescriptor;
import org.jlayer.impl.Unit.MethodDescriptor;
import org.jlayer.util.JLayerException;

/**
 * This class provides the method {@link #writeFiles(Element, Unit) writeFiles()},
 * which is used to create layer classes and the related interface and abstract classes.
 * 
 * @author Gerd Kock
 */
class LayerWriter {
	
	LayerWriter(ProcessingEnvironment pe) {	filer = pe.getFiler(); }
	
	private enum DimKind   { D1, D2, D3, NN }
	private enum TypeKind  { LA, LC, LP, LS, LI }  // for fields (abstract, construction) , parameters, return types (signature, implementation)
	private enum LevelKind { TOPLEVEL, NESTED }    // code for top level or nested classes
	
	private String tab0 = "   ";
	private String tab(int x){
		switch (x) {
			case 1: return tab0 + "   ";
			case 2: return tab0 + "      ";
			case 3: return tab0 + "         ";
			case 4: return tab0 + "            ";
			case 5: return tab0 + "               ";
			case 6: return tab0 + "                  ";
			default: return tab0;
		}
	}
	
	private Filer filer = null;
	
	private CharSequence packageName;
	private Unit 		 rootUnit;
	private CharSequence rootUnitType;
	private CharSequence rootILayerType, rootALayerType, rootLayerType;
	
	private StringBuilder interfaceCode, abstractCode, layerCode;
	
	/**
	 * is used to write layer classes and corresponding interfaces and abstract classes.
	 * <p>
	 * If {@code rootElem} represents the layer unit class {@code MyUnit.java},
	 * the three generated files are:
	 * <ul>
	 *   <li> interface {@code ILayer_MyUnit_.java} </li>
	 *   <li> abstract class {@code ALayer_MyUnit_.java} </li>
	 *   <li> layer class {@code Layer_MyUnit_.java} </li>
	 * </ul>
	 * 
	 * @param rootElem		this {@link javax.lang.model.element.Element element} represents a class object
	 * @param nextUnit		this {@link org.jlayer.impl.Unit unit} contains the results of visiting that class
	 * @throws IOException	may be raised while creating the corresponding source
	 *                      file or getting a writer for that file object
	 */
	void writeFiles(Element rootElem, Unit nextUnit) throws IOException {
		if (!nextUnit.isLayerUnit) return;
		
		PrintWriter  printWriter;
		FileObject 	 fileObject;
		CharSequence interfaceFileName, abstractFileName, layerFileName;
		
		rootUnit = nextUnit;
		packageName = "";
		if (!rootUnit.typeDescr.packageElement.isUnnamed()) {
			packageName = rootUnit.typeDescr.packageElement.getQualifiedName();
		}
		
		rootUnitType = rootUnit.typeDescr.typeName;
		rootILayerType = "ILayer_" + rootUnitType + "_";
		rootALayerType = "ALayer_" + rootUnitType + "_";
		rootLayerType = "Layer_" + rootUnitType + "_";
		
		if (rootUnit.typeDescr.packageElement.isUnnamed()) {
			interfaceFileName = rootILayerType;
			abstractFileName = rootALayerType;
			layerFileName = rootLayerType;
		} else {
			interfaceFileName = packageName + "." + rootILayerType;
			abstractFileName = packageName + "." + rootALayerType;
			layerFileName = packageName + "." + rootLayerType;
		}
		
		// create interface code
		interfaceCode = new StringBuilder();
		createSourceCode(interfaceCode);
		fileObject = filer.createSourceFile(interfaceFileName, rootElem);
		printWriter = new PrintWriter(fileObject.openWriter());
		printWriter.print(interfaceCode);
		printWriter.close();
		
		// create abstract code
		abstractCode = new StringBuilder();
		createSourceCode(abstractCode);
		fileObject = filer.createSourceFile(abstractFileName, rootElem);
		printWriter = new PrintWriter(fileObject.openWriter());
		printWriter.print(abstractCode);
		printWriter.close();
		
		// create source code
		layerCode = new StringBuilder();
		createSourceCode(layerCode);
		fileObject = filer.createSourceFile(layerFileName, rootElem);
		printWriter = new PrintWriter(fileObject.openWriter());
		printWriter.print(layerCode);
		printWriter.close();
	}

	//////////////////////////////////////////////////////////////////////////////
	//	CREATE SOURCE CODE				    									//
	//////////////////////////////////////////////////////////////////////////////
	
	private void createSourceCode(StringBuilder srcCode) {
		
		if ((srcCode != interfaceCode) && (srcCode != abstractCode) && (srcCode != layerCode)) {
			throw new JLayerException();
		}
		
		// preamble
		if (packageName.length() > 0) {
			srcCode.append("package " + packageName + "; \n");
			srcCode.append("\n");
		}
		appendImports(srcCode);
    	srcCode.append("\n");
		
		// top level class (first lines)
    	srcCode.append("@Generated(\"org.jlayer.Processor\") \n");
    	String unitMod0 = getModifiers0(rootUnit.typeDescr.modifiers);
		String unitMod1 = getModifiers1(rootUnit.typeDescr.modifiers);
		String unitMod1F = getModifiers1F(rootUnit.typeDescr.modifiers);
		if (srcCode == interfaceCode){
			srcCode.append(unitMod0 + "interface " + rootILayerType + " extends BasedLayer<" + rootUnitType + "> {\n");
			srcCode.append("\n");
		} else if (srcCode == abstractCode){
			srcCode.append("abstract " + unitMod1F + "class " + rootALayerType + " extends LayerBase<" + rootUnitType + ">" + " implements " + rootILayerType + " {\n");
		} else if (srcCode == layerCode){
			srcCode.append(unitMod1 + "class " + rootLayerType + " extends " + rootALayerType + " implements " + rootILayerType + " {\n");
		}
    	srcCode.append("\n");
    	
    	// nested classes
    	tab0 = "   ";
    	for (Unit nestedUnit : rootUnit.nestedUnits) {
    		// nested class (first lines)
    		CharSequence nestedUnitType;
    		CharSequence nestedILayerType, nestedALayerType, nestedLayerType;
    		
    		nestedUnitType = nestedUnit.typeDescr.typeName;
    		int ix = nestedUnitType.toString().lastIndexOf(".");
    		nestedILayerType = "ILayer_" + nestedUnitType.toString().substring(ix+1) + "_";
    		nestedALayerType = "ALayer_" + nestedUnitType.toString().substring(ix+1) + "_";
    		nestedLayerType  = "Layer_" + nestedUnitType.toString().substring(ix+1) + "_";

    		// nested class (first lines)
    		String nestedUnitMod0 = getModifiers0(nestedUnit.typeDescr.modifiers);
    		String nestedUnitMod1 = getModifiers1(nestedUnit.typeDescr.modifiers);
    		String nestedUnitMod1F = getModifiers1F(nestedUnit.typeDescr.modifiers);
    		if (srcCode == interfaceCode){
    			srcCode.append(tab(0) + "// nested interface " + nestedILayerType + "\n");
        		srcCode.append(tab(0) + nestedUnitMod0 + "interface " + nestedILayerType + " extends BasedLayer<" + nestedUnitType + "> {\n");
        		srcCode.append("\n");
    		} else if (srcCode == abstractCode){
    			srcCode.append(tab(0) + "// nested class " + nestedALayerType + "\n");
            	srcCode.append(tab(0) + "abstract " + nestedUnitMod1F + "class " + nestedALayerType + " extends LayerBase<" + nestedUnitType + ">" + " implements " + nestedILayerType + " {\n");
            	srcCode.append("\n");
    		} else if (srcCode == layerCode){
    			srcCode.append(tab(0) + "// nested class " + nestedLayerType + "\n");
            	srcCode.append(tab(0) + nestedUnitMod1 + "class " + nestedLayerType + " extends " + nestedALayerType + " implements " + nestedILayerType + " {\n");
            	srcCode.append("\n");
    		}
        	
        	// nested class (append layer items)
        	appendLayerItems(srcCode, LevelKind.NESTED, nestedUnit, nestedUnitType, 
        					 nestedILayerType, nestedALayerType, nestedLayerType);
        	
        	// nested class (last lines)
        	if (srcCode == interfaceCode){
        		srcCode.append(tab(0) + "} // end of nested interface " + nestedILayerType + "\n");
    		} else if (srcCode == abstractCode){
    			srcCode.append(tab(0) + "} // end of nested class " + nestedALayerType + "\n");
    		} else if (srcCode == layerCode){
    			srcCode.append(tab(0) + "} // end of nested class " + nestedLayerType + "\n");
    		}
        	srcCode.append("\n");
		}
    	
    	// top level class (append layer items)
    	tab0 = "";
    	appendLayerItems(srcCode, LevelKind.TOPLEVEL, rootUnit, rootUnitType, 
    					 rootILayerType, rootALayerType, rootLayerType);
    	
    	// top level class (last lines)
    	if (srcCode == interfaceCode){
    		srcCode.append("} // end of interface " + rootILayerType + "\n");
    	} else if (srcCode == abstractCode){
    		srcCode.append("} // end of class " + rootALayerType + "\n");
		} else if (srcCode == layerCode){
			srcCode.append("} // end of class " + rootLayerType + "\n");
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	APPEND IMPORTS				    										//
	//////////////////////////////////////////////////////////////////////////////

	private void appendImports(StringBuilder srcCode){

		List<String> importNames = new ArrayList<String>();
		
		importNames.add("javax.annotation.processing.Generated");
		
		// importing types required for forkAndJoin implementation
		boolean forkAndJoin = false;
		for (MethodDescriptor mDescr : rootUnit.methods) {
			if (mDescr.runtimeMode == RuntimeMode.FORKJOIN){
				forkAndJoin = true;
			}
		}
		for (Unit nestedUnit : rootUnit.nestedUnits) {
			for (MethodDescriptor mDescr : nestedUnit.methods) {
				if (mDescr.runtimeMode == RuntimeMode.FORKJOIN) {
					forkAndJoin = true;
				}
			}
		}
		if ((srcCode==layerCode) && forkAndJoin) {
			importNames.add("java.util.List");
			importNames.add("java.util.ArrayList");
		}
		
		// importing required field item types
		if ((srcCode==layerCode) || (srcCode==abstractCode)) {
			for (FieldDescriptor fDescr : rootUnit.fields) {
				if (fDescr.isFieldItem && (fDescr.typeDescr.packageElement != null) 
						&& !fDescr.typeDescr.packageElement.equals(rootUnit.typeDescr.packageElement)
							&& !fDescr.typeDescr.packageElement.getQualifiedName().contentEquals("java.lang")) {
					if (!importNames.contains(fDescr.typeDescr.typeElement.toString())){
						importNames.add(fDescr.typeDescr.typeElement.toString());
					}
	    		}
			}
			for (Unit nestedUnit : rootUnit.nestedUnits) {
				for (FieldDescriptor fDescr : nestedUnit.fields) {
					if (fDescr.isFieldItem && (fDescr.typeDescr.packageElement != null) 
							&& !fDescr.typeDescr.packageElement.equals(nestedUnit.typeDescr.packageElement)
								&& !fDescr.typeDescr.packageElement.getQualifiedName().contentEquals("java.lang")) {
						if (!importNames.contains(fDescr.typeDescr.typeElement.toString())){
							importNames.add(fDescr.typeDescr.typeElement.toString());
						}
		    		}
				}
		}
		}
		
		// importing required return and param item types
		for (MethodDescriptor mDescr : rootUnit.methods) {
			if (!mDescr.isMethodItem) continue;
			if ((mDescr.returnDescr.packageElement != null) 
					&& !mDescr.returnDescr.packageElement.equals(rootUnit.typeDescr.packageElement)
						&& !mDescr.returnDescr.packageElement.getQualifiedName().contentEquals("java.lang")) {
				if (!importNames.contains(mDescr.returnDescr.typeElement.toString())){
					importNames.add(mDescr.returnDescr.typeElement.toString());
				}
    		}
			for (TypeDescriptor tDescr : mDescr.paramTypes) {
				if ((tDescr.packageElement != null) 
						&& !tDescr.packageElement.equals(rootUnit.typeDescr.packageElement)
							&& !tDescr.packageElement.getQualifiedName().contentEquals("java.lang")) {
					if (!importNames.contains(tDescr.typeElement.toString())){
						importNames.add(tDescr.typeElement.toString());
					}
	    		}
			}
		}
		for (Unit nestedUnit : rootUnit.nestedUnits) {
			for (MethodDescriptor mDescr : nestedUnit.methods) {
				if (!mDescr.isMethodItem) continue;
				if ((mDescr.returnDescr.packageElement != null) 
						&& !mDescr.returnDescr.packageElement.equals(nestedUnit.typeDescr.packageElement)
							&& !mDescr.returnDescr.packageElement.getQualifiedName().contentEquals("java.lang")) {
					if (!importNames.contains(mDescr.returnDescr.typeElement.toString())){
						importNames.add(mDescr.returnDescr.typeElement.toString());
					}
	    		}
				for (TypeDescriptor tDescr : mDescr.paramTypes) {
					if ((tDescr.packageElement != null) 
							&& !tDescr.packageElement.equals(nestedUnit.typeDescr.packageElement)
								&& !tDescr.packageElement.getQualifiedName().contentEquals("java.lang")) {
						if (!importNames.contains(tDescr.typeElement.toString())){
							importNames.add(tDescr.typeElement.toString());
						}
		    		}
				}
			}
		}
		
		// importing jlayer.model, jlayer.util
		importNames.add("org.jlayer.model.*");
		if ((srcCode==layerCode) || (srcCode==abstractCode)) {
			importNames.add("org.jlayer.util.*");
		}
		
		for (String name : importNames) {
			srcCode.append("import " + name + ";\n");
		}
	}

	
	//////////////////////////////////////////////////////////////////////////////
	//	APPEND LAYER ITEMS			    										//
	//////////////////////////////////////////////////////////////////////////////
	
	private void appendLayerItems(StringBuilder srcCode, LevelKind levelKind,
								  Unit unit, CharSequence unitType, 
								  CharSequence iLayerType, CharSequence aLayerType, CharSequence layerType){
		
		String unitMod2 = null;
		switch (levelKind) {
			case TOPLEVEL: unitMod2 = getModifiers2(unit.typeDescr.modifiers);
			case NESTED:   unitMod2 = getModifiers2F(unit.typeDescr.modifiers);
		}
    	
    	// nested class D1
		if (srcCode == interfaceCode){
			srcCode.append(tab(1) + "// interface " + iLayerType + ".D1\n");
			srcCode.append(tab(1) + "interface D1 extends BasedLayer.D1<" + unitType + "> {\n");
			
			srcCode.append(tab(2) + "// method items \n");
	    	appendDxMethods(srcCode, unit, unitType, DimKind.D1);
	    	
	    	srcCode.append(tab(1) + "}\n\n");
		} else if (srcCode == abstractCode){
			srcCode.append(tab(1) + "// class " + aLayerType + ".D1\n");
	    	srcCode.append(tab(1) + "abstract " + unitMod2 + "class D1 extends LayerBase.D1<" + unitType + ">" + " implements "+ iLayerType + ".D1 {\n");
	    	
	    	srcCode.append(tab(2) + "// constructors \n");
	    	appendALayerConstructors(srcCode, unit, unitType, iLayerType, aLayerType, layerType, DimKind.D1);
	    	
	    	srcCode.append(tab(2) + "// field items \n");
	    	appendFieldItems(srcCode, unit, unitType, DimKind.D1);
	    	
	    	srcCode.append(tab(2) + "// method items \n");
	    	appendDxMethods(srcCode, unit, unitType, DimKind.D1);
	    	
	    	srcCode.append(tab(1) + "}\n\n");
		} else if (srcCode == layerCode){
			srcCode.append(tab(1) + "// class " + layerType + ".D1\n");
	    	srcCode.append(tab(1) + unitMod2 + "class D1 extends " + aLayerType + ".D1 implements "+ iLayerType + ".D1 {\n");
	    	
	    	srcCode.append(tab(2) + "// constructors \n");
	    	appendLayerConstructors(srcCode, unit, unitType, iLayerType, aLayerType, layerType, DimKind.D1);
	    	
	    	srcCode.append(tab(2) + "// method items \n");
	    	appendDxMethods(srcCode, unit, unitType, DimKind.D1);
	    	
	    	srcCode.append(tab(1) + "}\n\n");
		}
    	
    	// nested class D2
		if (srcCode == interfaceCode){
			srcCode.append(tab(1) + "// interface " + iLayerType + ".D2\n");
			srcCode.append(tab(1) + "interface D2 extends BasedLayer.D2<" + unitType + "> {\n");
			
			srcCode.append(tab(2) + "// method items \n");
	    	appendDxMethods(srcCode, unit, unitType, DimKind.D2);
	    	
	    	srcCode.append(tab(1) + "}\n\n");
		} else if (srcCode == abstractCode){
			srcCode.append(tab(1) + "// class " + aLayerType + ".D2\n");
	    	srcCode.append(tab(1) + "abstract " + unitMod2 + "class D2 extends LayerBase.D2<" + unitType + ">" + " implements "+ iLayerType + ".D2 {\n");
	    	
	    	srcCode.append(tab(2) + "// constructors \n");
	    	appendALayerConstructors(srcCode, unit, unitType, iLayerType, aLayerType, layerType, DimKind.D2);
	    	
	    	srcCode.append(tab(2) + "// field items \n");
	    	appendFieldItems(srcCode, unit, unitType, DimKind.D2);
	    	
	    	srcCode.append(tab(2) + "// method items \n");
	    	appendDxMethods(srcCode, unit, unitType, DimKind.D2);
	    	
	    	srcCode.append(tab(1) + "}\n\n");
		} else if (srcCode == layerCode){
			srcCode.append(tab(1) + "// class " + layerType + ".D2\n");
	    	srcCode.append(tab(1) + unitMod2 + "class D2 extends " + aLayerType + ".D2 implements "+ iLayerType + ".D2 {\n");
	    	
	    	srcCode.append(tab(2) + "// constructors \n");
	    	appendLayerConstructors(srcCode, unit, unitType, iLayerType, aLayerType, layerType, DimKind.D2);
	    	
	    	srcCode.append(tab(2) + "// method items \n");
	    	appendDxMethods(srcCode, unit, unitType, DimKind.D2);
	    	
	    	srcCode.append(tab(1) + "}\n\n");
		}
    	
    	// nested class D3
		if (srcCode == interfaceCode){
			srcCode.append(tab(1) + "// interface " + iLayerType + ".D3\n");
			srcCode.append(tab(1) + "interface D3 extends BasedLayer.D3<" + unitType + "> {\n"); 
			
			srcCode.append(tab(2) + "// method items \n");
			appendDxMethods(srcCode, unit, unitType, DimKind.D3);
			
	    	srcCode.append(tab(1) + "}\n\n");
		} else if (srcCode == abstractCode){
			srcCode.append(tab(1) + "// class " + aLayerType + ".D3\n");
	    	srcCode.append(tab(1) + "abstract " + unitMod2 + "class D3 extends LayerBase.D3<" + unitType + ">" + " implements "+ iLayerType + ".D3 {\n");
	    	
	    	srcCode.append(tab(2) + "// constructors \n");
	    	appendALayerConstructors(srcCode, unit, unitType, iLayerType, aLayerType, layerType, DimKind.D3);
	    	
	    	srcCode.append(tab(2) + "// field items \n");
	    	appendFieldItems(srcCode, unit, unitType, DimKind.D3);
	    	
	    	srcCode.append(tab(2) + "// method items \n");
	    	appendDxMethods(srcCode, unit, unitType, DimKind.D3);
	    	
	    	srcCode.append(tab(1) + "}\n\n");
    		
		} else if (srcCode == layerCode){
			srcCode.append(tab(1) + "// class " + layerType + ".D3\n");
	    	srcCode.append(tab(1) + unitMod2 + "class D3 extends " + aLayerType + ".D3 implements "+ iLayerType + ".D3 {\n");
	    	
	    	srcCode.append(tab(2) + "// constructors \n");
	    	appendLayerConstructors(srcCode, unit, unitType, iLayerType, aLayerType, layerType, DimKind.D3);
	    	
	    	srcCode.append(tab(2) + "// method items \n");
	    	appendDxMethods(srcCode, unit, unitType, DimKind.D3);
	    	
	    	srcCode.append(tab(1) + "}\n\n");
		}
    	
    	// class NN (final lines)
		if (srcCode == interfaceCode){
			srcCode.append(tab(0) + "// interface " + layerType + "\n");
			srcCode.append("\n");
			
	    	srcCode.append(tab(0) + "// method items \n");
	    	appendNNMethods(srcCode, unit, unitType);
	    	
	    	srcCode.append("\n");
		} else if (srcCode == abstractCode){
			srcCode.append(tab0 + "// class " + aLayerType + "\n");
	    	srcCode.append("\n");
	    	
	    	srcCode.append(tab(1) + "// constructors \n");
	    	appendALayerConstructors(srcCode, unit, unitType, iLayerType, aLayerType, layerType, DimKind.NN);
	    	srcCode.append("\n");
	    	
	    	srcCode.append(tab(1) + "// field items \n");
	    	appendFieldItems(srcCode, unit, unitType, DimKind.NN);
	    	
	    	srcCode.append(tab(1) + "// method items \n");
	    	appendNNMethods(srcCode, unit, unitType);
	    	
	    	srcCode.append("\n");
		} else if (srcCode == layerCode){
			srcCode.append(tab0 + "// class " + layerType + "\n");
	    	srcCode.append("\n");
	    	
	    	srcCode.append(tab(1) + "// constructors \n");
	    	appendLayerConstructors(srcCode, unit, unitType, iLayerType, aLayerType, layerType, DimKind.NN);
	    	srcCode.append("\n");
	    	
	    	srcCode.append(tab(1) + "// getD1(), getD2(), getD3() \n");
	    	srcCode.append(tab(1) + "public D1 getD1() {\n");
	    	srcCode.append(tab(2) + "if (this.dimensionality != 1) return null;\n");
	    	srcCode.append(tab(2) + "return new D1(layerD1.getBase());\n");
	    	srcCode.append(tab(1) + "}\n");
	    	srcCode.append(tab(1) + "public D2 getD2() {\n");
	    	srcCode.append(tab(2) + "if (this.dimensionality != 2) return null;\n");
	    	srcCode.append(tab(2) + "return new D2(layerD2.getBase());\n");
	    	srcCode.append(tab(1) + "}\n");
	    	srcCode.append(tab(1) + "public D3 getD3() {\n");
	    	srcCode.append(tab(2) + "if (this.dimensionality != 3) return null;\n");
	    	srcCode.append(tab(2) + "return new D3(layerD3.getBase());\n");
	    	srcCode.append(tab(1) + "}\n\n");
	    	
	    	srcCode.append(tab(1) + "// further method items \n");
	    	appendNNMethods(srcCode, unit, unitType);
	    	srcCode.append("\n");
		}

	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	APPENDING CONSTRUCTORS			    									//
	//////////////////////////////////////////////////////////////////////////////
	
	private void appendALayerConstructors(StringBuilder srcCode,
			Unit unit, CharSequence unitType, 
			CharSequence ilayerType, CharSequence alayerType, CharSequence layerType, 
			DimKind dimKind){
		
		if (srcCode != abstractCode) throw new JLayerException();
		
		String unitMod3 = getModifiers3(unit.typeDescr.modifiers);
		
		switch (dimKind) {
		
		case D1:
			srcCode.append(tab(2) + unitMod3 + "D1(" + ilayerType + ".D1 layerD1){ super(layerD1); }\n");
			srcCode.append(tab(2) + unitMod3 + "D1(" + unitType + "[] array){ super(array); }\n");
			break;
		
		case D2:
			srcCode.append(tab(2) + unitMod3 + "D2(" + ilayerType + ".D2 layerD2){ super(layerD2); }\n");
			srcCode.append(tab(2) + unitMod3 + "D2(" + unitType + "[][] array){ super(array); }\n");
			break;
		
		case D3: 
			srcCode.append(tab(2) + unitMod3 + "D3(" + ilayerType + ".D3 layerD3){ super(layerD3); }\n");
			srcCode.append(tab(2) + unitMod3 + "D3(" + unitType + "[][][] array){ super(array); }\n");
			break;
		
		case NN:
			srcCode.append(tab(1) + unitMod3 + alayerType + "(" + unitType + "[] array){ super(array); }\n");
			srcCode.append(tab(1) + unitMod3 + alayerType + "(" + unitType + "[][] array){ super(array); }\n");
			srcCode.append(tab(1) + unitMod3 + alayerType + "(" + unitType + "[][][] array){ super(array); }\n");
			srcCode.append("\n");
			srcCode.append(tab(1) + unitMod3 + alayerType + "(" + ilayerType + " layer){ super(layer); }\n");
			srcCode.append(tab(1) + unitMod3 + alayerType + "(" + ilayerType + ".D1 layer){ super(layer); }\n");
			srcCode.append(tab(1) + unitMod3 + alayerType + "(" + ilayerType + ".D2 layer){ super(layer); }\n");
			srcCode.append(tab(1) + unitMod3 + alayerType + "(" + ilayerType + ".D3 layer){ super(layer); }\n");
			break;
		
		default: throw new JLayerException();
		
		}
		
	}
	
	private void appendLayerConstructors(StringBuilder srcCode,
			Unit unit, CharSequence unitType, 
			CharSequence ilayerType, CharSequence alayerType, CharSequence layerType, 
			DimKind dimKind){
		
		if (srcCode != layerCode) throw new JLayerException();
		
		String unitMod3 = getModifiers3(unit.typeDescr.modifiers);
		
		List<String> indexNames = new ArrayList<String>();
		for (FieldDescriptor fDescr : unit.fields) {
			if (fDescr.isIndex) {
				indexNames.add(fDescr.itemName);
			}
		}
		
		switch (dimKind) {
		
		case D1:
			srcCode.append(tab(2) + unitMod3 + "D1(" + ilayerType + ".D1 layerD1){ \n");
			srcCode.append(tab(3) + "super(layerD1); \n");
			appendFieldCodeD1(srcCode, unit, unitType);
			srcCode.append(tab(2) + "}\n");
			
			srcCode.append(tab(2) + unitMod3 + "D1(" + unitType + "[] array){ \n");
			srcCode.append(tab(3) + "super(array); \n");
			appendIndexCode(srcCode, indexNames, DimKind.D1, 3);
			appendFieldCodeD1(srcCode, unit, unitType);
			srcCode.append(tab(2) + "}\n");
			
			break;
		
		case D2:
			srcCode.append(tab(2) + unitMod3 + "D2(" + ilayerType + ".D2 layerD2){ \n");
			srcCode.append(tab(3) + "super(layerD2); \n");
			appendFieldCodeD2(srcCode, unit, unitType);
			srcCode.append(tab(2) + "}\n");
			
			srcCode.append(tab(2) + unitMod3 + "D2(" + unitType + "[][] array){ \n");
			srcCode.append(tab(3) + "super(array); \n");
			appendIndexCode(srcCode, indexNames, DimKind.D2, 3);
			appendFieldCodeD2(srcCode, unit, unitType);
			srcCode.append(tab(2) + "}\n");
			
			break;
		
		case D3: 
			srcCode.append(tab(2) + unitMod3 + "D3(" + ilayerType + ".D3 layerD3){ \n");
			srcCode.append(tab(3) + "super(layerD3); \n");
			appendFieldCodeD3(srcCode, unit, unitType);
			srcCode.append(tab(2) + "}\n");
			
			srcCode.append(tab(2) + unitMod3 + "D3(" + unitType + "[][][] array){ \n");
			srcCode.append(tab(3) + "super(array); \n");
			appendIndexCode(srcCode, indexNames, DimKind.D3, 3);
			appendFieldCodeD3(srcCode, unit, unitType);
			srcCode.append(tab(2) + "}\n");
			
			break;
		
		case NN:
			srcCode.append(tab(1) + unitMod3 + layerType + "(" + unitType + "[] array){ \n");
			srcCode.append(tab(2) + "super(array); \n");
			appendIndexCode(srcCode, indexNames, DimKind.D1, 2);
			appendFieldCodeNN(srcCode, unit, unitType, layerType);
			srcCode.append(tab(1) + "}\n");
			
			srcCode.append(tab(1) + unitMod3 + layerType + "(" + unitType + "[][] array){ \n");
			srcCode.append(tab(2) + "super(array); \n");
			appendIndexCode(srcCode, indexNames, DimKind.D2, 2);
			appendFieldCodeNN(srcCode, unit, unitType, layerType);
			srcCode.append(tab(1) + "}\n");
			
			srcCode.append(tab(1) + unitMod3 + layerType + "(" + unitType + "[][][] array){ \n");
			srcCode.append(tab(2) + "super(array); \n");
			appendIndexCode(srcCode, indexNames, DimKind.D3, 2);
			appendFieldCodeNN(srcCode, unit, unitType, layerType);
			srcCode.append(tab(1) + "}\n");
			
			srcCode.append("\n");
			srcCode.append(tab(1) + unitMod3 + layerType + "(" + ilayerType + " layer){ \n");
			srcCode.append(tab(2) + "super(layer); \n");
			appendFieldCodeNN(srcCode, unit, unitType, layerType);
			srcCode.append(tab(1) + "}\n");
			
			srcCode.append(tab(1) + unitMod3 + layerType + "(" + ilayerType + ".D1 layer){ \n");
			srcCode.append(tab(2) + "super(layer); \n");
			appendFieldCodeNN(srcCode, unit, unitType, layerType);
			srcCode.append(tab(1) + "}\n");
			
			srcCode.append(tab(1) + unitMod3 + layerType + "(" + ilayerType + ".D2 layer){ \n");
			srcCode.append(tab(2) + "super(layer); \n");
			appendFieldCodeNN(srcCode, unit, unitType, layerType);
			srcCode.append(tab(1) + "}\n");
			
			srcCode.append(tab(1) + unitMod3 + layerType + "(" + ilayerType + ".D3 layer){ \n");
			srcCode.append(tab(2) + "super(layer); \n");
			appendFieldCodeNN(srcCode, unit, unitType, layerType);
			srcCode.append(tab(1) + "}\n");
			
			break;
		
		default: throw new JLayerException();
		
		}
		
	}
	
	private void appendIndexCode(StringBuilder srcCode, List<String> indexNames, DimKind dimKind, int no){
		
		if (indexNames.isEmpty()) return;
		
		srcCode.append(tab(no) + "// set index items \n");
		switch (dimKind) {
			case D1: 
				srcCode.append(tab(no) + "for (int i = 0; i < array.length; i++) {\n");
				for (String name : indexNames) {
					srcCode.append(tab(no+1) + "if (array[i] != null) array[i]." + name + " = new int[]{i}; \n");
				}
				srcCode.append(tab(no) + "}\n");
				break;
			case D2: 
				srcCode.append(tab(no) + "for (int i = 0; i < array.length; i++) {\n");
				srcCode.append(tab(no+1) + "for (int j = 0; j < array[i].length; j++) {\n");
				for (String name : indexNames) {
					srcCode.append(tab(no+2) + "if (array[i][j] != null) array[i][j]." + name + " = new int[]{i,j}; \n");
				}
				srcCode.append(tab(no+1) + "}\n");
				srcCode.append(tab(no) + "}\n");
				break;
			case D3: 
				srcCode.append(tab(no) + "for (int i = 0; i < array.length; i++) {\n");
				srcCode.append(tab(no+1) + "for (int j = 0; j < array[i].length; j++) {\n");
				srcCode.append(tab(no+2) + "for (int k = 0; k < array[i][j].length; k++) {\n");
				for (String name : indexNames) {
					srcCode.append(tab(no+3) + "if (array[i][j][k] != null) array[i][j][k]." + name + " = new int[]{i,j,k}; \n");
				}
				srcCode.append(tab(no+2) + "}\n");
				srcCode.append(tab(no+1) + "}\n");
				srcCode.append(tab(no) + "}\n");
				break;
			default: ;
		}
		
	}
	
	private void appendFieldCodeD1(StringBuilder srcCode, Unit unit, CharSequence unitType){
		
		srcCode.append(tab(3) + "// create adapters \n");
		for (FieldDescriptor fDescr : unit.fields) {
			
			String itemName = fDescr.itemName;
			String itemType = fDescr.typeDescr.getItemType();
			String coreType = fDescr.typeDescr.getItemTypeCore();
			String itemAdapter = getLayerTypeName(unitType, fDescr.typeDescr, DimKind.D1, TypeKind.LC);
			
			srcCode.append(tab(3) + itemName + " = new " + itemAdapter + "(){\n");
			srcCode.append(tab(4) + "public int length() { return D1.this.length(); } \n");
			srcCode.append(tab(4) + "public " + itemType + " get(int x1) { return D1.this.get(x1)." + itemName + "; }\n");
			srcCode.append(tab(4) + "public void set(int x1, " + itemType + " val){ D1.this.get(x1)." + itemName + " = val; }\n");
			srcCode.append(tab(4) + "public " + unitType + " getUnit(int x1) { return D1.this.getUnit(x1); }\n");
			srcCode.append(tab(4) + "public void setUnit(int x1, " + unitType + " unit){ D1.this.setUnit(x1, unit); }\n");
			if (fDescr.typeDescr.isConnectable()) {
				srcCode.append(tab(4) + "protected " + coreType + " newObject(){ return new " + coreType + "(); }\n");
				srcCode.append(tab(4) + "protected " + itemType + " newArray(int len){ return new " + coreType + "[len]; }\n");
			}
			srcCode.append(tab(3) + "};\n");
			
		}
		
	}
	
	private void appendFieldCodeD2(StringBuilder srcCode, Unit unit, CharSequence unitType){
		
		srcCode.append(tab(3) + "// create adapters \n");
		for (FieldDescriptor fDescr : unit.fields) {
			
			String itemName = fDescr.itemName;
			String itemType = fDescr.typeDescr.getItemType();
			String coreType = fDescr.typeDescr.getItemTypeCore();
			String itemAdapter = getLayerTypeName(unitType, fDescr.typeDescr, DimKind.D2, TypeKind.LC);
			
			srcCode.append(tab(3) + itemName + " = new " + itemAdapter + "(){\n");
			srcCode.append(tab(4) + "public int length() { return D2.this.length(); }\n");
			srcCode.append(tab(4) + "public int length(int x1) { return D2.this.length(x1); }\n");
			srcCode.append(tab(4) + "public " + itemType + " get(int x1, int x2) { return D2.this.get(x1, x2)." + itemName + "; }\n");
			srcCode.append(tab(4) + "public void set(int x1, int x2, " + itemType + " val){ D2.this.get(x1, x2)." + itemName + " = val; }\n");
			srcCode.append(tab(4) + "public " + unitType + " getUnit(int x1, int x2) { return D2.this.getUnit(x1, x2); }\n");
			srcCode.append(tab(4) + "public void setUnit(int x1, int x2, " + unitType + " unit){ D2.this.setUnit(x1, x2, unit); }\n");
			if (fDescr.typeDescr.isConnectable()) {
				srcCode.append(tab(4) + "protected " + coreType + " newObject(){ return new " + coreType + "(); }\n");
				srcCode.append(tab(4) + "protected " + itemType + " newArray(int len){ return new " + coreType + "[len]; }\n");
			}
			srcCode.append(tab(3) + "};\n");
			
		}
		
	}
	
	private void appendFieldCodeD3(StringBuilder srcCode, Unit unit, CharSequence unitType){
		
		srcCode.append(tab(3) + "// create adapters \n");
		for (FieldDescriptor fDescr : unit.fields) {
			
			String itemName = fDescr.itemName;
			String itemType = fDescr.typeDescr.getItemType();
			String coreType = fDescr.typeDescr.getItemTypeCore();
			String itemAdapter = getLayerTypeName(unitType, fDescr.typeDescr, DimKind.D3, TypeKind.LC);
			
			srcCode.append(tab(3) + itemName + " = new " + itemAdapter + "(){\n");
			srcCode.append(tab(4) + "public int length() { return D3.this.length(); }\n");
			srcCode.append(tab(4) + "public int length(int x1) { return D3.this.length(x1); }\n");
			srcCode.append(tab(4) + "public int length(int x1, int x2) { return D3.this.length(x1, x2); }\n");
			srcCode.append(tab(4) + "public " + itemType + " get(int x1, int x2, int x3) { return D3.this.get(x1, x2, x3)." + itemName + "; }\n");
			srcCode.append(tab(4) + "public void set(int x1, int x2, int x3, " + itemType + " val){ D3.this.get(x1, x2, x3)." + itemName + " = val; }\n");
			srcCode.append(tab(4) + "public " + unitType + " getUnit(int x1, int x2, int x3) { return D3.this.getUnit(x1, x2, x3); }\n");
			srcCode.append(tab(4) + "public void setUnit(int x1, int x2, int x3, " + unitType + " unit){ D3.this.setUnit(x1, x2, x3, unit); }\n");
			if (fDescr.typeDescr.isConnectable()) {
				srcCode.append(tab(4) + "protected " + coreType + " newObject(){ return new " + coreType + "(); }\n");
				srcCode.append(tab(4) + "protected " + itemType + " newArray(int len){ return new " + coreType + "[len]; }\n");
			}
			srcCode.append(tab(3) + "};\n");
			
		}
		
	}
	
	private void appendFieldCodeNN(StringBuilder srcCode, Unit unit, CharSequence unitType, CharSequence layerType){
		
		srcCode.append(tab(2) + "// create adapters \n");
		for (FieldDescriptor fDescr : unit.fields) {
			
			String itemName = fDescr.itemName;
			String itemType = fDescr.typeDescr.getItemType();
			String coreType = fDescr.typeDescr.getItemTypeCore();
			String itemAdapter = getLayerTypeName(unitType, fDescr.typeDescr, DimKind.NN, TypeKind.LC);
			
			srcCode.append(tab(2) + itemName + " = new " + itemAdapter + "(){\n");
			srcCode.append(tab(3) + "public int dims() { return " + layerType + ".this.dims(); } \n");
			srcCode.append(tab(3) + "public int length() { return " + layerType + ".this.length(); } \n");
			srcCode.append(tab(3) + "public int length(int... ix) { return " + layerType + ".this.length(ix); } \n");
			srcCode.append(tab(3) + "public " + itemType + " get(int... ix) { return " + layerType + ".this.get(ix)." + itemName + "; }\n");
			srcCode.append(tab(3) + "public void set(int[] ix, " + itemType + " val){ " + layerType + ".this.get(ix)." + itemName + " = val; }\n");
			srcCode.append(tab(3) + "public void set(int x1, " + itemType + " val){ " + layerType + ".this.get(x1)." + itemName + " = val; }\n");
			srcCode.append(tab(3) + "public void set(int x1, int x2, " + itemType + " val){ " + layerType + ".this.get(x1, x2)." + itemName + " = val; }\n");
			srcCode.append(tab(3) + "public void set(int x1, int x2, int x3, " + itemType + " val){ " + layerType + ".this.get(x1, x2, x3)." + itemName + " = val; }\n");
			srcCode.append(tab(3) + "public " + unitType + " getUnit(int... ix) { return " + layerType + ".this.getUnit(ix); }\n");
			srcCode.append(tab(3) + "public void setUnit(int[] ix, " + unitType + " unit){ " + layerType + ".this.setUnit(ix, unit); }\n");
			srcCode.append(tab(3) + "public void setUnit(int x1, " + unitType + " unit){ " + layerType + ".this.setUnit(x1, unit); }\n");
			srcCode.append(tab(3) + "public void setUnit(int x1, int x2, " + unitType + " unit){ " + layerType + ".this.setUnit(x1, x2, unit); }\n");
			srcCode.append(tab(3) + "public void setUnit(int x1, int x2, int x3, " + unitType + " unit){ " + layerType + ".this.setUnit(x1, x2, x3, unit); }\n");
			if (fDescr.typeDescr.isConnectable()) {
				srcCode.append(tab(3) + "protected " + coreType + " newObject(){ return new " + coreType + "(); }\n");
				srcCode.append(tab(3) + "protected " + itemType + " newArray(int len){ return new " + coreType + "[len]; }\n");
			}
			srcCode.append(tab(2) + "};\n");
			
		}
		
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	APPENDING ITEM FIELDS			    									//
	//////////////////////////////////////////////////////////////////////////////
	
	private void appendFieldItems(StringBuilder srcCode, Unit unit, CharSequence unitType, DimKind dimKind){
		if (srcCode != abstractCode) throw new JLayerException();
		
		int no = 2;
		if (dimKind == DimKind.NN) no = 1;
		
		for (FieldDescriptor fDescr : unit.fields) {
			if (!fDescr.isFieldItem) continue;
			
			String itemMod = getModifiers1(fDescr.modifiers);
			String adapterType = getLayerTypeName(unitType, fDescr.typeDescr, dimKind, TypeKind.LA);
			String itemName = fDescr.itemName;
			
			if (srcCode == abstractCode) {
				srcCode.append(tab(no) + itemMod + adapterType + " " + itemName + ";\n");
				continue;
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	APPENDING ITEM METHODS			    									//
	//////////////////////////////////////////////////////////////////////////////
	
	private void appendDxMethods(StringBuilder srcCode, Unit unit, CharSequence unitType, DimKind dimKind){
		
		for (MethodDescriptor mDescr : unit.methods) {
			if (!mDescr.isMethodItem) continue;
			appendDxMethod(srcCode, mDescr, unitType, dimKind);
		}
	}
	
	private void appendDxMethod(StringBuilder srcCode, MethodDescriptor mDescr, CharSequence unitType, DimKind dimKind){
		
		// append signature
		String modifiers = getModifiers1(mDescr.modifiers);
		if (srcCode == interfaceCode) {
			srcCode.append(tab(2));
			appendSignature(srcCode, unitType, mDescr, dimKind, ";");
			return;
		} else if (srcCode == abstractCode) {
			srcCode.append(tab(2) + "abstract " + modifiers);
			appendSignature(srcCode, unitType, mDescr, dimKind, ";");
			return;
		} else {
			srcCode.append(tab(2) + modifiers);
			appendSignature(srcCode, unitType, mDescr, dimKind, "{");
		}
		
		// append body
		String returnType = mDescr.returnDescr.getItemType();
		String returnCore = mDescr.returnDescr.getItemTypeCore();
		String returnBase = getLayerTypeName(unitType, mDescr.returnDescr, dimKind, TypeKind.LI);
		String methodName = mDescr.itemName;
		final int noOfPars = mDescr.paramTypes.size();
		boolean hasLayerParams = false;
		List<String> paramCalls = new ArrayList<String>();
		String suffix = "";
		switch (dimKind) {
			case D1: suffix = ".get(_x1)"; break;
			case D2: suffix = ".get(_x1,_x2)"; break;
			case D3: suffix = ".get(_x1,_x2,_x3)"; break;
			default: throw new JLayerException();
		}
		for (int i = 0; i < noOfPars; i++){
			if (mDescr.paramItems.get(i)) {
				hasLayerParams = true;
				paramCalls.add(mDescr.paramNames.get(i) + suffix);
			} else {
				paramCalls.add(mDescr.paramNames.get(i));
			}
		}
		
		// append array declaration (if necessary)
		if (!returnType.equals("void")){
			appendArrayDeclaration(srcCode, dimKind, mDescr.returnDescr);
		} 
		
		String dotClass;
		switch (dimKind) {
			case D1: dotClass = ".D1"; break;
			case D2: dotClass = ".D2"; break;
			case D3: dotClass = ".D3"; break;
			default: dotClass = "";
		}
		
		if (mDescr.runtimeMode == RuntimeMode.FORKJOIN) {
			
			ParamInfo paramInfo = getParamInfo(unitType, mDescr, dimKind, false);
			String actionItemParams = "";
			String actionItemParamCalls = "";
			if (paramInfo.noOfPars>0) {
				actionItemParams =  getParamDecls(", ", paramInfo);
				actionItemParamCalls =  getParamCalls(", ", paramInfo);
			}
			
			if (!returnType.equals("void")){
				
				// append class ThisActionItem
				srcCode.append(tab(3) + "class ThisActionItem extends MethodUtil" + dotClass + "X<" + unitType + ", " + returnType + "> { \n");
				srcCode.append(tab(4) + "ThisActionItem(XIterator" + dotClass + "<" + unitType + "> _xiter, " + getArrayType(dimKind, returnType) + " _array" + actionItemParams + ") { \n");
				srcCode.append(tab(5) + "this._xiter = _xiter; \n");
				srcCode.append(tab(5) + "this._array = _array; \n");
				appendActionItemInits(srcCode, tab(5), paramInfo);
				srcCode.append(tab(4) + "} \n");
				appendActionItemLocals(srcCode, tab(4), paramInfo);
				srcCode.append(tab(4) + "@Override \n");
				srcCode.append(tab(4) + "protected " + returnType + " computeNext(" + unitType + " _unit) { \n");
				srcCode.append(tab(5) + "return " + getRightSide(methodName, noOfPars, paramCalls) + "; \n");
				srcCode.append(tab(4) + "} \n");
				srcCode.append(tab(3) + "} \n");
				
				// append action invocation
				srcCode.append(tab(3) + "List<XIterator" + dotClass + "<" + unitType + ">> _iterList = MethodUtil.getXIteratorList(this); \n");
				srcCode.append(tab(3) + "List<ForkJoinUtil.ActionItem> _actionList = new ArrayList<ForkJoinUtil.ActionItem>(); \n");
				srcCode.append(tab(3) + "for (XIterator" + dotClass + "<" + unitType + "> _iter : _iterList) { \n");
				srcCode.append(tab(4) + "_actionList.add( new ThisActionItem(_iter, _array" + actionItemParamCalls + ")); \n");
				srcCode.append(tab(3) + "} \n");
				srcCode.append(tab(3) + "ForkJoinUtil.LayerAction _thisAction = new ForkJoinUtil.LayerAction(_actionList); \n");
				srcCode.append(tab(3) + "ForkJoinUtil.invoke( _thisAction ); \n");
				
			} else if (paramInfo.noOfPars>0) {
				
				// append class ThisActionItem
				srcCode.append(tab(3) + "class ThisActionItem extends MethodUtil" + dotClass + "P<" + unitType + "> { \n");
				srcCode.append(tab(4) + "ThisActionItem(XIterator" + dotClass + "<" + unitType + "> _xiter" + actionItemParams + ") { \n");
				srcCode.append(tab(5) + "this._xiter = _xiter; \n");
				appendActionItemInits(srcCode, tab(5), paramInfo);
				srcCode.append(tab(4) + "} \n");
				appendActionItemLocals(srcCode, tab(4), paramInfo);
				srcCode.append(tab(4) + "@Override \n");
				srcCode.append(tab(4) + "protected void computeNext(" + unitType + " _unit) { \n");
				srcCode.append(tab(5) + getRightSide(methodName, noOfPars, paramCalls) + "; \n");
				srcCode.append(tab(4) + "} \n");
				srcCode.append(tab(3) + "} \n");
				
				// append action invocation
				srcCode.append(tab(3) + "List<XIterator" + dotClass + "<" + unitType + ">> _iterList = MethodUtil.getXIteratorList(this); \n");
				srcCode.append(tab(3) + "List<ForkJoinUtil.ActionItem> _actionList = new ArrayList<ForkJoinUtil.ActionItem>(); \n");
				srcCode.append(tab(3) + "for (XIterator" + dotClass + "<" + unitType + "> _iter : _iterList) { \n");
				srcCode.append(tab(4) + "_actionList.add( new ThisActionItem(_iter" + actionItemParamCalls + ")); \n");
				srcCode.append(tab(3) + "} \n");
				srcCode.append(tab(3) + "ForkJoinUtil.LayerAction _thisAction = new ForkJoinUtil.LayerAction(_actionList); \n");
				srcCode.append(tab(3) + "ForkJoinUtil.invoke( _thisAction ); \n");
				
			} else {
				
				// append class ThisActionItem
				srcCode.append(tab(3) + "class ThisActionItem extends MethodUtil" + dotClass + "<" + unitType + "> { \n");
				srcCode.append(tab(4) + "ThisActionItem(XIterator" + dotClass + "<" + unitType + "> _xiter" + actionItemParams + ") { \n");
				srcCode.append(tab(5) + "this._xiter = _xiter; \n");
				appendActionItemInits(srcCode, tab(5), paramInfo);
				srcCode.append(tab(4) + "} \n");
				appendActionItemLocals(srcCode, tab(4), paramInfo);
				srcCode.append(tab(4) + "@Override \n");
				srcCode.append(tab(4) + "protected void computeNext(" + unitType + " _unit) { \n");
				srcCode.append(tab(5) + getRightSide(methodName, noOfPars, paramCalls) + "; \n");
				srcCode.append(tab(4) + "} \n");
				srcCode.append(tab(3) + "} \n");
				
				// append action invocation
				srcCode.append(tab(3) + "List<XIterator" + dotClass + "<" + unitType + ">> _iterList = MethodUtil.getXIteratorList(this); \n");
				srcCode.append(tab(3) + "List<ForkJoinUtil.ActionItem> _actionList = new ArrayList<ForkJoinUtil.ActionItem>(); \n");
				srcCode.append(tab(3) + "for (XIterator" + dotClass + "<" + unitType + "> _iter : _iterList) { \n");
				srcCode.append(tab(4) + "_actionList.add( new ThisActionItem(_iter" + actionItemParamCalls + ")); \n");
				srcCode.append(tab(3) + "} \n");
				srcCode.append(tab(3) + "ForkJoinUtil.LayerAction _thisAction = new ForkJoinUtil.LayerAction(_actionList); \n");
				srcCode.append(tab(3) + "ForkJoinUtil.invoke( _thisAction ); \n");
			
			}
		
		} else {
		
			// append while statement
			srcCode.append(tab(3) + "XIterator" + dotClass + "<" + unitType + "> iterator = this.xIterator(); \n");
			srcCode.append(tab(3) + "while( iterator.hasNext() ){\n");
			if (!returnType.equals("void") || hasLayerParams){
				appendIndexDeclaration(srcCode, dimKind);
			} 
			srcCode.append(tab(4) + unitType + " _unit = iterator.next();\n");
			String rightSide = getRightSide(methodName, noOfPars, paramCalls);
			if (!returnType.equals("void")){
				srcCode.append(tab(4) + getLeftSide(dimKind) + " = " + rightSide + ";\n");
			} else {
				srcCode.append(tab(4) + rightSide + ";\n");
			}
			srcCode.append(tab(3) + "}\n");
			
		}
		
		// append return statement (if necessary)
		if (!returnType.equals("void")){
			srcCode.append(tab(3) + "return new " + returnBase + "(_array)");
			if (mDescr.returnDescr.isConnectable()) {
				srcCode.append("{\n");
				srcCode.append(tab(4) + "protected " + returnCore + " newObject() { return new " + returnCore + "(); }\n");
				srcCode.append(tab(4) + "protected " + returnCore + "[] newArray(int len) { return new " + returnCore + "[len]; }\n");
				srcCode.append(tab(3) + "}");
			}
			srcCode.append(";\n");
		}
		srcCode.append(tab(2) + "}\n");
		
	}
	
	private void appendNNMethods(StringBuilder srcCode, Unit unit, CharSequence unitType){
		
		for (MethodDescriptor mDescr : unit.methods) {
			if (!mDescr.isMethodItem) continue;
			
			// signature
			String modifiers = getModifiers1(mDescr.modifiers);
			if (srcCode == interfaceCode) {
				srcCode.append(tab(1));
				appendSignature(srcCode, unitType, mDescr, DimKind.NN, ";");
				continue;
			} else if (srcCode == abstractCode) {
				srcCode.append(tab(1) + "abstract " + modifiers);
				appendSignature(srcCode, unitType, mDescr, DimKind.NN, ";");
				continue;
			} else {
				srcCode.append(tab(1) + modifiers);
				appendSignature(srcCode, unitType, mDescr, DimKind.NN, "{");
			}
			
			// delegating to the nested classes
			srcCode.append(tab(2) + "switch(this.dims()) {\n");	
			srcCode.append(tab(3) + "case 1:\n");
			srcCode.append(tab(4) + getDxCast(DimKind.D1));
			srcCode.append(tab(4) + getDxCall(unitType, mDescr, DimKind.D1));
			srcCode.append(tab(3) + "case 2:\n");
			srcCode.append(tab(4) + getDxCast(DimKind.D2));
			srcCode.append(tab(4) + getDxCall(unitType, mDescr, DimKind.D2));
			srcCode.append(tab(3) + "case 3:\n");
			srcCode.append(tab(4) + getDxCast(DimKind.D3));
			srcCode.append(tab(4) + getDxCall(unitType, mDescr, DimKind.D3));
			srcCode.append(tab(3) + "default: throw new JLayerException();\n");
			srcCode.append(tab(2) + "}\n");
			
			// final brace
			srcCode.append(tab(1) + "}\n");
		}
	}	
	
	//////////////////////////////////////////////////////////////////////////////
	//	GETTING LOCAL INFOS				    									//
	//////////////////////////////////////////////////////////////////////////////
	
	static private class ParamInfo {
		int noOfPars = 0;
		List<String> paramNames = new ArrayList<String>();
		List<String> paramTypes = new ArrayList<String>();
	}
	
	private ParamInfo getParamInfo(CharSequence unitType, MethodDescriptor mDescr, DimKind dimKind, boolean treatVP) {
		ParamInfo paramInfo = new ParamInfo();
		paramInfo.noOfPars = mDescr.paramTypes.size();
		paramInfo.paramNames = new ArrayList<String>();
		paramInfo.paramTypes = new ArrayList<String>();
		
		for (int i = 0; i < paramInfo.noOfPars; i++){
			paramInfo.paramNames.add(mDescr.paramNames.get(i));
			if (mDescr.paramItems.get(i)) {
				paramInfo.paramTypes.add(getLayerTypeName(unitType, mDescr.paramTypes.get(i), dimKind, TypeKind.LP));
			} else {
				paramInfo.paramTypes.add(mDescr.paramTypes.get(i).typeName);
			}
		}
		
		// special treatment of varparams
		if (treatVP && mDescr.hasVarArgs && !mDescr.paramItems.get(mDescr.paramItems.size()-1)) {
			int ix = mDescr.paramItems.size()-1;
			if (!paramInfo.paramTypes.get(ix).endsWith("[]")) {
				throw new JLayerException();
			}
			int len = paramInfo.paramTypes.get(ix).length();
			paramInfo.paramTypes.set(ix, paramInfo.paramTypes.get(ix).substring(0, len-2) + "...");
		}
		return paramInfo;
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	APPENDING STRING SNIPPETS		    									//
	//////////////////////////////////////////////////////////////////////////////
	
	private void appendSignature(StringBuilder srcCode, CharSequence unitType, MethodDescriptor mDescr, 
								 DimKind dimKind, String endChar){
		
		if ( (srcCode == interfaceCode) && !mDescr.modifiers.contains(Modifier.PUBLIC)) return;
		
		String returnBase = getLayerTypeName(unitType, mDescr.returnDescr, dimKind, TypeKind.LS);
		String methodName = mDescr.itemName;
		ParamInfo paramInfo = getParamInfo(unitType, mDescr, dimKind, true);
		
		// appending to the source code
		srcCode.append(returnBase + " " + methodName + "(" + getParamDecls("", paramInfo) + ")" + endChar + "\n");
	}
	
	private void appendActionItemInits(StringBuilder srcCode, String tab, ParamInfo paramInfo) {
		for (int i = 0; i < paramInfo.noOfPars; i++){
			String paramName = paramInfo.paramNames.get(i);
			srcCode.append(tab + "this." + paramName + " = " + paramName + ";\n");
		}
	}
	
	private void appendActionItemLocals(StringBuilder srcCode, String tab, ParamInfo paramInfo) {
		for (int i = 0; i < paramInfo.noOfPars; i++){
			String paramName = paramInfo.paramNames.get(i);
			String paramType = paramInfo.paramTypes.get(i);
			srcCode.append(tab + paramType + " " + paramName + ";\n");
		}
	}
	
	private void appendArrayDeclaration(StringBuilder srcCode, DimKind dimKind, Unit.TypeDescriptor typeDescr){
		String returnType = typeDescr.getItemType();
		String typeBase = typeDescr.getItemTypeCore();
		String typeDims = typeDescr.getItemTypeDims();
		switch (dimKind) {
			case D1: 
				srcCode.append(tab(3) + returnType + "[] _array = new " + typeBase + "[length()]" + typeDims + ";\n");
				break;
			case D2: 
				srcCode.append(tab(3) + returnType + "[][] _array = new " + typeBase + "[length()][]" + typeDims + ";\n");
				srcCode.append(tab(3) + "for (int _i = 0; _i < length(); _i++) {\n");
				srcCode.append(tab(4) + "_array[_i] = new " + typeBase + "[length(_i)]" + typeDims + ";\n");
				srcCode.append(tab(3) + "}\n");
				break;
			case D3: 
				srcCode.append(tab(3) + returnType + "[][][] _array = new " + typeBase + "[length()][][]" + typeDims + ";\n");
				srcCode.append(tab(3) + "for (int _i = 0; _i < length(); _i++) {\n");
				srcCode.append(tab(4) + "_array[_i] = new " + typeBase + "[length(_i)][]" + typeDims + ";\n");
				srcCode.append(tab(4) + "for (int _j = 0; _j < length(_i); _j++) {\n");
				srcCode.append(tab(5) + "_array[_i][_j] = new " + typeBase + "[length(_i, _j)]" + typeDims + ";\n");
				srcCode.append(tab(4) + "}\n");
				srcCode.append(tab(3) + "}\n");
				break;
			default: throw new JLayerException();
		}
	}
	
	private void appendIndexDeclaration(StringBuilder srcCode, DimKind dimKind){
		switch (dimKind) {
			case D3: srcCode.append(tab(4) + "int _x3 = iterator.getX3();\n");
			case D2: srcCode.append(tab(4) + "int _x2 = iterator.getX2();\n");
			case D1: srcCode.append(tab(4) + "int _x1 = iterator.getX1();\n"); break;
			default: throw new JLayerException();
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//	GETTING STRING SNIPPETS		    									//
	//////////////////////////////////////////////////////////////////////////////

	// get PUBLIC iff present
	private String getModifiers0(Set<Modifier> modifiers){
		StringBuilder str = new StringBuilder();
		if (modifiers.contains(Modifier.PUBLIC)) {
			str.append("public ");
		}
		return str.toString();
	}
	
	// get all but ABSTRACT
	private String getModifiers1(Set<Modifier> modifiers){
		StringBuilder str = new StringBuilder();
		for (Iterator<Modifier> iterator = modifiers.iterator(); iterator.hasNext();) {
			Modifier modifier = iterator.next();
			if (modifier == Modifier.ABSTRACT) continue;
			str.append(modifier.toString() + " ");
		}
		return str.toString();
	}
	
	// get all but ABSTRACT, FINAL
	private String getModifiers1F(Set<Modifier> modifiers){
		StringBuilder str = new StringBuilder();
		for (Iterator<Modifier> iterator = modifiers.iterator(); iterator.hasNext();) {
			Modifier modifier = iterator.next();
			if (modifier == Modifier.ABSTRACT) continue;
			if (modifier == Modifier.FINAL) continue;
			str.append(modifier.toString() + " ");
		}
		return str.toString();
	}
	
	// get all (plus STATIC) but ABSTRACT
	private String getModifiers2(Set<Modifier> modifiers){
		StringBuilder str = new StringBuilder();
		if (!modifiers.contains(Modifier.STATIC)) {
			str.append("static ");
		}
		for (Iterator<Modifier> iterator = modifiers.iterator(); iterator.hasNext();) {
			Modifier modifier = iterator.next();
			if (modifier == Modifier.ABSTRACT) continue;
			str.append(modifier.toString() + " ");
		}
		return str.toString();
	}
	
	// get all (plus STATIC) but ABSTRACT, FINAL
		private String getModifiers2F(Set<Modifier> modifiers){
			StringBuilder str = new StringBuilder();
			if (!modifiers.contains(Modifier.STATIC)) {
				str.append("static ");
			}
			for (Iterator<Modifier> iterator = modifiers.iterator(); iterator.hasNext();) {
				Modifier modifier = iterator.next();
				if (modifier == Modifier.ABSTRACT) continue;
				if (modifier == Modifier.FINAL) continue;
				str.append(modifier.toString() + " ");
			}
			return str.toString();
		}
	
	// get all but ABSTRACT, STATIC, FINAL
	private String getModifiers3(Set<Modifier> modifiers){
		StringBuilder str = new StringBuilder();
		for (Iterator<Modifier> iterator = modifiers.iterator(); iterator.hasNext();) {
			Modifier modifier = iterator.next();
			if (modifier == Modifier.ABSTRACT) continue;
			if (modifier == Modifier.STATIC) continue;
			if (modifier == Modifier.FINAL) continue;
			str.append(modifier.toString() + " ");
		}
		return str.toString();
	}
	
	private String getArrayType(DimKind kind, String type){
		switch (kind) {
			case D1: return type + "[]";
			case D2: return type + "[][]";
			case D3: return type + "[][][]";
			default: throw new JLayerException();
		}
	}
	
	private String getParamDecls(String prefix, ParamInfo paramInfo) {
		StringBuilder paramDecls = new StringBuilder();
		paramDecls.append(prefix);
		for (int i = 0; i < paramInfo.noOfPars; i++){
			paramDecls.append(paramInfo.paramTypes.get(i) + " " + paramInfo.paramNames.get(i));
			if (i+1<paramInfo.noOfPars) paramDecls.append(", ");
		}
		return paramDecls.toString();
	}
	
	private String getParamCalls(String prefix, ParamInfo paramInfo) {
		StringBuilder paramCalls = new StringBuilder();
		paramCalls.append(prefix);
		for (int i = 0; i < paramInfo.noOfPars; i++){
			paramCalls.append(paramInfo.paramNames.get(i));
			if (i+1<paramInfo.noOfPars) paramCalls.append(", ");
		}
		return paramCalls.toString();
	}
	
	private String getRightSide(String methodName, int noOfPars, List<String> paramCalls) {
		StringBuilder rightSide = new StringBuilder();
		rightSide.append("_unit." + methodName + "(");
		for (int i = 0; i < noOfPars; i++){
			rightSide.append(paramCalls.get(i));
			if (i+1<noOfPars) rightSide.append(", ");
		}
		rightSide.append(")");
		return rightSide.toString();
	}
	
	private String getLeftSide(DimKind kind){
		switch (kind) {
			case D1: return "_array[_x1]";
			case D2: return "_array[_x1][_x2]";
			case D3: return "_array[_x1][_x2][_x3]";
			default: return "";
		}
	}
	
	private String getDxCast(DimKind dimKind){
		switch (dimKind) {
			case D1: return "D1 _layerD1_ = new D1(layerD1.getBase());\n";
			case D2: return "D2 _layerD2_ = new D2(layerD2.getBase());\n";
			case D3: return "D3 _layerD3_ = new D3(layerD3.getBase());\n";
			default: throw new JLayerException();
		}
	}
	
	private String getDxCall(CharSequence unitType, MethodDescriptor mDescr, DimKind dimKind){
		
		String returnType = mDescr.returnDescr.getItemType();
		String returnCore = mDescr.returnDescr.getItemTypeCore();
		String returnBase = getLayerTypeName(unitType, mDescr.returnDescr, DimKind.NN, TypeKind.LI);
		
		final int noOfPars = mDescr.paramTypes.size();
		StringBuilder theCall = new StringBuilder();
		theCall.append("_layer" + dimKind + "_." + mDescr.itemName + "(");
		for (int i = 0; i < noOfPars; i++){
			String theParam = mDescr.paramNames.get(i);
			if (!mDescr.paramItems.get(i)){
				theCall.append(theParam);
			} else {
				theCall.append(theParam + ".get" + dimKind + "()");
			}
			if (i+1<noOfPars) theCall.append(", ");
		}
		theCall.append(")");
		
		StringBuilder retStr = new StringBuilder();
		if (returnType.equals("void")){
			retStr.append(theCall.toString() + "; break;\n");
		} else {
			retStr.append("return new " + returnBase + "(" + theCall.toString() + ")");
			if (mDescr.returnDescr.isConnectable()) {
				retStr.append("{\n");
				retStr.append(tab(5) + "protected " + returnCore + " newObject() { return new " + returnCore + "(); }\n");
				retStr.append(tab(5) + "protected " + returnCore + "[] newArray(int len) { return new " + returnCore + "[len]; }\n");
				retStr.append(tab(4) + "}");
			}
			retStr.append(";\n");
		}
		return retStr.toString();
		
	}	
 
	/**
	 * generates the layer type name for a field, a return type or a parameter
	 * 
	 * @param unitType		the name of the current unit type
	 * @param typeDescr		the type of the field, return type or parameter under consideration
	 * @param dimKind		the dimensionality of the current layer type to be generated (D1, D2, D3, NN)
	 * @param typeKind		the kind of type to be generated
	 * @return				the generated layer type
	 */
	private String getLayerTypeName(CharSequence unitType, TypeDescriptor typeDescr, DimKind dimKind, TypeKind typeKind){
		if (typeDescr.typeName.equals("void")) return "void";
		
		String prefix = "";
		String itemType = typeDescr.getItemType();
		if (typeDescr.isConnectable()) {
			prefix = "Vector";
			int len = itemType.length();
			itemType = itemType.substring(0, len-2);
		}

		String dotClass;
		switch (dimKind) {
			case D1: dotClass = ".D1"; break;
			case D2: dotClass = ".D2"; break;
			case D3: dotClass = ".D3"; break;
			default: dotClass = "";
		}
		
		String layerTypeName;
		switch (typeKind){
			case LA: layerTypeName = prefix + "Layer" + dotClass + "<" + unitType + "," + itemType + ">"; break;
			case LP: layerTypeName = prefix + "Layer" + dotClass + "<?," + itemType + ">"; break;
			case LC: layerTypeName = prefix + "LayerAdapter" + dotClass + "<" + unitType + "," + itemType + ">"; break;
			case LI: 
				if ( (typeDescr.dimX == 0 ) && (typeDescr.isUnitType) ) {
					int ix = itemType.lastIndexOf(".");
					if (ix < 0){ // the typeDescr refers to a top level class
						layerTypeName = "Layer_" + itemType + "_" + dotClass;
					} else { // the typeDescr refers to a nested class
						String topLayer = "Layer_" + itemType.substring(0, ix) + "_";
						String nestedLayer = "Layer_" + itemType.substring(ix+1) + "_";
						layerTypeName = topLayer + "." + nestedLayer + dotClass;
					}
				} else {
					layerTypeName = prefix + "LayerBase" + dotClass + "<" + itemType + ">";
				} break;
			case LS: 
				if ( (typeDescr.dimX == 0 ) && (typeDescr.isUnitType) ) {
					int ix = itemType.lastIndexOf(".");
					if (ix < 0){ // the typeDescr refers to a top level class
						layerTypeName = "ALayer_" + itemType + "_" + dotClass;
					} else { // the typeDescr refers to a nested class
						String topLayer = "ALayer_" + itemType.substring(0, ix) + "_";
						String nestedLayer = "ALayer_" + itemType.substring(ix+1) + "_";
						layerTypeName = topLayer + "." + nestedLayer + dotClass;
					}
				} else {
					String localPrefix = "Based";
					if (prefix.equals("Vector")) {
						localPrefix = "BasedVector";
					}
					layerTypeName = localPrefix + "Layer" + dotClass + "<" + itemType + ">";
				} break;
			default: layerTypeName = null;
		}
		
		return layerTypeName;
	}
	
}

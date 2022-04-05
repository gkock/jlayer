package org.jlayer.impl;

import static javax.lang.model.util.ElementFilter.typesIn;
import static javax.lang.model.util.ElementFilter.fieldsIn;
import static javax.lang.model.util.ElementFilter.methodsIn;

import java.lang.annotation.Annotation;
import java.lang.Boolean;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Types;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleElementVisitor14;


import org.jlayer.annotations.LayerUnit;
import org.jlayer.annotations.LayerField;
import org.jlayer.annotations.LayerMethod;
import org.jlayer.annotations.LayerParam;
import org.jlayer.annotations.RuntimeMode;

class UnitVisitor extends SimpleElementVisitor14<Boolean, Unit> {
	
	private Types typeUtils = null;
	private Elements elementUtils = null;
	private UnitChecker checker = null;
	
	UnitVisitor(ProcessingEnvironment pe) {
		super();
		typeUtils = pe.getTypeUtils();
		elementUtils = pe.getElementUtils();
		checker = new UnitChecker(pe);
	}
	
	private Unit.TypeDescriptor setTypeDescriptor(TypeMirror typeMirror){
		Unit.TypeDescriptor typeDescr = new Unit.TypeDescriptor();
		
		typeDescr.typeName = typeMirror.toString();
		typeDescr.dimX = 0;
		typeDescr.isUnitType = false;
		typeDescr.typeMirror = typeMirror;
		while (typeDescr.typeMirror.getKind() == TypeKind.ARRAY){
			typeDescr.typeMirror = ((ArrayType)typeDescr.typeMirror).getComponentType();
			typeDescr.dimX += 1;
		}
		
		Element typeElement = typeUtils.asElement(typeDescr.typeMirror);
		if (typeElement ==  null) { 		// element is not a DeclaredType (or TypeVariable),
			typeDescr.typeElement = null;	// i.e. it is a primitive type or void
			typeDescr.packageElement = null;
			typeDescr.typeParameters = Collections.emptyList();
			typeDescr.modifiers = null;
			typeDescr.nestingKind = null;
		} else {
			typeDescr.typeElement = (TypeElement)typeElement;
			Annotation annType = typeDescr.typeElement.getAnnotation(LayerUnit.class);
			if ( annType != null ) { typeDescr.isUnitType = true; }
			typeDescr.packageElement = elementUtils.getPackageOf(typeDescr.typeElement);
			typeDescr.typeParameters = typeDescr.typeElement.getTypeParameters();
			typeDescr.modifiers = typeDescr.typeElement.getModifiers();
			typeDescr.nestingKind = typeDescr.typeElement.getNestingKind();
		}
		
//		if (typeDescr.packageElement != null){  // cut qualifying prefix
		if ( (typeDescr.packageElement != null) && !typeDescr.packageElement.isUnnamed() ){  // cut qualifying prefix
			int tmpInt = typeDescr.packageElement.getQualifiedName().length();
			typeDescr.typeName = typeDescr.typeName.substring(tmpInt+1);
		}
		
		return typeDescr;
	}
	
	@Override
	public Boolean visitType(TypeElement nextElem, Unit nextUnit) {
		//System.out.println("UnitVisitor.visitType(BEGIN:  " + nextElem.getSimpleName());
		boolean visitTypeOK = false;
		boolean visitFieldOK = false;
		boolean visitMethodOK = false;
		
		// check for annotation @LayerUnit
		Annotation annType = nextElem.getAnnotation(LayerUnit.class);
		if ( annType == null ) { return true; }
		
		// collect unit attributes
		nextUnit.typeDescr = setTypeDescriptor(nextElem.asType());
		nextUnit.isLayerUnit = checker.checkClassAttributes(nextElem, nextUnit);
		
		// process nested units
		List<TypeElement> nestedElems = typesIn(nextElem.getEnclosedElements());
		nextUnit.nestedUnits = new LinkedList<Unit>();
		for (Iterator<TypeElement> iterator = nestedElems.iterator(); iterator.hasNext();) {
			TypeElement nestedElem = iterator.next();
			Unit nestedUnit = new Unit();
			nestedUnit.rootUnit = nextUnit;
			visitTypeOK = this.visit(nestedElem, nestedUnit);
			if (nestedUnit.isLayerUnit) nextUnit.nestedUnits.add(nestedUnit);
		}
		
		// collect field attributes
		List<VariableElement> fields = fieldsIn(nextElem.getEnclosedElements());
		nextUnit.fields = new LinkedList<Unit.FieldDescriptor>();
		for (Iterator<VariableElement> iterator = fields.iterator(); iterator.hasNext();) {
			VariableElement nextVariable = iterator.next();
			visitFieldOK = this.visit(nextVariable, nextUnit);
		}
		
		// collect method attributes
		List<ExecutableElement> methods = methodsIn(nextElem.getEnclosedElements());
		nextUnit.methods = new LinkedList<Unit.MethodDescriptor>();
		for (Iterator<ExecutableElement> iterator = methods.iterator(); iterator.hasNext();) {
			ExecutableElement nextMethod = iterator.next();
			visitMethodOK = this.visit(nextMethod, nextUnit);
		}
		
		return (visitTypeOK && visitFieldOK && visitMethodOK);
	}
	
	@Override
	public Boolean visitVariable(VariableElement nextVariable, Unit nextUnit) {
		//System.out.println("UnitVisitor.visitVariable(BEGIN:  " + nextVariable.getSimpleName());
		
		// check for annotation @LayerField
		Annotation annType = nextVariable.getAnnotation(LayerField.class);
		if ( annType == null ) { return true; }
		
		// collect field attributes
		Unit.FieldDescriptor fDescr = new Unit.FieldDescriptor();
		if ( ((LayerField)annType).isIndex() == true ) { 
			fDescr.isIndex = true;
		} else {
			fDescr.isIndex = false;
		}
		fDescr.modifiers = nextVariable.getModifiers();
		fDescr.itemName = nextVariable.getSimpleName().toString();
		fDescr.typeDescr = setTypeDescriptor(nextVariable.asType());
		if (!nextUnit.isLayerUnit){
			fDescr.isFieldItem = false;
		} else {
			fDescr.isFieldItem = checker.checkFieldAttributes(nextVariable, fDescr);;
		}
		
		// add field attributes
		if (fDescr.isFieldItem) nextUnit.fields.add(fDescr);
		
		return true;
	}
	
	@Override
	public Boolean visitExecutable(ExecutableElement nextMethod, Unit nextUnit) {
		// System.out.println("UnitVisitor.visitExecutable(BEGIN:  " + nextMethod.getSimpleName());
		
		// check for annotation @LayerMethod
		Annotation annType = nextMethod.getAnnotation(LayerMethod.class);
		if ( annType == null ) { return true; }
		
		// collect method attributes
		Unit.MethodDescriptor mDescr = new Unit.MethodDescriptor();
		if ( ((LayerMethod)annType).runtimeMode() == RuntimeMode.FORKJOIN ){
			mDescr.runtimeMode = RuntimeMode.FORKJOIN;
		} else {
			mDescr.runtimeMode = RuntimeMode.LOOP;
		}
		mDescr.modifiers = nextMethod.getModifiers();
		mDescr.itemType = nextMethod.asType();
		mDescr.itemName = nextMethod.getSimpleName().toString();
		mDescr.throwables = nextMethod.getThrownTypes();
		mDescr.typeParameters = nextMethod.getTypeParameters();
		mDescr.returnDescr = setTypeDescriptor(nextMethod.getReturnType());
		for (VariableElement param : nextMethod.getParameters()) {
			mDescr.paramItems.add(param.getAnnotation(LayerParam.class) != null);
			mDescr.paramNames.add(param.getSimpleName().toString());
			mDescr.paramTypes.add(setTypeDescriptor(param.asType()));
		}
		mDescr.hasVarArgs = nextMethod.isVarArgs();
		if (!nextUnit.isLayerUnit){
			mDescr.isMethodItem = false;
		} else {
			mDescr.isMethodItem = checker.checkMethodAttributes(nextMethod, mDescr);;
		}
		
		// add method attributes
		if (mDescr.isMethodItem) nextUnit.methods.add(mDescr);
		
		return true;
	}
	 
}

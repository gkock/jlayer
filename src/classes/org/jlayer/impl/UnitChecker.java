package org.jlayer.impl;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic.Kind;


class UnitChecker {
	
	private Messager messager = null;
	
	// constructor
	UnitChecker(ProcessingEnvironment pe) {
		messager = pe.getMessager();
	}
	
	private String unitNotProcessed  = "@LayerUnit is not processed: ";
	private String fieldNotProcessed  = "@LayerField is not processed: ";
	private String methodNotProcessed  = "@LayerMethod is not processed: ";
	
	private String unitMessage001 = "an enum type must not be a layer unit";
	private String unitMessage002 = "a generic class must  not be a layer unit";
	private String unitMessage003 = "a layer unit must be a top-level class or a static member class immediately nested in a top-level layer unit";
	private String unitMessage004 = "a nested layer unit must not be private";

	boolean checkClassAttributes(TypeElement nextElem, Unit nextUnit) {
		if ( (nextUnit.rootUnit != null) && !nextUnit.rootUnit.isLayerUnit ) {
			messager.printMessage(Kind.WARNING, unitNotProcessed, nextElem);
			return false;
		} else if (nextElem.getKind()==ElementKind.ENUM) {
			messager.printMessage(Kind.WARNING, unitNotProcessed + unitMessage001, nextElem);
			return false;
		} else if (!nextUnit.typeDescr.typeParameters.isEmpty()) {
			messager.printMessage(Kind.WARNING, unitNotProcessed + unitMessage002, nextElem);
			return false;
		} else if ( (nextUnit.typeDescr.nestingKind == NestingKind.MEMBER)
				   && (!nextUnit.typeDescr.modifiers.contains(Modifier.STATIC) || (nextUnit.rootUnit.rootUnit != null) ) ) {
			messager.printMessage(Kind.WARNING, unitNotProcessed + unitMessage003, nextElem);
			return false;  
		} else if ( (nextUnit.typeDescr.nestingKind == NestingKind.MEMBER)
				&& nextUnit.typeDescr.modifiers.contains(Modifier.PRIVATE ) ) {
			messager.printMessage(Kind.WARNING, unitNotProcessed + unitMessage004, nextElem);
			return false; 
		} else {
			return true;
		}
	}
	
	private String fieldMessage002 = "a layer field must not be static";
	private String fieldMessage003 = "a layer field must not be private";
	private String fieldMessage004 = "if the (array element) type of a layer field is a member class, this class must be static";
	private String fieldMessage005 = "if the (array element) type of a layer field is a member class, this class must not be private";
	private String fieldMessage006 = "the type of a layer index must be int[]";
	
	boolean checkFieldAttributes(VariableElement nextElem, Unit.FieldDescriptor fDescr){
		if (fDescr.modifiers.contains(Modifier.STATIC)) {
			messager.printMessage(Kind.WARNING, fieldNotProcessed + fieldMessage002, nextElem);
			return false;  
		} else if (fDescr.modifiers.contains(Modifier.PRIVATE)) {
			messager.printMessage(Kind.WARNING, fieldNotProcessed + fieldMessage003, nextElem);
			return false;  
		} else if ( (fDescr.typeDescr.nestingKind == NestingKind.MEMBER)
				   && !fDescr.typeDescr.modifiers.contains(Modifier.STATIC)) {
			messager.printMessage(Kind.WARNING, fieldNotProcessed + fieldMessage004, nextElem);
			return false;  
		} else if ( (fDescr.typeDescr.nestingKind == NestingKind.MEMBER)
				   && fDescr.typeDescr.modifiers.contains(Modifier.PRIVATE)) {
			messager.printMessage(Kind.WARNING, fieldNotProcessed + fieldMessage005, nextElem);
			return false;  
		} else if ( (fDescr.isIndex)
			       && ( (fDescr.typeDescr.dimX != 1) || (fDescr.typeDescr.typeMirror.getKind() != TypeKind.INT) ) ) {
			messager.printMessage(Kind.WARNING, fieldNotProcessed + fieldMessage006, nextElem);
			return false;
		} else {
			return true;
		}
	}
	
	private String methodMessage001 = "a layer method must not be private";
	private String methodMessage002 = "a layer method must not be static";
	private String methodMessage003 = "a layer method must not have this identifier: ";
	private String[] methodIds = {"length", "get", "set", "isAdapter", "getUnit", "setUnit", "iterator", "xIterator", "dims", "getD1", "getD2", "getD3", "getBase"};
	private Set<String> idSet = new HashSet<String>(Arrays.asList(methodIds));
	private String methodMessage004 = "the return type of a layer method must not be a concrete parameterized type";
	private String methodMessage005 = "a layer method must not have a throws clause";
	
	boolean checkMethodAttributes(ExecutableElement nextElem, Unit.MethodDescriptor mDescr){
		if (mDescr.modifiers.contains(Modifier.PRIVATE)) {
			messager.printMessage(Kind.WARNING, methodNotProcessed + methodMessage001, nextElem);
			return false;  
		} else if (mDescr.modifiers.contains(Modifier.STATIC)) {
			messager.printMessage(Kind.WARNING, methodNotProcessed + methodMessage002, nextElem);
			return false;  
		} else if (idSet.contains(mDescr.itemName)) {
			messager.printMessage(Kind.WARNING, methodNotProcessed + methodMessage003 + mDescr.itemName, nextElem);
			return false;  
		} else if (!mDescr.returnDescr.typeParameters.isEmpty()) {
			messager.printMessage(Kind.WARNING, methodNotProcessed + methodMessage004, nextElem);
			return false; 
		} else if (!mDescr.throwables.isEmpty()) {
			messager.printMessage(Kind.WARNING, methodNotProcessed + methodMessage005, nextElem);
			return false;  
		} else {
			return true;
		}
	}
	
}

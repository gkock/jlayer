package org.jlayer.impl;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.NestingKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeKind; 

import org.jlayer.annotations.*;

class Unit {
	
	// type descriptor
	static class TypeDescriptor {
		
		String typeName;    	// the type name (without qualifying prefix)
		int dimX;				// iff (dimX > 0) ...
		// then the attributes below refer to the component type
		boolean isUnitType;
		TypeMirror typeMirror;					
		TypeElement typeElement;
		PackageElement packageElement;
		List<? extends TypeParameterElement> typeParameters;
		Set<Modifier> modifiers;
		NestingKind nestingKind; // TOP_LEVEL, MEMBER, LOCAL, ANONYMOUS
		
		String getItemType() {
			if (dimX > 0) return typeName;
			String itemType;
			switch (typeMirror.getKind()) {
				case BOOLEAN : itemType = "Boolean"; break;
				case BYTE : itemType = "Byte"; break;
				case CHAR : itemType = "Character"; break;
				case DOUBLE : itemType = "Double"; break;
				case FLOAT : itemType = "Float"; break;
				case INT : itemType = "Integer"; break;
				case LONG : itemType = "Long"; break;
				case SHORT : itemType = "Short"; break;
				default: itemType = typeName;
			}
			return itemType;
		}
		
		String getItemTypeCore() {
			int len = typeName.length();
			switch (dimX) {
				case 1: return typeName.substring(0, len-2);
				case 2: return typeName.substring(0, len-4);
				case 3: return typeName.substring(0, len-6);
				default: return getItemType();
			}
		}
		
		String getItemTypeDims() {
			switch (dimX) {
				case 1: return "[]";
				case 2: return "[][]";
				case 3: return "[][][]";
				default: return "";
			}
		}
		
		boolean isPrimitiveType(){
			return (dimX == 0 && typeElement == null);
		}
		
		boolean isConnectable(){
			if (dimX != 1 || typeMirror.getKind() != TypeKind.DECLARED) return false;
			if (typeElement.getModifiers().contains(Modifier.FINAL)) return false;
			
			List<? extends Element> encList = typeElement.getEnclosedElements();
			Iterator<? extends Element> iter = encList.iterator();
			while (iter.hasNext()){
				Element elem = iter.next();
				if (elem.getKind() == ElementKind.CONSTRUCTOR) {
					ExecutableElement cons = (ExecutableElement)elem;
					if (cons.getParameters().isEmpty()) return true;
				}
			}
			return false;
		}
		
		@Override
		public String toString(){
			String s;
			s =  "-------------- \n";
			s += "   fDescr.typeName:  " + typeName + "\n";
			s += "   typeDescr.dimX:  " + dimX + "\n";
			s += "   typeDescr.isUnitType:  " + isUnitType + "\n";
			s += "   typeDescr.typeMirror:  " + typeMirror + "\n";
			s += "   typeDescr.typeElement:  " + typeElement + "\n";
			s += "   typeDescr.packageElement:  " + packageElement + "\n";
			s += "   typeDescr.typeParameters:  " + typeParameters + "\n";
			s += "   typeDescr.modifiers:  " + modifiers + "\n";
			s += "   typeDescr.nestingKind:  " + nestingKind + "\n";
			s += "   --------------------------------- \n";
			return s;
		}
	}
	
	// unit attributes
	Unit rootUnit = null;
	TypeDescriptor typeDescr = null;
	boolean isLayerUnit;
	
	// nested units
	List<Unit> nestedUnits;
	
	// field attributes
	static class FieldDescriptor {
		
		Boolean isIndex;
		Set<Modifier> modifiers;
		String itemName;			// the name of the item field
		TypeDescriptor typeDescr;   // the descriptor of the (component) type
		boolean isFieldItem;
		
		@Override
		public String toString(){
			String s;
			s  = "FIELD ATTRIBUTES: \n";
			s += "   fDescr.isIndex:  " + isIndex + "\n";
			s += "   fDescr.modifiers:  " + modifiers + "\n";
			s += "   fDescr.itemName:  " + itemName + "\n";
			s += "   fDescr.typeDescr: " + typeDescr.toString() + "\n";
			s += "   fDescr.isFieldItem:  " + isFieldItem + "\n";
			return s;
		}
	}
	List<FieldDescriptor> fields = null;
	
	// method attributes
	static class MethodDescriptor {
		
		RuntimeMode runtimeMode;
		Set<Modifier> modifiers;
		TypeMirror itemType;		// TODO DELETE
		String itemName;			// the name of the item method
		List<? extends TypeMirror> throwables;
		List<? extends TypeParameterElement> typeParameters;
		TypeDescriptor returnDescr;	// return type	
		// formal parameters
		List<Boolean> paramItems = new ArrayList<Boolean>();
		List<String> paramNames = new ArrayList<String>();
		List<TypeDescriptor> paramTypes = new ArrayList<TypeDescriptor>();
		boolean hasVarArgs;
		public boolean isMethodItem;
		
		@Override
		public String toString(){
			String s;
			s  = "METHOD ATTRIBUTES: \n";
			s += "   mDescr.runtimeMode:  " + runtimeMode + "\n";
			s += "   mDescr.modifiers:  " + modifiers + "\n";
			s += "   mDescr.itemType:  " + itemType + "\n";
			s += "   mDescr.itemName:  " + itemName + "\n";
			s += "   mDescr.typeParameters: " + typeParameters + " \n";
			s += "   mDescr.returnDescr: " + returnDescr.toString() + "\n";
			for (int i = 0; i < paramNames.size(); i++) {
				s += "   mDescr.paramItem: " + paramItems.get(i) + "\n";
				s += "   mDescr.paramName: " + paramNames.get(i) + "\n";
				s += "   mDescr.paramType: " + paramTypes.get(i).toString() + "\n";
			}
			
			s += "   mDescr.hasVarArgs:  " + hasVarArgs + "\n";
			s += "   mDescr.isMethodItem:  " + isMethodItem + "\n";
			return s;
		}
	}
	List<MethodDescriptor> methods = null;
	
	// method for test output
	private static int level = -1;
	@Override
	public String toString(){
		int ll = ++level;
		String s, start, end;
		if (typeDescr.nestingKind == NestingKind.TOP_LEVEL){
			start  = "BEGIN ROOT UNIT LEVEL = " + ll + "\n";
			end  = "END ROOT UNIT LEVEL = " + ll + "\n";
		} else {
			start  = "BEGIN NESTED UNIT LEVEL = " + ll + "\n";
			end  = "END NESTED UNIT LEVEL = " + ll + "\n";
		}
		s = start;
		s += "   unit.typeDescr:  " + typeDescr.toString() + "\n";
		s += "   unit.isLayerUnit:  " + isLayerUnit + "\n";
		for (Unit nestedUnit : nestedUnits) {
			s += nestedUnit.toString();
		}
		for (FieldDescriptor fDescr : fields) {
			s += fDescr.toString();
		}
		for (MethodDescriptor mDescr : methods) {
			s += mDescr.toString();
		}
		--level;
		return s + end;
	}

}

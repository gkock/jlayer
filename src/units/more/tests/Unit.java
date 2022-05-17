package more.tests;

import org.jlayer.annotations.*;

/**
 * 
 * This unit class underlies the www.jlayer.org/details page
 * 
 * @author Dr. Gerd Kock
 *
 */
@LayerUnit
class Unit {
	
	static class Type {
		int x;
		int y;
		int z;
		Type() { x = 0; y = 0; z = 0;}
		Type(int i) { x = i; y = 0; z = 0;}
		Type(int i, int j) { x = i; y = j; z = 0;}
		Type(int i, int j, int k) { x = i; y = j; z = k;}
		
		void show() {
			System.out.printf("(%d,%d)%n", x, y);
		}

	}
	
	Unit() { x = new Type(); }
	Unit(int i) { x = new Type(i); }
	Unit(int i, int j) { x = new Type(i,j); }
	Unit(int i, int j, int k) { x = new Type(i,j,k); }
	
	void show() {
		x.show();;
	}
	
	@LayerField
	Type x;
	
	@LayerField
	Unit[] v;
	
	@LayerField
	Type[] w;
	
	@LayerMethod
	Type f(Type p){
		return x;
	};
		
	@LayerMethod
	Type g(@LayerParam Type p){
		Type res = new Type(p.x-1, p.y, p.z+1);
		return res;
	};
}

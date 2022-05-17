package more.tests;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import more.tests.Unit.Type;

import org.jlayer.model.*;
import org.jlayer.util.IndexTools;
import org.jlayer.util.LayerBase;
import org.jlayer.util.JLayerException;

import static more.tests.Unit.Type;

@Test(groups = {"suiteC", "choice"})
public class C_Unit_Test {
	
	Relation myRel = new Relation() {
		@Override
		public boolean contains(int[] x, int[] y) { 
			if (x.length==1 && y.length==2) {
				if (x[0]==y[0]) {
					return true;
				}
			}
			return false;
		}
	};
	
	Unit a1[];
	Unit a2[][];
	Unit a3[][][];
	Unit a4[][][][];
	Layer_Unit_ wa1;
	Layer_Unit_ wa2;
	Layer_Unit_ wa3;
	Layer_Unit_ wa4;
	
	void initArrays() {
		// init arrays
		for (int i = 0; i < 3; i++) {
			a1[i] = new Unit(i);
			for (int j = 0; j < 3; j++) {
				a2[i][j] = new Unit(i,j);
				for (int k = 0; k < 3; k++) {
					a3[i][j][k] = new Unit(i,j,k);
					for (int l = 0; l < 3; l++) {
						a4[i][j][k][l] = new Unit(i,j,k);
					}
				}
			}
		}
	}
	
	@BeforeClass
    void beforeClass() {
		
		// create arrays
		a1 = new Unit[3];
		a2 = new Unit[3][3];
		a3 = new Unit[3][3][3];
		a4 = new Unit[3][3][3][3];
		
		// init arrays
		initArrays();
		
		// wrap arrays
		wa1 = new Layer_Unit_(a1);
		wa2 = new Layer_Unit_(a2);
		wa3 = new Layer_Unit_(a3);
//		wa4 = new Layer_Unit_(a4);  error: no suitable constructor found for Layer_Unit_(Unit[][][][]
 
	}
	
	// testing connect()
	
	@Test
	void testConnect1() {
		// connect
		wa1.v.connect(wa2, IndexTools.isNeighbour);
		// check
		for (int i = 0; i < 3; i++) {
			Unit[] v = a1[i].v;
			assertEquals(v.length, 0);
		}
		// connect
		wa2.v.connect(wa2, IndexTools.isNeighbour);
		// check
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Unit[] v = a2[i][j].v;
				if (i==0 && j==0) {
					assertEquals(v.length, 3);
					assertEquals(v[0], a2[0][1]);
					assertEquals(v[1], a2[1][0]);
					assertEquals(v[2], a2[1][1]);
				} else if (i==0 && j==2) {
					assertEquals(v.length, 3);
				} else if (i==2 && j==0) {
					assertEquals(v.length, 3);
				} else if (i==2 && j==2) {
					assertEquals(v.length, 3);
				} else if (i==1 && j==1) {
					assertEquals(v.length, 8);
					assertEquals(v[3], a2[1][0]);
					assertEquals(v[7], a2[2][2]);
				} else {
					assertEquals(v.length, 5);
				}
			}
		}
	}
	
	@Test
	void testConnect2() {
		// connect
		wa1.w.connect(wa2.x, IndexTools.isNeighbour);
		// check
		for (int i = 0; i < 3; i++) {
			Type[] w = a1[i].w;
			assertEquals(w.length, 0);
		}
		// connect
		wa2.w.connect(wa2.x, IndexTools.isNeighbour);
		// check
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Type[] w = a2[i][j].w;
				if (i==0 && j==0) {
					assertEquals(w.length, 3);
					assertEquals(w[0], a2[0][1].x);
					assertEquals(w[1], a2[1][0].x);
					assertEquals(w[2], a2[1][1].x);
				} else if (i==0 && j==2) {
					assertEquals(w.length, 3);
				} else if (i==2 && j==0) {
					assertEquals(w.length, 3);
				} else if (i==2 && j==2) {
					assertEquals(w.length, 3);
				} else if (i==1 && j==1) {
					assertEquals(w.length, 8);
					assertEquals(w[3], a2[1][0].x);
					assertEquals(w[7], a2[2][2].x);
				} else {
					assertEquals(w.length, 5);
				}
			}
		}
	}
	
	// testing associate()
	
	@Test(groups = {"ignore"})
//	@Test				// test output only
	void testMyRel() {
		System.out.println("testMyRel");
		for (int i = 0; i < 3; i++) {
			int[] ix = new int[]{i};
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					int[] iy = new int[]{j, k};
					System.out.printf("myRel([%d],[%d,%d]) == %b%n", i, j, k, myRel.contains(ix, iy));
				}
			}
		}
	}
	
	@Test
	void testAssociate() {
		// associate
		wa1.w.associate(wa2.w, IndexTools.isNeighbour);
		// check
		for (int i = 0; i < 3; i++) {
			Type[] w = a1[i].w;
			assertEquals(w.length, 0);
		}
		// associate
		wa1.w.associate(wa2.w, myRel);
		// check
		for (int i = 0; i < 3; i++) {
			Type[] w1 = a1[i].w;
			assertEquals(w1.length, 3);
			for (int j = 0; j < 3; j++) {
				Type[] w2 = a2[i][j].w;
				assertEquals(w2.length, 1);
			}
		}
	}
	
	// testing layer method f()
	
	@Test
	void testfdim1() {
		// dim == 1
		Type p1;				// the parameter
		p1 = new Type();		// setting the parameter
		BasedLayer<Type> ra1;	// based layer for return values
		ra1 = wa1.f(p1);		// invocation of the method layer

		// checks
		assertEquals(ra1.dims(), 1);
		// check elements
		for (int i = 0; i < 3; i++) {
			Type res = ra1.get(i);
			assertEquals(res.x, i);
			assertEquals(res.y, 0);
			assertEquals(res.z, 0);
		}
	}
	
	@Test
	void testfdim2() {
		// dim == 2
		Type p2;				// the parameter
		p2 = new Type();		// setting the parameter
		BasedLayer<Type> ra2;	// based layer for return values
		ra2 = wa2.f(p2);		// invocation of the method layer

		// checks
		assertEquals(ra2.dims(), 2);
		// check elements
		for (int i = 0; i < 3; i++) {
		for (int j = 0; j < 3; j++) {
				Type res = ra2.get(i,j);
				assertEquals(res.x, i);
				assertEquals(res.y, j);
				assertEquals(res.z, 0);
			}
		}
	}
	
	// testing layer method g()
	
	@Test
	void testfdim3() {
		// dim == 3
		Type p3;				// the parameter
		p3 = new Type();		// setting the parameter
		BasedLayer<Type> ra3;	// based layer for return values
		ra3 = wa3.f(p3);		// invocation of the method layer

		// checks
		assertEquals(ra3.dims(), 3);
		// check elements
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					Type res = ra3.get(i,j,k);
					assertEquals(res.x, i);
					assertEquals(res.y, j);
					assertEquals(res.z, k);
				}
			}
		}
	}
	
	@Test
	void testgdim1() {
		// dim == 1
		// initialize and set the parameter array
		Type[] par1 = new Type[3];
		for (int i = 0; i < 3; i++) {
			par1[i] = new Type(i);
		}
		// wrap the parameter array
		BasedLayer<Type> wpar1 = new LayerBase<Type>(par1);
		// declare the based layer for return values
		BasedLayer<Type> ra1;
		// invoke the method layer
		ra1 = wa1.g(wpar1);
		// checks
		assertEquals(ra1.dims(), 1);
		// check elements
		for (int i = 0; i < 3; i++) {
			Type res = ra1.get(i);
			assertEquals(res.x, i-1);
			assertEquals(res.y, 0);
			assertEquals(res.z, 1);
		}
	}
	
	@Test
	void testgdim2() {
		// dim == 2
		// initialize and set the parameter array
		Type[][] par2 = new Type[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				par2[i][j] = new Type(i,j);
			}
		}
		// wrap the parameter array
		BasedLayer<Type> wpar2 = new LayerBase<Type>(par2);
		// declare the based layer for return values
		BasedLayer<Type> ra2;
		// invoke the method layer
		ra2 = wa2.g(wpar2);
		// checks
		assertEquals(ra2.dims(), 2);
		// check elements
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Type res = ra2.get(i,j);
				assertEquals(res.x, i-1);
				assertEquals(res.y, j);
				assertEquals(res.z, 1);
			}
		}
	}
	
	@Test
	void testgdim3() {
		// dim == 3
		// initialize and set the parameter array
		Type[][][] par3 = new Type[3][3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					par3[i][j][k] = new Type(i,j,k);
				}
			}
		}
		// wrap the parameter array
		BasedLayer<Type> wpar3 = new LayerBase<Type>(par3);
		// declare the based layer for return values
		BasedLayer<Type> ra3;
		// invoke the method layer
		ra3 = wa3.g(wpar3);
		// checks
		assertEquals(ra3.dims(), 3);
		// check elements
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					Type res = ra3.get(i,j,k);
					assertEquals(res.x, i-1);
					assertEquals(res.y, j);
					assertEquals(res.z, k+1);
				}
			}
		}
	}
	
	// provoking runtime exceptions
	
	@Test(expectedExceptions = JLayerException.class)
	void fail1fdim1() {
		// dim == 1
		Type p1 = null;			// the parameter
		BasedLayer<Type> ra1;	// based layer for return values
		ra1 = wa1.f(p1);						// JLayerException: Invocation of method layer f(): parameter p==null
	}

	@Test(expectedExceptions = JLayerException.class)
	void failgdim1() {
		// dim == 1
		Type[][] par2 = new Type[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				par2[i][j] = new Type(i,j);
			}
		}
		// wrap the parameter array
		BasedLayer<Type> wpar2 = new LayerBase<Type>(par2);
		// declare the based layer for return values
		BasedLayer<Type> ra1;
		// invoke the method layer
		ra1 = wa1.g(wpar2);						// JLayerException: Invocation of method layer g(): p.dims() does not fit
	}
	
	// CURRENT WORK

	@Test(expectedExceptions = JLayerException.class)
	void fail1gdim2() {
		// dim == 2
		// initialize and set the parameter array
		Type[][] par2 = null;
		// wrap the parameter array
		BasedLayer<Type> wpar2;
		wpar2 = new LayerBase<Type>(par2);		// JLayerException: LayerBase constructor received a null parameter
	}
	
	@Test(expectedExceptions = JLayerException.class)
	void fail2gdim2() {
		// dim == 2
		// initialize and set the parameter array
		Type[] par1 = new Type[3];
		for (int i = 0; i < 3; i++) {
			par1[i] = new Type(i);
		}
		// wrap the parameter array
		BasedLayer<Type> wpar1 = new LayerBase<Type>(par1);
		// declare the based layer for return values
		BasedLayer<Type> ra2;
		// invoke the method layer
		ra2 = wa2.g(wpar1);						// JLayerException: Invocation of method layer g(): p.dims() does not fit
	}
	
	@Test(expectedExceptions = JLayerException.class)
	void failgdim3() {
		// dim == 3
		// initialize and set the parameter array
		Type[] par1 = new Type[3];
		for (int i = 0; i < 3; i++) {
			par1[i] = new Type(i);
		}
		// wrap the parameter array
		BasedLayer<Type> wpar1 = new LayerBase<Type>(par1);
		// declare the based layer for return values
		BasedLayer<Type> ra3;
		// invoke the method layer
		ra3 = wa3.g(wpar1);						// JLayerException: Invocation of method layer g(): p.dims() does not fi
	}
		
}

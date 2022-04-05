package org.jlayer.util;

import java.util.ArrayList;
import java.util.List;

import org.jlayer.model.Layer;
import org.jlayer.model.XIterator;

import static org.jlayer.util.ForkJoinUtil.getX1Slices;

import org.jlayer.util.ForkJoinUtil.ActionItem;
import org.jlayer.util.ForkJoinUtil.Tuple;

/**
 * This class provides the basic elements for the concurrent implementation of generated
 * {@link org.jlayer.annotations.LayerMethod LayerMethods}.
 * 
 * @see ForkJoinUtil
 * @see AssociateUtil
 * @see ConnectUtil
 * 
 * @author gko
 *
 */
public class MethodUtil {
	
	//////////////////////////////////////////////////////////////////////////////
	//	ACTION ITEMS					    									//
	//////////////////////////////////////////////////////////////////////////////

	// classes D1, D2, D3 for void and parameterless layer methods
	
	static abstract public class D1<U> extends ActionItem {
		abstract protected void computeNext(U _unit);
		protected XIterator.D1<U> _xiter;
		@Override
		protected void compute() {
			while(_xiter.hasNext() ){ 
				computeNext(_xiter.next()); 
			} 
		}
	}
	
	static abstract public class D2<U> extends ActionItem {
		abstract protected void computeNext(U _unit);
		protected XIterator.D2<U> _xiter;
		@Override
		protected void compute() {
			while(_xiter.hasNext() ){ 
				computeNext(_xiter.next()); 
			} 
		}
	}
	
	static abstract public class D3<U> extends ActionItem {
		abstract protected void computeNext(U _unit);
		protected XIterator.D3<U> _xiter;
		@Override
		protected void compute() {
			while(_xiter.hasNext() ){ 
				computeNext(_xiter.next()); 
			} 
		}
	}
	
	// classes D1P, D2P, D3P for void layer methods with parameters
	
	static abstract public class D1P<U> extends ActionItem {
		abstract protected void computeNext(U _unit);
		protected XIterator.D1<U> _xiter;
		protected int _x1;
		@Override
		protected void compute() {
			while(_xiter.hasNext() ){ 
				_x1 = _xiter.getX1();
				computeNext(_xiter.next()); 
			} 
		}
	}
	
	static abstract public class D2P<U> extends ActionItem {
		abstract protected void computeNext(U _unit);
		protected XIterator.D2<U> _xiter;
		protected int _x1;
		protected int _x2;
		@Override
		protected void compute() {
			while(_xiter.hasNext() ){ 
				_x1 = _xiter.getX1();
				_x2 = _xiter.getX2();
				computeNext(_xiter.next()); 
			} 
		}
	}
	
	static abstract public class D3P<U> extends ActionItem {
		abstract protected void computeNext(U _unit);
		protected XIterator.D3<U> _xiter;
		protected int _x1;
		protected int _x2;
		protected int _x3;
		@Override
		protected void compute() {
			while(_xiter.hasNext() ){ 
				_x1 = _xiter.getX1();
				_x2 = _xiter.getX2();
				_x3 = _xiter.getX3();
				computeNext(_xiter.next()); 
			} 
		}
	}
	
	// classes D1X, D2X, D3X for non void layer methods
	
	static abstract public class D1X<U,T> extends ActionItem {
		abstract protected T computeNext(U _unit);
		protected XIterator.D1<U> _xiter;
		protected T[] _array;
		protected int _x1;
		@Override
		protected void compute() {
			while(_xiter.hasNext() ){ 
				_x1 = _xiter.getX1();
				_array[_x1] = computeNext(_xiter.next()); 
			} 
		}
	}
	
	static abstract public class D2X<U,T> extends ActionItem {
		abstract protected T computeNext(U _unit);
		protected XIterator.D2<U> _xiter;
		protected T[][] _array;
		protected int _x1;
		protected int _x2;
		@Override
		protected void compute() {
			while(_xiter.hasNext() ){ 
				_x2 = _xiter.getX2();
	            _x1 = _xiter.getX1();
	            _array[_x1][_x2] = computeNext(_xiter.next()); 
			} 
		}
	}
	
	static abstract public class D3X<U,T> extends ActionItem {
		abstract protected T computeNext(U _unit);
		protected XIterator.D3<U> _xiter;
		protected T[][][] _array;
		protected int _x1;
		protected int _x2;
		protected int _x3;
		@Override
		protected void compute() {
			while(_xiter.hasNext() ){ 
				_x3 = _xiter.getX3();
				_x2 = _xiter.getX2();
	            _x1 = _xiter.getX1();
				_array[_x1][_x2][_x3] = computeNext(_xiter.next()); 
			} 
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	// 						GETTING LISTS OF XIterators							//
	//////////////////////////////////////////////////////////////////////////////
	
	static public <U> List<XIterator.D1<U>> getXIteratorList(Layer.D1<?,U> layer) {
		Tuple[] x1Slices = getX1Slices(layer.length());
		List<XIterator.D1<U>> xIterList = new ArrayList<XIterator.D1<U>>();
		for (int i = 0; i < x1Slices.length; i++) {
			xIterList.add(layer.xIterator(x1Slices[i].low, x1Slices[i].high));
		}
		return xIterList;
	}
	static public <U> List<XIterator.D2<U>> getXIteratorList(Layer.D2<?,U> layer) {
		Tuple[] x1Slices = getX1Slices(layer.length());
		List<XIterator.D2<U>> xIterList = new ArrayList<XIterator.D2<U>>();
		for (int i = 0; i < x1Slices.length; i++) {
			xIterList.add(layer.xIterator(x1Slices[i].low, x1Slices[i].high));
		}
		return xIterList;
	}
	static public <U> List<XIterator.D3<U>> getXIteratorList(Layer.D3<?,U> layer) {
		Tuple[] x1Slices = getX1Slices(layer.length());
		List<XIterator.D3<U>> xIterList = new ArrayList<XIterator.D3<U>>();
		for (int i = 0; i < x1Slices.length; i++) {
			xIterList.add(layer.xIterator(x1Slices[i].low, x1Slices[i].high));
		}
		return xIterList;
	}
	
	
}

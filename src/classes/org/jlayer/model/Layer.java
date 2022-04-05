package org.jlayer.model;

import java.util.Iterator;

public interface Layer<U,T> {
	
	// interface Layer.D1
	interface D1<U,T> {
		int length();
		T get(int x1);
		void set(int x1, T elem);
		boolean isAdapter();
		U getUnit(int x1);
		void setUnit(int x1, U unit);
		Iterator<T> iterator();
		XIterator.D1<T> xIterator();
		XIterator.D1<T> xIterator(int x1low, int x1upb);
    }
	
	// interface Layer.D2
	interface D2<U,T> {
		int length();
		int length(int x1);
		T get(int x1, int x2);
		void set(int x1, int x2, T elem);
		boolean isAdapter();
		U getUnit(int x1, int x2);
		void setUnit(int x1, int x2, U unit);
		Iterator<T> iterator();
		XIterator.D2<T> xIterator();
		XIterator.D2<T> xIterator(int x1low, int x1upb);
	}
		
	// interface Layer.D3
	interface D3<U,T> {
		int length();
		int length(int x1);
		int length(int x1, int x2);
		T get(int x1, int x2, int x3);
		void set(int x1, int x2, int x3, T elem);
		boolean isAdapter();
		U getUnit(int x1, int x2, int x3);
		void setUnit(int x1, int x2, int x3, U unit);
		Iterator<T> iterator();
		XIterator.D3<T> xIterator();
		XIterator.D3<T> xIterator(int x1low, int x1upb);
	}
		
	// interface Layer
	int dims();
	int length();
	int length(int... ix);
	T get(int... ix);
	void set(int[] ix, T elem);
	void set(int x1, T elem);
	void set(int x1, int x2, T elem);
	void set(int x1, int x2, int x3, T elem);
	boolean isAdapter();
	U getUnit(int... ix);
	void setUnit(int[] ix, U unit);
	void setUnit(int x1, U unit);
	void setUnit(int x1, int x2, U unit);
	void setUnit(int x1, int x2, int x3, U unit);
	Iterator<T> iterator();
	XIterator<T> xIterator();
	XIterator<T> xIterator(int x1low, int x1upb);
	D1<U,T> getD1();
	D2<U,T> getD2();
	D3<U,T> getD3();
	
}

package org.jlayer.model;


import java.util.Iterator;

public interface XIterator<T> extends Iterator<T> {
	
	// interface XIterator.D1
	public static interface D1<T> extends Iterator<T> {
		int getX1();
    }
	
	// interface XIterator.D2
	public static interface D2<T> extends Iterator<T> {
		int getX1();
		int getX2();
	}
	
	// interface XIterator.D3
	public static interface D3<T> extends Iterator<T> {
		int getX1();
		int getX2();
		int getX3();
	}
	
	// interface XIterator
	
	int[] getXX();
	
}

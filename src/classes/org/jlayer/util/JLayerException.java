package org.jlayer.util;

public class JLayerException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public JLayerException() { super("runtime exception raised by org.jlayer"); }
	public JLayerException(final String s) { super(s); }

}
package org.jlayer.annotations;

import java.lang.annotation.*;

/**
 * 
 * The annotation for {@link java.lang.annotation.ElementType} <code>FIELD</code>
 * @author Dr. Gerd Kock
 * 
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface LayerField {
	/**
	 * If <code>isIndex==true</code> and if the field item is of type <code>int[]</code>,
	 * then the corresponding field layers are automatically initialized with the correct indices.
	 */
	boolean isIndex() default false;
}

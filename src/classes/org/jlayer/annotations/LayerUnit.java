package org.jlayer.annotations;

import java.lang.annotation.*;

/**
 * 
 * The annotation for {@link java.lang.annotation.ElementType} <code>TYPE</code>
 * @author Dr. Gerd Kock
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface LayerUnit {
	
}

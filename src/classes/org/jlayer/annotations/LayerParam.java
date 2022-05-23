package org.jlayer.annotations;

import java.lang.annotation.*;

/**
 * 
 * The annotation for {@link java.lang.annotation.ElementType} <code>PARAMETER</code>
 * @author Dr. Gerd Kock
 *
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface LayerParam {
	
}

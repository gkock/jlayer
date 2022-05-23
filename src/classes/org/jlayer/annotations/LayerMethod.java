package org.jlayer.annotations;

import java.lang.annotation.*;

/**
 * 
 * The annotation for {@link java.lang.annotation.ElementType} <code>METHOD</code>
 * @author Dr. Gerd Kock
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface LayerMethod {
	/**
	 * The <code>runtimeMode</code> determines the mode of execution.
	 * <br/>
	 * In case of <code>RuntimeMode.LOOP</code> the method layer is executed in a loop.
	 * <br/>
	 * In case of <code>RuntimeMode.FORKJOIN</code> the method layer is executed in a parallel, 
	 * where the implementation is based on the Java Fork/Join Framework.
	 * @return
	 */
	RuntimeMode runtimeMode() default RuntimeMode.LOOP;
}

package org.jlayer.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface LayerMethod {
	RuntimeMode runtimeMode() default RuntimeMode.LOOP;
}

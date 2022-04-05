package org.jlayer.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface LayerField {
	boolean isIndex() default false;
}

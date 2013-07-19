package xixi.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventMethod {

	 public abstract String name();
	 
	 public abstract String filter() default "";
	 

}

/**
 * 
 */
package xixi.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Jason.Yan
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface EventService {
	 public abstract String name();
	 
	 //if moduleId=1, it means it is a broadcast message. it will send 
	 // message to all the modules it is depend to.
	 public abstract short moduleId() default -1;
	 
	 public abstract String version();
}

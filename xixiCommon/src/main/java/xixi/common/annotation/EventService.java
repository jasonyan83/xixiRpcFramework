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
	 
	 public abstract short moduleId();
	 
	 public abstract String version();
}

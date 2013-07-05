/**
 * 
 */
package xixi.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Jason.Yan
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)

public @interface DefaultImplement {
    public String value() default "";
}

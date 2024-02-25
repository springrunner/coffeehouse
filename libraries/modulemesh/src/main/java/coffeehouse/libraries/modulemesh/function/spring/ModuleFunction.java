package coffeehouse.libraries.modulemesh.function.spring;

import java.lang.annotation.*;

/**
 * @author springrunner.kr@gmail.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ModuleFunction {
    String path() default "";
}

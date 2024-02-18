package coffeehouse.libraries.spring.beans.factory.annotation;

import coffeehouse.libraries.spring.beans.factory.config.PublishedBeanRegisterProcessor;

import java.lang.annotation.*;

/**
 * A marking annotation that can be used when publishing a bean to the parent bean factory.
 * Used with {@link PublishedBeanRegisterProcessor}.
 * 
 * @author springrunner.kr@gmail.com
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Published {
}

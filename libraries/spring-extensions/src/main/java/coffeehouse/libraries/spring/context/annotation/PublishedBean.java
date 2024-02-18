package coffeehouse.libraries.spring.context.annotation;

import coffeehouse.libraries.spring.beans.factory.annotation.Published;
import coffeehouse.libraries.spring.beans.factory.config.PublishedBeanRegisterProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * It extends the functionality of the Spring's {@link Bean} annotation by adding the {@link Published} annotation.
 * This allows the bean to be registered in the parent bean factory when used with {@link PublishedBeanRegisterProcessor}.
 * 
 * @author springrunner.kr@gmail.com
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Published
@Bean
public @interface PublishedBean {

    /**
     * Alias for {@link Bean#name}.
     */
    @AliasFor(annotation = Bean.class)
    String[] name() default {};

    /**
     * Alias for {@link Bean#value}.
     */
    @AliasFor(annotation = Bean.class)
    String[] value() default {};

    /**
     * Alias for {@link Bean#autowireCandidate}.
     */
    @AliasFor(annotation = Bean.class)
    boolean autowireCandidate() default true;

    /**
     * Alias for {@link Bean#initMethod}.
     */
    @AliasFor(annotation = Bean.class)
    String initMethod() default "";

    /**
     * Alias for {@link Bean#destroyMethod}.
     */    
    @AliasFor(annotation = Bean.class)
    String destroyMethod() default AbstractBeanDefinition.INFER_METHOD;
}

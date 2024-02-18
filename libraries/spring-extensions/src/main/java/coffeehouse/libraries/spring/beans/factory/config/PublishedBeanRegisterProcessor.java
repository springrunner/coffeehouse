package coffeehouse.libraries.spring.beans.factory.config;

import coffeehouse.libraries.spring.beans.factory.annotation.Published;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * Register beans annotated with Published to the parent bean factory.
 * 
 * @author springrunner.kr@gmail.com
 */
public class PublishedBeanRegisterProcessor implements BeanPostProcessor, BeanFactoryAware, InitializingBean {

    private ConfigurableListableBeanFactory beanFactory; 
    private ConfigurableListableBeanFactory parentBeanFactory;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (Objects.nonNull(beanFactory) && Objects.nonNull(parentBeanFactory)) {
            if (hasAnnotation(bean, Published.class)) {
                // If the Published annotation is declared on the component class
                parentBeanFactory.registerSingleton(beanName, bean);
                logger.debug("Published components to parent bean-factory: {}({})", beanName, bean);
            } else {
                var source = beanFactory.getBeanDefinition(beanName).getSource();
                if (source instanceof AnnotatedTypeMetadata metadata) {
                    if (metadata.isAnnotated(Published.class.getName())) {
                        //  If the Published annotation is declared on the bean definition method
                        parentBeanFactory.registerSingleton(beanName, bean);
                        logger.debug("Published components to parent bean-factory: {}({})", beanName, bean);
                    }
                }
            }
        }
        return bean;
    }

    private <A extends Annotation> Boolean hasAnnotation(Object bean, Class<A> annotationType) {
        var beanClass = ClassUtils.getUserClass(bean);
        if (AopUtils.isAopProxy(bean)) {
            beanClass = AopUtils.getTargetClass(bean);
        }
        return Objects.nonNull(AnnotationUtils.findAnnotation(beanClass, annotationType));
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof ConfigurableListableBeanFactory clbf) {
            this.beanFactory = clbf;
            if (clbf.getParentBeanFactory() instanceof ConfigurableListableBeanFactory pclbf) {
                this.parentBeanFactory = pclbf;
                logger.info("Found a parent bean-factory: {}", pclbf);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Objects.requireNonNull(beanFactory, "BeanFactory must not be null");
        if (Objects.isNull(parentBeanFactory)) {
            logger.warn("Missing ParentBeanFactory; component may not work properly");
        }
    }
}

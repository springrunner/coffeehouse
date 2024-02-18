package coffeehouse.libraries.spring.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.*;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Objects;

/**
 * It provides limited access to the beans in the Spring container by delegating the access to the underlying ListableBeanFactory.
 * 
 * @author springrunner.kr@gmail.com
 */
public class LimitedBeanFactoryAccessor implements BeanFactoryAware, InitializingBean {

    private ListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof ListableBeanFactory clbf) {
            this.beanFactory = clbf;
        } else {
            throw new BeanCreationException("Provided beanFactory is not an instance of ListableBeanFactory interface");
        }
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Objects.requireNonNull(beanFactory, "BeanFactory must not be null");
    }

    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        if (Objects.isNull(beanFactory)) {
            throw new InvalidPropertyException(getClass(), "beanFactory", "BeanFactory must not be null");
        }
        return beanFactory.getBeansWithAnnotation(annotationType);
    }
}

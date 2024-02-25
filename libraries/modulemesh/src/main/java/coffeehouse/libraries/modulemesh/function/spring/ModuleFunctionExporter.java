package coffeehouse.libraries.modulemesh.function.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public class ModuleFunctionExporter implements BeanPostProcessor {

    private final ConfigurableListableBeanFactory beanFactory;

    public ModuleFunctionExporter(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = Objects.requireNonNull(beanFactory);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        for (var beanType : ClassUtils.getAllInterfaces(bean)) {
            for (var beanMethod : beanType.getDeclaredMethods()) {
                var moduleFunctionMapping = AnnotationUtils.getAnnotation(beanMethod, ModuleFunctionMapping.class);
                if (!Objects.isNull(moduleFunctionMapping)) {
                    var moduleFunction = new InvocableModuleFunctionMethod(bean, beanType, beanMethod);
                    beanFactory.registerSingleton(moduleFunction.getPath(), moduleFunction);
                }
            }
        }
        return null;
    }
}

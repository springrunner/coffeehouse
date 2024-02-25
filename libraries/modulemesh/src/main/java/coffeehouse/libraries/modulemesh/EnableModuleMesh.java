package coffeehouse.libraries.modulemesh;

import coffeehouse.libraries.modulemesh.function.DefaultModuleFunctionOperations;
import coffeehouse.libraries.modulemesh.function.DefaultModuleFunctionRegistry;
import coffeehouse.libraries.modulemesh.function.ModuleFunctionOperations;
import coffeehouse.libraries.modulemesh.function.ModuleFunctionRegistry;
import coffeehouse.libraries.modulemesh.function.spring.ModuleFunctionExporter;
import coffeehouse.libraries.modulemesh.mapper.ObjectMapper;
import coffeehouse.libraries.modulemesh.mapper.jackson.JacksonObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author springrunner.kr@gmail.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({EnableModuleMesh.MapperConfiguration.class, EnableModuleMesh.FunctionConfiguration.class})
public @interface EnableModuleMesh {

    @Configuration(proxyBeanMethods = false)
    class MapperConfiguration {

        @Bean
        ObjectMapper moduleMeshObjectMapper(ObjectProvider<com.fasterxml.jackson.databind.ObjectMapper> mapperProvider) {
            return new JacksonObjectMapper(mapperProvider.getIfAvailable());
        }
    }

    @Configuration(proxyBeanMethods = false)
    class FunctionConfiguration {

        @Bean
        ModuleFunctionRegistry moduleFunctionRegistry() {
            return new DefaultModuleFunctionRegistry();
        }

        @Bean
        ModuleFunctionOperations moduleFunctionOperations(ModuleFunctionRegistry moduleFunctionRegistry, ObjectMapper objectMapper) {
            return new DefaultModuleFunctionOperations(moduleFunctionRegistry, objectMapper);
        }

        @Bean
        ModuleFunctionExporter moduleFunctionExporter(ConfigurableListableBeanFactory beanFactory) {
            return new ModuleFunctionExporter(beanFactory);
        }
    }
}

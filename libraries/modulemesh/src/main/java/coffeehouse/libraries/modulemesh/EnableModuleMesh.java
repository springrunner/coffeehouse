package coffeehouse.libraries.modulemesh;

import coffeehouse.libraries.modulemesh.event.ModuleEventChannel;
import coffeehouse.libraries.modulemesh.event.spring.ApplicationModuleEventChannels;
import coffeehouse.libraries.modulemesh.event.spring.ApplicationModuleEventInvokers;
import coffeehouse.libraries.modulemesh.event.spring.ApplicationModuleEventInvokers.ModuleEventInvoker;
import coffeehouse.libraries.modulemesh.event.EnableModuleEventProcessor;
import coffeehouse.libraries.modulemesh.function.DefaultModuleFunctionOperations;
import coffeehouse.libraries.modulemesh.function.DefaultModuleFunctionRegistry;
import coffeehouse.libraries.modulemesh.function.ModuleFunctionOperations;
import coffeehouse.libraries.modulemesh.function.ModuleFunctionRegistry;
import coffeehouse.libraries.modulemesh.function.spring.ModuleFunctionExporter;
import coffeehouse.libraries.modulemesh.mapper.ObjectMapper;
import coffeehouse.libraries.modulemesh.mapper.jackson.JacksonObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;


/**
 * @author springrunner.kr@gmail.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({
        EnableModuleMesh.MapperConfiguration.class,
        EnableModuleMesh.FunctionConfiguration.class,
        EnableModuleMesh.EventConfiguration.class,
})
public @interface EnableModuleMesh {

    ModuleEventChannelMode moduleEventChannelMode() default ModuleEventChannelMode.DIRECT;

    enum ModuleEventChannelMode {DIRECT, QUEUE}

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

    @Configuration(proxyBeanMethods = false)
    @EnableModuleEventProcessor
    class EventConfiguration implements ImportAware {

        private ModuleEventChannelMode moduleEventChannelMode;

        @Bean
        ModuleEventChannel moduleEventChannel(ApplicationEventPublisher applicationEventPublisher) {
            return switch (Objects.requireNonNull(moduleEventChannelMode, "ModuleEventChannelMode must not be null")) {
                case DIRECT -> ApplicationModuleEventChannels.direct(applicationEventPublisher);
                case QUEUE -> ApplicationModuleEventChannels.queue(applicationEventPublisher);
            };
        }

        @Bean
        ModuleEventInvoker moduleEventInvoker() {
            return ApplicationModuleEventInvokers.simple();
        }

        @Override
        public void setImportMetadata(AnnotationMetadata metadata) {
            var attributes = Objects.requireNonNull(metadata.getAnnotationAttributes(EnableModuleMesh.class.getName()));

            moduleEventChannelMode = (ModuleEventChannelMode) attributes.get("moduleEventChannelMode");
        }
    }
}

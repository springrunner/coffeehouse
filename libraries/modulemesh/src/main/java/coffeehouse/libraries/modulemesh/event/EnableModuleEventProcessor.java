package coffeehouse.libraries.modulemesh.event;

import coffeehouse.libraries.modulemesh.event.spring.ApplicationModuleEventInvokers;
import coffeehouse.libraries.modulemesh.event.spring.ApplicationModuleEventProcessor;
import coffeehouse.libraries.modulemesh.mapper.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.scheduling.annotation.EnableAsync;

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
@Import(EnableModuleEventProcessor.ModuleEventProcessorConfiguration.class)
@EnableAsync
public @interface EnableModuleEventProcessor {
    
    @Configuration(proxyBeanMethods = false)
    @Conditional(ConditionalOnMissingApplicationEventMulticasterBean.class)
    class ModuleEventProcessorConfiguration {

        private final Logger logger = LoggerFactory.getLogger(getClass());
        
        @Bean(AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME)
        ApplicationModuleEventProcessor applicationModuleEventProcessor(ApplicationContext applicationContext) {
            if (Objects.nonNull(applicationContext.getParent())) {
                var provider = applicationContext.getParent().getBeanProvider(ApplicationModuleEventProcessor.class);
                return provider.getIfAvailable(() -> {
                    logger.info("ApplicationModuleEventProcessor bean not found in parent context");
                    return createApplicationModuleEventProcessor(applicationContext);
                });
            }
            return createApplicationModuleEventProcessor(applicationContext);
        }

        private ApplicationModuleEventProcessor createApplicationModuleEventProcessor(ApplicationContext applicationContext) {
            var moduleEventChannel = applicationContext.getBean(ModuleEventChannel.class);
            var moduleEventInvoker = applicationContext.getBean(ApplicationModuleEventInvokers.ModuleEventInvoker.class);
            var objectMapper = applicationContext.getBean(ObjectMapper.class);

            var applicationModuleEventProcessor =  new ApplicationModuleEventProcessor(moduleEventChannel, moduleEventInvoker, objectMapper);
            logger.debug("Created ApplicationModuleEventProcessor: {}", applicationModuleEventProcessor);

            return applicationModuleEventProcessor;
        }
    }

    class ConditionalOnMissingApplicationEventMulticasterBean implements ConfigurationCondition {
        
        @Override
        public ConfigurationPhase getConfigurationPhase() {
            return ConfigurationPhase.REGISTER_BEAN;
        }
        
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return !context.getBeanFactory().containsLocalBean(
                    AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME
            );
        }
    }
}

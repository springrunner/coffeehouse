package coffeehouse.libraries.modulemesh;

import coffeehouse.libraries.modulemesh.event.EnableModuleEventProcessor;
import coffeehouse.libraries.modulemesh.event.ModuleEvent;
import coffeehouse.libraries.modulemesh.event.ModuleEventChannel;
import coffeehouse.libraries.modulemesh.event.inbox.ModuleEventInbox;
import coffeehouse.libraries.modulemesh.event.outbox.ModuleEventOutbox;
import coffeehouse.libraries.modulemesh.event.outbox.support.OutboxModuleEventChannel;
import coffeehouse.libraries.modulemesh.event.serializer.ModuleEventDeserializer;
import coffeehouse.libraries.modulemesh.event.serializer.ModuleEventSerializer;
import coffeehouse.libraries.modulemesh.event.spring.*;
import coffeehouse.libraries.modulemesh.event.spring.ApplicationModuleEventInvokers.ModuleEventInvoker;
import coffeehouse.libraries.modulemesh.event.spring.amqp.RabbitModuleEventChannel;
import coffeehouse.libraries.modulemesh.event.spring.amqp.RabbitOutboxModuleEventProcessor;
import coffeehouse.libraries.modulemesh.function.DefaultModuleFunctionOperations;
import coffeehouse.libraries.modulemesh.function.DefaultModuleFunctionRegistry;
import coffeehouse.libraries.modulemesh.function.ModuleFunctionOperations;
import coffeehouse.libraries.modulemesh.function.ModuleFunctionRegistry;
import coffeehouse.libraries.modulemesh.function.spring.ModuleFunctionExporter;
import coffeehouse.libraries.modulemesh.mapper.ObjectMapper;
import coffeehouse.libraries.modulemesh.mapper.jackson.JacksonObjectMapper;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.*;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    int moduleEventOutboxTaskSchedulerPollSize() default 1;
    
    long moduleEventOutboxTaskInitialDelay() default 5;

    long moduleEventOutboxTaskPeriod() default 3;

    TimeUnit moduleEventOutboxTaskUnit() default TimeUnit.SECONDS;

    enum ModuleEventChannelMode {DIRECT, QUEUE, RABBIT}

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
        private Integer moduleEventOutboxTaskSchedulerPollSize;
        private Long moduleEventOutboxTaskInitialDelay;
        private Long moduleEventOutboxTaskPeriod;
        private TimeUnit moduleEventOutboxTaskUnit;
        
        @Bean
        ModuleEventChannel moduleEventChannel(
                ObjectProvider<ApplicationEventPublisher> applicationEventPublisher,
                ObjectProvider<ApplicationEventMulticaster> applicationEventMulticaster,
                ObjectProvider<ModuleEventSerializer<ModuleEvent>> moduleEventSerializer,
                ObjectProvider<ModuleEventDeserializer<ModuleEvent>> moduleEventDeserializer,
                ObjectProvider<ConnectionFactory> connectionFactory,
                Environment environment

        ) {
            return switch (Objects.requireNonNull(moduleEventChannelMode, "ModuleEventChannelMode must not be null")) {
                case DIRECT -> ApplicationModuleEventChannels.direct(applicationEventPublisher.getIfAvailable());
                case QUEUE -> ApplicationModuleEventChannels.queue(applicationEventPublisher.getIfAvailable());
                case RABBIT -> {
                    var queueNames = environment.getRequiredProperty("modulemesh.event.rabbit.queue-names", String[].class);
                    var moduleEventChannelProperties = new RabbitModuleEventChannel.RabbitModuleEventChannelProperties(
                            environment.getRequiredProperty("modulemesh.event.rabbit.default-exchange"),
                            environment.getProperty("modulemesh.event.rabbit.default-routing-key", ""),
                            new HashSet<>(Arrays.asList(queueNames))
                    );

                    yield RabbitModuleEventChannel.simple(
                            connectionFactory,
                            applicationEventPublisher,
                            applicationEventMulticaster,
                            moduleEventSerializer,
                            moduleEventDeserializer,
                            moduleEventChannelProperties,
                            null
                    );
                }
            };
        }

        @Primary
        @Bean
        OutboxModuleEventChannel outboxModuleEventChannel(List<ModuleEventOutbox> outboxes, ModuleEventChannel moduleEventChannel) {
            return new OutboxModuleEventChannel(outboxes, moduleEventChannel);
        }

        @Bean
        AbstractOutboxModuleEventProcessor outboxModuleEventProcessor(
                PlatformTransactionManager transactionManager,
                LockRegistry lockRegistry,
                ApplicationEventMulticaster applicationEventMulticaster,
                Optional<RabbitModuleEventChannel> rabbitEventChannel
        ) {
            if (rabbitEventChannel.isPresent()) {
                return new RabbitOutboxModuleEventProcessor(
                        rabbitEventChannel.get(),
                        transactionManager,
                        lockRegistry,
                        moduleEventOutboxTaskSchedulerPollSize,
                        moduleEventOutboxTaskInitialDelay,
                        moduleEventOutboxTaskPeriod,
                        moduleEventOutboxTaskUnit
                );
            } else {
                return new ApplicationOutboxModuleEventProcessor(
                        applicationEventMulticaster,
                        transactionManager,
                        lockRegistry,
                        moduleEventOutboxTaskSchedulerPollSize,
                        moduleEventOutboxTaskInitialDelay,
                        moduleEventOutboxTaskPeriod,
                        moduleEventOutboxTaskUnit
                );
            }
        }

        @Bean
        ModuleEventInvoker moduleEventInvoker() {
            return ApplicationModuleEventInvokers.simple();
        }

        @Primary
        @Bean
        InboxModuleEventInvoker inboxModuleEventInvoker(List<ModuleEventInbox> moduleEventInboxes, ModuleEventInvoker moduleEventInvoker) {
            return new InboxModuleEventInvoker(moduleEventInboxes, moduleEventInvoker);
        }

        @Override
        public void setImportMetadata(AnnotationMetadata metadata) {
            var attributes = Objects.requireNonNull(metadata.getAnnotationAttributes(EnableModuleMesh.class.getName()));

            moduleEventChannelMode = (ModuleEventChannelMode) attributes.get("moduleEventChannelMode");
            moduleEventOutboxTaskSchedulerPollSize = (Integer) attributes.get("moduleEventOutboxTaskSchedulerPollSize");
            moduleEventOutboxTaskInitialDelay = (Long) attributes.get("moduleEventOutboxTaskInitialDelay");
            moduleEventOutboxTaskPeriod = (Long) attributes.get("moduleEventOutboxTaskPeriod");
            moduleEventOutboxTaskUnit = (TimeUnit) attributes.get("moduleEventOutboxTaskUnit");
        }
    }
}

package coffeehouse.libraries.modulemesh.event.spring.amqp;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;
import coffeehouse.libraries.modulemesh.event.ModuleEventChannel;
import coffeehouse.libraries.modulemesh.event.serializer.ModuleEventDeserializer;
import coffeehouse.libraries.modulemesh.event.serializer.ModuleEventSerializer;
import coffeehouse.libraries.modulemesh.event.serializer.ObjectModuleEventSerde;
import coffeehouse.libraries.modulemesh.event.spring.PayloadModuleEvent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.config.ContainerCustomizer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Objects;
import java.util.Set;

/**
 * @author springrunner.kr@gmail.com
 */
public class RabbitModuleEventChannel implements ModuleEventChannel, ApplicationContextAware, InitializingBean, DisposableBean, SmartLifecycle {

    private final RabbitTemplate rabbitTemplate;
    private final String defaultExchange;
    private final String defaultRoutingKey;
    private final AbstractMessageListenerContainer messageListenerContainer;
    private final ModuleEventSerializer<ModuleEvent> moduleEventSerializer;

    RabbitModuleEventChannel(
            RabbitTemplate rabbitTemplate,
            String defaultExchange,
            String defaultRoutingKey,
            AbstractMessageListenerContainer messageListenerContainer,
            ModuleEventSerializer<ModuleEvent> moduleEventSerializer
    ) {
        this.rabbitTemplate = Objects.requireNonNull(rabbitTemplate, "RabbitTemplate must not be null");
        this.defaultExchange = Objects.requireNonNull(defaultExchange, "DefaultExchange must not be null");
        this.defaultRoutingKey = Objects.requireNonNull(defaultRoutingKey, "DefaultRoutingKey must not be null");
        this.messageListenerContainer = Objects.requireNonNull(messageListenerContainer, "MessageListenerContainer must not be null");
        this.moduleEventSerializer = Objects.requireNonNull(moduleEventSerializer, "ModuleEventSerializer must not be null");
    }

    @Override
    public void sendEvent(ModuleEvent event) {
        var message = MessageBuilder.withBody(moduleEventSerializer.serializeToByteArray(event)).build();
        rabbitTemplate.send(defaultExchange, defaultRoutingKey, message);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        messageListenerContainer.setApplicationContext(applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rabbitTemplate.afterPropertiesSet();
        messageListenerContainer.afterPropertiesSet();
    }

    @Override
    public void destroy() throws Exception {
        rabbitTemplate.destroy();
        messageListenerContainer.destroy();
    }

    @Override
    public void start() {
        messageListenerContainer.start();
    }

    @Override
    public void stop() {
        messageListenerContainer.stop();
    }

    @Override
    public void stop(Runnable callback) {
        messageListenerContainer.stop(callback);
    }

    @Override
    public boolean isRunning() {
        return messageListenerContainer.isRunning();
    }

    private static class MessageToModuleEventRepeater implements MessageListener {

        private final ObjectProvider<ApplicationEventMulticaster> applicationEventMulticaster;
        private final ModuleEventDeserializer<ModuleEvent> moduleEventDeserializer;


        private MessageToModuleEventRepeater(ObjectProvider<ApplicationEventMulticaster> applicationEventMulticaster, ModuleEventDeserializer<ModuleEvent> moduleEventDeserializer) {
            this.applicationEventMulticaster = Objects.requireNonNull(applicationEventMulticaster, "ApplicationEventMulticaster must not be null");
            this.moduleEventDeserializer = Objects.requireNonNull(moduleEventDeserializer, "ModuleEventDeserializer must not be null");
        }

        @Override
        public void onMessage(Message message) {
            var moduleEvent = PayloadModuleEvent.of(this, moduleEventDeserializer.deserializeFromByteArray(message.getBody()));
            applicationEventMulticaster.ifAvailable(it -> it.multicastEvent(moduleEvent));
        }
    }

    public record RabbitModuleEventChannelProperties(
            String defaultExchange,
            String defaultRoutingKey,
            Set<String> queueNames) {

        public RabbitModuleEventChannelProperties {
            Objects.requireNonNull(defaultExchange, "defaultExchange must not be null");
            Objects.requireNonNull(defaultRoutingKey, "defaultRoutingKey must not be null");
            Objects.requireNonNull(queueNames, "queueNames must not be null");

            if (queueNames.isEmpty()) {
                throw new IllegalArgumentException("Do not allow empty queues");
            }
        }
    }

    public static RabbitModuleEventChannel simple(
            ObjectProvider<ConnectionFactory> connectionFactory,
            ObjectProvider<ApplicationEventPublisher> applicationEventPublisher,
            ObjectProvider<ApplicationEventMulticaster> applicationEventMulticaster,
            ObjectProvider<ModuleEventSerializer<ModuleEvent>> moduleEventSerializer,
            ObjectProvider<ModuleEventDeserializer<ModuleEvent>> moduleEventDeserializer,
            RabbitModuleEventChannelProperties moduleEventChannelProperties,
            ContainerCustomizer<SimpleMessageListenerContainer> containerCustomizer
    ) {
        Objects.requireNonNull(connectionFactory.getIfAvailable(), "connectionFactory must not be null");
        Objects.requireNonNull(applicationEventPublisher.getIfAvailable(), "applicationEventPublisher must not be null");
        Objects.requireNonNull(moduleEventChannelProperties, "moduleEventChannelProperties must not be null");

        var defaultModuleEventSerde = new ObjectModuleEventSerde();

        var rabbitTemplate = new RabbitTemplate(connectionFactory.getIfAvailable());
        var messageListener = new MessageToModuleEventRepeater(applicationEventMulticaster, moduleEventDeserializer.getIfAvailable(() -> defaultModuleEventSerde));
        var messageListenerContainer = new SimpleMessageListenerContainer(connectionFactory.getIfAvailable());
        messageListenerContainer.setApplicationEventPublisher(applicationEventPublisher.getIfAvailable());
        messageListenerContainer.setQueueNames(moduleEventChannelProperties.queueNames().toArray(String[]::new));
        messageListenerContainer.setMessageListener(messageListener);
        messageListenerContainer.setMissingQueuesFatal(false);
        messageListenerContainer.setConsumerStartTimeout(10000);
        messageListenerContainer.setDefaultRequeueRejected(false);
        messageListenerContainer.setShutdownTimeout(10 * 1000);
        messageListenerContainer.setRecoveryBackOff(new FixedBackOff(5000, FixedBackOff.UNLIMITED_ATTEMPTS));
        messageListenerContainer.setMismatchedQueuesFatal(false);
        messageListenerContainer.setAutoDeclare(false);

        if (!Objects.isNull(containerCustomizer)) {
            containerCustomizer.configure(messageListenerContainer);
        }

        return new RabbitModuleEventChannel(
                rabbitTemplate,
                moduleEventChannelProperties.defaultExchange(),
                moduleEventChannelProperties.defaultRoutingKey(),
                messageListenerContainer,
                moduleEventSerializer.getIfAvailable(() -> defaultModuleEventSerde)
        );
    }
}

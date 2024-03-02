package coffeehouse.libraries.modulemesh.event.spring;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;
import coffeehouse.libraries.modulemesh.event.ModuleEventAttributes.EventType;
import coffeehouse.libraries.modulemesh.event.ModuleEventChannel;
import coffeehouse.libraries.modulemesh.event.ModuleEventPublisher;
import coffeehouse.libraries.modulemesh.event.spring.ApplicationModuleEventInvokers.ModuleEventInvoker;
import coffeehouse.libraries.modulemesh.mapper.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.AbstractApplicationEventMulticaster;
import org.springframework.context.event.ApplicationListenerMethodAdapter;
import org.springframework.core.ResolvableType;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author springrunner.kr@gmail.com
 */
public class ApplicationModuleEventProcessor extends AbstractApplicationEventMulticaster implements ModuleEventPublisher {
    
    private final ModuleEventChannel moduleEventChannel;
    private final ModuleEventInvoker moduleEventInvoker;
    private final ObjectMapper objectMapper;

    private final ModuleEventTypeRegistry moduleEventTypeRegistry = new ModuleEventTypeRegistry();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ApplicationModuleEventProcessor(ModuleEventChannel moduleEventChannel, ModuleEventInvoker moduleEventInvoker, ObjectMapper objectMapper) {
        this.moduleEventChannel = Objects.requireNonNull(moduleEventChannel, "ModuleEventChannel must not be null");
        this.moduleEventInvoker = Objects.requireNonNull(moduleEventInvoker, "ModuleEventInvoker must not be null");
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper must not be null");
    }

    @Override
    public void publishEvent(ModuleEvent event) {
        moduleEventChannel.sendEvent(event);
    }

    @Override
    public void multicastEvent(ApplicationEvent event) {
        multicastEvent(event, null);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {
        if (event instanceof PayloadModuleEvent container) {
            final var moduleEvent = container.getPayload();
            final var moduleEventType = container.getResolvableType();
            final var relatedModuleEventTypes = moduleEventTypeRegistry.getEventTypes(moduleEvent);

            for (var relatedModuleEventType : relatedModuleEventTypes) {
                final var mappedEvent = mapApplicationEvent(moduleEvent, moduleEventType, relatedModuleEventType);
                final var applicationListeners = getApplicationListeners(mappedEvent, mappedEvent.getResolvableType());

                moduleEventInvoker.invoke(mappedEvent.getPayload(), () -> {
                    for (ApplicationListener listener : applicationListeners) {
                        listener.onApplicationEvent(mappedEvent);
                    }
                });
            }
        } else {
            var type = (eventType != null ? eventType : ResolvableType.forInstance(event));
            for (ApplicationListener listener : getApplicationListeners(event, type)) {
                listener.onApplicationEvent(event);
            }
        }
    }

    private PayloadApplicationEvent<ModuleEvent> mapApplicationEvent(ModuleEvent event, ResolvableType eventType, ResolvableType mappedEventType) {
        if ((ObjectUtils.nullSafeEquals(eventType.getType(), mappedEventType.getType()) &&
                ObjectUtils.nullSafeEquals(eventType.getComponentType(), mappedEventType.getComponentType()))) {
            return new PayloadApplicationEvent<>(this, event, eventType);
        } else {
            var mappedPayload = (ModuleEvent) objectMapper.map(event, mappedEventType.toClass());
            return new PayloadApplicationEvent<>(this, mappedPayload, mappedEventType);
        }
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        super.addApplicationListener(listener);
        obtainModuleEventTypes(listener).forEach(moduleEventTypeRegistry::register);
    }

    @SuppressWarnings("unchecked")
    private List<ResolvableType> obtainModuleEventTypes(ApplicationListener<?> listener) {
        try {
            if (listener instanceof ApplicationListenerMethodAdapter) {
                var bean = PropertyAccessorFactory.forDirectFieldAccess(listener);
                return (List<ResolvableType>) bean.getPropertyValue("declaredEventTypes");
            }
        } catch (Exception error) {
            logger.warn("Failed to obtain module-events in listener: " + listener, error);
        }
        return Collections.emptyList();
    }

    private class ModuleEventTypeRegistry {

        private final Map<EventType, Set<ResolvableType>> eventTypes = new ConcurrentHashMap<>();

        void register(ResolvableType eventType) {
            var eventClass = eventType.toClass();
            if (ModuleEvent.class.isAssignableFrom(eventClass)) {
                var parsedEventType = EventType.of(eventClass);
                eventTypes.computeIfAbsent(parsedEventType, types -> new HashSet<>()).add(eventType);
                logger.debug("Register new event-type: {}", eventType);
            }
        }

        Set<ResolvableType> getEventTypes(ModuleEvent moduleEvent) {
            return eventTypes.getOrDefault(moduleEvent.eventType(), Collections.emptySet());
        }

        Set<ResolvableType> getEventTypes(Class<ModuleEvent> eventClass) {
            return eventTypes.getOrDefault(EventType.of(eventClass), Collections.emptySet());
        }
    }
}

package coffeehouse.libraries.modulemesh.event.spring;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;
import coffeehouse.libraries.modulemesh.event.ModuleEventChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.ErrorHandler;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class ApplicationModuleEventChannels {

    public static DirectModuleEventChannel direct(ApplicationEventPublisher applicationEventPublisher) {
        return new DirectModuleEventChannel(applicationEventPublisher);
    }

    public static QueueModuleEventChannel queue(ApplicationEventPublisher applicationEventPublisher) {
        return new QueueModuleEventChannel(Integer.MAX_VALUE, applicationEventPublisher);
    }

    public static QueueModuleEventChannel queue(int capacity, ApplicationEventPublisher applicationEventPublisher) {
        return new QueueModuleEventChannel(capacity, applicationEventPublisher);
    }

    public static class DirectModuleEventChannel implements ModuleEventChannel {

        private final ApplicationEventPublisher applicationEventPublisher;

        public DirectModuleEventChannel(ApplicationEventPublisher applicationEventPublisher) {
            this.applicationEventPublisher = Objects.requireNonNull(applicationEventPublisher, "ApplicationEventPublisher must not be null");
        }

        @Override
        public void sendEvent(ModuleEvent event) {
            applicationEventPublisher.publishEvent(PayloadModuleEvent.of(this, event));
        }
    }

    public static class QueueModuleEventChannel implements ModuleEventChannel, SmartLifecycle {

        private static final Logger logger = LoggerFactory.getLogger(QueueModuleEventChannel.class);

        private final Queue<ModuleEvent> events;
        private final ApplicationEventPublisher applicationEventPublisher;
        private final ErrorHandler errorHandler;

        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        private final AtomicBoolean running = new AtomicBoolean(false);

        public QueueModuleEventChannel(int capacity, ApplicationEventPublisher applicationEventPublisher) {
            this(capacity, applicationEventPublisher, error -> logger.error("Failed to publish event", error));
        }

        public QueueModuleEventChannel(int capacity, ApplicationEventPublisher applicationEventPublisher, ErrorHandler errorHandler) {
            this.events = new LinkedBlockingQueue<>(capacity);
            this.applicationEventPublisher = Objects.requireNonNull(applicationEventPublisher, "ApplicationEventPublisher must not be null");
            this.errorHandler = errorHandler;
        }

        @Override
        public void sendEvent(ModuleEvent event) {
            events.add(event);
        }

        @Override
        public void start() {
            scheduler.scheduleWithFixedDelay(() -> {
                var event = events.poll();
                if (Objects.nonNull(event)) {
                    try {
                        applicationEventPublisher.publishEvent(PayloadModuleEvent.of(this, event));
                    } catch (Throwable error) {
                        errorHandler.handleError(error);

                        // TODO Implementing retry policies
                    }
                }
            }, 0, 100, TimeUnit.MILLISECONDS);
            running.set(true);
        }

        @Override
        public void stop() {
            scheduler.shutdown();
            running.set(false);
        }

        @Override
        public boolean isRunning() {
            return running.get();
        }
    }
}

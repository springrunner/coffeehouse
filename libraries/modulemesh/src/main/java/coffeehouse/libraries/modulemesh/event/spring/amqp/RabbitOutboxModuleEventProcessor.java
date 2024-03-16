package coffeehouse.libraries.modulemesh.event.spring.amqp;

import coffeehouse.libraries.modulemesh.event.spring.AbstractOutboxModuleEventProcessor;
import coffeehouse.libraries.modulemesh.event.spring.PayloadModuleEvent;
import coffeehouse.libraries.modulemesh.event.spring.amqp.RabbitModuleEventChannel;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author springrunner.kr@gmail.com
 */
public class RabbitOutboxModuleEventProcessor extends AbstractOutboxModuleEventProcessor {

    private final RabbitModuleEventChannel rabbitModuleEventChannel;

    public RabbitOutboxModuleEventProcessor(
            RabbitModuleEventChannel rabbitModuleEventChannel, 
            PlatformTransactionManager transactionManager,
            LockRegistry lockRegistry,
            int taskSchedulerPollSize,
            long taskInitialDelay,
            long taskPeriod,
            TimeUnit taskUnit
    ) {
        super(transactionManager, lockRegistry, taskSchedulerPollSize, taskInitialDelay, taskPeriod, taskUnit);
        this.rabbitModuleEventChannel = Objects.requireNonNull(rabbitModuleEventChannel, "RabbitModuleEventChannel must not be null");
    }

    @Override
    protected void multicastEvent(PayloadModuleEvent<?> event) {
        rabbitModuleEventChannel.sendEvent(event.getPayload());
    }
}

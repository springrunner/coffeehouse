package coffeehouse.libraries.modulemesh.event.spring;

import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author springrunner.kr@gmail.com
 */
public class ApplicationOutboxModuleEventProcessor extends AbstractOutboxModuleEventProcessor {

    private final ApplicationEventMulticaster applicationEventMulticaster;

    public ApplicationOutboxModuleEventProcessor(
            ApplicationEventMulticaster applicationEventMulticaster, 
            PlatformTransactionManager transactionManager, 
            LockRegistry lockRegistry,
            int taskSchedulerPollSize,
            long taskInitialDelay,
            long taskPeriod,
            TimeUnit taskUnit
    ) {
        super(transactionManager, lockRegistry, taskSchedulerPollSize, taskInitialDelay, taskPeriod, taskUnit);
        this.applicationEventMulticaster = Objects.requireNonNull(applicationEventMulticaster, "ApplicationEventMulticaster must not be null");
    }

    @Override
    protected void multicastEvent(PayloadModuleEvent<?> event) {
        applicationEventMulticaster.multicastEvent(event);
    }
}

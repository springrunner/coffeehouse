package coffeehouse.libraries.modulemesh.event.spring;

import coffeehouse.libraries.modulemesh.event.outbox.ModuleEventOutboxAccessor;
import coffeehouse.libraries.modulemesh.event.outbox.ModuleEventOutboxAccessorRegistry;
import coffeehouse.libraries.modulemesh.event.spring.PayloadModuleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class AbstractOutboxModuleEventProcessor implements ModuleEventOutboxAccessorRegistry, InitializingBean, DisposableBean {
    
    private final TransactionTemplate transactionTemplate;
    private final Lock lock;
    private final ScheduledExecutorService taskScheduler;
    private final long taskInitialDelay;
    private final long taskPeriod;
    private final TimeUnit taskUnit;

    private final Map<ModuleEventOutboxAccessor, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    protected AbstractOutboxModuleEventProcessor(
            PlatformTransactionManager transactionManager, 
            LockRegistry lockRegistry,
            int taskSchedulerPollSize,
            long taskInitialDelay,
            long taskPeriod,
            TimeUnit taskUnit
    ) {
        this.transactionTemplate = new TransactionTemplate(Objects.requireNonNull(transactionManager, "PlatformTransactionManager must not be null"));
        this.lock = Objects.requireNonNull(lockRegistry, "LockRegistry must not be null").obtain("ModuleEventOutboxProcessor");
        this.taskScheduler = Executors.newScheduledThreadPool(taskSchedulerPollSize, new CustomizableThreadFactory("outbox-task-"));
        this.taskInitialDelay = taskInitialDelay;
        this.taskPeriod = taskPeriod;
        this.taskUnit = Objects.requireNonNull(taskUnit, "TaskUnit must not be null");
    }

    protected abstract void multicastEvent(PayloadModuleEvent<?> event);

    @Override
    public void registerModuleEventOutboxAccessor(ModuleEventOutboxAccessor moduleEventOutboxAccessor) {
        if (tasks.containsKey(moduleEventOutboxAccessor)) {
            throw new IllegalArgumentException("The outbox-accessor is already registered");
        }
        
        tasks.put(moduleEventOutboxAccessor, scheduleTask(moduleEventOutboxAccessor));
        logger.info("Registered a new outbox-accessor: {}", moduleEventOutboxAccessor);
    }

    protected ScheduledFuture<?> scheduleTask(ModuleEventOutboxAccessor outbox) {
        return taskScheduler.scheduleAtFixedRate(() -> {
            boolean lockAcquired = false;
            try {
                lockAcquired = lock.tryLock(1, TimeUnit.SECONDS);
            } catch (Exception error) {
                logger.info("Failed to acquire lock", error);
            }

            if(lockAcquired){
                logger.debug("Started scheduled outbox task: {}", outbox);
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.MINUTE, -1);

                    outbox.poll(calendar.getTime()).forEach(invoice -> {
                        transactionTemplate.executeWithoutResult(transactionStatus -> {
                            multicastEvent(PayloadModuleEvent.of(this, invoice.event()));
                            outbox.remove(invoice.identifier());
                        });
                    });
                    logger.info("Finished scheduled outbox task: {}", outbox);
                } catch (Exception error) {
                    logger.error("Failed to scheduled outbox task: %s".formatted(outbox) , error);
                } finally {
                    lock.unlock();
                }
            }
        }, taskInitialDelay, taskPeriod, taskUnit);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.transactionTemplate.afterPropertiesSet();
    }

    @Override
    public void destroy() throws Exception {
        taskScheduler.shutdown();
    }
}

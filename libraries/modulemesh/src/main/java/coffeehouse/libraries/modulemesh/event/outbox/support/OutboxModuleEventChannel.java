package coffeehouse.libraries.modulemesh.event.outbox.support;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;
import coffeehouse.libraries.modulemesh.event.ModuleEventChannel;
import coffeehouse.libraries.modulemesh.event.outbox.ModuleEventOutbox;
import coffeehouse.libraries.modulemesh.event.outbox.ModuleEventOutboxRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public class OutboxModuleEventChannel implements ModuleEventOutboxRegistry, ModuleEventChannel {

    private final ModuleEventOutboxComposite moduleEventOutboxComposite;
    private final ModuleEventChannel moduleEventChannel;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public OutboxModuleEventChannel(List<ModuleEventOutbox> outboxes, ModuleEventChannel moduleEventChannel) {
        this.moduleEventOutboxComposite = new ModuleEventOutboxComposite(outboxes);
        this.moduleEventChannel = Objects.requireNonNull(moduleEventChannel, "ModuleEventChannel must not be null");
    }

    @Override
    public void registerModuleEventOutbox(ModuleEventOutbox moduleEventOutbox) {
        moduleEventOutboxComposite.addOutbox(moduleEventOutbox);
    }

    @Override
    public void sendEvent(ModuleEvent event) {
        if (moduleEventOutboxComposite.supports(event)) {
            moduleEventOutboxComposite.put(event);
            logger.debug("Put module-event in outbox: {}", event);
        } else {
            moduleEventChannel.sendEvent(event);
        }
    }
}

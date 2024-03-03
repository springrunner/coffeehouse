package coffeehouse.libraries.modulemesh.event.outbox;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;

/**
 * @author springrunner.kr@gmail.com
 */
public interface ModuleEventOutbox {

    boolean supports(ModuleEvent moduleEvent);

    OutboxIdentifier put(ModuleEvent event);
}

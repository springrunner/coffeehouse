package coffeehouse.libraries.modulemesh.event.inbox;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;

/**
 * @author springrunner.kr@gmail.com
 */
public interface ModuleEventInbox {

    boolean supports(ModuleEvent moduleEvent);

    InboxIdentifier process(ModuleEvent event, Runnable eventProcessor);
}

package coffeehouse.libraries.modulemesh.event.spring;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;
import coffeehouse.libraries.modulemesh.event.inbox.ModuleEventInbox;
import coffeehouse.libraries.modulemesh.event.inbox.ModuleEventInboxRegistry;
import coffeehouse.libraries.modulemesh.event.inbox.support.ModuleEventInboxComposite;
import coffeehouse.libraries.modulemesh.event.spring.ApplicationModuleEventInvokers;

import java.util.List;
import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public class InboxModuleEventInvoker implements ModuleEventInboxRegistry, ApplicationModuleEventInvokers.ModuleEventInvoker {

    private final ModuleEventInboxComposite moduleEventInboxComposite;
    private final ApplicationModuleEventInvokers.ModuleEventInvoker moduleEventInvoker;

    public InboxModuleEventInvoker(List<ModuleEventInbox> moduleEventInboxes, ApplicationModuleEventInvokers.ModuleEventInvoker moduleEventInvoker) {
        this.moduleEventInboxComposite = new ModuleEventInboxComposite(moduleEventInboxes);
        this.moduleEventInvoker = Objects.requireNonNull(moduleEventInvoker, "ModuleEventInvoker must not be null");
    }


    @Override
    public void registerModuleEventInbox(ModuleEventInbox moduleEventInbox) {
        moduleEventInboxComposite.addInbox(moduleEventInbox);
    }

    @Override
    public void invoke(ModuleEvent moduleEvent, Runnable moduleEventHandler) {
        if (moduleEventInboxComposite.supports(moduleEvent)) {
            moduleEventInboxComposite.process(moduleEvent, moduleEventHandler);
        } else {
            moduleEventInvoker.invoke(moduleEvent, moduleEventHandler);
        }
    }
}

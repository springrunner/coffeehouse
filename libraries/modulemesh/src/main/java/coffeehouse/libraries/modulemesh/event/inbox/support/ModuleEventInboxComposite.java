package coffeehouse.libraries.modulemesh.event.inbox.support;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;
import coffeehouse.libraries.modulemesh.event.inbox.InboxIdentifier;
import coffeehouse.libraries.modulemesh.event.inbox.ModuleEventInbox;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author springrunner.kr@gmail.com
 */
public class ModuleEventInboxComposite implements ModuleEventInbox {

    private final List<ModuleEventInbox> inboxes;

    public ModuleEventInboxComposite(List<ModuleEventInbox> inboxes) {
        this.inboxes = new CopyOnWriteArrayList<>(Objects.requireNonNullElse(inboxes, Collections.emptyList()));
    }

    @Override
    public boolean supports(ModuleEvent moduleEvent) {
        return !inboxes.isEmpty();
    }

    @Override
    public InboxIdentifier process(ModuleEvent event, Runnable eventProcessor) {
        var candidates = inboxes.stream().filter(outbox -> outbox.supports(event)).toList();
        if (candidates.size() != 1) {
            throw new IllegalStateException("no inbox or multiple inbox");
        }
        return candidates.getFirst().process(event, eventProcessor);
    }

    public void addInbox(ModuleEventInbox inbox) {
        if (!inboxes.contains(Objects.requireNonNull(inbox, "ModuleEventInbox must not be null"))) {
            inboxes.add(inbox);
        }
    }
}

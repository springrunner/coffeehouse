package coffeehouse.libraries.modulemesh.event.outbox.support;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;
import coffeehouse.libraries.modulemesh.event.outbox.ModuleEventOutbox;
import coffeehouse.libraries.modulemesh.event.outbox.OutboxIdentifier;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author springrunner.kr@gmail.com
 */
public class ModuleEventOutboxComposite implements ModuleEventOutbox {

    private final List<ModuleEventOutbox> outboxes;

    public ModuleEventOutboxComposite(List<ModuleEventOutbox> outboxes) {
        this.outboxes = new CopyOnWriteArrayList<>(Objects.requireNonNullElse(outboxes, Collections.emptyList()));
    }

    @Override
    public boolean supports(ModuleEvent moduleEvent) {
        return !outboxes.isEmpty();
    }

    @Override
    public OutboxIdentifier put(ModuleEvent event) {
        var candidates = outboxes.stream().filter(outbox -> outbox.supports(event)).toList();
        if (candidates.size() != 1) {
            throw new IllegalStateException("no outbox or multiple outbox");
        }
        return candidates.getFirst().put(event);
    }

    void addOutbox(ModuleEventOutbox outbox) {
        if (!outboxes.contains(Objects.requireNonNull(outbox, "ModuleEventOutbox must not be null"))) {
            outboxes.add(outbox);
        }
    }
}

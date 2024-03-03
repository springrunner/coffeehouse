package coffeehouse.libraries.modulemesh.event.outbox;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;

import java.util.Date;

/**
 * @author springrunner.kr@gmail.com
 */
public interface ModuleEventOutboxAccessor {

    void remove(OutboxIdentifier identifier);

    Iterable<OutboxRecord> poll(Date date);

    record OutboxRecord(OutboxIdentifier identifier, ModuleEvent event) {
    }
}

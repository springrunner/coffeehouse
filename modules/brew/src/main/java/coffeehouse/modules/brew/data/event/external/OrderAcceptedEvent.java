package coffeehouse.modules.brew.data.event.external;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;
import coffeehouse.modules.order.domain.OrderId;

import java.util.Date;
import java.util.UUID;

/**
 * @author springrunner.kr@gmail.com
 */
public record OrderAcceptedEvent(OrderId orderId, UUID eventId, Date eventOccurrenceTime) implements ModuleEvent {

    @Override
    public EventSource eventSource() {
        return EventSource.ofModule("order");
    }
}

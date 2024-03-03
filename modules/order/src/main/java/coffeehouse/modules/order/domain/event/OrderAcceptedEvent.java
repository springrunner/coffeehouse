package coffeehouse.modules.order.domain.event;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;
import coffeehouse.modules.order.domain.OrderId;

import java.util.Date;
import java.util.UUID;

/**
 * @author springrunner.kr@gmail.com
 */
public record OrderAcceptedEvent(OrderId orderId, UUID eventId, Date eventOccurrenceTime) implements ModuleEvent {

    public static OrderAcceptedEvent of(OrderId orderId) {
        return new OrderAcceptedEvent(orderId, UUID.randomUUID(), new Date());
    }
}

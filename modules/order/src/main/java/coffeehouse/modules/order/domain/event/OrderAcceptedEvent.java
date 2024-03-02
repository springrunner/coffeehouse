package coffeehouse.modules.order.domain.event;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;
import coffeehouse.modules.order.domain.OrderId;

/**
 * @author springrunner.kr@gmail.com
 */
public record OrderAcceptedEvent(OrderId orderId) implements ModuleEvent {
}

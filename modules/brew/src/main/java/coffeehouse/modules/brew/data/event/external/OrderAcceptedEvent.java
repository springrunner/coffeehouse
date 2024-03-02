package coffeehouse.modules.brew.data.event.external;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;
import coffeehouse.modules.order.domain.OrderId;

/**
 * @author springrunner.kr@gmail.com
 */
public record OrderAcceptedEvent(OrderId orderId) implements ModuleEvent {
}

package coffeehouse.modules.brew.domain.message;

import coffeehouse.modules.order.domain.OrderId;

/**
 * @author springrunner.kr@gmail.com
 */
public record BrewCompletedEventMessage(OrderId orderId) {
}

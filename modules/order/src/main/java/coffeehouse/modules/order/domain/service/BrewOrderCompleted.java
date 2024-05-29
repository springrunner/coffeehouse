package coffeehouse.modules.order.domain.service;

import coffeehouse.modules.order.domain.OrderId;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface BrewOrderCompleted {

    void changeOrderStatus(OrderId orderId);
}

package coffeehouse.modules.order.domain.service;

import coffeehouse.modules.order.domain.OrderId;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface BarCounter {

    /**
     * Request to brew a drink.
     *
     * @param orderId The ID of the order
     */
    void brew(OrderId orderId);
}

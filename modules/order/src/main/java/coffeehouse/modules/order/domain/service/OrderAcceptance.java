package coffeehouse.modules.order.domain.service;

import coffeehouse.modules.order.domain.OrderId;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface OrderAcceptance {

    /**
     * Accepts a received order.
     *
     * @param orderId The ID of the order to accept
     */
    void acceptOrder(OrderId orderId);
}

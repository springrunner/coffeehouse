package coffeehouse.modules.order.domain.service;

import coffeehouse.modules.order.domain.OrderId;

@FunctionalInterface
public interface OrderCompletion {

    /**
     * Completes an order.
     *
     * @param orderId The ID of the order to complete
     */
    void completeOrder(OrderId orderId);
}

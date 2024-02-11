package coffeehouse.modules.order.domain.service;

import coffeehouse.modules.order.domain.OrderId;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface OrderCancellation {

    /**
     * Cancels an order.
     *
     * @param orderId            The ID of the order to cancel
     * @param cancellationReason The reason for the order's cancellation
     */
    void cancelOrder(OrderId orderId, String cancellationReason);
}

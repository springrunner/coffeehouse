package coffeehouse.modules.brew.domain.service;

import coffeehouse.modules.order.domain.OrderId;

/**
 * @author springrunner.kr@gmail.com
 */
public interface Notification {

    void notifyUser(OrderId orderId);

    void notifyOrder(OrderId orderId);
}

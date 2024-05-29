package coffeehouse.modules.order.domain.service;

import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.entity.Order;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface OrderSearcher {
    Order findById(OrderId orderId);
}

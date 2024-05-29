package coffeehouse.modules.order.domain.entity;

import coffeehouse.modules.order.domain.OrderId;

import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public interface OrderRepository {
    Optional<Order> findById(OrderId id);
    Order save(Order order);
}

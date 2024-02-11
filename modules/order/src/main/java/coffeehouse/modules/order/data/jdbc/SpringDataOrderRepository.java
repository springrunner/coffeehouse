package coffeehouse.modules.order.data.jdbc;

import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.entity.Order;
import coffeehouse.modules.order.domain.entity.OrderRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * @author springrunner.kr@gmail.com
 */
public interface SpringDataOrderRepository extends OrderRepository, CrudRepository<Order, OrderId> {
}

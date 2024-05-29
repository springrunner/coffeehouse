package coffeehouse.modules.brew.domain.entity;

import coffeehouse.modules.brew.domain.OrderId;

import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public interface OrderSheetRepository {
    Optional<OrderSheet> findByOrderId(OrderId orderId);
    OrderSheet save(OrderSheet orderSheet);
}

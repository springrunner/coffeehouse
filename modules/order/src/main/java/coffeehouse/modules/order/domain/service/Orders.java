package coffeehouse.modules.order.domain.service;

import coffeehouse.modules.catalog.domain.ProductId;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.OrderItemId;
import coffeehouse.modules.order.domain.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public interface Orders {

    /**
     * Retrieves order information.
     *
     * @param orderId The ID of the order
     * @return An Optional containing the order information object, if found
     */
    Optional<OrderDetails> getOrderDetails(OrderId orderId);

    record OrderDetails(
            OrderId orderId,
            List<OrderLine> orderLines,
            OrderStatus status,
            LocalDateTime placedAt,
            LocalDateTime acceptedAt,
            LocalDateTime processedAt,
            LocalDateTime canceledAt) {
    }

    record OrderLine(OrderItemId orderItemId, ProductId orderProductId, int orderQuantity) {
    }
}

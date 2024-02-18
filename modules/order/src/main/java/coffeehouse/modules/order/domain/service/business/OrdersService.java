package coffeehouse.modules.order.domain.service.business;

import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.entity.OrderRepository;
import coffeehouse.modules.order.domain.service.Orders;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
class OrdersService implements Orders {

    private final OrderRepository orderRepository;

    public OrdersService(OrderRepository orderRepository) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository must not be null");
    }

    @Override
    public Optional<OrderDetails> getOrderDetails(OrderId orderId) {
        return orderRepository.findById(orderId).map(it -> new OrderDetails(
                it.getId(),
                StreamSupport
                        .stream(it.getOrderItems().spliterator(), false)
                        .map(item -> new OrderLine(item.id(), item.productId(), item.quantity())).toList(),
                it.getStatus(),
                it.getPlacedAt(),
                it.getAcceptedAt().orElse(null),
                it.getProcessedAt().orElse(null),
                it.getCanceledAt().orElse(null)
        ));
    }
}

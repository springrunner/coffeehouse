package coffeehouse.modules.order.domain.service.business;

import coffeehouse.modules.order.domain.OrderException;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.entity.Order;
import coffeehouse.modules.order.domain.entity.OrderRepository;
import coffeehouse.modules.order.domain.service.OrderCancellation;
import coffeehouse.modules.order.domain.service.UnidentifiedOrderException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
class OrderCancellationProcessor implements OrderCancellation {

    private final OrderRepository orderRepository;

    public OrderCancellationProcessor(OrderRepository orderRepository) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository must not be null");
    }

    @Override
    public void cancelOrder(OrderId orderId, String cancellationReason) {
        var canceledOrder = orderRepository.findById(orderId)
                .map(Order::cancel)
                .orElseThrow(() -> UnidentifiedOrderException.ofOrderId(orderId));

        orderRepository.save(canceledOrder);
        
        // Cancel an order to BarCounter
        throw new OrderException("NotImplemented: failed to cancel order");
    }
}

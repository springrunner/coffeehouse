package coffeehouse.modules.order.domain.service.business;

import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.entity.Order;
import coffeehouse.modules.order.domain.entity.OrderRepository;
import coffeehouse.modules.order.domain.service.OrderCompletion;
import coffeehouse.modules.order.domain.service.UnidentifiedOrderException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
class OrderCompletionProcessor implements OrderCompletion {

    private final OrderRepository orderRepository;

    public OrderCompletionProcessor(OrderRepository orderRepository) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository must not be null");
    }
    
    @Override
    public void completeOrder(OrderId orderId) {
        var completedOrder = orderRepository.findById(orderId)
                .map(Order::complete)
                .orElseThrow(() -> UnidentifiedOrderException.ofOrderId(orderId));

        orderRepository.save(completedOrder);
    }
}

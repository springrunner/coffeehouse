package coffeehouse.modules.order.domain.service.business;

import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.entity.Order;
import coffeehouse.modules.order.domain.entity.OrderRepository;
import coffeehouse.modules.order.domain.service.BarCounter;
import coffeehouse.modules.order.domain.service.OrderAcceptance;
import coffeehouse.modules.order.domain.service.UnidentifiedOrderException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
class OrderAcceptanceProcessor implements OrderAcceptance {

    private final BarCounter barCounter;
    private final OrderRepository orderRepository;

    public OrderAcceptanceProcessor(BarCounter barCounter, OrderRepository orderRepository) {
        this.barCounter = Objects.requireNonNull(barCounter, "BarCounter must not be null");
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository must not be null");
    }

    @Override
    public void acceptOrder(OrderId orderId) {
        var acceptedOrder = orderRepository.findById(orderId)
                .map(Order::accept)
                .orElseThrow(() -> UnidentifiedOrderException.ofOrderId(orderId));

        orderRepository.save(acceptedOrder);

        barCounter.brew(orderId);
    }
}

package coffeehouse.modules.order.domain.service.business;

import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.entity.OrderNotFoundException;
import coffeehouse.modules.order.domain.entity.OrderRepository;
import coffeehouse.modules.order.domain.service.BarCounter;
import coffeehouse.modules.order.domain.service.OrderAcceptance;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
class OrderAcceptanceProcessor implements OrderAcceptance {

    private final OrderRepository orderRepository;
    private final BarCounter barCounter;
    
    public OrderAcceptanceProcessor(OrderRepository orderRepository, BarCounter barCounter) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository must not be null");
        this.barCounter = Objects.requireNonNull(barCounter, "BarCounter must not be null");
    }

    @Override
    public void acceptOrder(OrderId orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        order.accept();
        orderRepository.save(order);
        barCounter.brew(order.getId());
    }
}

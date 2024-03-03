package coffeehouse.modules.order.domain.service.business;

import coffeehouse.libraries.modulemesh.event.ModuleEventPublisher;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.entity.Order;
import coffeehouse.modules.order.domain.entity.OrderRepository;
import coffeehouse.modules.order.domain.event.OrderAcceptedEvent;
import coffeehouse.modules.order.domain.service.OrderAcceptance;
import coffeehouse.modules.order.domain.service.UnidentifiedOrderException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
class OrderAcceptanceProcessor implements OrderAcceptance {

    private final OrderRepository orderRepository;
    private final ModuleEventPublisher moduleEventPublisher;

    public OrderAcceptanceProcessor(OrderRepository orderRepository, ModuleEventPublisher moduleEventPublisher) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository must not be null");
        this.moduleEventPublisher = Objects.requireNonNull(moduleEventPublisher, "ModuleEventPublisher must not be null");
    }

    @Override
    public void acceptOrder(OrderId orderId) {
        var acceptedOrder = orderRepository.findById(orderId)
                .map(Order::accept)
                .orElseThrow(() -> UnidentifiedOrderException.ofOrderId(orderId));

        orderRepository.save(acceptedOrder);

        moduleEventPublisher.publishEvent(OrderAcceptedEvent.of(orderId));
    }
}

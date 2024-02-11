package coffeehouse.modules.order.domain.service.business;

import coffeehouse.modules.order.domain.OrderException;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.entity.Order;
import coffeehouse.modules.order.domain.entity.OrderRepository;
import coffeehouse.modules.order.domain.service.OrderAcceptance;
import coffeehouse.modules.order.domain.service.UnidentifiedOrderException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
public class OrderAcceptanceProcessor implements OrderAcceptance {

    private final OrderRepository orderRepository;

    public OrderAcceptanceProcessor(OrderRepository orderRepository) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository must not be null");
    }

    @Override
    public void acceptOrder(OrderId orderId) {
        var acceptedOrder = orderRepository.findById(orderId)
                .map(Order::accept)
                .orElseThrow(() -> UnidentifiedOrderException.ofOrderId(orderId));

        orderRepository.save(acceptedOrder);

        // Fulfillment request to BarCounter
        throw new OrderException("NotImplemented: failed to accept order");
    }
}

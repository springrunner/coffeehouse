package coffeehouse.modules.order.domain.service.business;

import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.entity.Order;
import coffeehouse.modules.order.domain.entity.OrderItems;
import coffeehouse.modules.order.domain.entity.OrderRepository;
import coffeehouse.modules.order.domain.service.OrderPlacement;
import coffeehouse.modules.order.domain.service.UnidentifiedCustomerException;
import coffeehouse.modules.user.domain.UserAccountId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
class OrderPlacementProcessor implements OrderPlacement {

    private final OrdererVerification ordererVerification;
    private final OrderItemFactory orderItemFactory;
    private final OrderRepository orderRepository;

    public OrderPlacementProcessor(OrdererVerification ordererVerification, OrderItemFactory orderItemFactory, OrderRepository orderRepository) {
        this.ordererVerification = Objects.requireNonNull(ordererVerification, "OrdererVerification must not be null");
        this.orderItemFactory = Objects.requireNonNull(orderItemFactory, "OrderItemFactory must not be null");
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository must not be null");
    }

    @Override
    public OrderId placeOrder(UserAccountId customerId, List<OrderLine> orderLines) {
        var orderer = ordererVerification.verify(customerId)
                .orElseThrow(() -> UnidentifiedCustomerException.ofCustomerId(customerId));
        var orderId = new OrderId(UUID.randomUUID().toString());
        var orderItems = orderLines.stream()
                .map(orderDetail -> orderItemFactory.create(orderId, orderDetail.productCode(), orderDetail.quantity()))
                .collect(Collectors.toSet());

        orderRepository.save(Order.place(orderId, orderer.id(), OrderItems.wrap(orderItems)));

        return orderId;
    }
}

package coffeehouse.modules.order.domain.service.business;

import coffeehouse.modules.catalog.domain.service.Catalogs;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.OrderItemId;
import coffeehouse.modules.order.domain.entity.Order;
import coffeehouse.modules.order.domain.entity.OrderItem;
import coffeehouse.modules.order.domain.entity.OrderItems;
import coffeehouse.modules.order.domain.entity.OrderRepository;
import coffeehouse.modules.order.domain.service.OrderPlacement;
import coffeehouse.modules.order.domain.service.UnidentifiedCustomerException;
import coffeehouse.modules.order.domain.service.UnidentifiedProductException;
import coffeehouse.modules.user.domain.UserAccountId;
import coffeehouse.modules.user.domain.service.Customers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
public class OrderPlacementProcessor implements OrderPlacement {

    private final Customers customers;
    private final Catalogs catalogs;
    private final OrderRepository orderRepository;

    public OrderPlacementProcessor(Customers customers, Catalogs catalogs, OrderRepository orderRepository) {
        this.customers = Objects.requireNonNull(customers, "Customers must not be null");
        this.catalogs = Objects.requireNonNull(catalogs, "Catalogs must not be null");
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository must not be null");
    }

    @Override
    public OrderId placeOrder(UserAccountId customerId, List<OrderLine> orderLines) {
        var customer = customers.getCustomerDetails(customerId).orElseThrow(
                () -> UnidentifiedCustomerException.ofCustomerId(customerId)
        );
        var orderId = new OrderId(UUID.randomUUID().toString());
        var orderItems = orderLines.stream().map(orderLine -> {
            var orderItemId = new OrderItemId(UUID.randomUUID().toString());
            var product = catalogs.getProductDetails(orderLine.productCode()).orElseThrow(
                    () -> UnidentifiedProductException.ofProductCode(orderLine.productCode())
            );
            
            return OrderItem.create(orderItemId, orderId, product.id(), product.price(), orderLine.quantity());
        }).collect(Collectors.toSet());

        orderRepository.save(Order.place(orderId, customer.id(), OrderItems.wrap(orderItems)));

        return orderId;
    }
}

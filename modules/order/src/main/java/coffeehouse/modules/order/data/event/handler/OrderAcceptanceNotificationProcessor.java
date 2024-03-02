package coffeehouse.modules.order.data.event.handler;

import coffeehouse.libraries.modulemesh.function.ModuleFunctionOperations;
import coffeehouse.modules.order.domain.entity.OrderRepository;
import coffeehouse.modules.order.domain.event.OrderAcceptedEvent;
import coffeehouse.modules.order.domain.service.UnidentifiedCustomerException;
import coffeehouse.modules.order.domain.service.UnidentifiedOrderException;
import coffeehouse.modules.user.domain.UserAccountId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Component
class OrderAcceptanceNotificationProcessor {

    private final OrderRepository orderRepository;
    private final ModuleFunctionOperations moduleFunctionOperations;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public OrderAcceptanceNotificationProcessor(OrderRepository orderRepository, ModuleFunctionOperations moduleFunctionOperations) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository must not be null");
        this.moduleFunctionOperations = Objects.requireNonNull(moduleFunctionOperations, "ModuleFunctionOperations must not be null");
    }

    @EventListener
    void on(OrderAcceptedEvent event) {
        var order = orderRepository.findById(event.orderId())
                .orElseThrow(() -> new UnidentifiedOrderException("Not found order: " + event.orderId()));
        var customerDetails = moduleFunctionOperations.execute(
                "customers/get-customer-details", 
                order.getOrdererId(), 
                CustomerDetails.class
        );
        if (customerDetails == null) {
            throw new UnidentifiedCustomerException("Unidentified customer(id: %s)".formatted(order.getOrdererId()));
        }

        // notify processing

        logger.info("Notify customers({}) that order accepted: {}", customerDetails.username(), order.getId());
    }

    record CustomerDetails(UserAccountId id, String username) {
    }
}

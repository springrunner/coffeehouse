package coffeehouse.tests.integration.order;

import coffeehouse.modules.brew.domain.entity.OrderSheetRepository;
import coffeehouse.modules.catalog.domain.ProductCode;
import coffeehouse.modules.order.domain.entity.Order;
import coffeehouse.modules.order.domain.entity.OrderRepository;
import coffeehouse.modules.order.domain.entity.OrderStatus;
import coffeehouse.modules.order.domain.service.OrderAcceptance;
import coffeehouse.modules.order.domain.service.OrderPlacement;
import coffeehouse.modules.user.domain.UserAccountId;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author springrunner.kr@gmail.com
 */
@SpringBootTest
class OrderAcceptanceTests {

    @Autowired OrderPlacement orderPlacement;
    @Autowired OrderAcceptance orderAcceptance;
    @Autowired OrderRepository orderRepository;
    @Autowired OrderSheetRepository orderSheetRepository;

    @Test
    void shouldChangeOrderStatusToAcceptedWhenOrderIsSuccessfullyAccepted() {
        var customerId = new UserAccountId("springrunner");
        var orderLines = List.of(new OrderPlacement.OrderLine(new ProductCode("americano"), 1));
        var orderId = orderPlacement.placeOrder(customerId, orderLines);

        // Ensure the order is initially in the 'PLACED' state and no OrderSheet exists for it yet
        assertEquals(OrderStatus.PLACED, orderRepository.findById(orderId).map(Order::getStatus).orElse(null));
        assertNull(orderSheetRepository.findByOrderId(orderId).orElse(null), "OrderSheet should not be null");

        orderAcceptance.acceptOrder(orderId);

        var order = orderRepository.findById(orderId).orElse(null);
        assertNotNull(order, "Order should not be null");
        assertEquals(OrderStatus.ACCEPTED, order.getStatus(), "Order status is not 'ACCEPTED'");

        // Check that an OrderSheet has been created when the order is accepted
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            var orderSheet = orderSheetRepository.findByOrderId(orderId).orElse(null);
            assertNotNull(orderSheet, "OrderSheet should not be null");
        });
    }
}

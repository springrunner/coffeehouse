package coffeehouse.tests.integration.order;

import coffeehouse.modules.catalog.domain.ProductCode;
import coffeehouse.modules.order.domain.entity.OrderStatus;
import coffeehouse.modules.order.domain.service.OrderPlacement;
import coffeehouse.modules.order.domain.service.Orders;
import coffeehouse.modules.order.domain.service.UnidentifiedCustomerException;
import coffeehouse.modules.order.domain.service.UnidentifiedProductException;
import coffeehouse.modules.user.domain.UserAccountId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author springrunner.kr@gmail.com
 */
@SpringBootTest
class OrderPlacementTests {

    @Autowired OrderPlacement orderPlacement;
    @Autowired Orders orders;

    @Test
    void shouldThrowUnidentifiedCustomerExceptionForNonExistentCustomer() {
        var nonExistentCustomerId = new UserAccountId("nonExistentCustomer");
        var orderLines = List.of(new OrderPlacement.OrderLine(new ProductCode("americano"), 1));

        assertThrows(
                UnidentifiedCustomerException.class,
                () -> orderPlacement.placeOrder(nonExistentCustomerId, orderLines),
                "Expected UnidentifiedCustomerException to be thrown, but it was not"
        );
    }

    @Test
    void shouldThrowUnidentifiedProductExceptionForNonExistentCustomer() {
        var customerId = new UserAccountId("springrunner");
        var orderLines = List.of(new OrderPlacement.OrderLine(new ProductCode("nonExistentProduct"), 1));

        assertThrows(
                UnidentifiedProductException.class,
                () -> orderPlacement.placeOrder(customerId, orderLines),
                "Expected UnidentifiedCustomerException to be thrown, but it was not"
        );
    }

    @Test
    void shouldPlaceOrderSuccessfully() {
        var customerId = new UserAccountId("springrunner");
        var orderLines = List.of(new OrderPlacement.OrderLine(new ProductCode("americano"), 1));

        var orderId = orderPlacement.placeOrder(customerId, orderLines);
        assertNotNull(orderId, "OrderId should not be null");

        var details = orders.getOrderDetails(orderId).orElse(null);
        assertNotNull(details, "OrderDetails should not be null");
        assertEquals(1, details.orderLines().size(), "The number of ordered products should be correct");
        assertEquals(OrderStatus.PLACED, details.status(), "Order status is not 'PLACED'");
    }
}

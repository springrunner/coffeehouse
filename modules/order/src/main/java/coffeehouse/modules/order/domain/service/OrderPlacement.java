package coffeehouse.modules.order.domain.service;

import coffeehouse.modules.catalog.domain.ProductCode;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.user.domain.UserAccountId;

import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface OrderPlacement {

    /**
     * Receives an order from a customer.
     *
     * @param customerId The ID of the customer placing the order
     * @param orderLines The details of the order being placed
     * @return The ID of the placed order
     */
    OrderId placeOrder(UserAccountId customerId, List<OrderLine> orderLines);

    record OrderLine(ProductCode productCode, int quantity) {
    }
}

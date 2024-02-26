package coffeehouse.modules.brew.domain.service;

import coffeehouse.modules.catalog.domain.ProductId;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.OrderItemId;

import java.util.Set;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface OrderSheetSubmission {

    /**
     * Submits an order sheet.
     *
     * @param orderId The ID of the order to submit
     * @param orderLines The set of order lines to submit, each representing an item in the order.
     */
    void submitOrderSheet(OrderId orderId, Set<OrderLine> orderLines);
    
    record OrderLine(OrderItemId orderItemId, ProductId orderProductId, int orderQuantity) {
    }
}

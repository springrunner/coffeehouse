package coffeehouse.modules.brew.domain.service;

import coffeehouse.modules.brew.domain.OrderSheetId;
import coffeehouse.modules.brew.domain.OrderSheetItemId;
import coffeehouse.modules.brew.domain.entity.OrderSheetStatus;
import coffeehouse.modules.catalog.domain.ProductId;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.OrderItemId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public interface OrderSheets {

    /**
     * Retrieves a list of order sheets that are pending review.
     *
     * @return A list of order sheets that are pending review.
     */
    List<OrderSheetDetails> fetchOrderSheetsForReview();
    
    /**
     * Retrieves order-sheet information.
     *
     * @param orderSheetId The ID of the order-sheet
     * @return An Optional containing the order-sheet information object, if found
     */
    Optional<OrderSheetDetails> getOrderSheetDetails(OrderSheetId orderSheetId);

    record OrderSheetDetails(
            OrderSheetId orderSheetId,
            OrderId orderId,
            List<OrderSheetLine> orderSheetLines,
            OrderSheetStatus status,
            LocalDateTime submittedAt,
            LocalDateTime confirmedAt,
            LocalDateTime processedAt,
            LocalDateTime refusedAt) {
    }

    record OrderSheetLine(OrderSheetItemId orderSheetItemId, OrderItemId orderItemId, ProductId orderProductId, int orderQuantity) {
    }
}

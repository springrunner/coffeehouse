package coffeehouse.modules.brew.domain.entity;

import coffeehouse.modules.brew.domain.OrderSheetId;
import coffeehouse.modules.brew.domain.OrderSheetItemId;
import coffeehouse.modules.catalog.domain.ProductId;
import coffeehouse.modules.order.domain.OrderItemId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public record OrderSheetItem(
        @Id
        OrderSheetItemId id,
        OrderSheetId orderSheetId,
        OrderItemId orderItemId,
        ProductId orderProductId,
        int orderQuantity,
        OrderSheetItemStatus status,
        LocalDateTime confirmedAt,
        LocalDateTime pickupRequestedAt,
        LocalDateTime pickupCompletedAt,
        LocalDateTime refusedAt,
        @Version
        Long version
) {

    public OrderSheetItem confirm() {
        return new OrderSheetItem(
                id,
                orderSheetId,
                orderItemId,
                orderProductId,
                orderQuantity,
                OrderSheetItemStatus.CONFIRMED,
                LocalDateTime.now(),
                pickupRequestedAt,
                pickupCompletedAt,
                refusedAt,
                version
        );
    }

    public OrderSheetItem refuse() {
        return new OrderSheetItem(
                id,
                orderSheetId,
                orderItemId,
                orderProductId,
                orderQuantity,
                OrderSheetItemStatus.CONFIRMED,
                confirmedAt,
                pickupRequestedAt,
                pickupCompletedAt,
                refusedAt,
                version
        );
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (!(target instanceof OrderSheetItem that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderSheetItem(id=%s, orderSheetId=%s, orderProductId=%s, status=%s)".formatted(id, orderSheetId, orderProductId, status);
    }

    public static OrderSheetItem submit(OrderSheetItemId id, OrderSheetId orderSheetId, OrderItemId orderItemId, ProductId productId, int quantity) {
        return new OrderSheetItem(
                id,
                orderSheetId,
                orderItemId,
                productId,
                quantity,
                OrderSheetItemStatus.SUBMITTED,
                null,
                null,
                null,
                null,
                null
        );
    }
}

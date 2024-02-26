package coffeehouse.modules.brew.domain.entity;

import coffeehouse.modules.brew.domain.OrderSheetId;
import coffeehouse.modules.brew.domain.OrderSheetItemId;
import coffeehouse.modules.order.domain.OrderId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Embedded;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author springrunner.kr@gmail.com
 */
public class OrderSheet {

    @Id
    private final OrderSheetId id;
    private final OrderId orderId;
    @Embedded.Empty
    private final OrderSheetItems orderSheetItems;
    private final OrderSheetStatus status;
    private final LocalDateTime submittedAt;
    private final LocalDateTime confirmedAt;
    private final LocalDateTime processedAt;
    private final LocalDateTime refusedAt;

    @Version
    private final Long version;

    OrderSheet(OrderSheetId id, OrderId orderId, OrderSheetItems orderSheetItems, OrderSheetStatus status, LocalDateTime submittedAt, LocalDateTime confirmedAt, LocalDateTime processedAt, LocalDateTime refusedAt, Long version) {
        this.id = Objects.requireNonNull(id, "OrderSheetId must not be null");
        this.orderId = Objects.requireNonNull(orderId, "OrderId must not be null");
        this.orderSheetItems = Objects.requireNonNull(orderSheetItems, "OrderSheetItems must not be null");
        this.status = Objects.requireNonNull(status, "OrderSheetStatus must not be null");
        this.submittedAt = Objects.requireNonNull(submittedAt, "SubmittedAt must not be null");
        this.confirmedAt = confirmedAt;
        this.processedAt = processedAt;
        this.refusedAt = refusedAt;
        this.version = version;
    }

    public OrderSheetId getId() {
        return id;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public OrderSheetItems getOrderSheetItems() {
        return orderSheetItems;
    }

    public OrderSheetStatus getStatus() {
        return status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public Optional<LocalDateTime> getConfirmedAt() {
        return Optional.ofNullable(confirmedAt);
    }

    public Optional<LocalDateTime> getProcessedAt() {
        return Optional.ofNullable(processedAt);
    }

    public Optional<LocalDateTime> getRefusedAt() {
        return Optional.ofNullable(refusedAt);
    }

    public OrderSheet confirm(Set<OrderSheetItemId> itemIds) {
        var revisedOrderSheetItems = orderSheetItems.confirm(itemIds);
        
        return new OrderSheet(
                id, 
                orderId,
                revisedOrderSheetItems,
                revisedOrderSheetItems.resolveOrderSheetStatus(), 
                submittedAt, 
                LocalDateTime.now(), 
                processedAt, 
                refusedAt, 
                version
        );
    }

    public OrderSheet refuse(Set<OrderSheetItemId> itemIds) {
        var revisedOrderSheetItems = orderSheetItems.refuse(itemIds);
        
        return new OrderSheet(
                id,
                orderId,
                revisedOrderSheetItems,
                revisedOrderSheetItems.resolveOrderSheetStatus(),
                submittedAt,
                confirmedAt,
                processedAt,
                LocalDateTime.now(),
                version
        );
    }
    
    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (!(target instanceof OrderSheet that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderSheet(id=%s, orderId=%s, status=%s)".formatted(id, orderId, status);
    }
    
    public static OrderSheet submit(OrderSheetId orderSheetId, OrderId orderId, OrderSheetItems orderSheetItems) {
        var orderSheetStatus = orderSheetItems.resolveOrderSheetStatus();
        if (orderSheetStatus != OrderSheetStatus.SUBMITTED) {
            throw new OrderSheetCreationException("Submission failed: Not all items are submitted.");
        }
        
        return new OrderSheet(
                orderSheetId,
                orderId,
                orderSheetItems,
                orderSheetStatus,
                LocalDateTime.now(),
                null,
                null,
                null,
                null
        );
    }
}

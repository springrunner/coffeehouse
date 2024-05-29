package coffeehouse.modules.brew.domain.entity;

import coffeehouse.modules.brew.domain.OrderId;
import coffeehouse.modules.brew.domain.OrderSheetId;

/**
 * @author springrunner.kr@gmail.com
 */
public class OrderSheet {
    private final OrderSheetId orderSheetId;
    private final OrderId orderId;
    private OrderSheetStatus orderSheetStatus;

    public OrderId getOrderId() {
        return orderId;
    }

    public OrderSheetStatus getOrderSheetStatus() {
        return orderSheetStatus;
    }

    public OrderSheet(OrderSheetId orderSheetId, OrderId orderId, OrderSheetStatus orderSheetStatus) {
        this.orderSheetId = orderSheetId;
        this.orderId = orderId;
        this.orderSheetStatus = orderSheetStatus;
    }

    public OrderSheet confirm() {
        this.orderSheetStatus = OrderSheetStatus.CONFIRMED;
        return this;
    }

    public OrderSheet process() {
        this.orderSheetStatus = OrderSheetStatus.PROCESSED;
        return this;
    }

    public static OrderSheet create(OrderSheetId orderSheetId, OrderId orderId) {
        return new OrderSheet(orderSheetId, orderId, OrderSheetStatus.SUBMITTED);
    }
}

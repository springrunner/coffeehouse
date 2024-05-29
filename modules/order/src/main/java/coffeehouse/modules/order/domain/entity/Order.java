package coffeehouse.modules.order.domain.entity;

import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.UserAccountId;

/**
 * @author springrunner.kr@gmail.com
 */
public class Order {
    private final OrderId id;
    private final UserAccountId ordererId;
    private OrderStatus orderStatus;

    public OrderId getId() {
        return id;
    }

    public UserAccountId getOrdererId() {
        return ordererId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Order(OrderId id, UserAccountId ordererId, OrderStatus orderStatus) {
        this.id = id;
        this.ordererId = ordererId;
        this.orderStatus = orderStatus;
    }

    public Order accept() {
        this.orderStatus = OrderStatus.ACCEPTED;
        return this;
    }

    public Order complete() {
        this.orderStatus = OrderStatus.COMPLETED;
        return this;
    }

    public static Order create(OrderId id, UserAccountId ordererId) {
        return new Order(id, ordererId, OrderStatus.PLACED);
    }
}

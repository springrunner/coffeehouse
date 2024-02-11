package coffeehouse.modules.order.domain.entity;

import coffeehouse.libraries.base.lang.Money;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.user.domain.UserAccountId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Embedded;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.NumberValue;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public class Order {

    @Id
    private final OrderId id;
    private final UserAccountId ordererId;
    @Embedded.Empty
    private final OrderItems orderItems;
    @Embedded.Empty(prefix = "TOTAL_PRICE_")
    private final OrderTotalPrice totalPrice;
    private final OrderStatus status;
    private final LocalDateTime placedAt;
    private final LocalDateTime acceptedAt;
    private final LocalDateTime completedAt;
    private final LocalDateTime canceledAt;
    
    @Version
    private final Long version;

    Order(OrderId id, UserAccountId ordererId, OrderItems orderItems, OrderTotalPrice totalPrice, OrderStatus status, LocalDateTime placedAt, LocalDateTime acceptedAt, LocalDateTime completedAt, LocalDateTime canceledAt, Long version) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.ordererId = Objects.requireNonNull(ordererId, "ordererId must not be null");
        this.orderItems = Objects.requireNonNull(orderItems, "orderItems must not be null");
        this.totalPrice = Objects.requireNonNull(totalPrice, "totalPrice must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.placedAt = Objects.requireNonNull(placedAt, "placedAt must not be null");
        this.acceptedAt = acceptedAt;
        this.completedAt = completedAt;
        this.canceledAt = canceledAt;
        this.version = version;
    }

    public Order accept() {
        return new Order(id, ordererId, orderItems, totalPrice, OrderStatus.ACCEPTED, placedAt, LocalDateTime.now(), completedAt, canceledAt, version);
    }

    public Order complete() {
        return new Order(id, ordererId, orderItems, totalPrice, OrderStatus.COMPLETED, placedAt, LocalDateTime.now(), completedAt, canceledAt, version);
    }

    public Order cancel() {
        return new Order(id, ordererId, orderItems, totalPrice, OrderStatus.CANCELED, placedAt, LocalDateTime.now(), completedAt, canceledAt, version);
    }

    public OrderId getId() {
        return id;
    }

    public UserAccountId getOrdererId() {
        return ordererId;
    }

    public OrderItems getOrderItems() {
        return orderItems;
    }

    public MonetaryAmount getTotalPrice() {
        return totalPrice.toMonetaryAmount();
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getPlacedAt() {
        return placedAt;
    }

    public Optional<LocalDateTime> getAcceptedAt() {
        return Optional.ofNullable(acceptedAt);
    }

    public Optional<LocalDateTime> getProcessedAt() {
        return Optional.ofNullable(completedAt);
    }

    public Optional<LocalDateTime> getCanceledAt() {
        return Optional.ofNullable(canceledAt);
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (!(target instanceof Order that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order(id=%s, status=%s)".formatted(id, status);
    }

    record OrderTotalPrice(NumberValue number, CurrencyUnit currency) {

        MonetaryAmount toMonetaryAmount() {
            return Money.of(number, currency);
        }

        static OrderTotalPrice of(MonetaryAmount price) {
            return new OrderTotalPrice(price.getNumber(), price.getCurrency());
        }
    }

    public static Order place(OrderId id, UserAccountId ordererId, OrderItems orderItems) {
        return new Order(
                id,
                ordererId,
                orderItems,
                OrderTotalPrice.of(orderItems.calculateTotalPrice()),
                OrderStatus.PLACED,
                LocalDateTime.now(),
                null,
                null,
                null,
                null
        );
    }
}

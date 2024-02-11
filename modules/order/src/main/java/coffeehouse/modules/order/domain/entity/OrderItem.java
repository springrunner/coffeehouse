package coffeehouse.modules.order.domain.entity;

import coffeehouse.modules.catalog.domain.ProductId;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.OrderItemId;
import org.javamoney.moneta.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Embedded;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.NumberValue;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public record OrderItem(
        @Id
        OrderItemId id,
        OrderId orderId,
        ProductId productId,
        @Embedded.Empty(prefix = "PRODUCT_PRICE_")
        ProductItemPrice productPrice,
        int quantity,
        OrderItemStatus status,
        LocalDateTime confirmedAt,
        LocalDateTime deliveredAt,
        LocalDateTime refusedAt,
        LocalDateTime canceledAt,
        @Version
        Long version
) {

    public OrderItem {
        Objects.requireNonNull(id, "Id must not be null");
        Objects.requireNonNull(orderId, "OrderId must not be null");
        Objects.requireNonNull(productId, "ProductId must not be null");
        Objects.requireNonNull(productPrice, "ProductPrice must not be null");
        Objects.requireNonNull(status, "Status must not be null");
        
        if (quantity <= 0) {
            throw new IllegalArgumentException("Order quantity must be greater than 0");
        }
    }

    public MonetaryAmount calculatePrice() {
        return productPrice.toMonetaryAmount().multiply(quantity);
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (!(target instanceof OrderItem that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderItem(id=%s, orderId=%s, productId=%s, status=%s)".formatted(id, orderId, productId, status);
    }

    record ProductItemPrice(NumberValue number, CurrencyUnit currency) {

        public MonetaryAmount toMonetaryAmount() {
            return Money.of(number, currency);
        }

        public static ProductItemPrice of(MonetaryAmount price) {
            return new ProductItemPrice(price.getNumber(), price.getCurrency());
        }
    }

    public static OrderItem create(OrderItemId id, OrderId orderId, ProductId productId, MonetaryAmount productPrice, int quantity) {
        return new OrderItem(
                id,
                orderId,
                productId,
                ProductItemPrice.of(productPrice),
                quantity,
                OrderItemStatus.PENDING_PREPARE,
                null,
                null,
                null,
                null,
                null
        );
    }
}

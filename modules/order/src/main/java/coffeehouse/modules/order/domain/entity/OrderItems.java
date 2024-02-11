package coffeehouse.modules.order.domain.entity;

import coffeehouse.libraries.base.lang.IterableItem;
import coffeehouse.libraries.base.lang.Money;
import org.springframework.data.relational.core.mapping.MappedCollection;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.*;

/**
 * @author springrunner.kr@gmail.com
 */
public class OrderItems implements IterableItem<OrderItem> {

    @MappedCollection(idColumn = "ORDER_ID", keyColumn = "ID")
    private final Set<OrderItem> items;

    OrderItems(Set<OrderItem> items) {
        this.items = Objects.requireNonNull(items, "items must not be null");
    }

    public MonetaryAmount calculateTotalPrice() {
        return calculateTotalPrice(Monetary.getCurrency(Locale.getDefault()));
    }

    public MonetaryAmount calculateTotalPrice(CurrencyUnit currencyUnit) {
        return items.stream()
                .map(OrderItem::calculatePrice)
                .reduce(Money.of(0, currencyUnit), MonetaryAmount::add);
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public Iterator<OrderItem> iterator() {
        return items.iterator();
    }

    public static OrderItems wrap(Set<OrderItem> items) {
        return new OrderItems(Objects.requireNonNullElse(items, Collections.emptySet()));
    }
}

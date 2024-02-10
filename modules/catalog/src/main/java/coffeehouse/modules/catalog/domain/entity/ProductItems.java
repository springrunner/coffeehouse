package coffeehouse.modules.catalog.domain.entity;

import coffeehouse.libraries.base.lang.IterableItem;
import org.javamoney.moneta.Money;
import org.springframework.data.relational.core.mapping.MappedCollection;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.*;

/**
 * @author springrunner.kr@gmail.com
 */
public class ProductItems implements IterableItem<ProductItem> {

    @MappedCollection(idColumn = "PRODUCT_ID", keyColumn = "ID")
    private final List<ProductItem> items;

    ProductItems(List<ProductItem> items) {
        this.items = Objects.requireNonNull(items, "items must not be null");
    }

    public MonetaryAmount calculatePrice() {
        return calculatePrice(Monetary.getCurrency(Locale.getDefault()));
    }

    public MonetaryAmount calculatePrice(CurrencyUnit currencyUnit) {
        return items.stream().map(ProductItem::price).map(ProductItem.ProductItemPrice::toMonetaryAmount).reduce(Money.of(0, currencyUnit), MonetaryAmount::add);
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
    public Iterator<ProductItem> iterator() {
        return items.iterator();
    }

    public static ProductItems wrap(List<ProductItem> items) {
        return new ProductItems(Objects.requireNonNullElse(items, Collections.emptyList()));
    }
}

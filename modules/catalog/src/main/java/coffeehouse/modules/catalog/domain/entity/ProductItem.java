package coffeehouse.modules.catalog.domain.entity;

import coffeehouse.modules.catalog.domain.ProductItemId;
import coffeehouse.modules.catalog.domain.StockKeepingUnitId;
import org.javamoney.moneta.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Embedded;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.NumberValue;
import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public record ProductItem(
        @Id
        ProductItemId id,
        StockKeepingUnitId skuId,
        @Embedded.Empty(prefix = "PRICE_")
        ProductItemPrice price,
        boolean base,
        @Version
        Long version
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductItem that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ProductItem(id=%s, skuId=%s)".formatted(id, skuId);
    }

    public static ProductItem base(ProductItemId id, StockKeepingUnit sku, MonetaryAmount price) {
        return new ProductItem(id, sku.getId(), ProductItemPrice.of(price), true, null);
    }

    public static ProductItem additive(ProductItemId id, StockKeepingUnit sku, MonetaryAmount price) {
        return new ProductItem(id, sku.getId(), ProductItemPrice.of(price), false, null);
    }

    record ProductItemPrice(NumberValue number, CurrencyUnit currency) {

        public MonetaryAmount toMonetaryAmount() {
            System.out.println(Money.of(number, currency));
            return Money.of(number, currency);
        }

        public static ProductItemPrice of(MonetaryAmount price) {
            return new ProductItemPrice(price.getNumber(), price.getCurrency());
        }
    }
}

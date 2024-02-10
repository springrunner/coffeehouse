package coffeehouse.modules.catalog.domain.entity;

import coffeehouse.modules.catalog.domain.CatalogException;
import coffeehouse.modules.catalog.domain.StockKeepingUnitId;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public class StockKeepingUnit {

    private StockKeepingUnitId id;
    private String code;
    private String text;
    private long stockAmount;

    public StockKeepingUnit(StockKeepingUnitId id, String code, String text, long stockAmount) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.code = Objects.requireNonNull(code, "code must not be null");
        this.text = Objects.requireNonNull(text, "text must not be null");
        this.stockAmount = stockAmount;
    }

    public void verifyAvailability(long amount) {
        if (this.stockAmount < amount) {
            throw new InsufficientStockException("Insufficient inventory (SKU: %s)".formatted(id));
        }
    }

    public StockKeepingUnit refillBy(long amount) {
        return new StockKeepingUnit(id, code, text, this.stockAmount + amount);
    }

    public StockKeepingUnit reduceStockBy(long amount) {
        verifyAvailability(amount);
        return new StockKeepingUnit(id, code, text, this.stockAmount - amount);
    }

    public StockKeepingUnitId getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public long getStockAmount() {
        return stockAmount;
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (!(target instanceof StockKeepingUnit that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StockKeepingUnit(id=%s, code='%s', text='%s')".formatted(id, code, text);
    }

    static class InsufficientStockException extends CatalogException {

        public InsufficientStockException(String message) {
            super(message);
        }
    }

    public static StockKeepingUnit create(StockKeepingUnitId id, String code, String text) {
        return new StockKeepingUnit(id, code, text, 0);
    }
}

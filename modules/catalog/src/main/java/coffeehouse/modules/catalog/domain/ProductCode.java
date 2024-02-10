package coffeehouse.modules.catalog.domain;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public record ProductCode(String value) {

    public ProductCode {
        Objects.requireNonNull(value, "value must not be null");
    }
    
    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (!(target instanceof ProductCode that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

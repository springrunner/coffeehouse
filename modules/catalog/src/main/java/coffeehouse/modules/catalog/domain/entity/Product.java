package coffeehouse.modules.catalog.domain.entity;

import coffeehouse.modules.catalog.domain.ProductCode;
import coffeehouse.modules.catalog.domain.ProductId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Embedded;

import javax.money.MonetaryAmount;
import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public class Product {
    @Id
    private final ProductId id;
    private final ProductCode code;
    private final String name;
    @Embedded.Empty
    private final ProductItems items;
    @Embedded.Empty
    private final ProductCategories categories;

    @Version
    private final Long version;

    Product(ProductId id, ProductCode code, String name, ProductItems items, ProductCategories categories, Long version) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.code = Objects.requireNonNull(code, "code must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.items = Objects.requireNonNull(items, "items must not be null");
        this.categories = Objects.requireNonNullElse(categories, ProductCategories.empty());
        this.version = version;
    }

    public ProductId getId() {
        return id;
    }

    public ProductCode getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public MonetaryAmount getPrice() {
        return items.calculatePrice();
    }

    public ProductItems getItems() {
        return items;
    }

    public ProductCategories getCategories() {
        return categories;
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (!(target instanceof Product that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product(id=%s, code='%s', name='%s')".formatted(id, code, name);
    }

    public static Product create(ProductId id, String code, String name, ProductItems items, ProductCategories categories) {
        return new Product(id, new ProductCode(code), name, items, categories, null);
    }
}

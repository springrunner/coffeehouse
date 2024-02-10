package coffeehouse.modules.catalog.domain.entity;


import coffeehouse.modules.catalog.domain.CategoryId;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author springrunner.kr@gmail.com
 */
public class ProductCategories {

    @MappedCollection(idColumn = "ID")
    private final Set<ProductCategory> categories;

    ProductCategories(Set<ProductCategory> categories) {
        this.categories = categories;
    }

    public static ProductCategories of(Set<CategoryId> categories) {
        return new ProductCategories(categories.stream().map(ProductCategory::new).collect(Collectors.toSet()));
    }

    public static ProductCategories empty() {
        return new ProductCategories(Collections.emptySet());
    }

    record ProductCategory(@Id CategoryId categoryId) {
    }
}

package coffeehouse.modules.catalog.domain.service;

import coffeehouse.modules.catalog.domain.CatalogException;
import coffeehouse.modules.catalog.domain.ProductCode;

/**
 * @author springrunner.kr@gmail.com
 */
public class ProductNotFoundException extends CatalogException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public static ProductNotFoundException ofProductId(String productId) {
        return new ProductNotFoundException("No registered product by `%s`".formatted(productId));
    }

    public static ProductNotFoundException ofProductCode(String productCode) {
        return new ProductNotFoundException("No registered product by `%s`".formatted(productCode));
    }
}

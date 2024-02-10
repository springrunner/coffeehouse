package coffeehouse.modules.catalog.domain.service;

import coffeehouse.modules.catalog.domain.ProductCode;
import coffeehouse.modules.catalog.domain.ProductId;

import javax.money.MonetaryAmount;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public interface Catalogs {

    /**
     * Retrieves product details for a given product ID.
     *
     * @param productId productId The ID of the product
     * @return An Optional containing the product details if found
     */
    Optional<ProductDetails> getProductDetails(ProductId productId);

    /**
     * Retrieves product details for a given product Code.
     *
     * @param productCode productId The Code of the product
     * @return An Optional containing the product details if found
     */
    Optional<ProductDetails> getProductDetails(ProductCode productCode);

    record ProductDetails(ProductId id, ProductCode code, String name, MonetaryAmount price) {
    }
}

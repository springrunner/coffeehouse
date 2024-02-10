package coffeehouse.modules.catalog.domain.entity;

import coffeehouse.modules.catalog.domain.ProductCode;
import coffeehouse.modules.catalog.domain.ProductId;

import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public interface ProductRepository {

    /**
     * Saves a product entity to the repository.
     *
     * @param entity The product entity to save
     * @return The saved product entity
     */
    Product save(Product entity);

    /**
     * Finds a product by its ID.
     *
     * @param id The ID of the product
     * @return An Optional containing the found product, if available
     */
    Optional<Product> findById(ProductId id);

    /**
     * Finds a product by its unique code.
     *
     * @param code The unique code of the product
     * @return An Optional containing the found product, if available
     */
    Optional<Product> findByCode(ProductCode code);
}

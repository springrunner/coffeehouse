package coffeehouse.modules.catalog.domain.service.business;

import coffeehouse.libraries.spring.beans.factory.annotation.Published;
import coffeehouse.modules.catalog.domain.ProductCode;
import coffeehouse.modules.catalog.domain.ProductId;
import coffeehouse.modules.catalog.domain.entity.ProductRepository;
import coffeehouse.modules.catalog.domain.service.Catalogs;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
@Published
@Service
public class CatalogsService implements Catalogs {

    private final ProductRepository productRepository;

    public CatalogsService(ProductRepository productRepository) {
        this.productRepository = Objects.requireNonNull(productRepository, "ProductRepository must not be null");
    }

    @Override
    public Optional<ProductDetails> getProductDetails(ProductId productId) {
        return productRepository.findById(productId)
                .map(it -> new ProductDetails(it.getId(), it.getCode(), it.getName(), it.getPrice()));
    }

    @Override
    public Optional<ProductDetails> getProductDetails(ProductCode productCode) {
        return productRepository.findByCode(productCode)
                .map(it -> new ProductDetails(it.getId(), it.getCode(), it.getName(), it.getPrice()));
    }
}

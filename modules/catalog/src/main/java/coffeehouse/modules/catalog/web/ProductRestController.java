package coffeehouse.modules.catalog.web;

import coffeehouse.contracts.catalog.web.ProductsApi;
import coffeehouse.contracts.catalog.web.model.ProductDetails;
import coffeehouse.modules.catalog.domain.ProductCode;
import coffeehouse.modules.catalog.domain.service.Catalogs;
import coffeehouse.modules.catalog.domain.service.ProductNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class ProductRestController implements ProductsApi {

    private final Catalogs catalogs;

    public ProductRestController(Catalogs catalogs) {
        this.catalogs = Objects.requireNonNull(catalogs, "Catalogs must not be null");
    }

    @Override
    public ResponseEntity<ProductDetails> getProductDetails(String productCode) {
        return catalogs.getProductDetails(new ProductCode(productCode)).map(it -> {
            var productDetails = new ProductDetails().code(it.code().toString()).name(it.name()).price(it.price().toString());
            return ResponseEntity.ok(productDetails);
        }).orElseThrow(() -> ProductNotFoundException.ofProductCode(productCode));
    }
}

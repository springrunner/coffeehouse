package coffeehouse.modules.catalog.data.jdbc;

import coffeehouse.modules.catalog.domain.ProductId;
import coffeehouse.modules.catalog.domain.entity.Product;
import coffeehouse.modules.catalog.domain.entity.ProductRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * @author springrunner.kr@gmail.com
 */
public interface SpringJdbcProductRepository extends ProductRepository, CrudRepository<Product, ProductId> {
}

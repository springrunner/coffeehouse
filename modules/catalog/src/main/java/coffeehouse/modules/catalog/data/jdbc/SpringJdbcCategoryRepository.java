package coffeehouse.modules.catalog.data.jdbc;

import coffeehouse.modules.catalog.domain.CategoryId;
import coffeehouse.modules.catalog.domain.entity.Category;
import coffeehouse.modules.catalog.domain.entity.CategoryRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * @author springrunner.kr@gmail.com
 */
public interface SpringJdbcCategoryRepository extends CategoryRepository, CrudRepository<Category, CategoryId> {
}

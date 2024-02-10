package coffeehouse.modules.catalog.domain.entity;

import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public interface CategoryRepository {

    /**
     * Saves a category entity to the repository.
     *
     * @param entity The category entity to save
     * @return The saved category entity
     */
    Category save(Category entity);

    /**
     * Finds a category by its unique code
     *
     * @param code The unique code of the category
     * @return An Optional containing the found category, if available
     */
    Optional<Category> findByCode(String code);
}

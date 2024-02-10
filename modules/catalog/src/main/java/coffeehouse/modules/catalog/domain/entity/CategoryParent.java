package coffeehouse.modules.catalog.domain.entity;

import coffeehouse.modules.catalog.domain.CategoryId;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public record CategoryParent(CategoryId id) {

    public static CategoryParent of(CategoryId parentId) {
        if (Objects.isNull(parentId)) {
            return null;
        }
        return new CategoryParent(parentId);
    }
}

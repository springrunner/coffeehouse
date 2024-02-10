package coffeehouse.modules.catalog.domain.entity;

import coffeehouse.libraries.base.lang.IterableItem;
import coffeehouse.modules.catalog.domain.CategoryId;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author springrunner.kr@gmail.com
 */
public class CategoryChildren implements IterableItem<CategoryChildren.CategoryChild> {

    @MappedCollection(idColumn = "ID")
    private final Set<CategoryChild> children;

    CategoryChildren(Set<CategoryChild> children) {
        this.children = Objects.requireNonNull(children, "children must not be null");
    }

    @Override
    public int size() {
        return children.size();
    }

    @Override
    public boolean isEmpty() {
        return children.isEmpty();
    }

    @Override
    public Iterator<CategoryChild> iterator() {
        return children.iterator();
    }

    public record CategoryChild(@Id CategoryId childId) {
    }

    public static CategoryChildren of(Set<CategoryId> categories) {
        return new CategoryChildren(categories.stream().map(CategoryChild::new).collect(Collectors.toSet()));
    }

    public static CategoryChildren empty() {
        return new CategoryChildren(Collections.emptySet());
    }
}

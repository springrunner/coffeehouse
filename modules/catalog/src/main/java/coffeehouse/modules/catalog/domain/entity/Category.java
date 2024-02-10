package coffeehouse.modules.catalog.domain.entity;

import coffeehouse.modules.catalog.domain.CategoryId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Embedded;

import java.util.Objects;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public class Category {

    @Id
    private final CategoryId id;
    private final String code;
    private final String text;
    @Embedded.Nullable
    private final CategoryParent parent;
    @Embedded.Empty
    private final CategoryChildren children;

    @Version
    private final Long version;

    Category(CategoryId id, String code, String text, CategoryParent parent, CategoryChildren children, Long version) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.code = Objects.requireNonNull(code, "code must not be null");
        this.text = Objects.requireNonNull(text, "text must not be null");
        this.parent = parent;
        this.children = Objects.requireNonNullElse(children, CategoryChildren.empty());
        this.version = version;
    }

    public CategoryId getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public Optional<CategoryParent> getParent() {
        return Optional.of(parent);
    }

    public CategoryChildren getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Category(id=%s, code='%s', text='%s')".formatted(id, code, text);
    }

    public static Category create(CategoryId id, String code, String text) {
        return create(id, code, text, null, null);
    }

    public static Category create(CategoryId id, String code, String text, CategoryId parentId) {
        return create(id, code, text, parentId, null);
    }

    public static Category create(CategoryId id, String code, String text, CategoryId parentId, CategoryChildren children) {
        return new Category(id, code, text, CategoryParent.of(parentId), children, null);
    }
}

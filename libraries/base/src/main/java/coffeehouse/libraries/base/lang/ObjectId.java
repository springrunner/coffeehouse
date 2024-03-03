package coffeehouse.libraries.base.lang;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class ObjectId<T> implements Serializable {

    private final T value;

    public ObjectId(T value) {
        this.value = Objects.requireNonNull(value, "Value must not be null");
    }

    public <U extends ObjectId<T>> U map(Class<U> type) {
        try {
            return type.getDeclaredConstructor(value.getClass()).newInstance(value);
        } catch (ReflectiveOperationException error) {
            throw new IllegalStateException("Failed to map %s to %s".formatted(value.getClass(), type), error);
        }
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (!(target instanceof ObjectId<?> that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

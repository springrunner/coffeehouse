package coffeehouse.libraries.base.crypto;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public record Password(String value) {

    public Password {
        Objects.requireNonNull(value, "value must not be null");
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (!(target instanceof Password password)) return false;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }

    public static Password of(String password) {
        return new Password(password);
    }
}

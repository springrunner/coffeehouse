package coffeehouse.libraries.base.lang;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author springrunner.kr@gmail.com
 */
public record Email(String value) {

    public static final String RFC5322_RULE = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public Email {
        Objects.requireNonNull(value, "value must not be null");
        if (!Pattern.compile(RFC5322_RULE).matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email: '%s'".formatted(value));
        }
    }

    /**
     * Returns the local part (name) of the email.
     *
     * @return The local part of the email
     */
    public String getName() {
        return value.substring(0, value.indexOf('@'));
    }

    /**
     * Returns the domain part of the email.
     *
     * @return The domain part of the email
     */
    public String getDomain() {
        return value.substring(value.indexOf('@') + 1);
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (!(target instanceof Email email)) return false;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }

    public static Email of(String email) {
        return new Email(email);
    }
}

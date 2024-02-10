package coffeehouse.libraries.security;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public final class PlainToken extends Token {

    private String value;

    public PlainToken(String value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    @Override
    public String toString() {
        return value;
    }
}

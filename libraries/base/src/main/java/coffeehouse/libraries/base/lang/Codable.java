package coffeehouse.libraries.base.lang;

import java.util.EnumSet;
import java.util.NoSuchElementException;

/**
 * @author springrunner.kr@gmail.com
 */
public interface Codable {

    String getCode();

    String getText();

    String getDescription();

    static <E extends Enum<E> & Codable> E ofCode(Class<E> enumClass, String code) {
        return EnumSet.allOf(enumClass).stream()
                .filter(it -> it.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No code present: %s".formatted(code)));
    }
}

package coffeehouse.modules.user.domain.entity;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Enumerates the different roles of user accounts within the system,
 * each with a unique code, text representation, and description.
 *
 * @author springrunner.kr@gmail.com
 */
public enum UserAccountRole {
    CUSTOMER("CUSTOMER", "Customer", "Represents a regular customer of the coffeehouse, with access to place orders and view their account."),
    CASHIER("CASHIER", "Cashier", "Handles transactions, including accepting orders and processing refunds. Has access to sales data."),
    BARISTA("BARISTA", "Barista", "Responsible for preparing and serving coffee and other drinks. Manages inventory of ingredients."),
    BOSS("BOSS", "Boss", "The owner or manager of the coffeehouse, with full access to all system features, including employee management and financial reports."),
    ADMINISTRATOR("ADMINISTRATOR", "Administrator", "Oversees and manages the entire system, including user accounts, system settings, and data integrity. Has the highest level of access.");

    private final String code;
    private final String text;
    private final String description;

    UserAccountRole(String code, String text, String description) {
        this.code = Objects.requireNonNull(code, "code must not be null");
        this.text = Objects.requireNonNull(text, "text must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return code;
    }

    public static UserAccountRole ofCode(String code) {
        return Arrays.stream(values())
                .filter(it -> it.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No code present: %s".formatted(code)));
    }
}

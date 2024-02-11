package coffeehouse.modules.order.domain.entity;

import coffeehouse.libraries.base.lang.Codable;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public enum OrderStatus implements Codable {
    PLACED("PLACED", "PLACED", "Order has been placed"),
    ACCEPTED("ACCEPTED", "ACCEPTED", "Order has been accepted by staff"),
    COMPLETED("COMPLETED", "COMPLETED", "Order is completed"),
    CANCELED("CANCELED", "CANCELED", "Order has been canceled (if any of the items are canceled)");

    private final String code;
    private final String text;
    private final String description;

    OrderStatus(String code, String text, String description) {
        this.code = Objects.requireNonNull(code, "code must not be null");
        this.text = Objects.requireNonNull(text, "text must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return code;
    }

    public static OrderStatus ofCode(String code) {
        return Codable.ofCode(OrderStatus.class, code);
    }
}

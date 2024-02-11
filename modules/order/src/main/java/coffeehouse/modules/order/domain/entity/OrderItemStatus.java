package coffeehouse.modules.order.domain.entity;

import coffeehouse.libraries.base.lang.Codable;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public enum OrderItemStatus implements Codable {
    PENDING_PREPARE("PENDING_PREPARE", "PENDING_PREPARE", "Waiting for confirmation by barista"),
    PREPARING("PREPARING", "PREPARING", "Item is being prepared (drink being made, bakery item being warmed up, etc)"),
    DELIVERING("DELIVERING", "DELIVERING", "Item is being delivered"),
    DELIVERED("DELIVERED", "DELIVERED", "Item has been delivered to the customer"),
    REFUSED("REFUSED", "REFUSED", "Item preparation has been refused (e.g., due to lack of stock)"),
    CANCELED("CANCELED", "CANCELED", "Item has been canceled");

    private final String code;
    private final String text;
    private final String description;

    OrderItemStatus(String code, String text, String description) {
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

    public static OrderItemStatus ofCode(String code) {
        return Codable.ofCode(OrderItemStatus.class, code);
    }
}

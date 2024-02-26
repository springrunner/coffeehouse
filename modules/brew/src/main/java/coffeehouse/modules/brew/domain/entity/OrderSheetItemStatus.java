package coffeehouse.modules.brew.domain.entity;

import coffeehouse.libraries.base.lang.Codable;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public enum OrderSheetItemStatus implements Codable {
    SUBMITTED("SUBMITTED", "Submitted", "Item has been submitted"),
    CONFIRMED("CONFIRMED", "Confirmed", "Item is confirmed (drink being made, bakery item being warmed up, etc.)"),
    PICKUP_REQUESTED("PICKUP_REQUESTED", "Pickup requested", "Item is begin picked up"),
    PICKUP_COMPLETED("PICKUP_COMPLETED", "Pickup completed", "Item has been picked up by the customer"),
    REFUSED("REFUSED", "Refused", "Item preparation has been refused (e.g., due to lack of stock)");

    private String code;
    private String text;
    private String description;

    OrderSheetItemStatus(String code, String text, String description) {
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

    public static OrderSheetItemStatus ofCode(String code) {
        return Codable.ofCode(OrderSheetItemStatus.class, code);
    }
}

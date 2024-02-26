package coffeehouse.modules.brew.domain.entity;

import coffeehouse.libraries.base.lang.Codable;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public enum OrderSheetStatus implements Codable {
    SUBMITTED("SUBMITTED", "Submitted", "Order sheet has been submitted"),
    CONFIRMED("CONFIRMED", "Confirmed", "Order sheet has been confirmed by staff"),
    PARTIALLY_CONFIRMED("PARTIALLY_CONFIRMED", "Partially confirmed", "Order sheet has been partially confirmed by staff"),
    PROCESSED("PROCESSED", "Processed", "Order sheet is processed"),
    REFUSED("REFUSED", "Refused", "Order sheet has been refused");

    private final String code;
    private final String text;
    private final String description;

    OrderSheetStatus(String code, String text, String description) {
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

    public static OrderSheetStatus ofCode(String code) {
        return Codable.ofCode(OrderSheetStatus.class, code);
    }
}

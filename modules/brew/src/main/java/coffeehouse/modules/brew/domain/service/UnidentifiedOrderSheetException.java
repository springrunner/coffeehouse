package coffeehouse.modules.brew.domain.service;

import coffeehouse.modules.brew.domain.OrderSheetId;
import coffeehouse.modules.order.domain.OrderException;

/**
 * @author springrunner.kr@gmail.com
 */
public class UnidentifiedOrderSheetException extends OrderException {

    public UnidentifiedOrderSheetException(String message) {
        super(message);
    }

    public static UnidentifiedOrderSheetException ofOrderSheetId(OrderSheetId orderSheetId) {
        return new UnidentifiedOrderSheetException("No registered order-sheet by `%s`".formatted(orderSheetId));
    }

    public static UnidentifiedOrderSheetException ofOrderSheetId(String orderSheetId) {
        return new UnidentifiedOrderSheetException("No registered order-sheet by `%s`".formatted(orderSheetId));
    }
}

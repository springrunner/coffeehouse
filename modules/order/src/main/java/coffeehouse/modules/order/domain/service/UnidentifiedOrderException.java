package coffeehouse.modules.order.domain.service;

import coffeehouse.modules.order.domain.OrderException;
import coffeehouse.modules.order.domain.OrderId;

/**
 * @author springrunner.kr@gmail.com
 */
public class UnidentifiedOrderException extends OrderException {

    public UnidentifiedOrderException(String message) {
        super(message);
    }

    public static UnidentifiedOrderException ofOrderId(OrderId orderId) {
        return new UnidentifiedOrderException("No registered order by `%s`".formatted(orderId));
    }

    public static UnidentifiedOrderException ofOrderId(String orderId) {
        return new UnidentifiedOrderException("No registered order by `%s`".formatted(orderId));
    }
}

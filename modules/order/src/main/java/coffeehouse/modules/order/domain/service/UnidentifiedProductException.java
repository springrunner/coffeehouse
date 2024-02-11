package coffeehouse.modules.order.domain.service;

import coffeehouse.modules.catalog.domain.ProductCode;
import coffeehouse.modules.order.domain.OrderException;

/**
 * @author springrunner.kr@gmail.com
 */
public class UnidentifiedProductException extends OrderException {

    public UnidentifiedProductException(String message) {
        super(message);
    }

    public static UnidentifiedProductException ofProductCode(ProductCode productCode) {
        return new UnidentifiedProductException("Unregistered product(code: %s)".formatted(productCode));
    }
}

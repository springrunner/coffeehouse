package coffeehouse.modules.order.domain.service;

import coffeehouse.modules.order.domain.OrderException;
import coffeehouse.modules.user.domain.UserAccountId;

/**
 * @author springrunner.kr@gmail.com
 */
public class UnidentifiedCustomerException extends OrderException {

    public UnidentifiedCustomerException(String message) {
        super(message);
    }
    
    public static UnidentifiedCustomerException ofCustomerId(UserAccountId customerId) {
        return new UnidentifiedCustomerException("Unregistered customer(id: %s)".formatted(customerId));
    }

    public static UnidentifiedCustomerException ofUsername(String username) {
        return new UnidentifiedCustomerException("Unregistered account(username: %s)".formatted(username));
    }
}

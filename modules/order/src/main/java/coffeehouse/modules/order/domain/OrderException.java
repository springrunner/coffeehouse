package coffeehouse.modules.order.domain;

/**
 * @author springrunner.kr@gmail.com
 */
public class OrderException extends RuntimeException {

    public OrderException(String message) {
        super(message);
    }
}

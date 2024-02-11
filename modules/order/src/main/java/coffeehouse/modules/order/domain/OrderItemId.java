package coffeehouse.modules.order.domain;

import coffeehouse.libraries.base.lang.ObjectId;

/**
 * @author springrunner.kr@gmail.com
 */
public class OrderItemId extends ObjectId<String> {

    public OrderItemId(String value) {
        super(value);
    }
}

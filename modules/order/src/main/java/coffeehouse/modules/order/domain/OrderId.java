package coffeehouse.modules.order.domain;

import coffeehouse.libraries.base.lang.ObjectId;

/**
 * @author springrunner.kr@gmail.com
 */
public class OrderId extends ObjectId<String> {

    public OrderId(String value) {
        super(value);
    }
}

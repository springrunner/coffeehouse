package coffeehouse.modules.catalog.domain;

import coffeehouse.libraries.base.lang.ObjectId;

/**
 * @author springrunner.kr@gmail.com
 */
public class ProductId extends ObjectId<String> {

    public ProductId(String value) {
        super(value);
    }
}

package coffeehouse.modules.catalog.domain;

import coffeehouse.libraries.base.lang.ObjectId;

/**
 * @author springrunner.kr@gmail.com
 */
public class ProductItemId extends ObjectId<String> {

    public ProductItemId(String value) {
        super(value);
    }
}

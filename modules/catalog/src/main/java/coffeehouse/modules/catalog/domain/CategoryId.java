package coffeehouse.modules.catalog.domain;

import coffeehouse.libraries.base.lang.ObjectId;

/**
 * @author springrunner.kr@gmail.com
 */
public class CategoryId extends ObjectId<String> {

    public CategoryId(String value) {
        super(value);
    }
}

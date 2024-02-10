package coffeehouse.modules.user.domain;

import coffeehouse.libraries.base.lang.ObjectId;

/**
 * @author springrunner.kr@gmail.com
 */
public class UserAccountId extends ObjectId<String> {

    public UserAccountId(String value) {
        super(value);
    }
}

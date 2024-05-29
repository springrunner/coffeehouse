package coffeehouse.modules.user.domain.entity;

import coffeehouse.modules.user.domain.UserAccountId;
/**
 * @author springrunner.kr@gmail.com
 */
public class UserAccount {
    private final UserAccountId userAccountId;

    public UserAccountId getUserAccountId() {
        return userAccountId;
    }

    public UserAccount(UserAccountId userAccountId) {
        this.userAccountId = userAccountId;
    }

    public static UserAccount createCustomer(UserAccountId id) {
        return new UserAccount(id);
    }
}

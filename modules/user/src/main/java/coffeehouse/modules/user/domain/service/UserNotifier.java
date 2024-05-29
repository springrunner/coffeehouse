package coffeehouse.modules.user.domain.service;

import coffeehouse.modules.user.domain.entity.UserAccount;
/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface UserNotifier {
    void notify(UserAccount userAccount);
}

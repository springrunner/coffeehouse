package coffeehouse.modules.user.domain.entity;

import coffeehouse.modules.user.domain.UserAccountId;

import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public interface UserAccountRepository {
    Optional<UserAccount> findById(UserAccountId userAccountId);
    UserAccount save(UserAccount userAccount);
}

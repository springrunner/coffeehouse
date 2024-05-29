package coffeehouse.modules.user.data.repository;

import coffeehouse.modules.user.domain.UserAccountId;
import coffeehouse.modules.user.domain.entity.UserAccount;
import coffeehouse.modules.user.domain.entity.UserAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
@Repository
public class InMemoryUserAccountRepository implements UserAccountRepository {

    private final List<UserAccount> userAccounts = new ArrayList<>();

    @Override
    public Optional<UserAccount> findById(UserAccountId userAccountId) {
        return userAccounts.stream().filter(userAccount -> userAccount.getUserAccountId().equals(userAccountId)).findFirst();
    }

    @Override
    public UserAccount save(UserAccount userAccount) {
        userAccounts.add(userAccount);
        return userAccount;
    }
}

package coffeehouse.modules.user.data.jdbc;

import coffeehouse.modules.user.domain.UserAccountId;
import coffeehouse.modules.user.domain.entity.UserAccount;
import coffeehouse.modules.user.domain.entity.UserAccountRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * @author springrunner.kr@gmail.com
 */
public interface SpringJdbcUserAccountRepository extends UserAccountRepository, CrudRepository<UserAccount, UserAccountId> {
}

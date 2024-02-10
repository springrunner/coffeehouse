package coffeehouse.modules.user.data.jdbc;

import coffeehouse.modules.user.domain.UserAccountId;
import coffeehouse.modules.user.domain.entity.UserAccountToken;
import coffeehouse.modules.user.domain.entity.UserAccountTokenRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * @author springrunner.kr@gmail.com
 */
public interface SpringJdbcUserAccountTokenRepository extends UserAccountTokenRepository, CrudRepository<UserAccountToken, UserAccountId> {
}

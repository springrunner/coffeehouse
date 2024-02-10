package coffeehouse.modules.user.domain.entity;

import coffeehouse.libraries.security.Token;

import java.util.Optional;

/**
 * Interface for the UserAccountToken repository
 *
 * @author springrunner.kr@gmail.com
 */
public interface UserAccountTokenRepository {

    /**
     * Saves a UserAccountToken entity to the repository
     *
     * @param entity The UserAccountToken to save
     * @return The saved UserAccountToken
     */
    UserAccountToken save(UserAccountToken entity);

    /**
     * Finds a UserAccountToken by its Value
     *
     * @param value The value of the UserAccountToken
     * @return An Optional containing the found UserAccountToken, or empty if not found
     */
    Optional<UserAccountToken> findByValue(String value);
}

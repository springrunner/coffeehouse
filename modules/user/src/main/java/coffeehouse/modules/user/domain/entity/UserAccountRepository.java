package coffeehouse.modules.user.domain.entity;

import coffeehouse.libraries.base.lang.Email;
import coffeehouse.modules.user.domain.UserAccountId;

import java.util.Optional;

/**
 * Interface for the UserAccount repository
 *
 * @author springrunner.kr@gmail.com
 */
public interface UserAccountRepository {

    /**
     * Saves a UserAccount entity to the repository
     *
     * @param entity The UserAccount to save
     * @return The saved UserAccount
     */
    UserAccount save(UserAccount entity);

    /**
     * Finds a UserAccount by its ID
     *
     * @param id The ID of the UserAccount
     * @return An Optional containing the found UserAccount, or empty if not found
     */
    Optional<UserAccount> findById(UserAccountId id);

    /**
     * Finds a UserAccount by email
     *
     * @param email The email of the UserAccount
     * @return An Optional containing the found UserAccount, or empty if not found
     */
    Optional<UserAccount> findByEmail(Email email);

    /**
     * Finds a UserAccount by username
     *
     * @param username The username of the UserAccount
     * @return An Optional containing the found UserAccount, or empty if not found
     */
    Optional<UserAccount> findByUsername(String username);

    /**
     * Checks if an account with the given email exists
     *
     * @param email The email to check
     * @return True if an account with the email exists, false otherwise
     */
    Boolean existsByEmail(Email email);
}

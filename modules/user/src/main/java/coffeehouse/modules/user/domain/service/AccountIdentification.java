package coffeehouse.modules.user.domain.service;

import coffeehouse.modules.user.domain.UserAccountId;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface AccountIdentification {

    /**
     * Identify a UserAccount by email or username and password
     *
     * @param identifier  UserAccount identifier (email or username)
     * @param rawPassword Raw(unencrypted) password
     * @return The confirmed user account ID
     */
    UserAccountId identify(String identifier, String rawPassword);
}

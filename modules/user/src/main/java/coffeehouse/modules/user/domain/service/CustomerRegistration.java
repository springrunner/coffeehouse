package coffeehouse.modules.user.domain.service;

import coffeehouse.libraries.base.lang.Email;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface CustomerRegistration {

    /**
     * Register a new customer
     *
     * @param email       Email address
     * @param rawPassword Raw(unencrypted) password
     */
    void register(Email email, String rawPassword);
}

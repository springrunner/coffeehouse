package coffeehouse.modules.user.domain.service;

import coffeehouse.libraries.base.lang.Email;
import coffeehouse.modules.user.domain.UserAccountId;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public interface Customers {

    /**
     * Retrieve customer information
     *
     * @param customerId Customer(user) account ID
     * @return An Optional containing the customer information object, or empty if not found
     */
    Optional<CustomerDetails> getCustomerDetails(UserAccountId customerId);

    /**
     * Retrieve customer information
     *
     * @param username Customer(user) account Username
     * @return An Optional containing the customer information object, or empty if not found
     */
    Optional<CustomerDetails> getCustomerDetails(String username);    

    record CustomerDetails(UserAccountId id, Email email, String username, LocalDateTime createdAt) {
    }
}

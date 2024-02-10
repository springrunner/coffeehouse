package coffeehouse.modules.user.domain.service.business;

import coffeehouse.libraries.base.crypto.Password;
import coffeehouse.libraries.base.lang.Email;
import coffeehouse.modules.user.domain.UserAccountId;
import coffeehouse.modules.user.domain.entity.UserAccount;
import coffeehouse.modules.user.domain.entity.UserAccountRepository;
import coffeehouse.modules.user.domain.service.CustomerRegistration;
import coffeehouse.modules.user.domain.service.Customers;
import coffeehouse.modules.user.domain.service.DuplicateEmailException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
public class CustomerService implements CustomerRegistration, Customers {

    private final UserAccountRepository userAccountRepository;

    public CustomerService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = Objects.requireNonNull(userAccountRepository, "UserAccountRepository must not be null");
    }

    @Override
    public void register(Email email, String rawPassword) {
        if (userAccountRepository.existsByEmail(email)) {
            throw DuplicateEmailException.ofEmail(email);
        }

        userAccountRepository.save(
                UserAccount.createCustomer(
                        new UserAccountId(UUID.randomUUID().toString()),
                        email,
                        email.getName(),
                        new Password(rawPassword))
        );
    }

    @Override
    public Optional<CustomerDetails> getCustomerDetails(UserAccountId customerId) {
        return userAccountRepository.findById(customerId)
                .map(it -> new CustomerDetails(it.getId(), it.getEmail(), it.getUsername(), it.getCreatedAt()));
    }

    @Override
    public Optional<CustomerDetails> getCustomerDetails(String username) {
        return userAccountRepository.findByUsername(username)
                .map(it -> new CustomerDetails(it.getId(), it.getEmail(), it.getUsername(), it.getCreatedAt()));
    }
}

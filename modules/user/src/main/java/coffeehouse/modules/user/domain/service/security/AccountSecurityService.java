package coffeehouse.modules.user.domain.service.security;

import coffeehouse.libraries.base.crypto.Password;
import coffeehouse.libraries.base.lang.Email;
import coffeehouse.libraries.security.PlainToken;
import coffeehouse.libraries.security.Token;
import coffeehouse.libraries.security.authentication.TokenAuthentication;
import coffeehouse.libraries.security.authentication.TokenIssuance;
import coffeehouse.libraries.spring.beans.factory.annotation.Published;
import coffeehouse.modules.user.domain.UserAccountId;
import coffeehouse.modules.user.domain.entity.UserAccountRepository;
import coffeehouse.modules.user.domain.entity.UserAccountRole;
import coffeehouse.modules.user.domain.entity.UserAccountToken;
import coffeehouse.modules.user.domain.entity.UserAccountTokenRepository;
import coffeehouse.modules.user.domain.service.AccountIdentification;
import coffeehouse.modules.user.domain.service.TokenIssuanceException;
import coffeehouse.modules.user.domain.service.UserAccountNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author springrunner.kr@gmail.com
 */
@Published
@Service
class AccountSecurityService implements AccountIdentification, TokenIssuance<UserAccountId>, TokenAuthentication<UserAccountId> {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountTokenRepository userAccountTokenRepository;

    public AccountSecurityService(UserAccountRepository userAccountRepository, UserAccountTokenRepository userAccountTokenRepository) {
        this.userAccountRepository = Objects.requireNonNull(userAccountRepository, "UserAccountRepository must not be null");
        this.userAccountTokenRepository = Objects.requireNonNull(userAccountTokenRepository, "UserAccountTokenRepository must not be null");
    }

    @Override
    public UserAccountId identify(String identifier, String rawPassword) {
        var userAccount = userAccountRepository.findByUsername(identifier)
                .or(() -> userAccountRepository.findByEmail(Email.of(identifier)))
                .orElseThrow(() -> new UserAccountNotFoundException("No registered user account by `%s`".formatted(identifier)));

        userAccount.verifyPassword(Password.of(rawPassword));

        return userAccount.getId();
    }

    @Override
    public Token issue(UserAccountId authentication) {
        var userAccount = userAccountRepository.findById(authentication)
                .orElseThrow(() -> new TokenIssuanceException("Unidentified user account by id: `%s`".formatted(authentication)));
        
        var userAccountToken = UserAccountToken.generate(userAccount.getId());
        userAccountTokenRepository.save(userAccountToken);
        
        return new PlainToken(userAccountToken.getValue());
    }

    @Override
    public Optional<Authentication<UserAccountId>> authenticate(Token token) {
        return switch (token) {
            case PlainToken plainToken -> {
                var userAccountToken = userAccountTokenRepository.findByValue(plainToken.toString());
                if (userAccountToken.isEmpty()) {
                    yield Optional.empty();
                }

                yield userAccountRepository.findById(userAccountToken.get().getId()).map(userAccount -> 
                        new Authentication<>(
                                userAccount.getId(), 
                                userAccount.getUsername(), 
                                StreamSupport.stream(userAccount.getRoles().spliterator(), false)
                                        .map(UserAccountRole::getCode)
                                        .collect(Collectors.toSet())
                        )
                );
            }
            default -> Optional.empty();
        };
    }
}

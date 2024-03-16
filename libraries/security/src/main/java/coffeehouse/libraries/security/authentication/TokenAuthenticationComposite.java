package coffeehouse.libraries.security.authentication;

import coffeehouse.libraries.security.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public class TokenAuthenticationComposite implements TokenAuthentication<Object> {
    
    private final List<TokenAuthentication<?>> tokenAuthentications;
    
    private final Logger logger = LoggerFactory.getLogger(getClass()); 

    public TokenAuthenticationComposite(List<TokenAuthentication<?>> tokenAuthentications) {
        this.tokenAuthentications = Objects.requireNonNull(tokenAuthentications);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Authentication<Object>> authenticate(Token token) {
        for (var tokenAuthentication : tokenAuthentications) {
            try {
                var authentication = tokenAuthentication.authenticate(token);
                if (authentication.isPresent()) {
                    return Optional.of((Authentication<Object>) authentication.get());
                }
            } catch (Exception error) {
                logger.info("Authentication failed for token: {} and error: {} in {}", token, error.getMessage(), tokenAuthentication);
            }
        }
        return Optional.empty();
    }
}

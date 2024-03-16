package coffeehouse.libraries.security.authentication.jwt;

import coffeehouse.libraries.security.Token;
import coffeehouse.libraries.security.authentication.TokenAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author springrunner.kr@gmail.com
 */
public class Auth0JSONWebTokenAuthentication<T> implements TokenAuthentication<T> {
    
    private final SecretKey secretKey;
    private final Function<String, T> principalConverter;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Auth0JSONWebTokenAuthentication(SecretKey secretKey, Function<String, T> principalConverter) {
        this.secretKey = Objects.requireNonNull(secretKey);
        this.principalConverter = Objects.requireNonNull(principalConverter);
    }

    @Override
    public Optional<Authentication<T>> authenticate(Token token) {
        var authentication = Auth0JSONWebToken.verify(secretKey, token.toString());
        logger.info("Successful jwt verify: {}", authentication);

        return Optional.of(
                new Authentication<>(
                        principalConverter.apply(authentication.principal()),
                        authentication.username(),
                        authentication.roles()
                )
        );
    }
}

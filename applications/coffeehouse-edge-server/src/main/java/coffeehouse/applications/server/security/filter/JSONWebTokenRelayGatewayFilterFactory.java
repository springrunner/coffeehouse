package coffeehouse.applications.server.security.filter;

import coffeehouse.libraries.security.PlainToken;
import coffeehouse.libraries.security.UnauthorizedException;
import coffeehouse.libraries.security.authentication.TokenAuthentication;
import coffeehouse.libraries.security.authentication.jwt.Auth0JSONWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public class JSONWebTokenRelayGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    
    public static final String TOKEN_PREFIX = "Bearer ";

    private final TokenAuthentication<?> tokenAuthentication;
    private final SecretKey secretKey;
    
    private final Logger logger = LoggerFactory.getLogger(getClass()); 
    
    public JSONWebTokenRelayGatewayFilterFactory(TokenAuthentication<?> tokenAuthentication, SecretKey secretKey) {
        super(Object.class);
        this.tokenAuthentication = Objects.requireNonNull(tokenAuthentication, "TokenAuthentication must not be null");
        this.secretKey = Objects.requireNonNull(secretKey, "SecretKey must not be null");
    }
    
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            var authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (StringUtils.startsWithIgnoreCase(authorization, TOKEN_PREFIX)) {
                logger.debug("Attempt authentication with Bearer Token");

                var token = new PlainToken(authorization.substring(TOKEN_PREFIX.length()));
                logger.debug("Token used for authentication: {}", token);

                var authentication = tokenAuthentication.authenticate(token).orElseThrow(UnauthorizedException::new);
                logger.info("`{}` is authenticated", authentication);

                var jsonWebToken = Auth0JSONWebToken.sign(secretKey, authentication);
                var modifiedExchange = exchange.mutate()
                        .request(it -> it.header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + jsonWebToken))
                        .build();

                return chain.filter(modifiedExchange);
            } else {
                return chain.filter(exchange);
            }
        };
    }
}

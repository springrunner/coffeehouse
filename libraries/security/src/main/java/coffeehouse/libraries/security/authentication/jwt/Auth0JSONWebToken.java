package coffeehouse.libraries.security.authentication.jwt;

import coffeehouse.libraries.security.Token;
import coffeehouse.libraries.security.authentication.TokenAuthentication;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public final class Auth0JSONWebToken extends Token {

    private final String value;

    Auth0JSONWebToken(String value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    @Override
    public String toString() {
        return value;
    }
    
    public static Auth0JSONWebToken sign(SecretKey secretKey, TokenAuthentication.Authentication<?> authentication) {
        return new Auth0JSONWebToken(
                JWT.create()
                        .withClaim("principal", authentication.principal().toString())
                        .withClaim("username", authentication.username())
                        .withClaim("roles", new ArrayList<>(authentication.roles()))
                        .sign(Algorithm.HMAC256(secretKey.getEncoded()))
                
        );
    }
    
    public static TokenAuthentication.Authentication<String> verify(SecretKey secretKey, String token) {
        var jwt = JWT.require(Algorithm.HMAC256(secretKey.getEncoded())).build().verify(token);
        var principal = jwt.getClaim("principal").asString();
        var username = jwt.getClaim("username").asString();
        var roles = new HashSet<>(jwt.getClaim("roles").asList(String.class));

        return new TokenAuthentication.Authentication<>(principal, username, roles);
    }
}

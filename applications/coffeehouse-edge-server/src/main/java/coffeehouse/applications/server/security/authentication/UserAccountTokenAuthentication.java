package coffeehouse.applications.server.security.authentication;

import coffeehouse.libraries.security.Token;
import coffeehouse.libraries.security.authentication.TokenAuthentication;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Objects;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public interface UserAccountTokenAuthentication extends TokenAuthentication<UserAccountTokenAuthentication.Identifier> {
    
    @Override
    @GetExchange("/accounts/authenticate")
    Optional<Authentication<Identifier>> authenticate(@RequestHeader("X-AccessToken") Token token);

    record Identifier(String value) {

        public Identifier {
            Objects.requireNonNull(value);
        }

        @Override
        public String toString() {
            return value;
        }

        @JsonCreator
        static Identifier of(String value) {
            return new Identifier(value);
        }
    }
}

package coffeehouse.libraries.security.authentication;

import coffeehouse.libraries.security.Token;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface TokenAuthentication<T> {

    /**
     * 토큰으로 인증하기
     *
     * @param token 토큰
     * @return 인증된 사용자 객체
     */
    Optional<Authentication<T>> authenticate(Token token);

    record Authentication<T>(T principal, String username, Set<String> roles) implements Principal {

        @Override
        public String getName() {
            return username;
        }
    }
}

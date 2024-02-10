package coffeehouse.libraries.security.authentication;

import coffeehouse.libraries.security.Token;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface TokenIssuance<T> {

    /**
     * 인증 정보로 토큰 발급하기
     *
     * @param authentication 인증 정보
     * @return 발급된 토큰
     */
    Token issue(T authentication);
}

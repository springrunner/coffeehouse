package coffeehouse.modules.user.domain.service;

import coffeehouse.modules.user.domain.UserException;

/**
 * @author springrunner.kr@gmail.com
 */
public class TokenIssuanceException extends UserException {

    public TokenIssuanceException(String message) {
        super(message);
    }
}

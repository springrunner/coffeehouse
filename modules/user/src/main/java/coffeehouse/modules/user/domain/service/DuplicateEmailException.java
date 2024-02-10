package coffeehouse.modules.user.domain.service;

import coffeehouse.libraries.base.lang.Email;
import coffeehouse.modules.user.domain.UserException;

/**
 * @author springrunner.kr@gmail.com
 */
public class DuplicateEmailException extends UserException {

    public DuplicateEmailException(String message) {
        super(message);
    }

    public static DuplicateEmailException ofEmail(Email email) {
        return new DuplicateEmailException("Email(`%s`) is already in use".formatted(email));
    }
}

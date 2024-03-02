package coffeehouse.modules.brew.domain.service;

/**
 * @author springrunner.kr@gmail.com
 */
public class UnidentifiedOrderException extends RuntimeException {

    public UnidentifiedOrderException(String message) {
        super(message);
    }
}

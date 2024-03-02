package coffeehouse.modules.brew.domain.service;

/**
 * @author springrunner.kr@gmail.com
 */
public class UnidentifiedProductException extends RuntimeException {

    public UnidentifiedProductException(String message) {
        super(message);
    }
}

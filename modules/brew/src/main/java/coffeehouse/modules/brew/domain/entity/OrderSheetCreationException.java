package coffeehouse.modules.brew.domain.entity;

import coffeehouse.modules.brew.domain.BrewException;

/**
 * @author springrunner.kr@gmail.com
 */
public class OrderSheetCreationException extends BrewException {

    public OrderSheetCreationException(String message) {
        super(message);
    }
}

package coffeehouse.modules.brew.domain;

/**
 * @author springrunner.kr@gmail.com
 */
public record OrderSheetId(String value) {

    @Override
    public String toString() {
        return value;
    }
}

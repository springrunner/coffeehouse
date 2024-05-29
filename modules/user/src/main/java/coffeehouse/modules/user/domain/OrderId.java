package coffeehouse.modules.user.domain;

/**
 * @author springrunner.kr@gmail.com
 */
public record OrderId(String value) {

    @Override
    public String toString() {
        return value;
    }
}

package coffeehouse.libraries.modulemesh.event.inbox;

/**
 * @author springrunner.kr@gmail.com
 */
public record InboxIdentifier(Number number) {

    @Override
    public String toString() {
        return number.toString();
    }
}

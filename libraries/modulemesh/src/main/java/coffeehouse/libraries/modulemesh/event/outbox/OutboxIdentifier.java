package coffeehouse.libraries.modulemesh.event.outbox;

/**
 * @author springrunner.kr@gmail.com
 */
public record OutboxIdentifier(Number number) {

    @Override
    public String toString() {
        return number.toString();
    }
}

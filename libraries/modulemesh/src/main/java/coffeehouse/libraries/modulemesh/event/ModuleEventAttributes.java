package coffeehouse.libraries.modulemesh.event;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public interface ModuleEventAttributes {

    default EventType eventType() {
        return EventType.of(getClass());
    }

    record EventType(String value) {

        @Override
        public String toString() {
            return value;
        }

        public static EventType of(Class<?> eventClass) {
            return new EventType(Objects.requireNonNull(eventClass, "EventClass must not be null").getSimpleName());
        }
    }
}

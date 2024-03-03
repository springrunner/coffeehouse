package coffeehouse.libraries.modulemesh.event;

import java.net.URI;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author springrunner.kr@gmail.com
 */
public interface ModuleEventAttributes {

    UUID eventId();

    Date eventOccurrenceTime();

    default EventType eventType() {
        return EventType.of(getClass());
    }

    default EventSource eventSource() {
        return EventSource.of(getClass());
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

    record EventSource(URI value) {

        @Override
        public String toString() {
            return value.toString();
        }

        public static EventSource of(Class<?> eventClass) {
            var name = Objects.requireNonNull(eventClass, "EventClass must not be null").getName();
            var moduleName = name.replaceAll("^.*?\\.modules\\.([^\\.]+).*$", "$1").toLowerCase();
            if (moduleName.equalsIgnoreCase(name)) {
                moduleName = name.replaceAll("\\.", "/");
            }

            return ofModule(moduleName);
        }

        public static EventSource ofModule(String moduleName) {
            return new EventSource(URI.create("module://%s".formatted(moduleName)));
        }
    }
}

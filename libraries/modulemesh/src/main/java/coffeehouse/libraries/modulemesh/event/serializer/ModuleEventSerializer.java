package coffeehouse.libraries.modulemesh.event.serializer;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface ModuleEventSerializer<T extends ModuleEvent> {

    void serialize(T object, OutputStream outputStream);

    default byte[] serializeToByteArray(T object) {
        try (var out = new ByteArrayOutputStream(1024)) {
            serialize(object, out);
            return out.toByteArray();
        } catch (IOException error) {
            throw new IllegalStateException(error);
        }
    }
    
    default String serializeToString(T object) {
        return Base64.getEncoder().encodeToString(serializeToByteArray(object));
    }
}

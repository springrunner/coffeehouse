package coffeehouse.libraries.modulemesh.event.serializer;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

/**
 * @author springrunner.kr@gmail.com
 */
public interface ModuleEventDeserializer<T extends ModuleEvent> {

    T deserialize(InputStream inputStream);

    default T deserializeFromByteArray(byte[] serialized) {
        try (var input = new ByteArrayInputStream(serialized)) {
            return deserialize(input);
        } catch (Exception error) {
            throw new IllegalStateException(error);
        }
    }

    default T deserializeFromString(String serialized) {
        return deserializeFromByteArray(Base64.getDecoder().decode(serialized));
    }
}

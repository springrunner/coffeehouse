package coffeehouse.libraries.modulemesh.event.serializer;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * @author springrunner.kr@gmail.com
 */
public class ObjectModuleEventSerde implements ModuleEventSerializer<ModuleEvent>, ModuleEventDeserializer<ModuleEvent> {
    
    public static final ObjectModuleEventSerde INSTANCE = new ObjectModuleEventSerde();

    @Override
    public void serialize(ModuleEvent object, OutputStream outputStream) {
        try (var objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
        } catch (Exception error) {
            throw new IllegalStateException("Failed to module-event serialize", error);
        }
    }

    @Override
    public ModuleEvent deserialize(InputStream inputStream) {
        try (var objectInputStream = new ObjectInputStream(inputStream)) {
            return (ModuleEvent) objectInputStream.readObject();
        } catch (Exception error) {
            throw new IllegalStateException("Failed to module-event deserialize", error);
        }
    }
}

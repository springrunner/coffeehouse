package coffeehouse.libraries.base.serialize.jackson;

import coffeehouse.libraries.base.lang.ObjectId;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/**
 * @author springrunner.kr@gmail.com
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ObjectIdModule extends SimpleModule {

    @SafeVarargs
    public ObjectIdModule(Class<? extends ObjectId<?>>... objectIdTypes) {
        super("ObjectIdModule");
        for (Class<? extends ObjectId<?>> type : objectIdTypes) {
            addSerializer(type, new ObjectIdSerializer(type));
            addDeserializer(type, new ObjectIdDeserializer(type));
        }
    }

    private static class ObjectIdSerializer<T extends ObjectId<?>> extends StdSerializer<T> {

        protected ObjectIdSerializer(Class<T> type) {
            super(type);
        }

        @Override
        public void serialize(T objectId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            var handledTypeName = handledType().getSimpleName();
            var idValueClass = resolveObjectIdValueClass(handledType());

            if (idValueClass == String.class) {
                jsonGenerator.writeString(objectId.toString());
            } else {
                throw new JsonGenerationException("Unsupported value type by `%s`: %s".formatted(handledTypeName, idValueClass.getName()), jsonGenerator);
            }
        }
    }

    private static class ObjectIdDeserializer<T extends ObjectId<?>> extends StdDeserializer<T> {

        public ObjectIdDeserializer(Class<? extends ObjectId<?>> type) {
            super(type);
        }

        @Override
        public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            var handledTypeName = handledType().getSimpleName();
            var idValueClass = resolveObjectIdValueClass(handledType());
            var idValue = jsonParser.getCodec().readTree(jsonParser);

            Object value;
            if (idValueClass == String.class) {
                value = jsonParser.getCodec().treeToValue(idValue, String.class);
            } else {
                throw new JsonMappingException(jsonParser, "Unsupported value type by `%s`: %s".formatted(handledTypeName, idValueClass.getName()));
            }

            try {
                return (T) handledType().getConstructor(idValueClass).newInstance(value);
            } catch (ReflectiveOperationException error) {
                throw new JsonMappingException(jsonParser, "Could not create `%s` with value type: %s".formatted(handledTypeName, idValueClass.getName()), error);
            }
        }
    }

    static Class<?> resolveObjectIdValueClass(Class<?> objectIdType) {
        try {
            var idValueType = ((ParameterizedType) objectIdType.getGenericSuperclass()).getActualTypeArguments()[0];
            return Class.forName(idValueType.getTypeName());
        } catch (Exception error) {
            throw new IllegalStateException("Cannot resolve value type of `%s`".formatted(objectIdType));
        }
    }
}

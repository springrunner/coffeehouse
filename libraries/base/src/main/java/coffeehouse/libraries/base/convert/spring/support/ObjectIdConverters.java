package coffeehouse.libraries.base.convert.spring.support;

import coffeehouse.libraries.base.lang.ObjectId;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.convert.WritingConverter;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class ObjectIdConverters {

    public static <ID extends ObjectId<?>> GenericConverter objectIdToString(Class<ID> idType) {
        return new ObjectIdToStringConverter<>(idType);
    }

    public static <ID extends ObjectId<V>, V> GenericConverter stringToObjectId(Class<ID> idType) {
        return new StringToObjectIdConverter<>(idType);
    }

    @WritingConverter
    static class ObjectIdToStringConverter<ID extends ObjectId<?>> implements GenericConverter {

        private final Class<ID> idType;

        public ObjectIdToStringConverter(Class<ID> idType) {
            this.idType = Objects.requireNonNull(idType, "idType must not be null");
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(new GenericConverter.ConvertiblePair(idType, String.class));
        }

        @Override
        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            return source.toString();
        }
    }

    static class StringToObjectIdConverter<ID extends ObjectId<V>, V> implements GenericConverter {

        private final Class<ID> idType;
        private final Class<V> valueType;
        private final ConversionService conversionService;

        @SuppressWarnings("unchecked")
        public StringToObjectIdConverter(Class<ID> idType) {
            this(idType, (Class<V>) ResolvableType.forClass(idType).getSuperType().getGenerics()[0].toClass(), DefaultConversionService.getSharedInstance());
        }

        public StringToObjectIdConverter(Class<ID> idType, Class<V> valueType) {
            this(idType, valueType, DefaultConversionService.getSharedInstance());
        }

        public StringToObjectIdConverter(Class<ID> idType, Class<V> valueType, ConversionService conversionService) {
            this.idType = Objects.requireNonNull(idType, "idType must not be null");
            this.valueType = Objects.requireNonNull(valueType, "valueType must not be null");
            this.conversionService = Objects.requireNonNull(conversionService, "ConversionService must not be null");
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(new GenericConverter.ConvertiblePair(String.class, idType));
        }

        @Override
        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            try {
                return idType.getConstructor(valueType).newInstance(conversionService.convert(source.toString(), valueType));
            } catch (Exception error) {
                throw new IllegalStateException("Failed to create %s with '%s'".formatted(idType, source), error);
            }
        }
    }
}

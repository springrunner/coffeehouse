package coffeehouse.libraries.base.convert.spring.support;

import coffeehouse.libraries.base.lang.Codable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class CodableConverters {

    public static <E extends Enum<E> & Codable> GenericConverter enumToString(Class<E> enumClass) {
        return new EnumCodableToStringConverter<>(enumClass);
    }

    public static <E extends Enum<E> & Codable> GenericConverter stringToEnum(Class<E> enumClass) {
        return new StringToEnumCodableConverter<>(enumClass);
    }

    @WritingConverter
    static class EnumCodableToStringConverter<E extends Enum<E> & Codable> implements GenericConverter {

        private final Class<E> enumClass;

        public EnumCodableToStringConverter(Class<E> enumClass) {
            this.enumClass = Objects.requireNonNull(enumClass, "enumClass must not be null");
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(new GenericConverter.ConvertiblePair(enumClass, String.class));
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            return ((E) source).getCode();
        }
    }

    @ReadingConverter
    static class StringToEnumCodableConverter<E extends Enum<E> & Codable> implements GenericConverter {

        private final Class<E> enumClass;

        public StringToEnumCodableConverter(Class<E> enumClass) {
            this.enumClass = Objects.requireNonNull(enumClass, "EnumClass must not be null");
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(new GenericConverter.ConvertiblePair(String.class, enumClass));
        }

        @Override
        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            return Codable.ofCode(enumClass, source.toString());
        }
    }
}

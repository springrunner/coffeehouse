package coffeehouse.libraries.base.convert.spring;

import coffeehouse.libraries.base.crypto.Password;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class PasswordConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(new PasswordToStringConverter(), new StringToPasswordConverter());
    }

    @WritingConverter
    static class PasswordToStringConverter implements Converter<Password, String> {

        @Override
        public String convert(Password source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToPasswordConverter implements Converter<String, Password> {

        @Override
        public Password convert(String source) {
            return Password.of(source);
        }
    }
}

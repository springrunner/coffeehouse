package coffeehouse.libraries.base.convert.spring;

import coffeehouse.libraries.base.lang.Email;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class EmailConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(new EmailToStringConverter(), new StringToEmailConverter());
    }

    @WritingConverter
    static class EmailToStringConverter implements Converter<Email, String> {

        @Override
        public String convert(Email source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToEmailConverter implements Converter<String, Email> {

        @Override
        public Email convert(String source) {
            return Email.of(source);
        }
    }
}

package coffeehouse.modules.catalog.data.convert;

import coffeehouse.modules.catalog.domain.CategoryId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class CategoryIdConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(new CategoryIdToStringConverter(), new StringToCategoryIdConverter());
    }

    @WritingConverter
    static class CategoryIdToStringConverter implements Converter<CategoryId, String> {

        @Override
        public String convert(CategoryId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToCategoryIdConverter implements Converter<String, CategoryId> {

        @Override
        public CategoryId convert(String source) {
            return new CategoryId(source);
        }
    }
}

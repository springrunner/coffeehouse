package coffeehouse.modules.catalog.data.convert;

import coffeehouse.modules.catalog.domain.ProductCode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class ProductCodeConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(new ProductCodeToStringConverter(), new StringToProductCodeConverter());
    }

    @WritingConverter
    static class ProductCodeToStringConverter implements Converter<ProductCode, String> {

        @Override
        public String convert(ProductCode source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToProductCodeConverter implements Converter<String, ProductCode> {

        @Override
        public ProductCode convert(String source) {
            return new ProductCode(source);
        }
    }
}

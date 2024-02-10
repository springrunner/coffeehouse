package coffeehouse.modules.catalog.data.convert;

import coffeehouse.modules.catalog.domain.ProductId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class ProductIdConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(new ProductIdToStringConverter(), new StringToProductIdConverter());
    }

    @WritingConverter
    static class ProductIdToStringConverter implements Converter<ProductId, String> {

        @Override
        public String convert(ProductId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToProductIdConverter implements Converter<String, ProductId> {

        @Override
        public ProductId convert(String source) {
            return new ProductId(source);
        }
    }
}

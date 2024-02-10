package coffeehouse.modules.catalog.data.convert;

import coffeehouse.modules.catalog.domain.ProductItemId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class ProductItemIdConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(new ProductItemIdToStringConverter(), new StringToProductItemIdConverter());
    }

    @WritingConverter
    static class ProductItemIdToStringConverter implements Converter<ProductItemId, String> {

        @Override
        public String convert(ProductItemId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToProductItemIdConverter implements Converter<String, ProductItemId> {

        @Override
        public ProductItemId convert(String source) {
            return new ProductItemId(source);
        }
    }
}

package coffeehouse.modules.catalog.data.convert;

import coffeehouse.modules.catalog.domain.StockKeepingUnitId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class StockKeepingUnitIdConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(new StockKeepingUnitIdToStringConverter(), new StringToStockKeepingUnitIdConverter());
    }

    @WritingConverter
    static class StockKeepingUnitIdToStringConverter implements Converter<StockKeepingUnitId, String> {

        @Override
        public String convert(StockKeepingUnitId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToStockKeepingUnitIdConverter implements Converter<String, StockKeepingUnitId> {

        @Override
        public StockKeepingUnitId convert(String source) {
            return new StockKeepingUnitId(source);
        }
    }
}

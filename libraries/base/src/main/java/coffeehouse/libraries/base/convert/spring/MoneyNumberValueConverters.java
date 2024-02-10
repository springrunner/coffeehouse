package coffeehouse.libraries.base.convert.spring;

import org.javamoney.moneta.spi.DefaultNumberValue;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import javax.money.NumberValue;
import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class MoneyNumberValueConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(new NumberValueToDoubleConverter(), new DoubleToProductIdConverter());
    }

    @WritingConverter
    static class NumberValueToDoubleConverter implements Converter<NumberValue, Double> {

        @Override
        public Double convert(NumberValue source) {
            return source.doubleValue();
        }
    }

    @ReadingConverter
    static class DoubleToProductIdConverter implements Converter<Double, NumberValue> {

        @Override
        public NumberValue convert(Double source) {
            return new DefaultNumberValue(source);
        }
    }
}

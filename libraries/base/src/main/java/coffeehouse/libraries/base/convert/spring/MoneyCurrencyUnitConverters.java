package coffeehouse.libraries.base.convert.spring;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class MoneyCurrencyUnitConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(new CurrencyUnitToStringConverter(), new StringToCurrencyUnitConverter());
    }

    @WritingConverter
    static class CurrencyUnitToStringConverter implements Converter<CurrencyUnit, String> {

        @Override
        public String convert(CurrencyUnit source) {
            return source.getCurrencyCode();
        }
    }

    @ReadingConverter
    static class StringToCurrencyUnitConverter implements Converter<String, CurrencyUnit> {

        @Override
        public CurrencyUnit convert(String source) {
            return Monetary.getCurrency(source);
        }
    }
}

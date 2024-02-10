package coffeehouse.libraries.base.convert.spring;

import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class BaseConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(
                new MoneyCurrencyUnitConverters.CurrencyUnitToStringConverter(),
                new MoneyCurrencyUnitConverters.StringToCurrencyUnitConverter(),
                new MoneyNumberValueConverters.NumberValueToDoubleConverter(),
                new MoneyNumberValueConverters.DoubleToProductIdConverter(),
                new EmailConverters.EmailToStringConverter(),
                new EmailConverters.StringToEmailConverter(),
                new PasswordConverters.PasswordToStringConverter(),
                new PasswordConverters.StringToPasswordConverter()
        );
    }
}

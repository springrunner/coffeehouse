package coffeehouse.libraries.base.lang;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Locale;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class Money {

    public static MonetaryAmount of(Number number) {
        return org.javamoney.moneta.Money.of(number, Monetary.getCurrency(Locale.getDefault()));
    }

    public static MonetaryAmount of(Number number, CurrencyUnit currency) {
        return org.javamoney.moneta.Money.of(number, currency);
    }
}

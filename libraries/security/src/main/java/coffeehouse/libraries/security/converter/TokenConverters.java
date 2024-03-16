package coffeehouse.libraries.security.converter;

import coffeehouse.libraries.security.PlainToken;
import coffeehouse.libraries.security.Token;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class TokenConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(new TokenToStringConverter(), new StringToTokenConverter());
    }

    static class TokenToStringConverter implements Converter<Token, String> {

        @Override
        public String convert(Token source) {
            return source.toString();
        }
    }

    static class StringToTokenConverter implements Converter<String, Token> {

        @Override
        public PlainToken convert(String source) {
            return new PlainToken(source);
        }
    }
}

package coffeehouse.modules.user.data.convert;

import coffeehouse.modules.user.domain.UserAccountId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class UserAccountIdConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(new UserAccountIdToStringConverter(), new StringToUserAccountIdConverter());
    }

    @WritingConverter
    static class UserAccountIdToStringConverter implements Converter<UserAccountId, String> {

        @Override
        public String convert(UserAccountId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToUserAccountIdConverter implements Converter<String, UserAccountId> {

        @Override
        public UserAccountId convert(String source) {
            return new UserAccountId(source);
        }
    }
}

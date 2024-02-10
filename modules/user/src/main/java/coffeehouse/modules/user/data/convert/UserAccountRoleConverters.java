package coffeehouse.modules.user.data.convert;

import coffeehouse.modules.user.domain.entity.UserAccountRole;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class UserAccountRoleConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(new UserAccountRoleToStringConverter(), new StringToUserAccountRoleConverter());
    }

    @WritingConverter
    static class UserAccountRoleToStringConverter implements Converter<UserAccountRole, String> {

        @Override
        public String convert(UserAccountRole source) {
            return source.getCode();
        }
    }

    @ReadingConverter
    static class StringToUserAccountRoleConverter implements Converter<String, UserAccountRole> {

        @Override
        public UserAccountRole convert(String source) {
            return UserAccountRole.ofCode(source);
        }
    }
}

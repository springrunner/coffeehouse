package coffeehouse.modules.user.data.convert;

import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class UserConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(
                new UserAccountIdConverters.UserAccountIdToStringConverter(),
                new UserAccountIdConverters.StringToUserAccountIdConverter(),
                new UserAccountRoleConverters.UserAccountRoleToStringConverter(),
                new UserAccountRoleConverters.StringToUserAccountRoleConverter()
        );
    }
}

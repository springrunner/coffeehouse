package coffeehouse.modules.user.data.convert;

import coffeehouse.libraries.base.convert.spring.support.ObjectIdConverters;
import coffeehouse.modules.user.domain.UserAccountId;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class UserConverters {

    public static List<?> converters() {
        return Arrays.asList(
                ObjectIdConverters.objectIdToString(UserAccountId.class),
                ObjectIdConverters.stringToObjectId(UserAccountId.class),
                new UserAccountRoleConverters.UserAccountRoleToStringConverter(),
                new UserAccountRoleConverters.StringToUserAccountRoleConverter()
        );
    }
}

package coffeehouse.modules.user.data.convert;

import coffeehouse.libraries.base.convert.spring.support.CodableConverters;
import coffeehouse.libraries.base.convert.spring.support.ObjectIdConverters;
import coffeehouse.modules.user.domain.UserAccountId;
import coffeehouse.modules.user.domain.entity.UserAccountRole;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class UserConverters {

    public static List<GenericConverter> converters() {
        return Arrays.asList(
                ObjectIdConverters.objectIdToString(UserAccountId.class),
                ObjectIdConverters.stringToObjectId(UserAccountId.class),
                CodableConverters.enumToString(UserAccountRole.class),
                CodableConverters.stringToEnum(UserAccountRole.class)
        );
    }
}

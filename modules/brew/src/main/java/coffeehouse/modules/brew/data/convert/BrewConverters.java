package coffeehouse.modules.brew.data.convert;

import coffeehouse.libraries.base.convert.spring.support.CodableConverters;
import coffeehouse.libraries.base.convert.spring.support.ObjectIdConverters;
import coffeehouse.modules.brew.domain.OrderSheetId;
import coffeehouse.modules.brew.domain.OrderSheetItemId;
import coffeehouse.modules.brew.domain.entity.OrderSheetItemStatus;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class BrewConverters {

    public static List<GenericConverter> converters() {
        return Arrays.asList(
                ObjectIdConverters.objectIdToString(OrderSheetId.class),
                ObjectIdConverters.stringToObjectId(OrderSheetId.class),
                ObjectIdConverters.objectIdToString(OrderSheetItemId.class),
                ObjectIdConverters.stringToObjectId(OrderSheetItemId.class),
                CodableConverters.enumToString(OrderSheetItemStatus.class),
                CodableConverters.stringToEnum(OrderSheetItemStatus.class)
        );
    }
}

package coffeehouse.modules.order.data.convert;

import coffeehouse.libraries.base.convert.spring.support.CodableConverters;
import coffeehouse.libraries.base.convert.spring.support.ObjectIdConverters;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.OrderItemId;
import coffeehouse.modules.order.domain.entity.OrderItemStatus;
import coffeehouse.modules.order.domain.entity.OrderStatus;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class OrderConverters {

    public static List<GenericConverter> converters() {
        return Arrays.asList(
                ObjectIdConverters.objectIdToString(OrderId.class),
                ObjectIdConverters.stringToObjectId(OrderId.class),
                CodableConverters.enumToString(OrderStatus.class),
                CodableConverters.stringToEnum(OrderStatus.class),
                ObjectIdConverters.objectIdToString(OrderItemId.class),
                ObjectIdConverters.stringToObjectId(OrderItemId.class),
                CodableConverters.enumToString(OrderItemStatus.class),
                CodableConverters.stringToEnum(OrderItemStatus.class)
        );
    }
}

package coffeehouse.modules.order.data.convert;

import coffeehouse.modules.order.domain.entity.OrderItemStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class OrderItemStatusConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(new OrderItemStatusToStringConverter(), new StringToOrderItemStatusConverter());
    }

    @WritingConverter
    static class OrderItemStatusToStringConverter implements Converter<OrderItemStatus, String> {

        @Override
        public String convert(OrderItemStatus source) {
            return source.getCode();
        }
    }

    @ReadingConverter
    static class StringToOrderItemStatusConverter implements Converter<String, OrderItemStatus> {

        @Override
        public OrderItemStatus convert(String source) {
            return OrderItemStatus.ofCode(source);
        }
    }
}

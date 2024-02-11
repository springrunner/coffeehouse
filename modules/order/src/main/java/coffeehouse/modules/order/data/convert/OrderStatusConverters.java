package coffeehouse.modules.order.data.convert;

import coffeehouse.modules.order.domain.entity.OrderStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class OrderStatusConverters {

    public static List<Converter<?, ?>> converters() {
        return Arrays.asList(new OrderStatusToStringConverter(), new StringToOrderStatusConverter());
    }

    @WritingConverter
    static class OrderStatusToStringConverter implements Converter<OrderStatus, String> {

        @Override
        public String convert(OrderStatus source) {
            return source.getCode();
        }
    }

    @ReadingConverter
    static class StringToOrderStatusConverter implements Converter<String, OrderStatus> {

        @Override
        public OrderStatus convert(String source) {
            return OrderStatus.ofCode(source);
        }
    }
}

package coffeehouse.modules.order.data.message;

import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.message.BrewRequestCommandMessage;
import coffeehouse.modules.order.domain.service.BarCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Component
public class BrewRequestProducer implements BarCounter {

    private final MessageChannel barCounterChannel;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BrewRequestProducer(MessageChannel barCounterChannel) {
        this.barCounterChannel = Objects.requireNonNull(barCounterChannel, "BarCounterChannel must not be null");
    }

    @Override
    public void brew(OrderId orderId) {
        logger.info("Request brew: %s".formatted(orderId));
        var brewRequestCommandMessage = new BrewRequestCommandMessage(orderId);
        var message = new GenericMessage<>(brewRequestCommandMessage);
        barCounterChannel.send(message);
    }
}

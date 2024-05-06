package coffeehouse.modules.brew.data.message;

import coffeehouse.modules.brew.domain.message.BrewCompletedEventMessage;
import coffeehouse.modules.brew.domain.service.Notification;
import coffeehouse.modules.order.domain.OrderId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class NotificationMessagePublisher implements Notification {

    private final MessageChannel notifyBrewCompletedEventMessageChannel;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public NotificationMessagePublisher(MessageChannel notifyBrewCompletedEventMessageChannel, ObjectMapper objectMapper) {
        this.notifyBrewCompletedEventMessageChannel = Objects.requireNonNull(notifyBrewCompletedEventMessageChannel, "NotifyBrewCompletedEventMessageChannel must not be null");
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper must not be null");
    }


    @Override
    public void notify(OrderId orderId) {
        logger.info("Notify user: %s".formatted(orderId));
        var brewCompletedEventMessage = new BrewCompletedEventMessage(orderId);

        String payload;
        try {
            payload = objectMapper.writeValueAsString(brewCompletedEventMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var message = new GenericMessage<>(payload);
        notifyBrewCompletedEventMessageChannel.send(message);
    }
}

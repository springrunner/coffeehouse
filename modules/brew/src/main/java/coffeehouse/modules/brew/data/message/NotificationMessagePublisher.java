package coffeehouse.modules.brew.data.message;

import coffeehouse.modules.brew.domain.message.BrewCompletedMessage;
import coffeehouse.modules.brew.domain.service.Notification;
import coffeehouse.modules.order.domain.OrderId;
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

    private final MessageChannel brewCompletedNotifyUserChannel;
    private final MessageChannel brewCompletedNotifyOrderChannel;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public NotificationMessagePublisher(MessageChannel brewCompletedNotifyUserChannel, MessageChannel brewCompletedNotifyOrderChannel) {
        this.brewCompletedNotifyUserChannel = Objects.requireNonNull(brewCompletedNotifyUserChannel, "BrewCompletedNotifyUserChannel must not be null");
        this.brewCompletedNotifyOrderChannel = Objects.requireNonNull(brewCompletedNotifyOrderChannel, "BrewCompletedNotifyOrderChannel must not be null");
    }


    @Override
    public void notifyUser(OrderId orderId) {
        logger.info("Notify user: %s".formatted(orderId));
        var brewCompletedMessage = new BrewCompletedMessage(orderId);
        var message = new GenericMessage<>(brewCompletedMessage);
        brewCompletedNotifyUserChannel.send(message);
    }

    @Override
    public void notifyOrder(OrderId orderId) {
        logger.info("Notify order: %s".formatted(orderId));
        var brewCompletedMessage = new BrewCompletedMessage(orderId);
        var message = new GenericMessage<>(brewCompletedMessage);
        brewCompletedNotifyOrderChannel.send(message);
    }
}

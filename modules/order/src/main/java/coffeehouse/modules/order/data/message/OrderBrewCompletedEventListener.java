package coffeehouse.modules.order.data.message;

import coffeehouse.modules.order.domain.message.BrewCompletedEventMessage;
import coffeehouse.modules.order.domain.service.BrewOrderCompleted;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Component
public class OrderBrewCompletedEventListener {

    private final BrewOrderCompleted brewOrderCompleted;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public OrderBrewCompletedEventListener(BrewOrderCompleted brewOrderCompleted, ObjectMapper objectMapper) {
        this.brewOrderCompleted = Objects.requireNonNull(brewOrderCompleted, "BrewOrderCompleted must not be null");
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper must not be null");
    }
    @ServiceActivator(inputChannel = "brewCompletedEventPublishSubscribeChannel")
    public void brewRequestMessageHandler(String payload) {
        logger.info("Receive Order brew-completed-event-message: %s".formatted(payload));

        BrewCompletedEventMessage brewCompletedEventMessage;
        try {
            brewCompletedEventMessage = objectMapper.readValue(payload, BrewCompletedEventMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        brewOrderCompleted.changeOrderStatus(brewCompletedEventMessage.orderId());
    }
}

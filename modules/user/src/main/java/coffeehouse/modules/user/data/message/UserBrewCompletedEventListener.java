package coffeehouse.modules.user.data.message;

import coffeehouse.modules.user.domain.message.BrewCompletedEventMessage;
import coffeehouse.modules.user.domain.service.UserBrewCompleted;
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
public class UserBrewCompletedEventListener {

    private final UserBrewCompleted userBrewCompleted;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public UserBrewCompletedEventListener(UserBrewCompleted userBrewCompleted, ObjectMapper objectMapper) {
        this.userBrewCompleted = Objects.requireNonNull(userBrewCompleted, "UserBrewCompleted must not be null");
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper must not be null");
    }

    @ServiceActivator(inputChannel = "brewCompletedEventPublishSubscribeChannel")
    public void brewRequestMessageHandler(String payload) {
        logger.info("Receive User brew-completed-event-message: %s".formatted(payload));

        BrewCompletedEventMessage brewCompletedEventMessage;
        try {
            brewCompletedEventMessage = objectMapper.readValue(payload, BrewCompletedEventMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        userBrewCompleted.notify(brewCompletedEventMessage.orderId());
    }
}

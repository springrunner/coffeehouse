package coffeehouse.modules.brew.data.message;

import coffeehouse.modules.brew.domain.service.OrderSheetSubmission;
import coffeehouse.modules.order.domain.message.BrewRequestCommandMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Component
public class BrewRequestConsumer {

    private final OrderSheetSubmission orderSheetSubmission;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public BrewRequestConsumer(OrderSheetSubmission orderSheetSubmission) {
        this.orderSheetSubmission = Objects.requireNonNull(orderSheetSubmission, "OrderSheetSubmission must not be null");
    }

    @ServiceActivator(inputChannel = "brewRequestChannel")
    public void brewRequestMessageHandler(BrewRequestCommandMessage brewRequestCommandMessage) {
        logger.info("Receive brew-request-command-message: %s".formatted(brewRequestCommandMessage));
        orderSheetSubmission.submit(new OrderSheetSubmission.OrderSheetForm(brewRequestCommandMessage.orderId()));
    }
}
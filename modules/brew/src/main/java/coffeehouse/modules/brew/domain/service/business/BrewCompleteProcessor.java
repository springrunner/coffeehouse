package coffeehouse.modules.brew.domain.service.business;

import coffeehouse.modules.brew.domain.service.BrewComplete;
import coffeehouse.modules.brew.domain.service.OrderCounter;
import coffeehouse.modules.brew.domain.service.Notification;
import coffeehouse.modules.order.domain.OrderId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
public class BrewCompleteProcessor implements BrewComplete {

    private final Notification notification;

    public BrewCompleteProcessor(Notification notification) {
        this.notification = Objects.requireNonNull(notification, "Notification must not be null");
    }

    @Override
    public void complete(OrderId orderId) {
        // ----------------------------------------------------------
        // Verify brew information with the order iD and completed it
        // ----------------------------------------------------------
        notification.notify(orderId);
    }
}

package coffeehouse.modules.user.domain.service.business;

import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.user.domain.service.UserBrewCompleted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
public class UserBrewCompletedProcessor implements UserBrewCompleted {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void notify(OrderId orderId) {
        logger.info("Notify user-brew-complete: %s".formatted(orderId));

        // ----------------------------------------------------------
        // Verify order information with the order iD and notify completed brew to user
        // ----------------------------------------------------------
    }
}

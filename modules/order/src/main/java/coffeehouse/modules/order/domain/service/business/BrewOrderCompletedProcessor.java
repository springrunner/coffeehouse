package coffeehouse.modules.order.domain.service.business;

import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.service.BrewOrderCompleted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
public class BrewOrderCompletedProcessor implements BrewOrderCompleted {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void changeOrderStatus(OrderId orderId) {
        logger.info("Change order-status: %s".formatted(orderId));

        // ----------------------------------------------------------
        // Verify order information with the order iD and change status as brew completed
        // ----------------------------------------------------------
    }
}

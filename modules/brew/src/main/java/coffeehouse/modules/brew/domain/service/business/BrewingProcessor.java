package coffeehouse.modules.brew.domain.service.business;

import coffeehouse.modules.brew.domain.service.OrderSheetSubmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
class BrewingProcessor implements OrderSheetSubmission {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void submit(OrderSheetForm orderSheetForm) {
        logger.info("Submitted order-sheet: %s".formatted(orderSheetForm));

        // ----------------------------------------------------------
        // Confirm the order-sheet and start making drinks
        // ----------------------------------------------------------
    }
}

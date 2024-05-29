package coffeehouse.modules.brew.domain.service.business;

import coffeehouse.modules.brew.domain.OrderSheetId;
import coffeehouse.modules.brew.domain.entity.OrderSheet;
import coffeehouse.modules.brew.domain.entity.OrderSheetRepository;
import coffeehouse.modules.brew.domain.service.OrderSheetSubmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
class BrewingProcessor implements OrderSheetSubmission {

    private final OrderSheetRepository orderSheetRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    BrewingProcessor(OrderSheetRepository orderSheetRepository) {
        this.orderSheetRepository = orderSheetRepository;
    }

    @Override
    public void submit(OrderSheetForm orderSheetForm) {
        logger.info("Submitted order-sheet: %s".formatted(orderSheetForm));
        var orderSheetId = new OrderSheetId(UUID.randomUUID().toString());
        var orderSheet = OrderSheet.create(orderSheetId, orderSheetForm.orderId());
        orderSheetRepository.save(orderSheet);
    }
}

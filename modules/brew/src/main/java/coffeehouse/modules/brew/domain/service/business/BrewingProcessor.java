package coffeehouse.modules.brew.domain.service.business;

import coffeehouse.modules.brew.domain.OrderSheetId;
import coffeehouse.modules.brew.domain.OrderSheetItemId;
import coffeehouse.modules.brew.domain.entity.OrderSheet;
import coffeehouse.modules.brew.domain.entity.OrderSheetItem;
import coffeehouse.modules.brew.domain.entity.OrderSheetItems;
import coffeehouse.modules.brew.domain.entity.OrderSheetRepository;
import coffeehouse.modules.brew.domain.service.OrderSheetConfirmation;
import coffeehouse.modules.brew.domain.service.OrderSheetSubmission;
import coffeehouse.modules.brew.domain.service.UnidentifiedOrderSheetException;
import coffeehouse.modules.order.domain.OrderId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
class BrewingProcessor implements OrderSheetSubmission, OrderSheetConfirmation {
    
    private final OrderSheetRepository orderSheetRepository;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BrewingProcessor(OrderSheetRepository orderSheetRepository) {
        this.orderSheetRepository = Objects.requireNonNull(orderSheetRepository, "OrderSheetRepository must not be null");
    }

    @Override
    public void submitOrderSheet(OrderId orderId, Set<OrderLine> orderLines) {
        var orderSheetId = new OrderSheetId(UUID.randomUUID().toString());
        var orderSheet = OrderSheet.submit(
                orderSheetId,
                orderId,
                OrderSheetItems.wrap(
                        orderLines.stream().map(it -> OrderSheetItem.submit(
                                new OrderSheetItemId(UUID.randomUUID().toString()),
                                orderSheetId,
                                it.orderItemId(),
                                it.orderProductId(),
                                it.orderQuantity()
                        )).collect(Collectors.toSet())
                )
        );
        
        orderSheetRepository.save(orderSheet);
        logger.info("Submitted order-sheet: {}", orderSheet);
    }

    @Override
    public void confirmOrderSheet(OrderSheetId orderSheetId, Set<OrderSheetItemId> orderSheetItemIds) {
        var confirmedOrderSheet = orderSheetRepository.findById(orderSheetId)
                .map(it -> it.confirm(orderSheetItemIds))
                .orElseThrow(() -> UnidentifiedOrderSheetException.ofOrderSheetId(orderSheetId));

        orderSheetRepository.save(confirmedOrderSheet);
        logger.info("Confirmed order-sheet: {}", confirmedOrderSheet);
    }
}

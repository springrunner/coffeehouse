package coffeehouse.modules.brew.domain.service.business;

import coffeehouse.modules.brew.domain.OrderSheetId;
import coffeehouse.modules.brew.domain.entity.OrderSheet;
import coffeehouse.modules.brew.domain.entity.OrderSheetRepository;
import coffeehouse.modules.brew.domain.entity.OrderSheetStatus;
import coffeehouse.modules.brew.domain.service.OrderSheets;
import coffeehouse.modules.brew.domain.service.UnidentifiedOrderSheetException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
class OrderSheetsService implements OrderSheets {
   
    private final OrderSheetRepository orderSheetRepository;

    public OrderSheetsService(OrderSheetRepository orderSheetRepository) {
        this.orderSheetRepository = Objects.requireNonNull(orderSheetRepository, "OrderSheetRepository must not be null");
    }

    @Override
    public List<OrderSheetDetails> fetchOrderSheetsForReview() {
        return StreamSupport.stream(orderSheetRepository.findAll().spliterator(), false)
                .filter(it -> it.getStatus() == OrderSheetStatus.SUBMITTED)
                .map(this::mapOrderSheetDetails)
                .toList();
    }

    @Override
    public Optional<OrderSheetDetails> getOrderSheetDetails(OrderSheetId orderSheetId) {
        return orderSheetRepository.findById(orderSheetId)
                .map(it -> Optional.of(mapOrderSheetDetails(it)))
                .orElseThrow(() -> UnidentifiedOrderSheetException.ofOrderSheetId(orderSheetId));
    }
    
    private OrderSheetDetails mapOrderSheetDetails(OrderSheet orderSheet) {
        var items = StreamSupport.stream(orderSheet.getOrderSheetItems().spliterator(), false)
                .map(it -> new OrderSheetLine(it.id(), it.orderItemId(), it.orderProductId(), it.orderQuantity()))
                .toList();
                
        return new OrderSheetDetails(
                orderSheet.getId(),
                orderSheet.getOrderId(),
                items,
                orderSheet.getStatus(),
                orderSheet.getSubmittedAt(),
                orderSheet.getConfirmedAt().orElse(null),
                orderSheet.getProcessedAt().orElse(null),
                orderSheet.getRefusedAt().orElse(null)
        );
    }
}

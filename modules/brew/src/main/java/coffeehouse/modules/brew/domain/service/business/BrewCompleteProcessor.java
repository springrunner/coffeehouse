package coffeehouse.modules.brew.domain.service.business;

import coffeehouse.modules.brew.domain.entity.OrderSheetNotFoundException;
import coffeehouse.modules.brew.domain.entity.OrderSheetRepository;
import coffeehouse.modules.brew.domain.service.BrewComplete;
import coffeehouse.modules.brew.domain.service.BrewNotifier;
import coffeehouse.modules.brew.domain.OrderId;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
public class BrewCompleteProcessor implements BrewComplete {

    private final OrderSheetRepository orderSheetRepository;
    private final BrewNotifier brewNotifier;

    public BrewCompleteProcessor(BrewNotifier brewNotifier, OrderSheetRepository orderSheetRepository) {
        this.orderSheetRepository = Objects.requireNonNull(orderSheetRepository, "OrderSheetRepository must not be null");
        this.brewNotifier = Objects.requireNonNull(brewNotifier, "BrewNotifier must not be null");
    }

    @Override
    public void complete(OrderId orderId) {
        // ----------------------------------------------------------
        // Verify brew information with the order iD and completed it
        // ----------------------------------------------------------
        var orderSheet = orderSheetRepository.findByOrderId(orderId).orElseThrow(OrderSheetNotFoundException::new);
        orderSheet.process();
        brewNotifier.notify(orderId);
    }
}

package coffeehouse.modules.order.domain.service.business;

import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.service.BarCounter;
import coffeehouse.modules.order.domain.service.OrderAcceptance;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
class OrderAcceptanceProcessor implements OrderAcceptance {

    private final BarCounter barCounter;
    
    public OrderAcceptanceProcessor(BarCounter barCounter) {
        this.barCounter = Objects.requireNonNull(barCounter, "BarCounter must not be null");
    }

    @Override
    public void acceptOrder(OrderId orderId) {
        // ----------------------------------------------------------
        // Verify order information with the order iD and accept it
        // ----------------------------------------------------------
        
        barCounter.brew(orderId);
    }
}

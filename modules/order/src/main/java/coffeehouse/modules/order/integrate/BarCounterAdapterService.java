package coffeehouse.modules.order.integrate;

import coffeehouse.modules.order.domain.OrderException;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.service.BarCounter;
import org.springframework.stereotype.Service;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
class BarCounterAdapterService implements BarCounter {

    @Override
    public void brew(OrderId orderId) {
        throw new OrderException("NotImplemented: failed to brew");
    }
}

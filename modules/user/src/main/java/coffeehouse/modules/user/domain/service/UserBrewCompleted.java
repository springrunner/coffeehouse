package coffeehouse.modules.user.domain.service;

import coffeehouse.modules.order.domain.OrderId;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface UserBrewCompleted {

    void notify(OrderId orderId);
}

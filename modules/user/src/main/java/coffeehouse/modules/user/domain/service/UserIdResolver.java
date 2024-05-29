package coffeehouse.modules.user.domain.service;

import coffeehouse.modules.user.domain.OrderId;
import coffeehouse.modules.user.domain.UserAccountId;
/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface UserIdResolver {
    UserAccountId resolveUserAccountIdByOrderId(OrderId orderId);
}

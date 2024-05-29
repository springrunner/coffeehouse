package coffeehouse.modules.user.domain.service.business;

import coffeehouse.modules.user.domain.OrderId;
import coffeehouse.modules.user.domain.entity.UserAccountNotFoundException;
import coffeehouse.modules.user.domain.entity.UserAccountRepository;
import coffeehouse.modules.user.domain.service.UserBrewCompleted;
import coffeehouse.modules.user.domain.service.UserIdResolver;
import coffeehouse.modules.user.domain.service.UserNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
public class UserBrewCompletedProcessor implements UserBrewCompleted {

    private final UserAccountRepository userAccountRepository;
    private final UserIdResolver userIdResolver;
    private final UserNotifier userNotifier;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public UserBrewCompletedProcessor(UserAccountRepository userAccountRepository, UserIdResolver userIdResolver, UserNotifier userNotifier) {
        this.userAccountRepository = Objects.requireNonNull(userAccountRepository, "UserAccountRepository must not be null");
        this.userIdResolver = Objects.requireNonNull(userIdResolver, "UserIdResolver must not be null");
        this.userNotifier = Objects.requireNonNull(userNotifier, "UserNotifier must not be null");
    }

    @Override
    public void notify(OrderId orderId) {
        logger.info("Notify user-brew-complete: %s".formatted(orderId));
        var userAccountId = userIdResolver.resolveUserAccountIdByOrderId(orderId);
        var userAccount = userAccountRepository.findById(userAccountId).orElseThrow(UserAccountNotFoundException::new);
        userNotifier.notify(userAccount);
    }
}

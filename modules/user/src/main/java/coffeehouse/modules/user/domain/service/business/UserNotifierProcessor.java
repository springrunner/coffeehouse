package coffeehouse.modules.user.domain.service.business;

import coffeehouse.modules.user.domain.entity.UserAccount;
import coffeehouse.modules.user.domain.service.UserNotifier;
import org.springframework.stereotype.Service;

@Service
public class UserNotifierProcessor implements UserNotifier {
    @Override
    public void notify(UserAccount userAccount) {
        // ----------------------------------------------------------
        // Notify user
        // ----------------------------------------------------------
    }
}

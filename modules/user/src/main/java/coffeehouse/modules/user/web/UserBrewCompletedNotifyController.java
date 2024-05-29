package coffeehouse.modules.user.web;

import coffeehouse.modules.user.domain.OrderId;
import coffeehouse.modules.user.domain.service.UserBrewCompleted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@RestController
@RequestMapping("/user/brew-completed")
public class UserBrewCompletedNotifyController {

    private final UserBrewCompleted userBrewCompleted;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public UserBrewCompletedNotifyController(UserBrewCompleted userBrewCompleted) {
        this.userBrewCompleted = Objects.requireNonNull(userBrewCompleted, "UserBrewComplete must not be null");
    }

    @PostMapping
    public ResponseEntity<Void> notifyCompletedBrewToUser(@RequestBody UserBrewCompleteCommand command) {
        logger.info("Receive command requests: {}", command);

        // ----------------------------------------------------------
        // Validate order information and submit order-sheet
        // ----------------------------------------------------------

        userBrewCompleted.notify(command.orderId());

        return ResponseEntity.accepted().build();
    }

    public record UserBrewCompleteCommand(OrderId orderId) {
    }
}

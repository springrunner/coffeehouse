package coffeehouse.modules.brew.data.http;

import coffeehouse.modules.brew.domain.service.Notification;
import coffeehouse.modules.order.domain.OrderId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Component
public class NotificationHttpClient implements Notification {
    private final RestTemplate restTemplate;
    private final URI brewCompletedNotificationUserUri;
    private final URI brewCompletedNotificationOrderUri;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public NotificationHttpClient(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = Objects.requireNonNull(restTemplate, "RestTemplate must not be null");
        this.brewCompletedNotificationUserUri = environment.getRequiredProperty("coffeehouse.user.notify-brew-complete-uri", URI.class);
        this.brewCompletedNotificationOrderUri = environment.getRequiredProperty("coffeehouse.brew.notify-brew-complete-uri", URI.class);
    }

    @Override
    public void notifyUser(OrderId orderId) {
        logger.info("notify brew-completed using User Web API, orderId: {}", orderId);

        var request = RequestEntity.post(brewCompletedNotificationUserUri).body(
                new NotifyBrewCompleteRequest(orderId)
        );
        restTemplate.exchange(request, Void.class);
    }

    @Override
    public void notifyOrder(OrderId orderId) {
        logger.info("notify brew-completed using Order Web API, orderId: {}", orderId);

        var request = RequestEntity.post(brewCompletedNotificationOrderUri).body(
                new NotifyBrewCompleteRequest(orderId)
        );
        restTemplate.exchange(request, Void.class);
    }

    public record NotifyBrewCompleteRequest(OrderId orderId) {
    }
}

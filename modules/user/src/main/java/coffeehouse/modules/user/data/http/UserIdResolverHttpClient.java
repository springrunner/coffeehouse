package coffeehouse.modules.user.data.http;

import coffeehouse.modules.user.domain.OrderId;
import coffeehouse.modules.user.domain.UserAccountId;
import coffeehouse.modules.user.domain.service.UserIdResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Component
class UserIdResolverHttpClient implements UserIdResolver {

    private final RestTemplate restTemplate;
    private final URI findOrderInfoUri;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public UserIdResolverHttpClient(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = Objects.requireNonNull(restTemplate, "RestTemplate must not be null");
        this.findOrderInfoUri = environment.getRequiredProperty("coffeehouse.order.search-order-uri", URI.class);
    }

    @Override
    public UserAccountId resolveUserAccountIdByOrderId(OrderId orderId) {
        logger.info("Find order using Web API, orderId: {}", orderId);
        var order = restTemplate.getForObject(findOrderInfoUri + "/" + orderId.value(), Order.class);
        return order.ordererId;
    }

    public record Order(OrderId id, UserAccountId ordererId) {
    }
}

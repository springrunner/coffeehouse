package coffeehouse.modules.order.data.http;

import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.service.BarCounter;
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
class BarCounterHttpClient implements BarCounter {
    
    private final RestTemplate restTemplate;
    private final URI submitOrderSheetUri;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BarCounterHttpClient(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = Objects.requireNonNull(restTemplate, "RestTemplate must not be null");
        this.submitOrderSheetUri = environment.getRequiredProperty("coffeehouse.order.submit-order-sheet-uri", URI.class);
    }
    
    @Override
    public void brew(OrderId orderId) {
        logger.info("Submit order-sheet using Web API, orderId: {}", orderId);
        
        var request = RequestEntity.post(submitOrderSheetUri).body(
                new SubmitOrderSheetRequest(orderId)
        );
        restTemplate.exchange(request, Void.class);
    }

    public record SubmitOrderSheetRequest(OrderId orderId) {
    }
}

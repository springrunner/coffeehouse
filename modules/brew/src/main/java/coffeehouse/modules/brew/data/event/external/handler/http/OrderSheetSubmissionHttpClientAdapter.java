package coffeehouse.modules.brew.data.event.external.handler.http;

import coffeehouse.modules.brew.data.event.external.OrderAcceptedEvent;
import coffeehouse.modules.brew.domain.service.OrderSheetSubmission;
import coffeehouse.modules.brew.domain.service.UnidentifiedOrderException;
import coffeehouse.modules.catalog.domain.ProductId;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.OrderItemId;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author springrunner.kr@gmail.com
 */
@Component
class OrderSheetSubmissionHttpClientAdapter {

    private final OrderSheetSubmission orderSheetSubmission;
    private final RestOperations restOperations;
    private final URI orderServerUri;

    public OrderSheetSubmissionHttpClientAdapter(OrderSheetSubmission orderSheetSubmission, RestOperations restOperations, Environment environment) {
        this.orderSheetSubmission = Objects.requireNonNull(orderSheetSubmission, "OrderSheetSubmission must not be null");
        this.restOperations = Objects.requireNonNull(restOperations, "RestOperations must not be null");
        this.orderServerUri = environment.getRequiredProperty("coffeehouse.order.server-uri", URI.class);
    }

    @EventListener
    public void on(OrderAcceptedEvent orderAcceptedEvent) {
        var getOrderDetailsUri = UriComponentsBuilder.fromUri(orderServerUri).path("/orders/{id}").build(orderAcceptedEvent.orderId());
        var responseEntity = restOperations.getForEntity(getOrderDetailsUri, OrderDetails.class);
        
        var orderDetails = responseEntity.getBody();
        if (Objects.isNull(orderDetails)) {
            throw new UnidentifiedOrderException("Unidentified order(id: %s)".formatted(orderAcceptedEvent.orderId()));
        }
        
        orderSheetSubmission.submitOrderSheet(
                orderDetails.orderId(),
                orderDetails.orderLines().stream()
                        .map(it -> new OrderSheetSubmission.OrderLine(it.orderItemId(), it.orderProductId(), it.orderQuantity()))
                        .collect(Collectors.toSet())
        );
    }

    record OrderDetails(OrderId orderId, List<OrderLine> orderLines) {
    }

    record OrderLine(OrderItemId orderItemId, ProductId orderProductId, int orderQuantity) {
    }
}

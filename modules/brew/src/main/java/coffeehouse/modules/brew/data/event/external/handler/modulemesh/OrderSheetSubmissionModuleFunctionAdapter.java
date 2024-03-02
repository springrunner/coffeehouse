package coffeehouse.modules.brew.data.event.external.handler.modulemesh;

import coffeehouse.libraries.modulemesh.function.ModuleFunctionOperations;
import coffeehouse.modules.brew.data.event.external.OrderAcceptedEvent;
import coffeehouse.modules.brew.domain.service.OrderSheetSubmission;
import coffeehouse.modules.brew.domain.service.UnidentifiedOrderException;
import coffeehouse.modules.catalog.domain.ProductId;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.OrderItemId;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author springrunner.kr@gmail.com
 */
@Component
class OrderSheetSubmissionModuleFunctionAdapter {

    private final OrderSheetSubmission orderSheetSubmission;
    private final ModuleFunctionOperations moduleFunctionOperations;

    public OrderSheetSubmissionModuleFunctionAdapter(OrderSheetSubmission orderSheetSubmission, ModuleFunctionOperations moduleFunctionOperations) {
        this.orderSheetSubmission = Objects.requireNonNull(orderSheetSubmission, "OrderSheetSubmission must not be null");
        this.moduleFunctionOperations = Objects.requireNonNull(moduleFunctionOperations, "ModuleFunctionOperations must not be null");
    }

    @EventListener
    public void on(OrderAcceptedEvent orderAcceptedEvent) {
        var orderDetails = moduleFunctionOperations.execute(
                "orders/get-order-details", 
                orderAcceptedEvent.orderId(), 
                OrderDetails.class
        );
        if (orderDetails == null) {
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

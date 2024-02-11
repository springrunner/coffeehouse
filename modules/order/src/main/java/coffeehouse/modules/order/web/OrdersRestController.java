package coffeehouse.modules.order.web;

import coffeehouse.contracts.order.web.OrdersApi;
import coffeehouse.contracts.order.web.model.*;
import coffeehouse.libraries.security.web.context.UserPrincipalContextHolder;
import coffeehouse.modules.catalog.domain.ProductCode;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.service.*;
import coffeehouse.modules.user.domain.UserAccountId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@RestController
public class OrdersRestController implements OrdersApi {

    private final Orders orders;
    private final OrderPlacement orderPlacement;
    private final OrderAcceptance orderAcceptance;
    private final OrderCancellation orderCancellation;

    public OrdersRestController(
            Orders orders,
            OrderPlacement orderPlacement,
            OrderAcceptance orderAcceptance,
            OrderCancellation orderCancellation
    ) {
        this.orders = Objects.requireNonNull(orders, "Orders must not be null");
        this.orderPlacement = Objects.requireNonNull(orderPlacement, "OrderPlacement must not be null");
        this.orderAcceptance = Objects.requireNonNull(orderAcceptance, "OrderAcceptance must not be null");
        this.orderCancellation = Objects.requireNonNull(orderCancellation, "OrderCancellation must not be null");
    }

    @Override
    public ResponseEntity<OrderDetails> getOrderDetails(String orderId) {
        return buildOrderDetailsResponse(orderId);
    }

    @Override
    public ResponseEntity<OrderDetails> placeOrder(PlaceOrderRequest request) {
        var principal = UserPrincipalContextHolder.<UserAccountId>currentAuthentication().principal();
        var orderLines = request.getOrderLines().stream()
                .map(it -> new OrderPlacement.OrderLine(new ProductCode(it.getProductCode()), it.getQuantity()))
                .toList();
        
        var orderId = orderPlacement.placeOrder(principal, orderLines);
        
        return buildOrderDetailsResponse(orderId.toString());
    }

    @Override
    public ResponseEntity<Void> acceptOrder(String orderId) {
        orderAcceptance.acceptOrder(new OrderId(orderId));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> cancelOrder(String orderId, CancelOrderRequest request) {
        orderCancellation.cancelOrder(new OrderId(orderId), request.getCancellationReason());
        return ResponseEntity.ok().build();
    }    

    private ResponseEntity<OrderDetails> buildOrderDetailsResponse(String orderId) {
        return orders.getOrderDetails(new OrderId(orderId)).map(order -> {
            var orderStatus = new OrderStatus().code(order.status().getCode()).text(order.status().getText()).description(order.status().getDescription());
            var orderLines = order.orderLines().stream().map(it -> new OrderLine().orderItemId(it.orderItemId().toString()).orderProductId(it.orderProductId().toString()).orderQuantity(it.orderQuantity())).toList();
            var orderDetails = new OrderDetails().orderId(order.orderId().toString()).orderLines(orderLines).orderStatus(orderStatus);
            return ResponseEntity.ok(orderDetails);
        }).orElseThrow(() -> UnidentifiedOrderException.ofOrderId(orderId));
    }
}

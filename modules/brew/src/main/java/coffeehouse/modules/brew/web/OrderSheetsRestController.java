package coffeehouse.modules.brew.web;

import coffeehouse.contracts.brew.web.OrdersheetsApi;
import coffeehouse.contracts.brew.web.model.ConfirmOrderSheetRequest;
import coffeehouse.contracts.brew.web.model.ListOrderSheets200Response;
import coffeehouse.contracts.brew.web.model.OrderSheetDetails;
import coffeehouse.contracts.brew.web.model.OrderSheetStatus;
import coffeehouse.modules.brew.domain.OrderSheetId;
import coffeehouse.modules.brew.domain.OrderSheetItemId;
import coffeehouse.modules.brew.domain.service.OrderSheetConfirmation;
import coffeehouse.modules.brew.domain.service.OrderSheets;
import coffeehouse.modules.brew.domain.service.UnidentifiedOrderSheetException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author springrunner.kr@gmail.com
 */
@RestController
public class OrderSheetsRestController implements OrdersheetsApi {
    
    private final OrderSheets orderSheets;
    private final OrderSheetConfirmation orderSheetConfirmation;

    public OrderSheetsRestController(OrderSheets orderSheets, OrderSheetConfirmation orderSheetConfirmation) {
        this.orderSheets = Objects.requireNonNull(orderSheets, "OrderSheets must not be null");
        this.orderSheetConfirmation = Objects.requireNonNull(orderSheetConfirmation, "OrderSheetConfirmation must not be null");
    }

    @Override
    public ResponseEntity<ListOrderSheets200Response> listOrderSheets() {
        var content = orderSheets.fetchOrderSheetsForReview().stream().map(this::buildOrderSheetDetails).toList();
        
        return ResponseEntity.ok(new ListOrderSheets200Response().content(content));
    }

    @Override
    public ResponseEntity<OrderSheetDetails> getOrderSheetDetails(String orderSheetId) {
        var body = orderSheets.getOrderSheetDetails(new OrderSheetId(orderSheetId))
                .map(this::buildOrderSheetDetails)
                .orElseThrow(() -> UnidentifiedOrderSheetException.ofOrderSheetId(orderSheetId));
        
        return ResponseEntity.ok(body);
    }

    @Override
    public ResponseEntity<Void> confirmOrderSheet(String orderSheetId, ConfirmOrderSheetRequest request) {
        orderSheetConfirmation.confirmOrderSheet(
                new OrderSheetId(orderSheetId),
                request.getSelectedOrderSheetItemIds().stream().map(OrderSheetItemId::new).collect(Collectors.toSet())
        );
        
        return ResponseEntity.ok().build();
    }

    private OrderSheetDetails buildOrderSheetDetails(OrderSheets.OrderSheetDetails source) {
        return new OrderSheetDetails()
                .orderSheetId(source.orderSheetId().toString())
                .orderStatus(
                        new OrderSheetStatus()
                                .code(source.status().getCode())
                                .text(source.status().toString())
                                .description(source.status().getDescription())
                );
    }
}

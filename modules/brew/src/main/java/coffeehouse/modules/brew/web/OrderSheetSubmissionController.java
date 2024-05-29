package coffeehouse.modules.brew.web;

import coffeehouse.modules.brew.domain.OrderId;
import coffeehouse.modules.brew.domain.service.OrderSheetSubmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/order-sheet")
public class OrderSheetSubmissionController {

    private final OrderSheetSubmission orderSheetSubmission;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public OrderSheetSubmissionController(OrderSheetSubmission orderSheetSubmission) {
        this.orderSheetSubmission = Objects.requireNonNull(orderSheetSubmission, "OrderSheetSubmission must not be null");
    }
    
    @PostMapping
    public ResponseEntity<Void> submitOrderSheet(@RequestBody SubmitOrderSheetCommand command) {
        logger.info("Receive command requests: {}", command);
        
        // ----------------------------------------------------------
        // Validate order information and submit order-sheet
        // ----------------------------------------------------------
        
        orderSheetSubmission.submit(new OrderSheetSubmission.OrderSheetForm(command.orderId()));
        
        return ResponseEntity.accepted().build();
    }
    
    public record SubmitOrderSheetCommand(OrderId orderId) {
    } 
}

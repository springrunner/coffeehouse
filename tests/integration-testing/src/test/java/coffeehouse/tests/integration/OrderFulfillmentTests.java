package coffeehouse.tests.integration;

import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.service.OrderAcceptance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

/**
 * @author springrunner.kr@gmail.com
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"coffeehouse.order.submit-order-sheet-uri=http://localhost:8080/order-sheet"})
class OrderFulfillmentTests {

    @Autowired
    OrderAcceptance orderAcceptance;
    
    @Test
    void acceptOrder() {
        orderAcceptance.acceptOrder(new OrderId(UUID.randomUUID().toString()));
    }
}

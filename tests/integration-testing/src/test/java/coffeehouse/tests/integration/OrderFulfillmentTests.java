package coffeehouse.tests.integration;

import coffeehouse.modules.brew.domain.service.OrderSheetSubmission;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.service.OrderAcceptance;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;

/**
 * @author springrunner.kr@gmail.com
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class OrderFulfillmentTests {

    @Autowired
    OrderAcceptance orderAcceptance;

    @MockBean
    OrderSheetSubmission orderSheetSubmission;

    @Test
    void acceptOrder() {
        // given
        var orderId = new OrderId(UUID.randomUUID().toString());

        // when
        orderAcceptance.acceptOrder(orderId);

        // then
        Awaitility.await()
                .pollDelay(1, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .atMost(3, TimeUnit.SECONDS).untilAsserted(() -> {
                    Mockito.verify(orderSheetSubmission, Mockito.times(1)).submit(any());
                });
    }
}

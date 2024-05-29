package coffeehouse.tests.integration;

import coffeehouse.modules.brew.domain.entity.OrderSheetRepository;
import coffeehouse.modules.brew.domain.entity.OrderSheetStatus;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.entity.OrderRepository;
import coffeehouse.modules.order.domain.entity.OrderStatus;
import coffeehouse.modules.order.domain.service.OrderAcceptance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author springrunner.kr@gmail.com
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class OrderFulfillmentTests {

    @Autowired
    OrderAcceptance orderAcceptance;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderSheetRepository orderSheetRepository;

    @Test
    void acceptOrder() {
        // given
        var orderId = new OrderId("7438b60b-7c68-4d55-a033-fa933e92832c");

        // when
        orderAcceptance.acceptOrder(orderId);

        // then
        var order = orderRepository.findById(orderId).orElse(null);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ACCEPTED);

        var orderSheet = orderSheetRepository.findByOrderId(new coffeehouse.modules.brew.domain.OrderId("7438b60b-7c68-4d55-a033-fa933e92832c")).orElse(null);
        assertThat(orderSheet.getOrderSheetStatus()).isEqualTo(OrderSheetStatus.SUBMITTED);
    }
}

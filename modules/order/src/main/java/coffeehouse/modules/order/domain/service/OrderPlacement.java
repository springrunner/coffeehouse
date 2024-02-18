package coffeehouse.modules.order.domain.service;

import coffeehouse.libraries.base.lang.Email;
import coffeehouse.modules.catalog.domain.ProductCode;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.entity.OrderItem;
import coffeehouse.modules.user.domain.UserAccountId;

import java.util.List;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface OrderPlacement {

    /**
     * Receives an order from a customer.
     *
     * @param customerId The ID of the customer placing the order
     * @param orderLines The details of the order being placed
     * @return The ID of the placed order
     */
    OrderId placeOrder(UserAccountId customerId, List<OrderLine> orderLines);

    record OrderLine(ProductCode productCode, int quantity) {
    }

    @FunctionalInterface
    interface OrdererVerification {

        /**
         * 주문자 확인하기
         *
         * @param customerId 고객 ID
         * @return 확인된 주문자 객체
         */
        Optional<Orderer> verify(UserAccountId customerId);

        record Orderer(UserAccountId id, Email email) {
        }
    }

    @FunctionalInterface
    interface OrderItemFactory {

        /**
         * 주문 항목 생성하기
         *
         * @param orderId       주문 ID
         * @param productCode   상품 Code 
         * @param orderQuantity 주문 수량
         * @return
         */
        OrderItem create(OrderId orderId, ProductCode productCode, int orderQuantity);
    }
}

package coffeehouse.modules.order.domain.service;

import coffeehouse.modules.order.domain.OrderId;

/**
 * @author springrunner.kr@gmail.com
 */
public interface BarCounter {

    /**
     * 음료 제조 요청하기
     *
     * @param orderId 주문 ID
     */
    void brew(OrderId orderId);
}

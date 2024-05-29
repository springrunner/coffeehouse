package coffeehouse.modules.brew.domain.service;


import coffeehouse.modules.brew.domain.OrderId;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface OrderSheetSubmission {

    /**
     * Submits an order sheet.
     *
     * @param orderSheetForm The form of the order to submit
     */
    void submit(OrderSheetForm orderSheetForm);

    record OrderSheetForm(OrderId orderId) {
    }
}

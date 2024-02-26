package coffeehouse.modules.brew.domain.service;

import coffeehouse.modules.brew.domain.OrderSheetId;
import coffeehouse.modules.brew.domain.OrderSheetItemId;

import java.util.Set;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface OrderSheetConfirmation {

    /**
     * Confirms a received order sheet.
     *
     * @param orderSheetId The ID of the order sheet to confirm
     * @param  orderSheetItemIds The set of IDs of the order sheet items to confirm
     */
    void confirmOrderSheet(OrderSheetId orderSheetId, Set<OrderSheetItemId> orderSheetItemIds);
}

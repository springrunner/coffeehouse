package coffeehouse.modules.brew.domain.entity;

import coffeehouse.modules.brew.domain.OrderSheetId;
import coffeehouse.modules.order.domain.OrderId;

import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public interface OrderSheetRepository {

    /**
     * Saves an order-sheet to the repository.
     *
     * @param entity The order-sheet to save.
     * @return The saved order-sheet.
     */
    OrderSheet save(OrderSheet entity);

    /**
     * Finds an order-sheet by its ID.
     *
     * @param id The ID of the order-sheet to find
     * @return An Optional containing the found order-sheet, if present
     */
    Optional<OrderSheet> findById(OrderSheetId id);

    /**
     * Finds an order-sheet by the associated order ID.
     *
     * @param orderId The ID of the order associated with the order-sheet to find
     * @return An Optional containing the found order-sheet, if present
     */
    Optional<OrderSheet> findByOrderId(OrderId orderId);    

    /**
     * Retrieves all order-sheets from the repository.
     *
     * @return An Iterable containing all order-sheets in the repository
     */    
    Iterable<OrderSheet> findAll();
}

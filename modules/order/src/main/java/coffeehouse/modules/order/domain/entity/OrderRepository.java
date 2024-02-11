package coffeehouse.modules.order.domain.entity;

import coffeehouse.modules.order.domain.OrderId;

import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public interface OrderRepository {

    /**
     * Saves an order to the repository.
     *
     * @param entity The order to save
     * @return The saved order
     */
    Order save(Order entity);

    /**
     * Finds an order by its ID in the repository.
     *
     * @param id The ID of the order
     * @return An Optional containing the found order, if present
     */
    Optional<Order> findById(OrderId id);
}

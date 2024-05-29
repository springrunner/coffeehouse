package coffeehouse.modules.brew.data.repository;

import coffeehouse.modules.brew.domain.OrderId;
import coffeehouse.modules.brew.domain.entity.OrderSheet;
import coffeehouse.modules.brew.domain.entity.OrderSheetRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
@Repository
public class InMemoryOrderSheetRepository implements OrderSheetRepository {

    private final List<OrderSheet> orderSheets = new ArrayList<>();

    @Override
    public Optional<OrderSheet> findByOrderId(OrderId orderId) {
        return orderSheets.stream().filter(orderSheet -> orderSheet.getOrderId().equals(orderId)).findFirst();
    }

    @Override
    public OrderSheet save(OrderSheet orderSheet) {
        orderSheets.add(orderSheet);
        return orderSheet;
    }
}

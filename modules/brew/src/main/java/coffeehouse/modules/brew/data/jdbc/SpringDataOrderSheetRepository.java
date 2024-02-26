package coffeehouse.modules.brew.data.jdbc;

import coffeehouse.modules.brew.domain.OrderSheetId;
import coffeehouse.modules.brew.domain.entity.OrderSheet;
import coffeehouse.modules.brew.domain.entity.OrderSheetRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * @author springrunner.kr@gmail.com
 */
public interface SpringDataOrderSheetRepository extends OrderSheetRepository, CrudRepository<OrderSheet, OrderSheetId> {
}

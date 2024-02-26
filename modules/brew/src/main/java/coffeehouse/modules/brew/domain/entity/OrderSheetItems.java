package coffeehouse.modules.brew.domain.entity;

import coffeehouse.libraries.base.lang.IterableItem;
import coffeehouse.modules.brew.domain.OrderSheetItemId;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author springrunner.kr@gmail.com
 */
public class OrderSheetItems implements IterableItem<OrderSheetItem> {

    @MappedCollection(idColumn = "ORDER_SHEET_ID", keyColumn = "ID")
    private final Set<OrderSheetItem> items;

    OrderSheetItems(Set<OrderSheetItem> items) {
        this.items = Objects.requireNonNull(items, "items must not be null");
    }

    private OrderSheetItems reviseToSelectedItems(Set<OrderSheetItemId> itemIds, Function<OrderSheetItem, OrderSheetItem> action) {
        return OrderSheetItems.wrap(
                StreamSupport.stream(spliterator(), false).map(item -> {
                    if (itemIds.contains(item.id())) {
                        return action.apply(item);
                    } else {
                        return item;
                    }
                }).collect(Collectors.toSet())
        );
    }
    
    OrderSheetItems confirm(Set<OrderSheetItemId> itemIds) {
        return reviseToSelectedItems(itemIds, OrderSheetItem::confirm);
    }
    
    OrderSheetItems refuse(Set<OrderSheetItemId> itemIds) {
        return reviseToSelectedItems(itemIds, OrderSheetItem::refuse);
    }
    
    OrderSheetStatus resolveOrderSheetStatus() {
        if (items.stream().allMatch(it -> it.status() == OrderSheetItemStatus.SUBMITTED)) {
            return OrderSheetStatus.SUBMITTED;
        }
        
        if (items.stream().allMatch(it -> it.status() == OrderSheetItemStatus.CONFIRMED)) {
            return OrderSheetStatus.CONFIRMED;
        }
        
        if (items.stream().allMatch(it -> it.status() == OrderSheetItemStatus.REFUSED)) {
            return OrderSheetStatus.REFUSED;
        }
        
        if (items.stream().allMatch(it -> it.status() == OrderSheetItemStatus.PICKUP_COMPLETED || it.status() == OrderSheetItemStatus.REFUSED)) {
            return OrderSheetStatus.PROCESSED;
        }

        return OrderSheetStatus.PARTIALLY_CONFIRMED;
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public Iterator<OrderSheetItem> iterator() {
        return items.iterator();
    }

    public static OrderSheetItems wrap(Set<OrderSheetItem> items) {
        return new OrderSheetItems(Objects.requireNonNullElse(items, Collections.emptySet()));
    }
}

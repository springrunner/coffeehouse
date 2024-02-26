package coffeehouse.modules.order.data.modulemesh;

import coffeehouse.libraries.modulemesh.function.ModuleFunctionOperations;
import coffeehouse.modules.catalog.domain.ProductId;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.OrderItemId;
import coffeehouse.modules.order.domain.entity.OrderRepository;
import coffeehouse.modules.order.domain.service.BarCounter;
import coffeehouse.modules.order.domain.service.UnidentifiedOrderException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author springrunner.kr@gmail.com
 */
@Component
class BarCounterModuleFunctionAdapter implements BarCounter {

    private final OrderRepository orderRepository;
    private final ModuleFunctionOperations moduleFunctionOperations;

    public BarCounterModuleFunctionAdapter(OrderRepository orderRepository, ModuleFunctionOperations moduleFunctionOperations) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository must not be null");
        this.moduleFunctionOperations = Objects.requireNonNull(moduleFunctionOperations, "ModuleFunctionOperations must not be null");
    }

    @Override
    public void brew(OrderId orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new UnidentifiedOrderException("Unidentified order(id: %s)".formatted(orderId)));
        var form = new OrderSheetSubmissionRequest(
                order.getId(),
                StreamSupport.stream(order.getOrderItems().spliterator(), false)
                        .map(it -> new OrderLine(it.id(), it.productId(), it.quantity()))
                        .collect(Collectors.toSet())
        );

        moduleFunctionOperations.execute("ordersheets/submit-ordersheet", form, Void.class);
    }

    record OrderSheetSubmissionRequest(OrderId orderId, Set<OrderLine> orderLines) {
    }

    record OrderLine(OrderItemId orderItemId, ProductId orderProductId, int orderQuantity) {
    }
}

package coffeehouse.modules.order.data.modulemesh;

import coffeehouse.libraries.modulemesh.function.ModuleFunctionOperations;
import coffeehouse.modules.catalog.domain.ProductCode;
import coffeehouse.modules.catalog.domain.ProductId;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.OrderItemId;
import coffeehouse.modules.order.domain.entity.OrderItem;
import coffeehouse.modules.order.domain.service.OrderPlacement;
import coffeehouse.modules.order.domain.service.UnidentifiedProductException;
import coffeehouse.modules.user.domain.UserAccountId;
import org.springframework.stereotype.Component;

import javax.money.MonetaryAmount;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author springrunner.kr@gmail.com
 */
@Component
class OrderPlacementModuleFunctionAdapter implements OrderPlacement.OrdererVerification, OrderPlacement.OrderItemFactory {

    private final ModuleFunctionOperations moduleFunctionOperations;

    public OrderPlacementModuleFunctionAdapter(ModuleFunctionOperations moduleFunctionOperations) {
        this.moduleFunctionOperations = Objects.requireNonNull(moduleFunctionOperations);
    }

    @Override
    public Optional<Orderer> verify(UserAccountId customerId) {
        var orderer = moduleFunctionOperations.execute(
                "customers/get-customer-details",
                customerId,
                OrderPlacement.OrdererVerification.Orderer.class
        );

        return Optional.ofNullable(orderer);
    }

    @Override
    public OrderItem create(OrderId orderId, ProductCode productCode, int orderQuantity) {
        var productDetails = moduleFunctionOperations.execute(
                "catalogs/get-product-details", 
                productCode, 
                ProductDetails.class
        );
        if (Objects.isNull(productDetails)) {
            throw UnidentifiedProductException.ofProductCode(productCode);
        }

        return OrderItem.create(
                new OrderItemId(UUID.randomUUID().toString()),
                orderId,
                productDetails.id(),
                productDetails.price(),
                orderQuantity
        );
    }

    record ProductDetails(ProductId id, ProductCode code, String name, MonetaryAmount price) {
    }
}

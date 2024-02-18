package coffeehouse.modules.order.integrate;

import coffeehouse.modules.catalog.domain.ProductCode;
import coffeehouse.modules.catalog.domain.service.Catalogs;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.OrderItemId;
import coffeehouse.modules.order.domain.entity.OrderItem;
import coffeehouse.modules.order.domain.service.OrderPlacement;
import coffeehouse.modules.order.domain.service.UnidentifiedProductException;
import coffeehouse.modules.user.domain.UserAccountId;
import coffeehouse.modules.user.domain.service.Customers;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
class OrderPlacementAdapterService implements OrderPlacement.OrdererVerification, OrderPlacement.OrderItemFactory {

    private final Customers customers;
    private final Catalogs catalogs;

    public OrderPlacementAdapterService(Customers customers, Catalogs catalogs) {
        this.customers = Objects.requireNonNull(customers, "Customers must not be null");
        this.catalogs = Objects.requireNonNull(catalogs, "Catalogs must not be null");
    }

    @Override
    public Optional<Orderer> verify(UserAccountId customerId) {
        return customers.getCustomerDetails(customerId).map(it -> new Orderer(it.id(), it.email()));
    }

    @Override
    public OrderItem create(OrderId orderId, ProductCode productCode, int orderQuantity) {
        var orderItemId = new OrderItemId(UUID.randomUUID().toString());
        var product = catalogs.getProductDetails(productCode)
                .orElseThrow(() -> UnidentifiedProductException.ofProductCode(productCode));

        return OrderItem.create(orderItemId, orderId, product.id(), product.price(), orderQuantity);
    }
}

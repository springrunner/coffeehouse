package coffeehouse.modules.user.web;

import coffeehouse.contracts.user.web.CustomersApi;
import coffeehouse.contracts.user.web.model.CustomerDetails;
import coffeehouse.contracts.user.web.model.RegisterCustomerRequest;
import coffeehouse.libraries.base.lang.Email;
import coffeehouse.libraries.security.web.context.UserPrincipalContextHolder;
import coffeehouse.modules.user.domain.service.CustomerRegistration;
import coffeehouse.modules.user.domain.service.Customers;
import coffeehouse.modules.user.domain.service.UserAccountNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@RestController
public class CustomersRestController implements CustomersApi {

    private final CustomerRegistration customerRegistration;
    private final Customers customers;

    public CustomersRestController(CustomerRegistration customerRegistration, Customers customers) {
        this.customerRegistration = Objects.requireNonNull(customerRegistration, "CustomerRegistration must not be null");
        this.customers = Objects.requireNonNull(customers, "Customers must not be null");
    }

    @Override
    public ResponseEntity<Void> registerCustomer(RegisterCustomerRequest request) {
        customerRegistration.register(Email.of(request.getEmail()), request.getPassword());
        return ResponseEntity.ok(null);
    }
    
    @Override
    public ResponseEntity<CustomerDetails> getCustomerDetails() {
        var username = UserPrincipalContextHolder.currentAuthentication().getName();
        
        return customers.getCustomerDetails(username).map(it -> {
            var customerDetails = new CustomerDetails().email(it.email().toString()).username(it.username()).registeredAt(it.createdAt().atOffset(ZoneOffset.UTC));
            return ResponseEntity.ok(customerDetails);
        }).orElseThrow(() -> new UserAccountNotFoundException("No registered customer by `%s`".formatted(username)));
    }
}

package coffeehouse.modules.user.web;

import coffeehouse.contracts.user.web.AccountsApi;
import coffeehouse.contracts.user.web.model.LoginAccount200Response;
import coffeehouse.contracts.user.web.model.LoginAccountRequest;
import coffeehouse.libraries.security.authentication.TokenIssuance;
import coffeehouse.modules.user.domain.UserAccountId;
import coffeehouse.modules.user.domain.service.AccountIdentification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@RestController
public class AccountsRestController implements AccountsApi {

    private final AccountIdentification accountIdentification;
    private final TokenIssuance<UserAccountId> tokenIssuance;

    public AccountsRestController(AccountIdentification accountIdentification, TokenIssuance<UserAccountId> tokenIssuance) {
        this.accountIdentification = Objects.requireNonNull(accountIdentification, "CustomerIdentification must not be null");
        this.tokenIssuance = Objects.requireNonNull(tokenIssuance, "TokenIssuance must not be null");
    }

    @Override
    public ResponseEntity<LoginAccount200Response> loginAccount(LoginAccountRequest request) {
        var accountId = accountIdentification.identify(request.getIdentifier(), request.getPassword());
        var accessToken = tokenIssuance.issue(accountId);

        var response = new LoginAccount200Response().accessToken(accessToken.toString());
        return ResponseEntity.ok(response);
    }
}

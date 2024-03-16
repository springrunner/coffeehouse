package coffeehouse.modules.user.web;

import coffeehouse.contracts.user.web.AccountsApi;
import coffeehouse.contracts.user.web.model.AuthenticateAccount200Response;
import coffeehouse.contracts.user.web.model.LoginAccount200Response;
import coffeehouse.contracts.user.web.model.LoginAccountRequest;
import coffeehouse.libraries.security.PlainToken;
import coffeehouse.libraries.security.UnauthorizedException;
import coffeehouse.libraries.security.authentication.TokenAuthentication;
import coffeehouse.libraries.security.authentication.TokenIssuance;
import coffeehouse.modules.user.domain.UserAccountId;
import coffeehouse.modules.user.domain.service.AccountIdentification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@RestController
public class AccountsRestController implements AccountsApi {

    private final AccountIdentification accountIdentification;
    private final TokenIssuance<UserAccountId> tokenIssuance;
    private final TokenAuthentication<UserAccountId> tokenAuthentication;

    public AccountsRestController(AccountIdentification accountIdentification, TokenIssuance<UserAccountId> tokenIssuance, TokenAuthentication<UserAccountId> accountSecurityService) {
        this.accountIdentification = Objects.requireNonNull(accountIdentification, "CustomerIdentification must not be null");
        this.tokenIssuance = Objects.requireNonNull(tokenIssuance, "TokenIssuance must not be null");
        this.tokenAuthentication = Objects.requireNonNull(accountSecurityService, "TokenAuthentication must not be null");
    }

    @Override
    public ResponseEntity<LoginAccount200Response> loginAccount(LoginAccountRequest request) {
        var accountId = accountIdentification.identify(request.getIdentifier(), request.getPassword());
        var accessToken = tokenIssuance.issue(accountId);

        var response = new LoginAccount200Response().accessToken(accessToken.toString());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AuthenticateAccount200Response> authenticateAccount(String xAccessToken) {
        var authentication = tokenAuthentication.authenticate(new PlainToken(xAccessToken)).orElseThrow(UnauthorizedException::new);
        
        return ResponseEntity.ok(
                new AuthenticateAccount200Response()
                        .principal(authentication.principal().toString())
                        .username(authentication.username())
                        .roles(new ArrayList<>(authentication.roles()))
        );
    }
}

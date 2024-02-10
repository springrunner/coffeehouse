package coffeehouse.libraries.security.web.context;

import coffeehouse.libraries.security.authentication.TokenAuthentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class UserPrincipalContextHolder {
    
    @SuppressWarnings("unchecked")
    public static <T> TokenAuthentication.Authentication<T> currentAuthentication() {
        var requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof WebRequest webRequest) {
            return mapAuthentication(webRequest.getUserPrincipal());
        } else if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return mapAuthentication(servletRequestAttributes.getRequest().getUserPrincipal());
        } else {
            throw new IllegalStateException("Can't access user-principal in request");
        }
    }

    @SuppressWarnings("rawtypes")
    private static TokenAuthentication.Authentication mapAuthentication(Principal principal) {
        if (principal instanceof TokenAuthentication.Authentication userPrincipal) {
            return userPrincipal;
        } else {
            throw new IllegalStateException("Unsupported principal: %s".formatted(principal));
        }
    }   
}

package coffeehouse.libraries.security.web.filter;

import coffeehouse.libraries.security.PlainToken;
import coffeehouse.libraries.security.UnauthorizedException;
import coffeehouse.libraries.security.authentication.TokenAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Principal;
import java.util.Objects;
import java.util.Set;

/**
 * @author springrunner.kr@gmail.com
 */
public class BearerTokenAuthenticationProcessingFilter extends OncePerRequestFilter {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ANONYMOUS_USERNAME = "anonymous";
    public static final Set<String> ANONYMOUS_ROLES = Set.of("ANONYMOUS");
    
    private final TokenAuthentication<?> tokenAuthentication;
    private final TokenAuthentication.Authentication<?> anonymous = new TokenAuthentication.Authentication<>(null, ANONYMOUS_USERNAME, ANONYMOUS_ROLES);

    public BearerTokenAuthenticationProcessingFilter(TokenAuthentication<?> tokenAuthentication) {
        this.tokenAuthentication = Objects.requireNonNull(tokenAuthentication, "TokenAuthentication must not be null");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain
    ) throws ServletException, IOException {
        var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.startsWithIgnoreCase(authorization, TOKEN_PREFIX)) {
            logger.debug("Attempt authentication with Bearer Token");

            var token = authorization.replace(TOKEN_PREFIX, "").trim();
            logger.debug("Token used for authentication: " + token);
            
            var authentication = tokenAuthentication.authenticate(new PlainToken(token)).orElseThrow(UnauthorizedException::new);
            logger.info("`%s` is authenticated".formatted(authentication));
            
            filterChain.doFilter(new AuthenticatedHttpServletRequest(request, authentication), response);
        } else {
            logger.info("Handling request for anonymous user");
            filterChain.doFilter(new AuthenticatedHttpServletRequest(request, anonymous), response);
        }
    }

    static class AuthenticatedHttpServletRequest extends HttpServletRequestWrapper {
        
        final TokenAuthentication.Authentication<?> authentication;
        
        AuthenticatedHttpServletRequest(HttpServletRequest request, TokenAuthentication.Authentication<?> authentication) {
            super(request);
            this.authentication = Objects.requireNonNull(authentication, "Authentication must not be null");
        }

        @Override
        public Principal getUserPrincipal() {
            return authentication;
        }

        @Override
        public boolean isUserInRole(String role) {
            return authentication.roles().contains(role);
        }
    }    
}

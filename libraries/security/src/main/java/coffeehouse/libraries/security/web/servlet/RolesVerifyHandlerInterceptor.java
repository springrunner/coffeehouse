package coffeehouse.libraries.security.web.servlet;

import coffeehouse.libraries.security.AccessDeniedException;
import coffeehouse.libraries.security.UnauthorizedException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public class RolesVerifyHandlerInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        obtainRolesAllowed(handler).ifPresent(it -> {
            var requiredRoles = it.value();
            logger.debug("Verifying access based on roles: {}", Arrays.toString(requiredRoles));

            if (Objects.isNull(request.getUserPrincipal())) {
                logger.warn("Unauthorized access attempt");
                throw new UnauthorizedException();
            }

            if (Arrays.stream(it.value()).noneMatch(request::isUserInRole)) {
                logger.warn("Access denied for user: {} requires `{}` permissions", request.getUserPrincipal().getName(), Arrays.toString(requiredRoles));
                throw new AccessDeniedException();
            }
        });
        return true;
    }

    private Optional<RolesAllowed> obtainRolesAllowed(Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            var annotation = handlerMethod.getMethodAnnotation(RolesAllowed.class);
            if (Objects.isNull(annotation)) {
                annotation = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), RolesAllowed.class);
            }
            return Optional.ofNullable(annotation);
        }
        return Optional.empty();
    }
}

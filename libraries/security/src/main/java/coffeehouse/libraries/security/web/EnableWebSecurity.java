package coffeehouse.libraries.security.web;

import coffeehouse.libraries.security.authentication.TokenAuthentication;
import coffeehouse.libraries.security.web.filter.BearerTokenAuthenticationProcessingFilter;
import coffeehouse.libraries.security.web.servlet.RolesVerifyHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author springrunner.kr@gmail.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableWebSecurity.WebSecurityConfiguration.class)
public @interface EnableWebSecurity {

    @Configuration
    class WebSecurityConfiguration implements WebMvcConfigurer {

        @Bean
        BearerTokenAuthenticationProcessingFilter bearerTokenAuthenticationProcessingFilter(TokenAuthentication<?> tokenAuthentication) {
            return new BearerTokenAuthenticationProcessingFilter(tokenAuthentication);
        }
        
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new RolesVerifyHandlerInterceptor());
        }
    }
}

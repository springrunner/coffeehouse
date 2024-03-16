package coffeehouse.applications.server;

import coffeehouse.applications.server.security.authentication.UserAccountTokenAuthentication;
import coffeehouse.applications.server.security.filter.JSONWebTokenRelayGatewayFilterFactory;
import coffeehouse.libraries.security.authentication.TokenAuthentication;
import coffeehouse.libraries.security.converter.TokenConverters;
import coffeehouse.libraries.security.web.EnableWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author springrunner.kr@gmail.com
 */
@SpringBootApplication
@EnableWebSecurity
public class CoffeehouseEdgeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeehouseEdgeServerApplication.class, args);
    }
    
    @Bean
    UserAccountTokenAuthentication userAccountTokenAuthentication(
            WebClient.Builder webClientBuilder, 
            ConversionService conversionService, 
            Environment environment
    ) {
        var webClient = webClientBuilder.baseUrl(environment.getRequiredProperty("coffeehouse.user.server-uri")).build();
        var httpServiceProxyFactory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .conversionService(conversionService)
                .build();
        
        return httpServiceProxyFactory.createClient(UserAccountTokenAuthentication.class);
    }
    
    @Bean
    JSONWebTokenRelayGatewayFilterFactory jsonWebTokenRelayProcessingFilter(
            TokenAuthentication<?> tokenAuthentication,
            Environment environment
    ) {
        var secretString = "coffeehouse";
        var secretKey = new SecretKeySpec(secretString.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        
        return new JSONWebTokenRelayGatewayFilterFactory(tokenAuthentication, secretKey);
    }
    
    @Autowired
    void addConverters(ConverterRegistry converterRegistry) {
        TokenConverters.converters().forEach(converterRegistry::addConverter);
    }
}

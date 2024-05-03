package coffeehouse.tests.integration;

import coffeehouse.libraries.message.DirectChannel;
import coffeehouse.modules.brew.EnableBrewModule;
import coffeehouse.modules.order.EnableOrderModule;
import coffeehouse.modules.user.EnableUserModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.client.RestTemplate;

/**
 * @author springrunner.kr@gmail.com
 */
@SpringBootApplication
@EnableOrderModule
@EnableBrewModule
@EnableUserModule
public class CoffeehouseIntegrationTestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeehouseIntegrationTestingApplication.class, args);
    }
    
    @Bean
    RestTemplate defaultRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    MessageChannel barCounterChannel() {
        return new DirectChannel();
    }
}

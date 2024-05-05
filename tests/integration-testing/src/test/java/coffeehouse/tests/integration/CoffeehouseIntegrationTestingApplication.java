package coffeehouse.tests.integration;

import coffeehouse.modules.brew.EnableBrewModule;
import coffeehouse.modules.order.EnableOrderModule;
import coffeehouse.modules.user.EnableUserModule;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
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
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        var rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareQueue(new Queue("brew"));
        return rabbitAdmin;
    }

    @Bean
    MessageChannel barCounterChannel() {
        return new DirectChannel();
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public IntegrationFlow amqpIntegrationFlow(AmqpTemplate amqpTemplate, DirectChannel barCounterChannel) {
        return IntegrationFlow.from(barCounterChannel)
                .handle(
                        Amqp.outboundAdapter(amqpTemplate)
                                .routingKey("brew")
                ).get();
    }

    @Bean
    DirectChannel brewRequestChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow amqpInbound(ConnectionFactory connectionFactory,
                                       DirectChannel brewRequestChannel
    ) {
        return IntegrationFlow.from(
                Amqp.inboundAdapter(connectionFactory, "brew")
                        .messageConverter(jsonMessageConverter())
        ).handle(message -> brewRequestChannel.send(message)).get();
    }
}

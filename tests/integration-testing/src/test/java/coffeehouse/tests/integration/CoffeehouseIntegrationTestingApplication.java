package coffeehouse.tests.integration;

import coffeehouse.modules.brew.EnableBrewModule;
import coffeehouse.modules.order.EnableOrderModule;
import coffeehouse.modules.user.EnableUserModule;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.router.HeaderValueRouter;
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
    @ServiceActivator(inputChannel = "barCounterChannel")
    public AmqpOutboundEndpoint amqpOutboundEndpoint(AmqpTemplate amqpTemplateContentTypeConverter) {
        var amqpOutboundEndpoint = new AmqpOutboundEndpoint(amqpTemplateContentTypeConverter);
        amqpOutboundEndpoint.setRoutingKey("brew");
        return amqpOutboundEndpoint;
    }

    @Bean
    MessageChannel brewRequestChannel() {
        return new DirectChannel();
    }

    @Bean
    MessageChannel amqpInboundChannel() {
        return new QueueChannel();
    }

    @Bean
    @Router(inputChannel = "amqpInboundChannel")
    public HeaderValueRouter messageRouter() {
        var router = new HeaderValueRouter("amqp_receivedRoutingKey");
        router.setChannelMapping("brew", "brewRequestChannel");
        return router;
    }

    @Bean
    public SimpleMessageListenerContainer amqpContainer(ConnectionFactory connectionFactory) {
        var container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames("brew");
        return container;
    }

    @Bean
    public AmqpInboundChannelAdapter inboundChannelAdapter(
            SimpleMessageListenerContainer amqpContainer,
            MessageChannel amqpInboundChannel
    ) {
        var amqpInboundChannelAdapter = new AmqpInboundChannelAdapter(amqpContainer);
        amqpInboundChannelAdapter.setMessageConverter(jsonMessageConverter());
        amqpInboundChannelAdapter.setOutputChannel(amqpInboundChannel);
        return amqpInboundChannelAdapter;
    }
}

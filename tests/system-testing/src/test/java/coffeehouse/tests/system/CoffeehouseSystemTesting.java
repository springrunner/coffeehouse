package coffeehouse.tests.system;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

/**
 * @author springrunner.kr@gmail.com
 */
@SpringBootConfiguration
@ImportAutoConfiguration(RabbitAutoConfiguration.class)
public class CoffeehouseSystemTesting {
    
    @Autowired
    void configRabbitMQ(ConnectionFactory connectionFactory) {
        var admin = new RabbitAdmin(connectionFactory);
        
        var fanoutExchange = new FanoutExchange("amq.fanout");
        var brewServerQueue = new Queue("coffeehouse-event-stream.brew-server");
        var serverQueue = new Queue("coffeehouse-event-stream.server");
        
        admin.declareExchange(fanoutExchange);
        admin.declareQueue(brewServerQueue);
        admin.declareQueue(serverQueue);
        admin.declareBinding(BindingBuilder.bind(brewServerQueue).to(fanoutExchange));
        admin.declareBinding(BindingBuilder.bind(serverQueue).to(fanoutExchange));
    }
}

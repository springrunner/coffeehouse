package coffeehouse.modules.brew;

import coffeehouse.modules.brew.domain.service.OrderSheetSubmission;
import coffeehouse.modules.order.domain.message.BrewRequestCommandMessage;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Observable;

/**
 * @author springrunner.kr@gmail.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableBrewModule.BrewModuleConfiguration.class)
public @interface EnableBrewModule {

    @Configuration
    @ComponentScan
    class BrewModuleConfiguration {
        
        @Bean
        public MessageHandler messageHandler(OrderSheetSubmission orderSheetSubmission, MessageChannel barCounterChannel) {
            var logger = LoggerFactory.getLogger(getClass());

            var messageHandler = new MessageHandler() {
                @Override
                public void handleMessage(Message<?> message) throws MessagingException {
                    var commandMessage = (BrewRequestCommandMessage) message.getPayload();
                    logger.info("Receive brew-request-command-message: %s".formatted(commandMessage));
                    orderSheetSubmission.submit(new OrderSheetSubmission.OrderSheetForm(commandMessage.orderId()));
                }
            };

            var observer = (Observable) barCounterChannel;
            observer.addObserver((o, message) -> {
                messageHandler.handleMessage((Message<?>) message);
            });

            return messageHandler;
        }
    }
}

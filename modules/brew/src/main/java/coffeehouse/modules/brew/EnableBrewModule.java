package coffeehouse.modules.brew;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
        MessageChannel brewCompletedNotifyUserChannel() {
            return new DirectChannel();
        }

        @Bean
        MessageChannel brewCompletedNotifyOrderChannel() {
            return new DirectChannel();
        }

        @Bean
        IntegrationFlow notifyUserIntegrationFlow(MessageChannel brewCompletedNotifyUserChannel, Environment environment) {
            var brewCompletedNotificationUserUri = environment.getRequiredProperty("coffeehouse.user.notify-brew-complete-uri");
            return IntegrationFlow.from(brewCompletedNotifyUserChannel)
                    .handle(
                            Http.outboundChannelAdapter(brewCompletedNotificationUserUri)
                                    .httpMethod(HttpMethod.POST)
                    ).get();
        }

        @Bean
        IntegrationFlow notifyBrewIntegrationFlow(MessageChannel brewCompletedNotifyOrderChannel, Environment environment) {
            var brewCompletedNotificationUserUri = environment.getRequiredProperty("coffeehouse.brew.notify-brew-complete-uri");
            return IntegrationFlow.from(brewCompletedNotifyOrderChannel)
                    .handle(
                            Http.outboundChannelAdapter(brewCompletedNotificationUserUri)
                                    .httpMethod(HttpMethod.POST)
                    ).get();
        }
    }
}

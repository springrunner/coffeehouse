package coffeehouse.modules.brew;

import coffeehouse.libraries.modulemesh.event.EnableModuleEventProcessor;
import coffeehouse.libraries.modulemesh.event.inbox.ModuleEventInboxRegistry;
import coffeehouse.libraries.modulemesh.event.outbox.ModuleEventOutboxAccessorRegistry;
import coffeehouse.libraries.modulemesh.event.outbox.ModuleEventOutboxRegistry;
import coffeehouse.libraries.modulemesh.event.serializer.ObjectModuleEventSerde;
import coffeehouse.libraries.modulemesh.event.spring.jdbc.JdbcModuleEventInbox;
import coffeehouse.libraries.modulemesh.event.spring.jdbc.JdbcModuleEventOutbox;
import coffeehouse.libraries.modulemesh.function.ModuleFunctionRegistry;
import coffeehouse.libraries.spring.beans.factory.config.PublishedBeanRegisterProcessor;
import coffeehouse.libraries.spring.beans.factory.support.LimitedBeanFactoryAccessor;
import coffeehouse.libraries.spring.context.annotation.PublishedBean;
import coffeehouse.modules.brew.domain.service.OrderSheetSubmission;
import coffeehouse.modules.order.domain.OrderId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

/**
 * @author springrunner.kr@gmail.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableBrewModule.BrewModuleConfiguration.class)
public @interface EnableBrewModule {

    @Configuration
    @ComponentScan
    @EnableJdbcRepositories
    @EnableModuleEventProcessor
    class BrewModuleConfiguration {

        @Bean
        DataSourceInitializer brewDataSourceInitializer(DataSource dataSource) {
            var databasePopulator = new ResourceDatabasePopulator();
            databasePopulator.addScript(new ClassPathResource("brew-schema.sql"));

            var initializer = new DataSourceInitializer();
            initializer.setDataSource(dataSource);
            initializer.setDatabasePopulator(databasePopulator);

            return initializer;
        }

        @Bean
        JdbcModuleEventOutbox brewModuleEventOutbox(DataSource dataSource) {
            return new JdbcModuleEventOutbox(
                    event -> event.getClass().getPackageName().startsWith("coffeehouse.modules.brew"),
                    "BREW",
                    dataSource,
                    ObjectModuleEventSerde.INSTANCE,
                    ObjectModuleEventSerde.INSTANCE
            );
        }

        @Bean
        JdbcModuleEventInbox brewModuleEventInbox(DataSource dataSource) {
            return new JdbcModuleEventInbox(
                    event -> event.getClass().getPackageName().startsWith("coffeehouse.modules.brew"),
                    "BREW",
                    dataSource,
                    ObjectModuleEventSerde.INSTANCE,
                    ObjectModuleEventSerde.INSTANCE
            );
        }        

        @PublishedBean
        LimitedBeanFactoryAccessor brewDelegatedBeanFactoryAccessor() {
            return new LimitedBeanFactoryAccessor();
        }

        @Bean
        PublishedBeanRegisterProcessor brewPublishedBeanRegisterProcessor() {
            return new PublishedBeanRegisterProcessor();
        }
    }

    @Configuration
    class BrewModuleMeshConfigurer {

        @Autowired
        void configureModuleEventOutbox(
                ModuleEventOutboxRegistry moduleEventOutboxRegistry,
                ModuleEventOutboxAccessorRegistry moduleEventOutboxAccessorRegistry,
                JdbcModuleEventOutbox brewModuleEventOutbox
        ) {
            moduleEventOutboxRegistry.registerModuleEventOutbox(brewModuleEventOutbox);
            moduleEventOutboxAccessorRegistry.registerModuleEventOutboxAccessor(brewModuleEventOutbox);
        }

        @Autowired
        void configureModuleEventInbox(
                ModuleEventInboxRegistry moduleEventInboxRegistry,
                JdbcModuleEventInbox brewModuleEventInbox
        ) {
            moduleEventInboxRegistry.registerModuleEventInbox(brewModuleEventInbox);
        }

        @Autowired
        void registerModuleFunctions(ModuleFunctionRegistry registry, OrderSheetSubmission orderSheetSubmission) {
            registry.registerModuleFunction(
                    "ordersheets/submit-ordersheet",
                    OrderSheetSubmissionRequest.class,
                    request -> orderSheetSubmission.submitOrderSheet(request.orderId(), request.orderLines())
            );
        }

        record OrderSheetSubmissionRequest(OrderId orderId, Set<OrderSheetSubmission.OrderLine> orderLines) {
        }
    }
}

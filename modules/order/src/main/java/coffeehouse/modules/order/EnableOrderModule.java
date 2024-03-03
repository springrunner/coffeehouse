package coffeehouse.modules.order;

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
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.service.Orders;
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

/**
 * @author springrunner.kr@gmail.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableOrderModule.OrderModuleConfiguration.class)
public @interface EnableOrderModule {

    @Configuration
    @ComponentScan
    @EnableJdbcRepositories
    @EnableModuleEventProcessor
    class OrderModuleConfiguration {

        @Bean
        DataSourceInitializer orderDataSourceInitializer(DataSource dataSource) {
            var databasePopulator = new ResourceDatabasePopulator();
            databasePopulator.addScript(new ClassPathResource("order-schema.sql"));

            var initializer = new DataSourceInitializer();
            initializer.setDataSource(dataSource);
            initializer.setDatabasePopulator(databasePopulator);

            return initializer;
        }

        @Bean
        JdbcModuleEventOutbox orderModuleEventOutbox(DataSource dataSource) {
            return new JdbcModuleEventOutbox(
                    event -> event.getClass().getPackageName().startsWith("coffeehouse.modules.order"),
                    "ORDER",
                    dataSource,
                    ObjectModuleEventSerde.INSTANCE,
                    ObjectModuleEventSerde.INSTANCE
            );
        }

        @Bean
        JdbcModuleEventInbox orderModuleEventInbox(DataSource dataSource) {
            return new JdbcModuleEventInbox(
                    event -> event.getClass().getPackageName().startsWith("coffeehouse.modules.order"),
                    "ORDER",
                    dataSource,
                    ObjectModuleEventSerde.INSTANCE,
                    ObjectModuleEventSerde.INSTANCE
            );
        }        
        
        @PublishedBean
        LimitedBeanFactoryAccessor orderDelegatedBeanFactoryAccessor() {
            return new LimitedBeanFactoryAccessor();
        }

        @Bean
        PublishedBeanRegisterProcessor orderPublishedBeanRegisterProcessor() {
            return new PublishedBeanRegisterProcessor();
        }
    }

    @Configuration
    class OrderModuleMeshConfigurer {
        
        @Autowired
        void configureModuleEventOutbox(
                ModuleEventOutboxRegistry moduleEventOutboxRegistry,
                ModuleEventOutboxAccessorRegistry moduleEventOutboxAccessorRegistry,
                JdbcModuleEventOutbox orderModuleEventOutbox
        ) {
            moduleEventOutboxRegistry.registerModuleEventOutbox(orderModuleEventOutbox);
            moduleEventOutboxAccessorRegistry.registerModuleEventOutboxAccessor(orderModuleEventOutbox);
        }

        @Autowired
        void configureModuleEventInbox(
                ModuleEventInboxRegistry moduleEventInboxRegistry,
                JdbcModuleEventInbox orderModuleEventInbox
        ) {
            moduleEventInboxRegistry.registerModuleEventInbox(orderModuleEventInbox);
        }        
        
        @Autowired
        void registerModuleFunctions(ModuleFunctionRegistry registry, Orders orders) {
            registry.registerModuleFunction(
                    "orders/get-order-details",
                    OrderId.class,
                    Orders.OrderDetails.class,
                    orderId -> orders.getOrderDetails(orderId).orElse(null)
            );
        }
    }
}

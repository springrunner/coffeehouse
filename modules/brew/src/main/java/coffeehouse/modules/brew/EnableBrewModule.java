package coffeehouse.modules.brew;

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

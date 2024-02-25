package coffeehouse.modules.catalog;

import coffeehouse.libraries.modulemesh.function.ModuleFunctionRegistry;
import coffeehouse.libraries.spring.beans.factory.config.PublishedBeanRegisterProcessor;
import coffeehouse.libraries.spring.beans.factory.support.LimitedBeanFactoryAccessor;
import coffeehouse.libraries.spring.context.annotation.PublishedBean;
import coffeehouse.modules.catalog.domain.ProductCode;
import coffeehouse.modules.catalog.domain.service.Catalogs;
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
@Import(EnableCatalogModule.CatalogModuleConfiguration.class)
public @interface EnableCatalogModule {

    @Configuration
    @ComponentScan
    @EnableJdbcRepositories
    class CatalogModuleConfiguration {

        @Bean
        DataSourceInitializer catalogDataSourceInitializer(DataSource dataSource) {
            var databasePopulator = new ResourceDatabasePopulator();
            databasePopulator.addScript(new ClassPathResource("catalog-schema.sql"));

            var initializer = new DataSourceInitializer();
            initializer.setDataSource(dataSource);
            initializer.setDatabasePopulator(databasePopulator);

            return initializer;
        }

        @PublishedBean
        LimitedBeanFactoryAccessor catalogDelegatedBeanFactoryAccessor() {
            return new LimitedBeanFactoryAccessor();
        }

        @Bean
        PublishedBeanRegisterProcessor catalogPublishedBeanRegisterProcessor() {
            return new PublishedBeanRegisterProcessor();
        }
    }
    
    @Configuration
    class CatalogModuleMeshConfigurer {
        
        @Autowired
        void registerModuleFunctions(ModuleFunctionRegistry registry, Catalogs catalogs) {
            registry.registerModuleFunction(
                    "catalogs/get-product-details",
                    ProductCode.class,
                    Catalogs.ProductDetails.class,
                    productCode -> catalogs.getProductDetails(productCode).orElse(null)
            );
        }
    }
}

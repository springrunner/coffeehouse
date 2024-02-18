package coffeehouse.modules.user;

import coffeehouse.libraries.spring.beans.factory.support.LimitedBeanFactoryAccessor;
import coffeehouse.libraries.spring.context.annotation.PublishedBean;
import coffeehouse.libraries.spring.beans.factory.config.PublishedBeanRegisterProcessor;
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
@Import(EnableUserModule.UserModuleConfiguration.class)
public @interface EnableUserModule {

    @Configuration
    @ComponentScan
    @EnableJdbcRepositories
    class UserModuleConfiguration {

        @Bean
        DataSourceInitializer userDataSourceInitializer(DataSource dataSource) {
            var databasePopulator = new ResourceDatabasePopulator();
            databasePopulator.addScript(new ClassPathResource("user-schema.sql"));

            var initializer = new DataSourceInitializer();
            initializer.setDataSource(dataSource);
            initializer.setDatabasePopulator(databasePopulator);

            return initializer;
        }

        @PublishedBean
        LimitedBeanFactoryAccessor userDelegatedBeanFactoryAccessor() {
            return new LimitedBeanFactoryAccessor();
        }
        
        @Bean
        PublishedBeanRegisterProcessor userPublishedBeanRegisterProcessor() {
            return new PublishedBeanRegisterProcessor();
        }
    }
}

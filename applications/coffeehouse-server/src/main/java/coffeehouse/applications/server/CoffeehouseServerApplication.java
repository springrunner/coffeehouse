package coffeehouse.applications.server;

import coffeehouse.libraries.base.convert.spring.BaseConverters;
import coffeehouse.libraries.base.serialize.jackson.ObjectIdModule;
import coffeehouse.libraries.modulemesh.EnableModuleMesh;
import coffeehouse.libraries.security.web.EnableWebSecurity;
import coffeehouse.libraries.spring.beans.factory.support.LimitedBeanFactoryAccessor;
import coffeehouse.libraries.spring.data.jdbc.core.mapping.ModularJdbcMappingContext;
import coffeehouse.modules.brew.EnableBrewModule;
import coffeehouse.modules.brew.data.convert.BrewConverters;
import coffeehouse.modules.brew.domain.OrderSheetId;
import coffeehouse.modules.catalog.EnableCatalogModule;
import coffeehouse.modules.catalog.data.convert.CatalogConverters;
import coffeehouse.modules.catalog.domain.ProductId;
import coffeehouse.modules.order.EnableOrderModule;
import coffeehouse.modules.order.data.convert.OrderConverters;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.order.domain.OrderItemId;
import coffeehouse.modules.user.EnableUserModule;
import coffeehouse.modules.user.data.convert.UserConverters;
import coffeehouse.modules.user.domain.UserAccountId;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.relational.RelationalManagedTypes;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.stereotype.Controller;
import org.zalando.jackson.datatype.money.MoneyModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public class CoffeehouseServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(InfrastructureConfiguration.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .child(EnableUserModule.UserModuleConfiguration.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .sibling(EnableCatalogModule.CatalogModuleConfiguration.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .sibling(EnableOrderModule.OrderModuleConfiguration.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .sibling(EnableBrewModule.BrewModuleConfiguration.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .sibling(WebConfiguration.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @EnableModuleMesh
    static class InfrastructureConfiguration extends AbstractJdbcConfiguration {
        
        @Bean
        Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
            return builder -> builder
                    .modules(
                            new Jdk8Module(),
                            new JavaTimeModule(),
                            new MoneyModule(),
                            new ObjectIdModule(
                                    UserAccountId.class,
                                    ProductId.class,
                                    OrderId.class,
                                    OrderItemId.class,
                                    OrderSheetId.class
                            )
                    )
                    .failOnEmptyBeans(false)
                    .failOnUnknownProperties(false);
        }

        @Override
        protected List<?> userConverters() {
            var converters = new ArrayList<>();
            converters.addAll(BaseConverters.converters());
            converters.addAll(UserConverters.converters());
            converters.addAll(CatalogConverters.converters());
            converters.addAll(OrderConverters.converters());
            converters.addAll(BrewConverters.converters());
            return converters;
        }

        @Bean
        @Override
        public JdbcMappingContext jdbcMappingContext(Optional<NamingStrategy> namingStrategy, JdbcCustomConversions customConversions, RelationalManagedTypes jdbcManagedTypes) {
            var mappingContext = new ModularJdbcMappingContext(namingStrategy.orElse(DefaultNamingStrategy.INSTANCE));
            mappingContext.setSimpleTypeHolder(customConversions.getSimpleTypeHolder());
            mappingContext.setManagedTypes(jdbcManagedTypes);

            return mappingContext;
        }
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration(exclude = {
            DataSourceAutoConfiguration.class,
            DataSourceTransactionManagerAutoConfiguration.class,
            HttpMessageConvertersAutoConfiguration.class,
            JacksonAutoConfiguration.class,
            JdbcRepositoriesAutoConfiguration.class,
            JdbcTemplateAutoConfiguration.class,
            PersistenceExceptionTranslationAutoConfiguration.class,
            RestTemplateAutoConfiguration.class,
            SqlInitializationAutoConfiguration.class,
            TaskExecutionAutoConfiguration.class,
            TaskSchedulingAutoConfiguration.class,
            TransactionAutoConfiguration.class,
    })
    @EnableWebSecurity
    static class WebConfiguration implements BeanFactoryPostProcessor {

        private final Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            var parentBeanFactory = beanFactory.getParentBeanFactory();
            if (parentBeanFactory instanceof ConfigurableListableBeanFactory clbf) {
                clbf.getBeansOfType(LimitedBeanFactoryAccessor.class).values().forEach(accessor -> {
                    accessor.getBeansWithAnnotation(Controller.class).entrySet().forEach(it -> {
                        beanFactory.registerSingleton(it.getKey(), it.getValue());
                        logger.debug("Registered the module-controller: {}", it);
                    });
                });
            }
        }
    }
}

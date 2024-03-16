package coffeehouse.applications.server;

import coffeehouse.libraries.base.convert.spring.BaseConverters;
import coffeehouse.libraries.base.serialize.jackson.ObjectIdModule;
import coffeehouse.libraries.modulemesh.EnableModuleMesh;
import coffeehouse.libraries.modulemesh.EnableModuleMesh.ModuleEventChannelMode;
import coffeehouse.libraries.spring.data.jdbc.core.mapping.ModularJdbcMappingContext;
import coffeehouse.modules.brew.EnableBrewModule;
import coffeehouse.modules.brew.data.convert.BrewConverters;
import coffeehouse.modules.brew.domain.OrderSheetId;
import coffeehouse.modules.catalog.data.convert.CatalogConverters;
import coffeehouse.modules.catalog.domain.ProductId;
import coffeehouse.modules.order.data.convert.OrderConverters;
import coffeehouse.modules.order.domain.OrderId;
import coffeehouse.modules.user.data.convert.UserConverters;
import coffeehouse.modules.user.domain.UserAccountId;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.relational.RelationalManagedTypes;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.web.client.RestTemplate;
import org.zalando.jackson.datatype.money.MoneyModule;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author springrunner.kr@gmail.com
 */
@SpringBootApplication
@EnableModuleMesh(moduleEventChannelMode = ModuleEventChannelMode.RABBIT)
@EnableBrewModule
public class CoffeehouseBrewServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeehouseBrewServerApplication.class, args);
    }

    @Bean
    Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .modules(
                        new Jdk8Module(),
                        new JavaTimeModule(),
                        new MoneyModule(),
                        new ObjectIdModule(UserAccountId.class, ProductId.class, OrderId.class, OrderSheetId.class)
                )
                .failOnEmptyBeans(false)
                .failOnUnknownProperties(false);
    }

    @Bean
    public DefaultLockRepository defaultLockRepository(DataSource dataSource) {
        return new DefaultLockRepository(dataSource);
    }

    @Bean
    public JdbcLockRegistry jdbcLockRegistry(DefaultLockRepository defaultLockRepository) {
        return new JdbcLockRegistry(defaultLockRepository);
    }    

    @Bean
    RestTemplate defaultRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Configuration
    static class DataJdbcConfiguration extends AbstractJdbcConfiguration {

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
}

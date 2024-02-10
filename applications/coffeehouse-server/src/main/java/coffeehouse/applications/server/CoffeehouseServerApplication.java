package coffeehouse.applications.server;

import coffeehouse.libraries.base.convert.spring.BaseConverters;
import coffeehouse.libraries.security.web.EnableWebSecurity;
import coffeehouse.libraries.spring.data.jdbc.core.mapping.ModularJdbcMappingContext;
import coffeehouse.modules.user.EnableUserModule;
import coffeehouse.modules.user.data.convert.UserConverters;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.relational.RelationalManagedTypes;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.data.relational.core.mapping.NamingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
@SpringBootApplication
@EnableWebSecurity
@EnableUserModule
public class CoffeehouseServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeehouseServerApplication.class, args);
    }
    
    @Configuration
    static class DataJdbcConfiguration extends AbstractJdbcConfiguration {

        @Override
        protected List<?> userConverters() {
            var converters = new ArrayList<>();
            converters.addAll(BaseConverters.converters());
            converters.addAll(UserConverters.converters());
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

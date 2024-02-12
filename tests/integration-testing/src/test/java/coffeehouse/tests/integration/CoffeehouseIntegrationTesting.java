package coffeehouse.tests.integration;

import coffeehouse.libraries.base.convert.spring.BaseConverters;
import coffeehouse.libraries.base.crypto.Password;
import coffeehouse.libraries.base.lang.Email;
import coffeehouse.libraries.base.lang.Money;
import coffeehouse.libraries.spring.data.jdbc.core.mapping.ModularJdbcMappingContext;
import coffeehouse.modules.catalog.EnableCatalogModule;
import coffeehouse.modules.catalog.data.convert.CatalogConverters;
import coffeehouse.modules.catalog.domain.CategoryId;
import coffeehouse.modules.catalog.domain.ProductId;
import coffeehouse.modules.catalog.domain.ProductItemId;
import coffeehouse.modules.catalog.domain.StockKeepingUnitId;
import coffeehouse.modules.catalog.domain.entity.*;
import coffeehouse.modules.order.EnableOrderModule;
import coffeehouse.modules.order.data.convert.OrderConverters;
import coffeehouse.modules.user.EnableUserModule;
import coffeehouse.modules.user.data.convert.UserConverters;
import coffeehouse.modules.user.domain.UserAccountId;
import coffeehouse.modules.user.domain.entity.UserAccount;
import coffeehouse.modules.user.domain.entity.UserAccountRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.relational.RelationalManagedTypes;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.data.relational.core.mapping.NamingStrategy;

import javax.money.Monetary;
import java.util.*;

/**
 * @author springrunner.kr@gmail.com
 */
@SpringBootConfiguration
@EnableUserModule
@EnableCatalogModule
@EnableOrderModule
@ImportAutoConfiguration(classes = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class,
})
public class CoffeehouseIntegrationTesting extends AbstractJdbcConfiguration {

    @Override
    protected List<?> userConverters() {
        var converters = new ArrayList<>();
        converters.addAll(BaseConverters.converters());
        converters.addAll(UserConverters.converters());
        converters.addAll(CatalogConverters.converters());
        converters.addAll(OrderConverters.converters());
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

    @Bean
    @DependsOn("userDataSourceInitializer")
    InitializingBean userDataInitializer(UserAccountRepository userAccountRepository) {
        return () -> {
            userAccountRepository.save(
                    UserAccount.createCustomer(
                            new UserAccountId("springrunner"),
                            Email.of("springrunner.kr@gmail.com"),
                            "springrunner.kr",
                            Password.of("password")
                    )
            );
        };
    }

    @Bean
    @DependsOn("catalogDataSourceInitializer")
    InitializingBean catalogDataInitializer(
            CategoryRepository categoryRepository,
            ProductRepository productRepository
    ) {
        return () -> {
            categoryRepository.save(
                    Category.create(
                            new CategoryId("coffee"),
                            "coffee",
                            "Coffee",
                            new CategoryId("beveridge"),
                            CategoryChildren.of(Set.of(new CategoryId("espresso"), new CategoryId("latte")))
                    )
            );

            productRepository.save(
                    Product.create(
                            new ProductId("americano"),
                            "americano",
                            "Caffé Americano",
                            ProductItems.wrap(
                                    Collections.singletonList(
                                            ProductItem.base(
                                                    new ProductItemId("beans"),
                                                    StockKeepingUnit.create(
                                                            new StockKeepingUnitId("beans"),
                                                            "panama-la-esmeralda-geisha",
                                                            "Panama La Esmeralda Geisha Mario San jose Natural"
                                                    ),
                                                    Money.of(3000, Monetary.getCurrency(Locale.getDefault()))
                                            )
                                    )
                            ),
                            ProductCategories.of(Set.of(new CategoryId("coffee"), new CategoryId("espresso")))
                    )
            );
        };
    }    
}

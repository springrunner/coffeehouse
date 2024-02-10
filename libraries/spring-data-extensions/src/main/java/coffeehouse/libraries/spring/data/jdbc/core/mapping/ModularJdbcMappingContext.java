package coffeehouse.libraries.spring.data.jdbc.core.mapping;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.data.util.TypeInformation;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public class ModularJdbcMappingContext extends JdbcMappingContext {

    private final SchemaStrategy schemaStrategy;

    public ModularJdbcMappingContext(NamingStrategy namingStrategy) {
        this(namingStrategy, new EntityPackageNameSchemaStrategy());
    }

    public ModularJdbcMappingContext(NamingStrategy namingStrategy, SchemaStrategy schemaStrategy) {
        super(namingStrategy);
        this.schemaStrategy = Objects.requireNonNull(schemaStrategy, "SchemaStrategy must not be null");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> RelationalPersistentEntity<T> createPersistentEntity(TypeInformation<T> typeInformation) {
        var entity = super.createPersistentEntity(typeInformation);

        var entityProxy = new ProxyFactory(entity);
        entityProxy.addAdvisor(createRefineQualifiedTableNameAdvisor(entity));

        return (RelationalPersistentEntity<T>) entityProxy.getProxy();
    }

    private Advisor createRefineQualifiedTableNameAdvisor(RelationalPersistentEntity<?> entity) {
        var moduleSchema = schemaStrategy.getSchema(entity, isForceQuote());
        var refineQualifiedTableName = SqlIdentifier.from(moduleSchema, entity.getTableName());

        var advisor = new NameMatchMethodPointcutAdvisor();
        advisor.setMappedName("getQualifiedTableName");
        advisor.setAdvice((MethodInterceptor) invocation -> refineQualifiedTableName);

        return advisor;
    }

    private static class EntityPackageNameSchemaStrategy implements SchemaStrategy {

        @Override
        public SqlIdentifier getSchema(RelationalPersistentEntity<?> entity, boolean forceQuote) {
            var moduleName = entity.getName().replaceAll("^.*?\\.modules\\.([^\\.]+).*$", "$1").toUpperCase();
            return forceQuote ? SqlIdentifier.quoted(moduleName) : SqlIdentifier.unquoted(moduleName);
        }
    }
}

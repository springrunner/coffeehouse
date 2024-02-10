package coffeehouse.libraries.spring.data.jdbc.core.mapping;

import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.sql.SqlIdentifier;

/**
 * @author springrunner.kr@gmail.com
 */
public interface SchemaStrategy {

    SqlIdentifier getSchema(RelationalPersistentEntity<?> entity, boolean forceQuote);
}

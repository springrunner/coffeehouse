package coffeehouse.libraries.modulemesh.event.spring.jdbc;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;
import coffeehouse.libraries.modulemesh.event.outbox.ModuleEventOutbox;
import coffeehouse.libraries.modulemesh.event.outbox.ModuleEventOutboxAccessor;
import coffeehouse.libraries.modulemesh.event.outbox.OutboxIdentifier;
import coffeehouse.libraries.modulemesh.event.serializer.ModuleEventDeserializer;
import coffeehouse.libraries.modulemesh.event.serializer.ModuleEventSerializer;
import coffeehouse.libraries.modulemesh.event.serializer.ObjectModuleEventSerde;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author springrunner.kr@gmail.com
 */
public class JdbcModuleEventOutbox implements ModuleEventOutbox, ModuleEventOutboxAccessor, BeanNameAware {

    public static final String TABLE_NAME = "MODULE_EVENT_OUTBOX";
    private static final String PUT_ACTION_SQL = "INSERT INTO %s(EVENT_ID, EVENT_TYPE, EVENT_SOURCE, EVENT_DATA, EVENT_OCCURRENCE_TIME) VALUES(?, ?, ?, ?, ?)";
    private static final String REMOVE_ACTION_SQL = "DELETE FROM %s WHERE ID = ?";
    private static final String POLL_ACTION_SQL = "SELECT * FROM %s WHERE EVENT_OCCURRENCE_TIME >= ?";

    private final Function<ModuleEvent, Boolean> supportsAction;
    private final String qualifiedTableName;
    private final JdbcTemplate jdbcTemplate;
    private final ModuleEventSerializer<ModuleEvent> serializer;
    private final ModuleEventDeserializer<ModuleEvent> deserializer;
    
    private String beanName;

    public JdbcModuleEventOutbox(DataSource dataSource) {
        this(event -> true, null, dataSource, ObjectModuleEventSerde.INSTANCE, ObjectModuleEventSerde.INSTANCE);
    }

    public JdbcModuleEventOutbox(Function<ModuleEvent, Boolean> supportsAction, String schemaName, DataSource dataSource, ModuleEventSerializer<ModuleEvent> serializer, ModuleEventDeserializer<ModuleEvent> deserializer) {
        this.supportsAction = Objects.requireNonNull(supportsAction, "SupportsAction must not be null");
        this.qualifiedTableName = Optional.ofNullable(schemaName).map(it -> "`%s`.`%s`".formatted(it, TABLE_NAME)).orElse("`%s`".formatted(TABLE_NAME));
        this.jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(dataSource, "DataSource must not be null"));
        this.serializer = Objects.requireNonNull(serializer, "ModuleEventSerializer must not be null");
        this.deserializer = Objects.requireNonNull(deserializer, "ModuleEventDeserializer must not be null");
    }

    @Override
    public boolean supports(ModuleEvent moduleEvent) {
        return supportsAction.apply(moduleEvent);
    }

    @Override
    public OutboxIdentifier put(ModuleEvent event) {
        if (!supports(event)) {
            throw new IllegalStateException("Unsupported module-event: " + event);
        }

        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement(
                    PUT_ACTION_SQL.formatted(qualifiedTableName),
                    Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, event.eventId().toString());
            preparedStatement.setString(2, event.eventType().toString());
            preparedStatement.setString(3, event.eventSource().toString());
            preparedStatement.setString(4, serializer.serializeToString(event));
            preparedStatement.setTimestamp(5, Timestamp.from(event.eventOccurrenceTime().toInstant()));
            return preparedStatement;
        }, keyHolder);

        return new OutboxIdentifier(keyHolder.getKey());
    }

    @Override
    public void remove(OutboxIdentifier identifier) {
        jdbcTemplate.update(REMOVE_ACTION_SQL.formatted(qualifiedTableName), identifier.number());
    }

    @Override
    public Iterable<OutboxRecord> poll(Date date) {
        return jdbcTemplate.query(
                POLL_ACTION_SQL.formatted(qualifiedTableName),
                (rs, rowNum) -> new OutboxRecord(
                        new OutboxIdentifier(rs.getLong("ID")),
                        deserializer.deserializeFromString(rs.getString("EVENT_DATA"))
                ),
                date
        );
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name; 
    }

    @Override
    public String toString() {
        return "%s@%s".formatted(StringUtils.hasText(beanName) ? beanName : getClass().getName(), Integer.toHexString(hashCode()));
    }
}

package coffeehouse.libraries.modulemesh.event.spring.jdbc;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;
import coffeehouse.libraries.modulemesh.event.inbox.InboxIdentifier;
import coffeehouse.libraries.modulemesh.event.inbox.ModuleEventInbox;
import coffeehouse.libraries.modulemesh.event.serializer.ModuleEventDeserializer;
import coffeehouse.libraries.modulemesh.event.serializer.ModuleEventSerializer;
import coffeehouse.libraries.modulemesh.event.serializer.ObjectModuleEventSerde;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class JdbcModuleEventInbox implements ModuleEventInbox, BeanNameAware {

    public static final String TABLE_NAME = "MODULE_EVENT_INBOX";
    private static final String PROCESS_CHECK_SQL = "SELECT * FROM %s WHERE EVENT_ID = ?";
    private static final String PROCESS_ACTION_SQL = "INSERT INTO %s(EVENT_ID, EVENT_TYPE, EVENT_SOURCE, EVENT_DATA, EVENT_OCCURRENCE_TIME, PROCESSED_AT) VALUES(?, ?, ?, ?, ?, ?)";

    private final Function<ModuleEvent, Boolean> supportsAction;
    private final String qualifiedTableName;
    private final JdbcTemplate jdbcTemplate;
    private final ModuleEventSerializer<ModuleEvent> serializer;
    private final ModuleEventDeserializer<ModuleEvent> deserializer;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String beanName;

    public JdbcModuleEventInbox(DataSource dataSource) {
        this(event -> true, null, dataSource, ObjectModuleEventSerde.INSTANCE, ObjectModuleEventSerde.INSTANCE);
    }

    public JdbcModuleEventInbox(Function<ModuleEvent, Boolean> supportsAction, String schemaName, DataSource dataSource, ModuleEventSerializer<ModuleEvent> serializer, ModuleEventDeserializer<ModuleEvent> deserializer) {
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
    public InboxIdentifier process(ModuleEvent event, Runnable eventProcessor) {
        if (!supports(event)) {
            throw new IllegalStateException("Unsupported module-event: " + event);
        }

        var record = jdbcTemplate.query(
                PROCESS_CHECK_SQL.formatted(qualifiedTableName),
                (rs, rowNum) -> new InboxRecord(
                        new InboxIdentifier(rs.getLong("ID")),
                        deserializer.deserializeFromString(rs.getString("EVENT_DATA")),
                        rs.getTimestamp("PROCESSED_AT")
                ),
                event.eventId()
        );
        if (!record.isEmpty()) {
            throw new IllegalStateException("Event with ID: '%s' was already processed".formatted(event.eventId()));
        }

        logger.trace("Processing a module-event with ID: '%s'".formatted(event.eventId()));

        eventProcessor.run();

        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement(
                    PROCESS_ACTION_SQL.formatted(qualifiedTableName),
                    Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, event.eventId().toString());
            preparedStatement.setString(2, event.eventType().toString());
            preparedStatement.setString(3, event.eventSource().toString());
            preparedStatement.setString(4, serializer.serializeToString(event));
            preparedStatement.setTimestamp(5, Timestamp.from(event.eventOccurrenceTime().toInstant()));
            preparedStatement.setTimestamp(6, Timestamp.from(new Date().toInstant()));
            return preparedStatement;
        }, keyHolder);

        logger.debug("Processed a module-event with ID: '%s'".formatted(event.eventId()));

        return new InboxIdentifier(keyHolder.getKey());
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public String toString() {
        return "%s@%s".formatted(StringUtils.hasText(beanName) ? beanName : getClass().getName(), Integer.toHexString(hashCode()));
    }

    private record InboxRecord(InboxIdentifier identifier, ModuleEvent event, Date processedAt) {
    }
}

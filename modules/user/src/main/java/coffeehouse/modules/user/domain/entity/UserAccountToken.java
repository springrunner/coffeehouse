package coffeehouse.modules.user.domain.entity;

import coffeehouse.modules.user.domain.UserAccountId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * @author springrunner.kr@gmail.com
 */
public class UserAccountToken {

    @Id
    private final UserAccountId id;
    private final String value;
    private final LocalDateTime createdAt;

    @Version
    private final Long version;

    UserAccountToken(UserAccountId id, String value, LocalDateTime createdAt, Long version) {
        this.id = Objects.requireNonNull(id, "UserAccountId must not be null");
        this.value = Objects.requireNonNull(value, "Value must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt must not be null");
        this.version = version;
    }
    
    public UserAccountId getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (!(target instanceof UserAccountToken that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserAccountToken(id=%s, value='%s')".formatted(id, value);
    }
    
    public static UserAccountToken generate(UserAccountId id) {
        return new UserAccountToken(id, UUID.randomUUID().toString(), LocalDateTime.now(), null);
    }
}

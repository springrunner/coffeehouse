package coffeehouse.modules.user.domain.entity;

import coffeehouse.libraries.base.crypto.Password;
import coffeehouse.libraries.base.lang.Email;
import coffeehouse.modules.user.domain.UserAccountId;
import coffeehouse.modules.user.domain.UserException;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Embedded;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public class UserAccount {

    @Id
    private final UserAccountId id;
    private final Email email;
    private final String username;
    private final Password password;
    @Embedded.Empty
    private final UserAccountRoles roles;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Version
    private final Long version;

    UserAccount(UserAccountId id, Email email, String username, Password password, UserAccountRoles roles, LocalDateTime createdAt, LocalDateTime updatedAt, Long version) {
        this.id = Objects.requireNonNull(id, "UserAccountId must not be null");
        this.email = Objects.requireNonNull(email, "Email must not be null");
        this.username = Objects.requireNonNull(username, "Username must not be null");
        this.password = Objects.requireNonNull(password, "Password must not be null");
        this.roles = Objects.requireNonNull(roles, "UserAccountRoles must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt must not be null");
        this.updatedAt = updatedAt;
        this.version = version;
    }

    public void verifyPassword(Password candidatePassword) {
        if (!password.equals(candidatePassword)) {
            throw new PasswordNotMatchedException("Failed to password verify: entered password does not match");
        }
    }

    public UserAccountId getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public UserAccountRoles getRoles() {
        return roles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Optional<LocalDateTime> getUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (!(target instanceof UserAccount that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserAccount(id=%s, email='%s', username='%s')".formatted(id, email, username);
    }

    static class PasswordNotMatchedException extends UserException {

        public PasswordNotMatchedException(String message) {
            super(message);
        }
    }

    public static UserAccount createCustomer(UserAccountId id, Email email, String username, Password password) {
        return new UserAccount(id, email, username, password, UserAccountRoles.wrap(UserAccountRole.CUSTOMER, UserAccountRole.BARISTA), LocalDateTime.now(), null, null);
    }
}

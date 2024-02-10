package coffeehouse.modules.user.domain.entity;

import coffeehouse.libraries.base.crypto.Password;
import coffeehouse.libraries.base.lang.Email;
import coffeehouse.modules.user.domain.UserAccountId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserAccountTest {

    private UserAccount userAccount;

    @BeforeEach
    void setUp() {
        userAccount = UserAccount.createCustomer(
                new UserAccountId(UUID.randomUUID().toString()),
                Email.of("springrunner.kr@gmail.com"),
                "springrunner",
                Password.of("testPassword")
        );
    }

    @Test
    void shouldPassWhenPasswordMatches() {
        // Given
        var candidatePassword = Password.of("testPassword");

        // When & Then
        assertDoesNotThrow(() -> userAccount.verifyPassword(candidatePassword));
    }

    @Test
    void shouldThrowExceptionWhenPasswordDoesNotMatch() {
        // Given
        var candidatePassword = new Password("wrongPassword");

        // When & Then
        assertThrows(
                UserAccount.PasswordNotMatchedException.class,
                () -> userAccount.verifyPassword(candidatePassword)
        );
    }
}

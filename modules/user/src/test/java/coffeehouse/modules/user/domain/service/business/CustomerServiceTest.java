package coffeehouse.modules.user.domain.service.business;

import coffeehouse.modules.user.UserFixtures;
import coffeehouse.modules.user.domain.entity.UserAccount;
import coffeehouse.modules.user.domain.entity.UserAccountRepository;
import coffeehouse.modules.user.domain.service.DuplicateEmailException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private UserAccountRepository mockUserAccountRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldCreateNewUserWhenEmailIsNotDuplicate() {
        // Given
        var email = UserFixtures.email();
        var password = UserFixtures.password().toString();

        var userAccountCaptor = ArgumentCaptor.forClass(UserAccount.class);
        when(mockUserAccountRepository.existsByEmail(email)).thenReturn(false);
        when(mockUserAccountRepository.save(userAccountCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        customerService.register(email, password);

        // Then
        verify(mockUserAccountRepository, times(1)).existsByEmail(email);
        verify(mockUserAccountRepository, times(1)).save(any(UserAccount.class));

        var savedUserAccount = userAccountCaptor.getValue();
        assertEquals(email, savedUserAccount.getEmail());
    }

    @Test
    void shouldThrowDuplicateEmailExceptionWhenEmailIsDuplicate() {
        // Given
        var duplicateEmail = UserFixtures.email();
        when(mockUserAccountRepository.existsByEmail(duplicateEmail)).thenReturn(true);

        // When & Then
        assertThrows(
                DuplicateEmailException.class,
                () -> customerService.register(duplicateEmail, "testPassword")
        );

        verify(mockUserAccountRepository, times(1)).existsByEmail(duplicateEmail);
        verify(mockUserAccountRepository, never()).save(any(UserAccount.class));
    }
}

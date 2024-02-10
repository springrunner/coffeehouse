package coffeehouse.libraries.base.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "name@domain.com",
            "name@sub.domain.com",
            "verylongname@domain.com",
            "name@verylongdomainpart.com"
    })
    void validEmailFormat(String email) {
        Email.of(email);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "name",
            "@domain.com",
            "name@domain",
            "name@domain@domain.com",
            "name @ domain.com"
    })
    void invalidEmailThrowException(String email) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Email.of(email));
    }
}

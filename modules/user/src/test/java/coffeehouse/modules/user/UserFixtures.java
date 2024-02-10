package coffeehouse.modules.user;

import coffeehouse.libraries.base.crypto.Password;
import coffeehouse.libraries.base.lang.Email;
import net.datafaker.Faker;

public class UserFixtures {

    static final Faker faker = new Faker();

    public static Email email() {
        return Email.of(faker.internet().emailAddress());
    }

    public static Password password() {
        return Password.of(faker.internet().password());
    }
}

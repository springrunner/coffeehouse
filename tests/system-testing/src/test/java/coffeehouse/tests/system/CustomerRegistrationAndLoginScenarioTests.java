package coffeehouse.tests.system;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author springrunner.kr@gmail.com
 */
@SpringBootTest
class CustomerRegistrationAndLoginScenarioTests {

    static final Faker faker = new Faker();
    
    @Value("${system-testing.coffeehouse-server.base-uri}")
    String baseURI;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = baseURI;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
    
    @Test
    void shouldRegisterAndLoginSuccessfully() {
        var email = faker.internet().emailAddress();
        var password = faker.internet().password();

        // customer register
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\": \"%s\",\"password\": \"%s\"}".formatted(email, password))
        .when()
                .post("/customers/register")
        .then()
                .statusCode(200);
        
        // login
        var accessToken = given()
                .contentType(ContentType.JSON)
                .body("{\"identifier\":\"%s\", \"password\":\"%s\"}".formatted(email, password))
        .when()
                .post("/accounts/login")
        .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .extract().path("accessToken");
        
        // get customer info
        given()
                .header("Authorization", "Bearer " + accessToken)
        .when()
                .get("/customers/me")
        .then()
                .statusCode(200)
                .body("email", equalTo(email));        
    }

    @Test
    void shouldReturn400WhenRegisterWithInvalidEmail() {
        var email = "invalidEmail";
        var password = faker.internet().password();

        given()
                .contentType(ContentType.JSON)
                .body("{\"email\": \"%s\",\"password\": \"%s\"}".formatted(email, password))
        .when()
                .post("/customers/register")
        .then()
                .statusCode(500);
    }    
}

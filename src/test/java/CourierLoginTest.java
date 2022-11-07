import Client.CourierClient;
import Model.CreateCourierRequest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {
    private int courierId = 0;
    private CreateCourierRequest randomCourier;
    private Client.CourierClient courier;

    @Before
    public void setUp() {
        courier = new CourierClient("https://qa-scooter.praktikum-services.ru");
        randomCourier = courier.getRandomUser();
        Response response = courier.createCourier(randomCourier);
        response.then().assertThat().statusCode(201);
    }

    @After
    public void tearDown() {
        if (courierId != 0) courier.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Login with valid creds returns 200 and courierId")
    public void courierCanLoginWithValidCreds() {
        Response response = courier.loginCourier(randomCourier);
        courierId = response.then().extract().body().path("id");
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("id", notNullValue());

    }

    @Test
    @DisplayName("Login without login returns 400 error")
    public void courierCantLoginWithEmptyLogin() {
        Response response = courier.loginCourier("", randomCourier.getPassword());
        courierId = courier.getCourierId(randomCourier);
        response.then().assertThat().statusCode(400);
        response.then().assertThat().body("message", is("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Login without password returns 400 error")
    public void courierCantLoginWithEmptyPassword() {
        Response response = courier.loginCourier(randomCourier.getLogin(), "");
        courierId = courier.getCourierId(randomCourier);
        response.then().assertThat().statusCode(400);
        response.then().assertThat().body("message", is("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Login with invalid password returns 404 error")
    public void courierCantLoginWithInvalidPassword() {
        Response response = courier.loginCourier(randomCourier.getLogin(), "Test");
        courierId = courier.getCourierId(randomCourier);
        response.then().assertThat().statusCode(404);
        response.then().assertThat().body("message", is("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Login with invalid login returns 404 error")
    public void courierCantLoginWithInvalidLogin() {
        Response response = courier.loginCourier("Test", randomCourier.getPassword());
        courierId = courier.getCourierId(randomCourier);
        response.then().assertThat().statusCode(404);
        response.then().assertThat().body("message", is("Учетная запись не найдена"));

    }
}

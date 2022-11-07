import Client.CourierClient;
import Model.CreateCourierRequest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class CourierCreateTest {
    private int courierId = 0;
    private Client.CourierClient courier;
    private CreateCourierRequest randomCourier;

    @Before
    public void setUp() {
        courier = new CourierClient("https://qa-scooter.praktikum-services.ru");
        randomCourier = courier.getRandomUser();
    }

    @After
    public void tearDown() {
        if (courierId != 0) courier.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Courier with unique login can be created")
    public void canCreateCourierWithUniqueLogin() {
        Response response = courier.createCourier(randomCourier);
        response.then().assertThat().statusCode(201)
                .and().assertThat().body("ok", is(true));
        courierId = courier.getCourierId(randomCourier);
    }

    @Test
    @DisplayName("Create courier with existed login returns error")
    public void createCourierWithExistedLoginReturnsError() {
        Response response = courier.createCourier(randomCourier);
        response.then().assertThat().statusCode(201);
        response = courier.createCourier(randomCourier);
        response.then().assertThat().statusCode(409);
        response.then().assertThat().body("message", is("Этот логин уже используется. Попробуйте другой."));
        courierId = courier.getCourierId(randomCourier);
    }

    @Test
    @DisplayName("Create courier with empty login returns error")
    public void createCourierWithoutLoginReturnsError() {
        Response response = courier.createCourier("", randomCourier.getPassword(), randomCourier.getFirstName());
        response.then().assertThat().statusCode(400);
        response.then().assertThat().body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Create courier with empty password returns error")
    public void createCourierWithoutPasswordReturnsError() {
        Response response = courier.createCourier(randomCourier.getLogin(), "", randomCourier.getFirstName());
        response.then().assertThat().statusCode(400);
        response.then().assertThat().body("message", is("Недостаточно данных для создания учетной записи"));
    }
}

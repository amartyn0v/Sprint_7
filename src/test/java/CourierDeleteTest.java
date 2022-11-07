import Client.CourierClient;
import Model.CreateCourierRequest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class CourierDeleteTest {

    private int courierId = 0;
    private Client.CourierClient courier;
    private CreateCourierRequest randomCourier;

    @Before
    public void setUp() {
        courier = new CourierClient("https://qa-scooter.praktikum-services.ru");
        randomCourier = courier.getRandomUser();
        Response response = courier.createCourier(randomCourier);
        response.then().assertThat().statusCode(201);
        courierId = courier.getCourierId(randomCourier);
    }

    @After
    public void tearDown() {
        if (courierId != 0) courier.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Delete courier")
    public void deleteCreatedCourier(){
        Response response = courier.deleteCourier(courierId);
        courierId = 0;
        response.then().assertThat().statusCode(200)
                .and().assertThat().body("ok", is(true));
    }

    @Test
    @DisplayName("Delete without courierId returns 400 error")
    @Description("Тест должен падать из-за некорректной реализации API. По документации должно возвращаться 400, возвращается 404")
    public void deleteWithoutCourierIdReturns400Error(){
        Response response = courier.deleteCourier();
        response.then().assertThat().statusCode(400)
                .and().assertThat().body("message", is("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Delete with invalid courierId returns 404 error")
    public void deleteWithInvalidCourierIdReturns404Error(){
        Response response = courier.deleteCourier(0);
        response.then().assertThat().statusCode(404)
                .and().assertThat().body("message", is("Курьера с таким id нет."));
    }


}

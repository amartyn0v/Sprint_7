import Client.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import junitparams.JUnitParamsRunner;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(JUnitParamsRunner.class)
public class OrdersCreateTest {

    OrderClient order;

    @Before
    public void setUp(){
        order = new OrderClient("https://qa-scooter.praktikum-services.ru");
    }

    @Test
    @DisplayName("Сreate order with different colours [parametrized]")
    @Parameters({"[\"BLACK\"]", "[\"GREY\"]", "[\"BLACK\",\"GREY\"]","[]"})
    public void createOrderWithDifferentColours(String[] colours){
        Response response = order.createOrder(colours);
        response.then().assertThat().statusCode(201);
        response.then().assertThat().body("track", notNullValue());
    }
}

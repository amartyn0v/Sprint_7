import Client.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class OrdersTrackTest {
    OrderClient order;
    private int trackId = 0;

    @Before
    public void setUp(){
        order = new OrderClient("https://qa-scooter.praktikum-services.ru");
        Response orderResponse = order.createOrder();
        orderResponse.then().assertThat().statusCode(201);
        trackId = orderResponse.then().extract().body().path("track");
    }

    @Test
    @DisplayName("Get order by valid trackId returns order info")
    public void getOrderByValidTrackIdReturnsOrderInfo(){
        Response response = order.trackOrder(trackId);
        response.then().assertThat().statusCode(200)
                .and()
                .assertThat().body("order.id", notNullValue());
    }

    @Test
    @DisplayName("Get order by invalid trackId returns 404 error")
    public void getOrderByInvalidTrackIdReturns404Error(){
        Response response = order.trackOrder(0);
        response.then().assertThat().statusCode(404)
                .and()
                .assertThat().body("message", is("Заказ не найден"));
    }

    @Test
    @DisplayName("Get order without trackId returns 400 error")
    public void getOrderWithoutTrackIdReturns400Error(){
        Response response = order.trackOrderWithNoTrackId();
        response.then().assertThat().statusCode(400)
                .and()
                .assertThat().body("message", is("Недостаточно данных для поиска"));
    }
}

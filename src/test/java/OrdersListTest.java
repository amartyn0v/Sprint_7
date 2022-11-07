import Client.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class OrdersListTest {
    OrderClient order;

    @Before
    public void setUp(){
        order = new OrderClient("https://qa-scooter.praktikum-services.ru");
    }

    @Test
    @DisplayName("Get orders list returns list of orders")
    public void getOrdersListReturnsOrders(){
        Response response = order.getOrders();
        response.then().assertThat().body("orders", hasSize(greaterThan(1)));
    }
}

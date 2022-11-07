import Client.CourierClient;
import Client.OrderClient;
import Model.CreateCourierRequest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrdersAcceptTest {

    private int courierId = 0;
    private int orderId = 0;
    private int trackId = 0;
    private CreateCourierRequest randomCourier;
    private Client.CourierClient courier;
    private OrderClient order;

    @Before
    public void setUp() {
        courier = new CourierClient("https://qa-scooter.praktikum-services.ru");
        order = new OrderClient("https://qa-scooter.praktikum-services.ru");
        randomCourier = courier.getRandomUser();
        Response orderResponse = order.createOrder();
        orderResponse.then().assertThat().statusCode(201);
        trackId = orderResponse.then().extract().body().path("track");
        orderId = order.trackOrder(trackId).then().extract().body().path("order.id");
        Response courierResponse = courier.createCourier(randomCourier);
        courierResponse.then().assertThat().statusCode(201);
        courierId = courier.getCourierId(randomCourier);
    }

    @After
    public void tearDown() {
        if (courierId != 0) courier.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Accept order returns 200")
    public void acceptOrder(){
        Response response = order.acceptOrder(orderId,courierId);
        response.then().assertThat().statusCode(200)
                .and().assertThat().body("ok", Matchers.is(true));
    }

    @Test
    @DisplayName("Accept order without order ID returns 400 error")
    @Description("Тест должен падать из-за некорректной реализации API. По документации должно возвращаться 400, возвращается 404")
    public void acceptOrderWithoutOrderIdReturns400Error(){
        Response response = order.acceptOrderNoOrderId(courierId);
        response.then().assertThat().statusCode(400)
                .and().assertThat().body("message", Matchers.is("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Accept order with invalid order ID returns 404 error")
    public void acceptOrderWithInvalidOrderIdReturns404Error(){
        Response response = order.acceptOrder(0,courierId);
        response.then().assertThat().statusCode(404)
                .and().assertThat().body("message", Matchers.is("Заказа с таким id не существует"));
    }

    @Test
    @DisplayName("Accept order with invalid courier ID returns 404 error")
    public void acceptOrderWithInvalidCourierIdReturns404Error(){
        Response response = order.acceptOrder(orderId,0);
        response.then().assertThat().statusCode(404)
                .and().assertThat().body("message", Matchers.is("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Accept order without courier ID returns 400 error")
    public void acceptOrderWithoutCourierIdReturns400Error(){
        Response response = order.acceptOrderNoCourierId(orderId);
        response.then().assertThat().statusCode(400)
                .and().assertThat().body("message", Matchers.is("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Accept order without courier and order ID returns 400 error")
    @Description("Тест должен падать из-за некорректной реализации API. По документации должно возвращаться 400, возвращается 404")
    public void acceptOrderWithoutCourierAndOrderIdReturns400Error(){
        Response response = order.acceptOrderNoCourierNoOrderId();
        response.then().assertThat().statusCode(400)
                .and().assertThat().body("message", Matchers.is("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Accept order already in process returns 409 error")
    public void acceptOrderAlreadyInProcessReturns409Error(){
        Response response = order.acceptOrder(orderId,courierId);
        response.then().assertThat().statusCode(200);
        response = order.acceptOrder(orderId,courierId);
        response.then().assertThat().statusCode(409)
                .and().assertThat().body("message", Matchers.is("Этот заказ уже в работе"));
    }

}

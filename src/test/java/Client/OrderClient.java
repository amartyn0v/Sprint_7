package Client;
import Model.CreateOrderRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;

public class OrderClient {

    private String firstName = "Naruto"+System.currentTimeMillis()/1000;;
    private String lastName = "Uchiha"+System.currentTimeMillis()/1000;;
    private String address = "Konoha, 142 apt."+System.currentTimeMillis()/1000;;
    private String metroStation = "4";
    private String phone = "+7 800 355 35 35";
    private int rentTime = 5;
    private String deliveryDate = "2020-06-06";
    private String comment = "Saske, come back to Konoha"+System.currentTimeMillis()/1000;;
    private String[] colours = {"BLACK"};

    public OrderClient(String baseUrl) {
        RestAssured.baseURI = baseUrl;
    }

    public Response createOrder(String[] colour) {
        CreateOrderRequest json = new CreateOrderRequest(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colour);
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .body(json)
                .when()
                .post("/api/v1/orders");
    }
    public Response createOrder() {
        CreateOrderRequest json = new CreateOrderRequest(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colours);
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .body(json)
                .when()
                .post("/api/v1/orders");
    }
    public Response getOrders() {
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .when()
                .get("/api/v1/orders");
    }

    public Response acceptOrder(int orderId, int courierId) {
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .queryParam("courierId", courierId)
                .when()
                .put(format("/api/v1/orders/accept/%d", orderId));
    }

    public Response acceptOrderNoOrderId(int courierId) {
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .when()
                .queryParam("courierId", courierId)
                .put("/api/v1/orders/accept/");
    }
    public Response acceptOrderNoCourierId(int orderId) {
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .when()
                .put(format("/api/v1/orders/accept/%d", orderId));
    }

    public Response acceptOrderNoCourierNoOrderId() {
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .when()
                .put("/api/v1/orders/accept/");
    }

    public Response trackOrder(int track) {
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .queryParam("t", track)
                .when()
                .get("/api/v1/orders/track");
    }

    public Response trackOrderWithNoTrackId() {
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .when()
                .get("/api/v1/orders/track");
    }
}

package Client;
import Model.CreateCourierRequest;
import Model.LoginCourierRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;

public class CourierClient {
    public CourierClient(String baseUrl){
        RestAssured.baseURI = baseUrl;
    }

    public CreateCourierRequest getRandomUser(){
        String sampleLogin = "TestYUser"+System.currentTimeMillis()/1000;
        String samplePassword = "TestYPassword"+System.currentTimeMillis()/1000;
        String sampleFirstName = "TestYFirstName"+System.currentTimeMillis()/1000;
        return new CreateCourierRequest(sampleLogin, samplePassword, sampleFirstName);
    }
    public Response createCourier(CreateCourierRequest courier){
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    public Response createCourier(String login, String password, String firstName){
        CreateCourierRequest json = new CreateCourierRequest(login, password, firstName);
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .body(json)
                .when()
                .post("/api/v1/courier");
    }

    public Response loginCourier(CreateCourierRequest courier){
        LoginCourierRequest json = new LoginCourierRequest(courier.getLogin(), courier.getPassword());
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .body(json)
                .when()
                .post("/api/v1/courier/login");
    }

    public Response loginCourier(String login, String password){
        LoginCourierRequest json = new LoginCourierRequest(login, password);
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .body(json)
                .when()
                .post("/api/v1/courier/login");
    }

    public Response deleteCourier(int id){
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .when()
                .delete(format("/api/v1/courier/%s",id));
    }

    public Response deleteCourier(){
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .when()
                .delete("/api/v1/courier/");
    }

    public int getCourierId(CreateCourierRequest courier){
        LoginCourierRequest json = new LoginCourierRequest(courier.getLogin(), courier.getPassword());
        Response response = given()
                .header("Content-type", "application/json; charset=utf-8")
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        if (response.getStatusCode()==200){
            return response.jsonPath().get("id");
        }else return 0;
    }
}

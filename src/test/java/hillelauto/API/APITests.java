package hillelauto.API;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class APITests {

    @AfterMethod (alwaysRun = true)
    public void contentType() {
        System.out.println("aftermethod");
        given().then().contentType("application/xml");
    }

    @Test (description = "Start server state", groups = "Sanity", priority = -3)
    //Начальное состояние сервера (при перезапуске):
    public void checkStartResponse() {
        given().when().get(Vars.host + "/Hyderabad").then().body("City", equalTo("Hyderabad"));
    }

    @Test (description = "Start server state", groups = "Sanity", priority = -3)
    //Начальное состояние сервера (при перезапуске):
    public void fd() {
        given().when().get(Vars.host + "/Hyderabad").then().body(equalTo(Vars.response));
    }

    @Test
    public void invalidContentType() {
        given().contentType("").when().get().then().statusCode(401);
    }

    @Test
    public void validContentType() {
        given().contentType("application/json").when().get().then().statusCode(200);
    }

}

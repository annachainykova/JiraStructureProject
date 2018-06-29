package hillelauto.API;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class API {


    public String GetResponseOnSetup() {
        RestAssured.baseURI = Vars.host;
        RequestSpecification httpRequest = given();
        Response response = httpRequest.request(Method.GET, "/Hyderabad");
        return response.getBody().asString();
    }

    public void blabla() {
        given().when().get("Hyberabad").body().equals(Vars.response);
    }
}

package hillelauto.API;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class NewClass {
    String bodyPet ="";


    @BeforeTest
    public void setUp(){

    }


    @Test
    public void postPet() {
        RestAssured.given()
                .baseUri("http://petstore.swagger.io")
                .basePath("/v2/pet/")
                .header("api_key", "1q2w3e4r5")
                .body("{\n" +
                        "        \"id\": 7678877,\n" +
                        "        \"name\": \"Lion 2An\",\n" +
                        "        \"photoUrls\": [], \n" +
                        "        \"status\": \"pending\"\n" +
                        "    }")
                .contentType("application/json")
                .when().post()
                .then()
                .contentType("application/json")
                .assertThat().body("id", equalTo(7678877));
    }

    



}

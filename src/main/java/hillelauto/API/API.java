package hillelauto.API;

import io.restassured.http.ContentType;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class API {
    public static ArrayList <String> specificData(String data) {
        return given().
                contentType(ContentType.JSON).
        when().
                get(Vars.basePath).
        then().
                contentType("application/json").
        extract().
                path(data);
    }
}

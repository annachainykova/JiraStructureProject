package hillelauto.API;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;


//to do
// 1. Объеденить методы создания юзера (а) без роли = студент, б) с ролью админа = админ, в) с некорректной ролью = 404 или какая-то там
// 2. Объеденить метода редактирования юзера а) позитив б) негатив
// 3. Вынести отдельно метод GET user list с созданием array на нужный параметр
// 4. Посмотреть как Роберт проверяет 



public class APITests {

    public String newUserID;


    @BeforeTest (groups = "Sanity")
    public void basicCred() {
        //
        System.out.println("beforemethod is running");
        RestAssured.baseURI = Vars.host;
        RestAssured.port = Vars.port; //8. Сервер должен запускаться на порту 20007
        RestAssured.basePath = Vars.basePath;
        RestAssured.requestSpecification = new RequestSpecBuilder().
                addHeader("Content-Type", "application/json").
                setContentType("application/json"). //1. Во всех запросах Content-Type: application/json
                setAccept("application/json").
                build().
                log().all();
    }

    @BeforeTest (groups = "Negative")
    public void basicCredForNegative() {
        //
        RestAssured.baseURI = Vars.host;
        RestAssured.port = Vars.port; //8. Сервер должен запускаться на порту 20007
        RestAssured.basePath = Vars.basePath;
        RestAssured.requestSpecification = new RequestSpecBuilder().
                 // 10. Если клиент передает некорректный заголовок Content-Type либо он отсутствует - возвращать 401 ошибку
                build().
                log().all();
    }

   @Test (description = "Requirement 2 - Get users  ", groups = {"Sanity", "Positiv"})
   public void set() {
        given().
        expect().
                statusCode(200).
                contentType("application/json").
                body(matchesJsonSchemaInClasspath("user_schema.json")).
        when().
                get("");
   }

   @Test (description = "Requirement 4 - Create new user(Student)", groups = "Sanity")
   public void createUser() {
       JSONObject jsonObject = new JSONObject();
       jsonObject.put("name", "Anna");
       jsonObject.put("phone", "+380934546469");

       newUserID =  given().
                contentType("application/json").
                body(jsonObject.toJSONString()).
       when().
                post("").
       then().
                assertThat().statusCode(200).
                // 9. Во всех ответах должен присутстовать заголовок Content-Type application/json
                assertThat().contentType(ContentType.JSON).
                //6. Создание нового пользователя было реализовано методом POST на winow.crudURL,
                // в ответ приходил id новой созданной сущности
                assertThat().body("$", hasKey("id")).
                // 6. Не передавая роль создаеться студент
                assertThat().body("role", equalTo("Student")).
               extract().
                       path("id");

       ArrayList<String> allID =
           given().
                    contentType(ContentType.JSON).
           when().
                    get("").
           then().
               extract().
                        path("id");

       Assert.assertTrue(allID.contains(newUserID));

   }

   @Test(description = "Requirement 3 - Edit user", dependsOnMethods = "createUser", groups = "Sanity")
   public void editUser() {
       JSONObject jsonObject = new JSONObject();
       jsonObject.put("name", "Anna Chainykova");

       given().
               contentType(ContentType.JSON).
               body(jsonObject.toJSONString()).
       when().
               put("/" + newUserID).
       then().
               assertThat().statusCode(200).
               assertThat().contentType(ContentType.JSON);

       ArrayList<String> allNames =
               given().
                       contentType(ContentType.JSON).
                       when().
                       get("").
                       then().
                       extract().
                       path("name");

       Assert.assertTrue(allNames.contains("Anna Chainykova"));

   }

   @Test(description = "Requirement 7 - Delete user", dependsOnMethods = "createUser", groups = "Sanity")
   public void deleteUser(){
       given().
               contentType(ContentType.JSON).
       when().
               delete("/" + newUserID).
       then().
                assertThat().statusCode(204).
                assertThat().contentType("application/json");

       ArrayList<String> allID = given().
               contentType(ContentType.JSON).
       when().
               get("").
       then().
               extract().path("id");

       Assert.assertFalse(allID.contains(newUserID));
   }

   //








   @Test(description = "Requirement 10 - Incorrect content-type in request", groups = "Negative")
   public void incorrectContentType() {
       given().
               expect().
               statusCode(401).
       when().
               get("");
   }

































    @AfterMethod (alwaysRun = true)
    public void contentType() {
        System.out.println("aftermethod");
        given().then().contentType("application/json");
    }

    @Test (description = "Start server state", groups = "Sanity", priority = -3)
    //Начальное состояние сервера (при перезапуске):
    public void checkStartResponse() {
        //given().when().get(Vars.host).then().body();
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

    @Test
    public void setUp() {
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.get(Vars.host);

        // Retrieve the body of the Response
        ResponseBody body = response.getBody();

        // By using the ResponseBody.asString() method, we can convert the  body
        // into the string representation.
        System.out.println("Response Body is: " + body.asString());
    }
}

package hillelauto.API;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;


//to do
// 3. Вынести отдельно метод GET user list с созданием array на нужный параметр


public class APITests {

    public String studentID;
    public String adminID;


    @BeforeTest(alwaysRun = true)
    public void basicCred() {
        RestAssured.baseURI = Vars.host;
        RestAssured.port = Vars.port; //8. Сервер должен запускаться на порту 20007
    }

    @Test(description = "Requirement 11 - Start server state", priority = -3)
    public void startServerState() {
        given().
                contentType("application/json").
        expect().
                body(equalTo(Vars.baseBody)).
        when().
                get(Vars.basePath);
    }


    @Test(description = "Requirement 2 - Get user list", groups = {"Sanity"})
    public void getUserList() {
        given().
                contentType("application/json").
        expect().
                statusCode(200).
                contentType("application/json").
                body(matchesJsonSchemaInClasspath("user_schema.json")).
        when().
                get(Vars.basePath);
    }

    @Test(description = "Requirement 4 - Create new user(Student)", groups = "Sanity")
    public void createUser() {
        studentID =
                given().
                        contentType("application/json").
                        body(APIjsonObjects.createStudent().toJSONString()).
                when().
                        post(Vars.basePath).
                then().
                        assertThat().statusCode(200).
                                assertThat().contentType(ContentType.JSON).
                                assertThat().body("$", hasKey("id")).
                                assertThat().body("role", equalTo("Student")).
                extract().
                        path("id");

        ArrayList<String> allID =
                given().
                        contentType(ContentType.JSON).
                when().
                        get(Vars.basePath).
                then().
                extract().
                        path("id");

        Assert.assertTrue(allID.contains(studentID));

    }

    @Test(description = "Requirement 3 - Edit user", dependsOnMethods = "createUser", groups = "Sanity")
    public void editUser() {
        given().
                contentType(ContentType.JSON).
                body(APIjsonObjects.editUsers().toJSONString()).
        when().
                put(Vars.basePath + "/" + studentID).
        then().
                assertThat().statusCode(200).
                assertThat().contentType(ContentType.JSON);

        ArrayList<String> allNames =
                given().
                        contentType(ContentType.JSON).
                when().
                        get(Vars.basePath).
                then().
                extract().
                        path("name");

        Assert.assertTrue(allNames.contains("Chainykova Anna"));
    }

    @Test(description = "Requirement 7 - Delete user", dependsOnMethods = "createUser", groups = "Sanity")
    public void deleteUser() {
        given().
                contentType(ContentType.JSON).
                when().
                delete(Vars.basePath + "/" + studentID).
                then().
                assertThat().statusCode(204).
                assertThat().contentType("application/json");

        ArrayList<String> allID = given().
                contentType(ContentType.JSON).
                when().
                get(Vars.basePath).
                then().
                extract().path("id");

        Assert.assertFalse(allID.contains(studentID));
    }

    @Test(description = "Requirement 6. Create administrator", groups = "Sanity")
    public void createAdmin() {
        adminID =
                given().
                        contentType("application/json").
                        body(APIjsonObjects.createNotStudent().toJSONString()).
                when().
                        post(Vars.basePath).
                then().
                        assertThat().statusCode(200).
                        // 9. Во всех ответах должен присутстовать заголовок Content-Type application/json
                                assertThat().contentType(ContentType.JSON).
                        //6. Создание нового пользователя было реализовано методом POST на winow.crudURL,
                        // в ответ приходил id новой созданной сущности
                                assertThat().body("$", hasKey("id")).
                        // 6. Не передавая роль создаеться студент
                                assertThat().body("role", equalTo("Administrator")).
                extract().
                        path("id");

        ArrayList<String> allID =
                given().
                        contentType(ContentType.JSON).
                when().
                        get(Vars.basePath).
                then().
                extract().
                        path("id");

        Assert.assertTrue(allID.contains(adminID));
    }

    @Test(description = "Requirement 5 - Edit admin", dependsOnMethods = "createAdmin", groups = "Sanity")
    public void editAdmin() {
        given().
                contentType(ContentType.JSON).
                body(APIjsonObjects.editUsers().toJSONString()).
        when().
                put(Vars.basePath + "/" + adminID).
        then().
                assertThat().statusCode(200).
                assertThat().contentType(ContentType.JSON);

        //При сохранении администратора надо было делать GET на /refreshAdmins на том же хосте где был хост
        given().
                contentType(ContentType.JSON).
        when().
                get("/refreshAdmins");

        ArrayList<String> allNames =
                given().
                        contentType(ContentType.JSON).
                when().
                        get(Vars.basePath).
                then().
                extract().
                        path("name");

        Assert.assertTrue(allNames.contains("Anna Chainykova"));
    }

    @Test(description = "Requirement 6. Create user with Invalid role")
    public void createUserInvalidRole() {
        given().
                contentType("application/json").
                body(APIjsonObjects.createUserInvalidRole().toJSONString()).
        when().
                post(Vars.basePath).
        then().
                assertThat().statusCode(401).
                assertThat().contentType(ContentType.JSON);
    }

    @Test(description = "Requirement 13. Edit invalid user")
    public void editInvalidID() {
        given().
                contentType("application/json").
                body(APIjsonObjects.createStudent().toJSONString()).
        when().
                put(Vars.basePath + "/" + "invalidID").
        then().
                assertThat().statusCode(404).
                assertThat().contentType(ContentType.JSON);
    }


    @Test(description = "Requirement 10 - Incorrect content-type in request")
    public void incorrectContentType() {
        given().
        expect().
                statusCode(401).
        when().
                get(Vars.basePath);
    }


}

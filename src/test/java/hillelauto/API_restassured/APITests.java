package hillelauto.API_restassured;

import hillelauto.Helper.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;


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
         List <User> users = given().
                contentType("application/json").
        when().
                get(Vars.basePath).
        then().
     //           assertThat().contentType("application/json").
//                assertThat().body("id", hasItem("1")).
                 extract().body().jsonPath().getList("", User.class);

         for(User user : users){
             System.out.println(user.toString());
         }
                //assertThat().body(equalTo(APIjsonObjects.startServer()));
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

        Assert.assertTrue(API.specificData("id").contains(studentID));

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

        Assert.assertTrue(API.specificData("name").contains("Chainykova Anna"));
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

        Assert.assertFalse(API.specificData("id").contains(studentID));
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
                        assertThat().contentType(ContentType.JSON).
                        assertThat().body("$", hasKey("id")).
                        assertThat().body("role", equalTo("Administrator")).
                extract().
                        path("id");


        Assert.assertTrue(API.specificData("id").contains(adminID));
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


        Assert.assertTrue(API.specificData("name").contains("Anna Chainykova"));
    }

    @Test(description = "Requirement 12. Create user with Invalid role")
    public void createUserInvalidRole() {
        given().
                contentType("application/json").
                body(APIjsonObjects.createUserInvalidRole().toJSONString()).
        when().
                post(Vars.basePath).
        then().
                assertThat().statusCode(401);
    }

    @Test(description = "Requirement 13. Edit invalid user")
    public void editInvalidID() {
        given().
                contentType("application/json").
                body(APIjsonObjects.createStudent().toJSONString()).
        when().
                put(Vars.basePath + "/" + "invalidID").
        then().
                assertThat().statusCode(404);
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

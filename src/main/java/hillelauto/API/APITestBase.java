package hillelauto.API;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class APITestBase {

    @BeforeClass (alwaysRun = true)
    public static void setup() {
        RestAssured.baseURI = Vars.host;

    }
}

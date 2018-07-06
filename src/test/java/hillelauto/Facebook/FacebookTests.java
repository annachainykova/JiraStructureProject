package hillelauto.Facebook;

import hillelauto.Facebook.Pages.LoginPage;
import hillelauto.Facebook.Pages.ProfilePage;
import hillelauto.Tools;
import hillelauto.WebDriverTestBase;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

public class FacebookTests extends WebDriverTestBase{
    private LoginPage loginPage;
    private ProfilePage profilePage;


    @BeforeClass(alwaysRun = true)
    public void setPages() {
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        profilePage = PageFactory.initElements(driver, ProfilePage.class);

    }
    @Test
    public void login() {
        loginPage.login();
    }

    @Test (dependsOnMethods = "login")
    public void fac() throws InterruptedException, IOException {
        List <String> lines = Tools.lineOfFile("new.csv");
        ArrayList <String> newLines = new ArrayList<>();
        for (String line : lines) {
            newLines.add(profilePage.getId(line));
        }
        Files.write(Paths.get("newfile.txt"), newLines);

    }

    @Test
    public void api() {
        String bodyresp = given().get("https://www.facebook.com/profile.php?id=100016584043282")
                .then()
                .extract().body().htmlPath().prettyPrint().toString();
        String patternString = ".*fb://profile/(\\d+)\".*";

        Pattern pattern = Pattern.compile(patternString);

        Matcher matcher = pattern.matcher(bodyresp);
        if(matcher.find()){
            String ID = matcher.group(1);
            System.out.println(ID);
        }
    }

    @Test
    public void apit() throws IOException, InterruptedException {
        List <String> lines = Tools.lineOfFile("new.csv");
        ArrayList <String> newLines = new ArrayList<>();
        for (String line : lines) {
            newLines.add(profilePage.getIDByAPI(line));
        }
        Files.write(Paths.get("newfile.txt"), newLines);
    }
}

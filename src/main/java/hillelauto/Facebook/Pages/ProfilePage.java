package hillelauto.Facebook.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

public class ProfilePage {
    private final WebDriver driver;

    @FindBy(css = "a[data-referrerid]")
    private WebElement IDElement;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    public String getId(String line) throws InterruptedException {
        driver.get(line.split(",")[1]);
        if(driver.getTitle().equals(line.split(",")[0])){
            return line.split(",")[0] + IDElement.getAttribute("data-referrerid");
        }
        else {
            return "There is no such user";
        }
    }

    public String getIDByAPI(String line) {
        String bodyresp = given().get(line)
                .then()
                .extract().body().htmlPath().prettyPrint().toString();
        String patternString = ".*fb://profile/(\\d+)\".*";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(bodyresp);
        if(matcher.find()){
            return matcher.group(1);
        } else {
            return " There is no such user";
        }
    }
}

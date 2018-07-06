package hillelauto.Facebook.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProfilePage {
    private final WebDriver driver;

    @FindBy(css = "a[data-referrerid]")
    private WebElement IDElement;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    public String getId(String line) {
        driver.get(line.split(",")[1]);
        if(driver.getTitle().equals(line.split(",")[0])){
            return line.split(",")[0] + IDElement.getAttribute("data-referrerid");
        }
        else {
            return "There is no such user";
        }
    }
}

package hillelauto.Facebook.Pages;
import hillelauto.Facebook.Facebook_Vars;
import hillelauto.Tools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage {
    private final WebDriver driver;

    private final By inputUsername = By.cssSelector("input[id='email']");
    private final By inputPassword = By.cssSelector("input[id='pass']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void login() {
        driver.get(Facebook_Vars.baseURL);

        Tools.clearAndFill(inputUsername, Facebook_Vars.username);
        Tools.clearAndFill(inputPassword, Facebook_Vars.password).submit();
    }

}

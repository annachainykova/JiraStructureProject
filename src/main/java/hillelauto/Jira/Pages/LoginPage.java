package hillelauto.Jira.Pages;

import hillelauto.Jira.JiraVars;
import hillelauto.Tools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;


import java.awt.*;
import java.io.IOException;
import java.util.List;

public class LoginPage {    
    private final WebDriver driver;
    
    private final By inputUsername = By.cssSelector("input[id='login-form-username']");
    private final By inputPassword = By.cssSelector("input[id='login-form-password']");
    
    @FindBy(css = "a[id='header-details-user-fullname']")
    private WebElement buttonProfile;
    @FindBy(css = "div[id='usernameerror']")
    private List<WebElement> messageError;
    
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }
    
    public void successfulLogin() {
        login(true);
        Assert.assertEquals(JiraVars.username, buttonProfile.getAttribute("data-username"));
    }
    
    public void failureLogin() throws IOException, AWTException {
        login(false);
        Assert.assertTrue(messageError.size() != 0);
    }
    
    public void login(boolean successful) {
        driver.get(JiraVars.baseURL);

        Tools.clearAndFill(inputUsername, successful ? JiraVars.username : "BadLogin");
        Tools.clearAndFill(inputPassword, JiraVars.password).submit();
    }

    public void successfulAdminLogin() {
        driver.get(JiraVars.baseURL);

        Tools.clearAndFill(inputUsername, JiraVars.adminUsername);
        Tools.clearAndFill(inputPassword, JiraVars.adminPassword).submit();

        Assert.assertEquals(JiraVars.adminUsername, buttonProfile.getAttribute("data-username"));
    }
}

package hillelauto.Jira.Pages;

import hillelauto.Jira.JiraVars;
import hillelauto.Tools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class AdminPanelPage {
    private final WebDriver driver;

    @FindBy(css = "a[id='create_user']")
    WebElement createUserButton;


    private final By inputUserEmail = By.cssSelector("input[id='user-create-email']");
    private final By inputUserFullname = By.cssSelector("input[id='user-create-fullname']");
    private final By inputUserUsername = By.cssSelector("input[id='user-create-username']");
    private final By inputUserPassword = By.cssSelector("input[id='password']");

    private final By inputFilterUsers = By.cssSelector("input[id='user-filter-userSearchFilter']");


    public AdminPanelPage(WebDriver driver) {
        this.driver = driver;
    }

    public void createUser() {
        createUserButton.click();
        Tools.clearAndFill(inputUserEmail, JiraVars.createUserEmail);
        Tools.clearAndFill(inputUserFullname, JiraVars.createUser);
        Tools.clearAndFill(inputUserUsername, JiraVars.createUser);
        Tools.clearAndFill(inputUserPassword, JiraVars.createUser).submit();

        Tools.clearAndFill(inputFilterUsers, JiraVars.createUser).submit();
        Assert.assertTrue(driver.findElements(By.cssSelector(("a[id='" + JiraVars.createUser + "']"))).size() != 0);

    }
}

package hillelauto.Jira.Pages;

import hillelauto.Jira.JiraVars;
import hillelauto.Tools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class MainPage {
    private final WebDriver driver;

    @FindBy(css = "a[id='admin_menu']")
    WebElement adminMenuDropDown;
    @FindBy(css = "a[id='admin_users_menu']")
    WebElement adminUsersMenu;
    private final By sudoAdminPassword = By.cssSelector("input[id='login-form-authenticatePassword']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openAdminPanelUserPage(){
        adminMenuDropDown.click();
        adminUsersMenu.click();
        Tools.clearAndFill(sudoAdminPassword, JiraVars.adminPassword).submit();

        Assert.assertEquals(driver.getTitle(), JiraVars.adminUserPageTitle);
    }
}

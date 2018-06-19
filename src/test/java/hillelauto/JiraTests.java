package hillelauto;
import hilleauto.Jira.Pages.AdminPanelPage;
import hilleauto.Jira.Pages.IssuePage;
import hilleauto.Jira.Pages.LoginPage;

import hilleauto.Jira.Pages.MainPage;
import hilleauto.WebDriverTestBase;


import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class JiraTests extends WebDriverTestBase{
    private LoginPage loginPage;
    private IssuePage issuePage;
    private MainPage mainPage;
    private AdminPanelPage adminPanelPage;

    @BeforeClass(alwaysRun = true)
    public void setLoginPage() {
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        issuePage = PageFactory.initElements(driver, IssuePage.class);
        mainPage = PageFactory.initElements(driver, MainPage.class);
        adminPanelPage = PageFactory.initElements(driver, AdminPanelPage.class);
        System.out.println("Jira Pages Initialized");
    }

    @Test(description = "1. Invalid Login", priority = -2)
    public void failureLogin() {
        loginPage.failureLogin();
    }

    @Test(description = "2. Valid Login", groups = {"Sanity", "Users"})
    public void successfulLogin() {
        loginPage.successfulLogin();
    }

    @Test(description = "3. Create Issue", dependsOnMethods = {"successfulLogin"}, groups = {"Sanity", "Issues", "Users"})
    public void createIssue() {
        issuePage.createIssue();
    }

    @Test(description = "4. Open Issue", dependsOnMethods = {"createIssue"},  groups = {"Sanity", "Issues", "Users"})
    public void openIssue() {
        issuePage.openIssue();
    }

    @Test(description = "5. Upload File", dependsOnMethods = {"openIssue"}, groups = {"Sanity", "Issues", "Users"})
    public void uploadFile() {
        issuePage.setInputUploadAttachment();
    }

    @Test(description = "6. Download FIle", dependsOnMethods = {"uploadFile"},  groups = {"Sanity", "Issues", "Users"})
    public void downloadFile() throws IOException {
        issuePage.downloadAttachment();
    }

    @Test(description = "Admin. Valid Login", priority = -1, groups = {"Sanity", "Admin"})
    public void successfulAdminLogin() {
        loginPage.successfulAdminLogin();
    }

    @Test(description = "Admin. Open Admin Panel for Users", dependsOnMethods = {"successfulAdminLogin"}, groups = {"Admin"})
    public void openAdminPanelUserPage() {
        mainPage.openAdminPanelUserPage();
    }

    @Test(description = "Admin. Create User", dependsOnMethods = {"openAdminPanelPage"}, groups = {"Admin"})
    public void adminCreateUser() {
        adminPanelPage.createUser();
    }
}

package hillelauto;

import hilleauto.Jira.Pages.IssuePage;
import hilleauto.Jira.Pages.LoginPage;
import hilleauto.WebDriverTestBase;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class JiraTests extends WebDriverTestBase{
    private LoginPage loginPage;
    private IssuePage issuePage;

    @BeforeClass(alwaysRun = true)
    public void setLoginPage() {
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        issuePage = PageFactory.initElements(driver, IssuePage.class);
        System.out.println("Jira Pages Initialized");
    }

    @Test(description = "1. Invalid Login", priority = -1)
    public void failureLogin() {
        loginPage.failureLogin();
    }

    @Test(description = "2. Valid Login", groups = "Sanity")
    public void successfulLogin() {
        loginPage.successfulLogin();
    }

    @Test(description = "3. Create Issue", dependsOnMethods = {"successfulLogin"}, groups = {"Sanity", "Issues"})
    public void createIssue() {
        issuePage.createIssue();
    }

    @Test(description = "4. Open Issue", dependsOnMethods = {"createIssue"},  groups = {"Sanity", "Issues"})
    public void openIssue() {
        issuePage.openIssue();
    }

    @Test(description = "5. Upload File", dependsOnMethods = {"openIssue"}, groups = {"Sanity", "Issues"})
    public void uploadFile() {
        issuePage.setInputUploadAttachment();
    }

    @Test(description = "6. Download FIle", dependsOnMethods = {"uploadFile"},  groups = {"Sanity", "Issues"})
    public void downloadFile() {
        issuePage.downloadAttachment();
    }
}

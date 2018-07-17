package hillelauto.Jira;
import hillelauto.Jira.Pages.AdminPanelPage;
import hillelauto.Jira.Pages.IssuePage;
import hillelauto.Jira.Pages.LoginPage;
import hillelauto.Jira.Pages.MainPage;

import hillelauto.WebDriverTestBase;
import org.openqa.selenium.support.PageFactory;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.IOException;


public class JiraTests extends WebDriverTestBase {
    private LoginPage loginPage;
    private IssuePage issuePage;
    private MainPage mainPage;
    private AdminPanelPage adminPanelPage;

    private int runID;
    private int testCaseID;

    @BeforeClass(alwaysRun = true)
    public void setPages() {
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        issuePage = PageFactory.initElements(driver, IssuePage.class);
        mainPage = PageFactory.initElements(driver, MainPage.class);
        adminPanelPage = PageFactory.initElements(driver, AdminPanelPage.class);
        System.out.println("Jira Pages Initialized");
    }


//    @BeforeClass(groups = {"Login"})
//    public void createTestRun() throws IOException, APIException {
//        Map data = new HashMap();
//        data.put("include_all", true);
//        data.put("name", "This is a new test run");
//        JSONObject c = (JSONObject) client.sendPost("add_run/7", data);
//        runID = Math.toIntExact((Long) c.get("id"));
//    }
//
//
//    @AfterClass(groups = {"Login"})
//    public void closeTestRun() throws IOException, APIException {
//        Map data = new HashMap();
//        JSONObject c = (JSONObject) client.sendPost("close_run/" + runID, data);
//        c.get("is_completed");
//    }



//    @AfterMethod( groups = {"Login"})
//    public void takeScreenShot(ITestResult result) throws IOException, AWTException, APIException {
//        ITestNGMethod instance = result.getMethod();
//        String testName = instance.getDescription();
//
//        Pattern pattern = Pattern.compile("(\\d+)\\.{1}.*");
//        Matcher matcher = pattern.matcher(testName);
//
//        if (matcher.find()) {
//            testCaseID = Integer.parseInt(matcher.group(1));
//            Map data = new HashMap();
//            if (result.isSuccess()) {
//                data.put("status_id", new Integer(1));
//            } else {
//                data.put("status_id", new Integer(5));
//            }
//            JSONObject r = (JSONObject) client.sendPost("add_result_for_case/" + runID + "/" + testCaseID, data);
//        }
//    }

    @Test(description = "45. Invalid Login", priority = -2, groups = {"Login"})
    public void failureLogin() throws IOException, AWTException {
        loginPage.failureLogin();
    }

    @Test(description = "46. Valid Login", groups = {"Sanity", "Users", "Login"})
    public void successfulLogin() {
        loginPage.successfulLogin();
    }

//    @Test(description = "3. Create Issue", dependsOnMethods = {"successfulLogin"}, groups = {"Sanity", "Issues", "Users"})
//    public void createIssue() {
//        issuePage.createIssue();
//    }
//
//    @Test(description = "4. Open Issue", dependsOnMethods = {"createIssue"},  groups = {"Sanity", "Issues", "Users"})
//    public void openIssue() {
//        issuePage.openIssue();
//    }
//
//    @Test(description = "5. Upload File", dependsOnMethods = {"openIssue"}, groups = {"Sanity", "Issues", "Users"})
//    public void uploadFile() {
//        issuePage.setInputUploadAttachment();
//    }
//
//    @Test(description = "6. Download FIle", dependsOnMethods = {"uploadFile"},  groups = {"Sanity", "Issues", "Users"})
//    public void downloadFile() throws IOException {
//        issuePage.downloadAttachment();
//    }
//
//    @Test(description = "Admin. Valid Login", priority = -1, groups = {"Sanity", "Admin", "Login"})
//    public void successfulAdminLogin() {
//        loginPage.successfulAdminLogin();
//    }
//
//    @Test(description = "Admin. Open Admin Panel for Users", dependsOnMethods = {"successfulAdminLogin"}, groups = {"Admin"})
//    public void openAdminPanelUserPage() {
//        mainPage.openAdminPanelUserPage();
//    }
//
//    @Test(description = "Admin. Create User", dependsOnMethods = {"openAdminPanelUserPage"}, groups = {"Admin"})
//    public void adminCreateUser() {
//        adminPanelPage.createUser();
//    }



}

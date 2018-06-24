package hillelauto;
import hilleauto.Jira.APIClient;
import hilleauto.Jira.APIException;
import hilleauto.Jira.Pages.AdminPanelPage;
import hilleauto.Jira.Pages.IssuePage;
import hilleauto.Jira.Pages.LoginPage;

import hilleauto.Jira.Pages.MainPage;
import hilleauto.Tools;
import hilleauto.WebDriverTestBase;


import org.json.simple.JSONObject;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestNGUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.SystemColor.info;

public class JiraTests extends WebDriverTestBase{
    private LoginPage loginPage;
    private IssuePage issuePage;
    private MainPage mainPage;
    private AdminPanelPage adminPanelPage;

    private int runID;

    @BeforeClass(alwaysRun = true)
    public void setPages() {
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        issuePage = PageFactory.initElements(driver, IssuePage.class);
        mainPage = PageFactory.initElements(driver, MainPage.class);
        adminPanelPage = PageFactory.initElements(driver, AdminPanelPage.class);
        System.out.println("Jira Pages Initialized");
    }

    @BeforeClass(groups = {"Login"})
    public void createTestRun() throws IOException, APIException {
        APIClient client = new APIClient("https://hillelmanold.testrail.io/");
        client.setUser("rvalek@intersog.com");
        client.setPassword("hillel");
        Map data = new HashMap();
        data.put("include_all", true);
        data.put("name", "This is a new test run");
        JSONObject c = (JSONObject) client.sendPost("add_run/7", data);
        Long id = (Long) c.get("id");
        runID = Math.toIntExact(id);
    }

//    @AfterClass(groups = {"Login"})
//    public void closeTestRun() throws IOException, APIException {
//        APIClient client = new APIClient("https://hillelmanold.testrail.io/");
//        client.setUser("rvalek@intersog.com");
//        client.setPassword("hillel");
//        Map data = new HashMap();
//        JSONObject c = (JSONObject) client.sendPost("close_run/" + runID, data);
//        c.get("is_completed");
//    }



    @AfterMethod( groups = {"Login"})
    public void takeScreenShot(ITestResult result) throws IOException, AWTException, APIException {
        System.out.println("aftermethod is running");
            int testCaseID;
            ITestNGMethod instance = result.getMethod();
            String testName = instance.getDescription();
            Pattern pattern = Pattern.compile("(\\d+)\\.{1}.*");
            Matcher matcher = pattern.matcher(testName);
        System.out.println(result.isSuccess());
        System.out.println(testName);
            if (matcher.find()) {
                testCaseID = Integer.parseInt(matcher.group(1));
                System.out.println(testCaseID);
                APIClient client = new APIClient("https://hillelmanold.testrail.io/");
                client.setUser("rvalek@intersog.com");
                client.setPassword("hillel");
                Map data = new HashMap();
                if (result.isSuccess()) {
                    data.put("status_id", new Integer(1));
                    JSONObject r = (JSONObject) client.sendPost("add_result_for_case/" + runID + "/" + testCaseID, data);
                } else {
                    data.put("status_id", new Integer(5));
                }
                JSONObject r = (JSONObject) client.sendPost("add_result_for_case/" + runID + "/" + testCaseID, data);
        }
    }

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
//    @Test(description = "Admin. Create User", dependsOnMethods = {"openAdminPanelPage"}, groups = {"Admin"})
//    public void adminCreateUser() {
//        adminPanelPage.createUser();
//    }
}

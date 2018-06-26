package hillelauto.ApiTests;

import hillelauto.ApiTests.Pages.ApiIssuePages;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiTests {


    ApiIssuePages apiIssuePages = new ApiIssuePages();

    @Test(description = "Run Server on Port", groups = {"Sanity"})
    public void runServerOnPort() {
        apiIssuePages.runServerOnPort();
    }


    @Test(description = "Get User List", groups = {"Sanity"})
    public void getUserList() {
        apiIssuePages.getUserList();
        //will be compared with hardcoded variables
        Assert.assertEquals();
    }


    @Test(description = "Create User", groups = {"Sanity"})
    public void createUser() {
        apiIssuePages.createUser();
        //will be checked if in response is present ID.
        Assert.assertTrue();
        //will be checked if in response is absent Role.
        Assert.assertTrue();
    }

    @Test(description = "Save User", dependsOnMethods = {"createUser"}, groups = {"Sanity"})
    public void saveUser() {
        apiIssuePages.saveUser();
        //will be compared if ID of created user is the same
        Assert.assertEquals();
    }

    @Test(description = "Delete User", dependsOnMethods = {"saveUser"}, groups = {"Sanity"})
    public void deleteUser() {
        apiIssuePages.deleteUser();
        //get user list and check if user ID is absent
        Assert.assertTrue();
    }

    @Test(description = "Delete Invalid User", priority = 0, groups = {"Sanity"})
    public void deleteInvalidUser() {
        apiIssuePages.deleteInvalidUser();
        //is response 401
        Assert.assertTrue();


        @Test(description = "Create Admin", groups = {"Sanity"})
        public void createAdmin() {
            apiIssuePages.createAdmin();
            //will be checked if in response is present ID.
            Assert.assertTrue();
            //will be checked if in response is present Role.
            Assert.assertTrue();
        }

        @Test(description = "Save Admin", dependsOnMethods = {"createAdmin"}, groups = {"Sanity"})
        public void saveAdmin() {
            apiIssuePages.saveAdmin();
            //will be compared if ROLE of created admin is the same
            Assert.assertEquals();
        }

    }

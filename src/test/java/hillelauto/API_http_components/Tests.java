package hillelauto.API_http_components;


import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tests {
    public String userId;

    private void checkContentType(String header) {
        Assert.assertTrue(header.contains("Content-Type: application/json"));
    }

    private void findUserID(String data) {
        Matcher m = Pattern.compile("\"id\":\"(\\d+)").matcher(data);
        if (m.find())
            userId = m.group(1);
    }

    @Test(description = "Requirement 2 - getting user list, requirement 11 - list when Start Server", priority = -3)
    void getUsers() throws IOException {
        String[] responseData = Requests.sendGet(Vars.base_url + Vars.users_path, null);
        Assert.assertEquals(responseData[1], Vars.serverStart);
        checkContentType(responseData[0]);
    }

    @Test(description = "Requirement 4 - Create user", groups = "Sanity")
    void createUser() throws IOException {
        String[] responseData = Requests.sendPost(Vars.base_url + Vars.users_path, Vars.addUserStudent);
        Assert.assertTrue(responseData[1].contains("\"id\""));
        findUserID(responseData[1]);
        Assert.assertTrue(responseData[1].contains("\"role\":\"Student\""));
        Assert.assertTrue(responseData[2].equals("200"));
        checkContentType(responseData[0]);
    }

    @Test(description = "Requirement 3 - Edit user", groups = "Sanity", dependsOnMethods = "createUser")
    void editUser() throws IOException{
        String[] responseData = Requests.sendPut(Vars.base_url + Vars.users_path +"/" + userId, Vars.editUserStudent);
        Assert.assertEquals(responseData[2], "200");
        Assert.assertTrue(responseData[1].contains(Vars.editUserStudent));
        checkContentType(responseData[0]);
    }

    @Test(description = "Requirement 7 - Delete user", groups = "Sanity", dependsOnMethods = "createUser")
    void deleteUser() throws IOException{
        String[] responseData = Requests.sendDelete(Vars.base_url + Vars.users_path + "/" + userId, null);
        Assert.assertEquals(responseData[2], "204");

        String[] responseData2 = Requests.sendGet(Vars.base_url + Vars.users_path, null);
        Assert.assertFalse(responseData2[1].contains(userId));
    }


    @Test(description = "Requirement 10 - Request Invalid Content-type")
    public void wrongContent() throws IOException {
        String[] responseData = Requests.sendWrongGet(Vars.base_url + Vars.users_path, null);
        Assert.assertEquals(responseData[2],"401");
        checkContentType(responseData[0]);
    }

}

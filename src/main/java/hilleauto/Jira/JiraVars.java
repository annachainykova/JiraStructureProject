package hilleauto.Jira;

import hilleauto.Tools;

public interface JiraVars {
    public static final String baseURL = "http://jira.hillel.it:8080/";

    // user credentials
    public static final String username = "leonid.haidanov";
    public static final String password = "kbfybn7323";

    //admin credentials
    public static final String adminUsername = "autorob";
    public static final String adminPassword = "forautotests";

    public static final String adminUserPageTitle = "Users - Hillel IT School JIRA";

    public static final String createUser = Tools.timestampForUser() + "annasuser";
    public static final String createUserEmail = createUser + "@example.com";


    public static final String newIssueSummary = "AnnaAutoTest " + Tools.timestamp();
    public static final String issueProject = "General QA Robert (GQR)\n";

    public static final String attachmentFileLocation = "C:\\Users\\anya_\\Downloads\\";
    public static final String attachmentFileName = "500 (1).jpeg";

    public static final String downloadedFileName = "500 (2).jpeg";;

    public static final String screenShotFailureFileLocation = "E:\\Anna\\JiraTests\\FailureScreenShots\\";

    //credentials for TestRail integration
    public static final String URLTestRail = "https://hillelmanold.testrail.io/";
    public static final String usernameTestRail = "rvalek@intersog.com";
    public static final String passwordTestRail = "hillel";

}

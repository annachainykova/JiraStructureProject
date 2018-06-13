package hilleauto.Jira;

import hilleauto.Tools;

public interface JiraVars {
    public static final String baseURL = "http://jira.hillel.it:8080/";
    public static final String username = "a.chainikova";
    public static final String password = "SarbonaRosta";

    public static final String newIssueSummary = "AnnaAutoTest " + Tools.timestamp();
    public static final String issueProject = "General QA Robert (GQR)\n";

    public static final String attachmentFileLocation = "C:\\Users\\anya_\\Downloads\\";
    public static final String attachmentFileName = "500.jpeg";
}

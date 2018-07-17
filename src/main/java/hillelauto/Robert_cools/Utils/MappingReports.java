package hillelauto.Robert_cools.Utils;

import hillelauto.Robert_cools.Utils.ConfluenceUpdate.UpdateMain;
import hillelauto.Robert_cools.Utils.connectionToSkyfence.SkyfenceConnectionSettings;
import org.apache.commons.io.FileUtils;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hillelauto.Robert_cools.Utils.helpers.EnvironmentHelper.getCurrentDate;


// Represents biggest problem found by test
enum ActivityVerdict {
    ACTION_FAILED, NO_ACTIVITY, NO_RECORD, HAS_UNEXPECTED, OK
}

public class MappingReports {

    private static final List<ActivityReport> reports = new ArrayList<>();
    private static final Map<String, String> methods = new HashMap<>();
    public static Map<String, List<String>> failedTests = new HashMap<>();
    public static Map<String, List<String>> passedTests = new HashMap<>();

    public static void addActivityReports(String testDesc, String xmlTestName, String testName, boolean actionSuccessful, String gwLog) {
        reports.add(new ActivityReport(testDesc.trim(), xmlTestName, testName, actionSuccessful, gwLog));
    }

    public static void addMethodWithStatus(String testName, int testStatus) {
        String result = "";
        switch (testStatus) {
            case ITestResult.CREATED:
                result = "Created";
                break;
            case ITestResult.SUCCESS:
                result = "Success";
                break;
            case ITestResult.FAILURE:
                result = "Failure";
                break;
            case ITestResult.SKIP:
                result = "Skip";
                break;
        }
        methods.put(testName, result);
    }

    // run after all test are finished
    public static void getReport(boolean showOKs, boolean updateConfluence) {
        reports.forEach(ActivityReport::activityChecks);
        writeReport(wrapHTML(showOKs));

        // Call for Confluence Page Update passing MapForSergey()
        if (updateConfluence) {
            UpdateMain.updateTestResult(MapForSergey());
        }
    }

    private static HashMap<String, Boolean> MapForSergey() {
        HashMap<String, Boolean> forSergey = new HashMap<>();
        Boolean status;

        for (ActivityReport r : reports) {
            switch (r.getVerdict()) {
                case ACTION_FAILED:
                    continue;
                case NO_ACTIVITY:
                case NO_RECORD:
                    status = Boolean.FALSE;
                    break;
                default:
                    status = Boolean.TRUE;
            }

            forSergey.put(String.join(",", r.expectedIDs), status);
        }
        return forSergey;
    }

    private static String wrapHTML(boolean showOKs) {
        StringBuilder upperPart = new StringBuilder();
        StringBuilder lowerPart = new StringBuilder();

        lowerPart.append(String.join("\n",
                "    </TABLE>",
                "    <br/>",
                "    <TABLE BORDER=0 CELLSPACING=2 CELLPADDING=2 Width=\"100%\">",
                "        <TR  BGCOLOR=\"#607D8B\">",
                "            <td><FONT COLOR=White FACE=\"Geneva, Arial\" SIZE=2>  Failure Analysis</font></td>",
                "        </TR>\n"));

        int entryCount = 0;

        for (ActivityReport entry : reports) {
            entry.summaryRef.testsRun++;
            ActivityVerdict verdict = entry.getVerdict();

            createDataForJSONReport(entry);

            if (!showOKs && verdict == ActivityVerdict.OK)
                continue;

            // Any colour you like
            String color;
            String anchorID;

            switch (verdict) {
                case ACTION_FAILED:
                    color = "#3F51B5";
                    anchorID = "FailedAct";
                    entry.summaryRef.actionsFailed++;
                    break;
                case NO_ACTIVITY:
                    color = "#f44336";
                    anchorID = "MissingAct";
                    entry.summaryRef.activitiesMissing++;
                    break;
                case NO_RECORD:
                    color = "#FF9800";
                    anchorID = "MissingRec";
                    entry.summaryRef.recordMissing++;
                    break;
                default:
                    color = "#4CAF50";
                    anchorID = "";
            }
            lowerPart.append(String.join("\n",
                    String.format("        <TR  BGCOLOR=\"%s\"%s>", color, anchorID.isEmpty() ? "" : " id=_" + anchorID),
                    String.format("            <td><FONT COLOR=White FACE=\"Courier New, Lucida Console\" SIZE=2>%s. %s %s: %s.</font></td>", ++entryCount, entry.assetName, entry.fullName, String.join(". ", entry.statusText)),
                    "        </TR>",
                    "       <TR  BGCOLOR=\"#212121\">",
                    String.format("            <td><FONT COLOR=White FACE=\"Courier New, Lucida Console\" SIZE=2>%s</font></td>", String.join("<br/>", entry.logLines)),
                    "        </TR>",
                    "    <br/>\n"));
        }

        lowerPart.append(String.join("\n",
                "    </TABLE>",
                "</body>",
                "</html>"));

        upperPart.append(String.join("\n",
                "<!DOCTYPE html>",
                "<html>",
                "<head></head>",
                "<body>",
                "<p><font color=\"#696969\" FACE=\"Geneva, Arial\" SIZE=2>Proxy:  " + SkyfenceConnectionSettings.getPROXY() + "</font>",
                "<font color=\"#696969\" FACE=\"Geneva, Arial\" SIZE=2>Date:   " + getCurrentDate() + "</font></p>",
                "    <TABLE BORDER=0 CELLSPACING=1 CELLPADDING=2 Width=\"100%\">",
                "        <TR>",
                "            <th BGCOLOR=\"#607D8B\"><FONT COLOR=White FACE=\"Geneva, Arial\" SIZE=2>Asset</font></th>",
                "            <th BGCOLOR=\"#4CAF50\"><FONT COLOR=White FACE=\"Geneva, Arial\" SIZE=2>Tests run</font></th>",
                "            <th BGCOLOR=\"#f44336\"><FONT COLOR=White FACE=\"Geneva, Arial\" SIZE=2>Activities missing</font></th>",
                "            <th BGCOLOR=\"#FF9800\"><FONT COLOR=White FACE=\"Geneva, Arial\" SIZE=2>Records missing</font></th>",
                "            <th BGCOLOR=\"#3F51B5\"><FONT COLOR=White FACE=\"Geneva, Arial\" SIZE=2>Actions failed</font></th>",
                "            <th BGCOLOR=\"#9E9E9E\"><FONT COLOR=White FACE=\"Geneva, Arial\" SIZE=2>Tests skipped</font></th>",
                "        </TR>\n"));

        for (Map.Entry<String, AssetSummary> asset : AssetSummary.assets.entrySet()) {
            AssetSummary summary = asset.getValue();

            upperPart.append(String.join("\n",
                    "        <TR>",
                    String.format("            <td BGCOLOR=\"#FFFFFF\"><FONT COLOR=Black FACE=\"Geneva, Arial\" SIZE=2>%s</font></td>", asset.getKey()),
                    String.format("            <td BGCOLOR=\"#FFFFFF\"><FONT COLOR=#4CAF50 FACE=\"Geneva, Arial\" SIZE=2>%s</font></td>", summary.testsRun),
                    String.format("            <td BGCOLOR=\"#FFFFFF\"><FONT COLOR=#f44336 FACE=\"Geneva, Arial\" SIZE=2>%s</font></td>", summary.activitiesMissing),
                    String.format("            <td BGCOLOR=\"#FFFFFF\"><FONT COLOR=#FF9800 FACE=\"Geneva, Arial\" SIZE=2>%s</font></td>", summary.recordMissing),
                    String.format("            <td BGCOLOR=\"#FFFFFF\"><FONT COLOR=#3F51B5 FACE=\"Geneva, Arial\" SIZE=2>%s</font></td>", summary.actionsFailed),
                    String.format("            <td BGCOLOR=\"#FFFFFF\"><FONT COLOR=#9E9E9E FACE=\"Geneva, Arial\" SIZE=2>%s</font></td>", summary.testsSkipped),
                    "        </TR>\n"));
        }

        upperPart.append(String.join("\n",
                "        <TR>",
                "            <td BGCOLOR=\"#FFFFFF\"><FONT COLOR=Black FACE=\"Geneva, Arial\" SIZE=2>Total</font></td>",
                String.format("            <td BGCOLOR=\"#FFFFFF\"><FONT COLOR=#4CAF50 FACE=\"Geneva, Arial\" SIZE=2>%s</font></td>", reports.size()),
                String.format("            <td BGCOLOR=\"#FFFFFF\"><FONT COLOR=#f44336 FACE=\"Geneva, Arial\" SIZE=2>%s</font></td>", AssetSummary.totalActivitiesMissing),
                String.format("            <td BGCOLOR=\"#FFFFFF\"><FONT COLOR=#FF9800 FACE=\"Geneva, Arial\" SIZE=2>%s</font></td>", AssetSummary.totalRecordsMissing),
                String.format("            <td BGCOLOR=\"#FFFFFF\"><FONT COLOR=#3F51B5 FACE=\"Geneva, Arial\" SIZE=2>%s</font></td>", AssetSummary.totalActionsFailed),
                String.format("            <td BGCOLOR=\"#FFFFFF\"><FONT COLOR=#9E9E9E FACE=\"Geneva, Arial\" SIZE=2>%s</font></td>", AssetSummary.totalTestsSkipped),
                "        </TR>\n"));

        return upperPart.append(lowerPart).toString();
    }

    private static void writeReport(String report) {
        try {
            FileUtils.writeStringToFile(new File("reports/report.html"), report, "utf-8");
            FileUtils.writeStringToFile(new File("reports/report" + new SimpleDateFormat(" dd-MM-yy HH-mm").format(new Date()) + ".html"), report, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void countSkipped(String desc) {
        ActivityReport.countSkipped(desc);
    }

    private static void createDataForJSONReport(ActivityReport entry) {
        if (!failedTests.containsKey(entry.xmlTestName))
            failedTests.put(entry.xmlTestName, new ArrayList<>());
        if (!passedTests.containsKey(entry.xmlTestName))
            passedTests.put(entry.xmlTestName, new ArrayList<>());

        if (!entry.statusText.contains("Found unexpected activities") && !entry.statusText.contains("OK")) {
            failedTests.get(entry.xmlTestName).add(entry.testName);
        } else {
            passedTests.get(entry.xmlTestName).add(entry.testName);
        }
    }
}

class ActivityReport {
    // List of extra activities to ignore
    private static final List<String> extraActivities = new ArrayList<>(Arrays.asList(
            //BOX
            "Box-95", "Box-99", "Box-181",
            //DBOX
            "DBox-31", "DBox-48", "DBox-50", "DBox-74",
            //GOOGLE
            "Google-34", "Google-35", "Google-32", "Google-33", "Google-864",
            "Google-71", "Google-82", "Google-83", "Google-116", "Google-472", "Google-476",
            "Google-516", "Google-526", "Google-567",
            //O365 office,azure,dynamics
            "O365-43", "O365-44", "O365-52", "O365-53", "O365-74", "O365-75", "O365-83", "O365-88",
            "O365-176", "O365-184", "O365-248", "O365-275", "O365-286", "O365-445", "O365-507",
            "O365-600", "O365-783", "O365-886", "O365-887", "O365-934", "O365-1032", "O365-1060",
            "O365-1159", "O365-1246", "O365-1278", "O365-1409",
            //SALESFORCE
            "SForce-23", "SForce-24", "SForce-32", "SForce-42", "SForce-45", "SForce-59",
            "SForce-60", "SForce-68", "SForce-69", "SForce-70", "SForce-117", "SForce-120",
            "SForce-123", "SForce-125", "SForce-144", "SForce-174", "SForce-915", "SForce-921",
            "SForce-923", "SForce-942", "SForce-943", "SForce-952", "SForce-953", "SForce-961",
            "SForce-962", "SForce-968", "SForce-969", "SForce-996", "SForce-997", "SForce-1002",
            "SForce-1048", "SForce-1053", "SForce-1484",
            //AWS
            "AWS-27", "AWS-69", "AWS-140", "AWS-2080",
            //Atlassian
            "Atlas-20", "Atlas-21", "Atlas-38", "Atlas-67", "Atlas-204"));

    // General activity report components
    public final String fullName;
    public final String assetName;
    public final String testName;
    public final String xmlTestName;
    public final AssetSummary summaryRef;
    public final String[] logLines;

    // TODO Compose status of ActivityVerdicts instead of String and add "stringify" method
    public final List<String> statusText = new ArrayList<>();

    // Expected Activity Properties
    public final List<String> expectedIDs = new ArrayList<>();
    private boolean expectingRecord;

    // Expected/Unexpected lines of log
    private String expectedActivity;
    private boolean hasUnexpected = false;

    public ActivityReport(String testDesc, String xmlTestName, String testName, boolean actionSuccessful, String gwLog) {
        this.testName = testName;
        this.xmlTestName = xmlTestName;
        this.fullName = testDesc;
        this.logLines = gwLog.split("[\r\n]");

        String propertyString = findProperties(testDesc);

        // Associating activity with summary of it's asset
        this.assetName = findAssetName(propertyString);
        AssetSummary.assets.putIfAbsent(assetName, new AssetSummary());
        this.summaryRef = AssetSummary.assets.get(assetName);


        if (!actionSuccessful) {
            this.statusText.add("Test action failed");
            AssetSummary.totalActionsFailed++;
        } else if (this.logLines.length == 1) {
            this.statusText.add("No activities at all");
            AssetSummary.totalActivitiesMissing++;
        } else
            // Parsing expected properties for further verification
            parseProperties(propertyString.split(","));
    }

    public static void countSkipped(String desc) {
        AssetSummary.totalTestsSkipped++;
        if (AssetSummary.totalTestsSkipped != 0)
            AssetSummary.assets.get(findAssetName(findProperties(desc))).testsSkipped++;
    }

    private static String findProperties(String desc) {
        return desc.substring(desc.indexOf('|') + 1).trim();
    }

    private static String findAssetName(String IDs) {
        if (IDs.contains("-")) {
            return IDs.substring(0, IDs.indexOf('-'));
        } else return "notID";
    }

    private void parseProperties(String[] props) {
        for (String prop : props)
            if (prop.contains("-"))
                this.expectedIDs.add(prop);
            else if (prop.equals("Record"))
                this.expectingRecord = true;
    }

    public void activityChecks() {
        if (!this.statusText.isEmpty())
            return;

        parseActivities();

        // Expected Activity checks
        if (this.expectedActivity == null) {
            this.statusText.add("Failed to find expected activity");
            AssetSummary.totalActivitiesMissing++;
        } else
            this.checkActivity(this.expectedActivity);

        // Unexpected Activity checks
        if (this.hasUnexpected)
            this.statusText.add("Found unexpected activities");

        // All checks succeeded
        if (this.statusText.isEmpty())
            this.statusText.add("OK");
    }

    private void parseActivities() {

        Pattern findIDs = Pattern.compile("Mapping Ids: ([A-Za-z0-9,.-]+)");
        Matcher m;

        // Starts with index 1 because 0 is date
        for (int i = 1; i < this.logLines.length; i++) {

            //Removes unnecessary text from activity line
            String trimmedActivity = trimActivity(this.logLines[i]);
            this.logLines[i] = trimmedActivity;

            m = findIDs.matcher(trimmedActivity);

            String foundIDs = "";
            List<String> activityIDs = new ArrayList<>();

            if (m.find()) {
                foundIDs = m.group(1);
                activityIDs = new ArrayList<>(Arrays.asList(foundIDs.split(",")));
            }

            if (!(Collections.disjoint(activityIDs, this.expectedIDs))) {
                this.expectedActivity = trimmedActivity;
                // Marks expected activity inside the log piece
                this.logLines[i] = String.format("<strong><FONT COLOR=Green FACE=\"Courier New, Lucida Console\" SIZE=2>%s</font></strong>", trimmedActivity);
            } else if (assetName.equals(findAssetName(foundIDs)) && hasUsername(this.logLines[i]) && Collections.disjoint(activityIDs, extraActivities)) {
                this.hasUnexpected = true;
            }
        }
    }

    private boolean hasUsername(String activity) {
        return !activity.contains("[Login Username: (repository: )]");
    }

    private String trimActivity(String activity) {
        String[] fieldNames = {"Login Username", "Data Object", "Records", "User Action", "User Action Status", "Labels", "Service type", "Mapping Ids", "URI"};

        StringBuilder cleanActivity = new StringBuilder();
//        cleanActivity.append(activity.substring(0, activity.indexOf("sky")));
        cleanActivity.append("Activity: ");

        Matcher matcher;

        for (String field : fieldNames) {
            matcher = Pattern.compile("\\[" + field + ":[^\\]]+(?:\\] )?").matcher(activity);
            if (matcher.find())
                cleanActivity.append(matcher.group(0));
        }

        return cleanActivity.toString().replace("<", "&lt;").replace(">", "&gt;");
    }

    private void checkActivity(String activity) {
        if (!hasUsername(activity)) {
            this.statusText.add("Username is missing");
            AssetSummary.totalActivitiesMissing++;
            return;
        }

        if (this.expectingRecord)
            checkRecord(activity);
    }

    private void checkRecord(String activity) {
        Matcher recordMatcher = Pattern.compile("Records: ([^\\]]+)").matcher(activity);
        if (!recordMatcher.find()) {
            this.statusText.add("Record is missing");
            AssetSummary.totalRecordsMissing++;
            return;
        }

//        int normalLength = 140;
//        String record = recordMatcher.group(1);
//        if (record.length() > normalLength)
//            this.statusText.add("Record might be too long");
//        if (record.length() > 0 && record.contains("%"))
//            this.statusText.add("Record is not decoded");
    }

    public ActivityVerdict getVerdict() {
        switch (this.statusText.get(0)) {
            case "Test action failed":
                return ActivityVerdict.ACTION_FAILED;
            case "No activities at all":
            case "Failed to find expected activity":
            case "Username is missing":
                return ActivityVerdict.NO_ACTIVITY;
            case "Record is missing":
                return ActivityVerdict.NO_RECORD;
            case "Found unexpected activities":
                return ActivityVerdict.HAS_UNEXPECTED;
            default:
                return ActivityVerdict.OK;
        }
    }
}

class AssetSummary {
    public static final HashMap<String, AssetSummary> assets = new HashMap<>();

    // Total fields
    public static int totalActionsFailed = 0;
    public static int totalActivitiesMissing = 0;
    public static int totalRecordsMissing = 0;
    public static int totalTestsSkipped = 0;

    public int testsRun = 0;
    public int activitiesMissing = 0;
    public int recordMissing = 0;
    public int actionsFailed = 0;
    public int testsSkipped = 0;
}
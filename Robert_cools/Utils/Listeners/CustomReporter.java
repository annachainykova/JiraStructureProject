package hillelauto.Robert_cools.Utils.Listeners;


import hillelauto.Robert_cools.Utils.MappingReports;
import org.apache.commons.io.FileUtils;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static hillelauto.Robert_cools.Utils.helpers.EnvironmentHelper.getCurrentDate;


public class CustomReporter implements IReporter {
    private StringBuilder bodyHTML = new StringBuilder();

    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

        ISuiteResult suiteResult;
        ITestContext testContext;
        IResultMap skippedResult;
        Set<ITestResult> testsSkipped;
        for (ISuite suite : suites) {

            Map<String, ISuiteResult> suiteResults = suite.getResults();

            for (String testName : suiteResults.keySet()) {
                bodyHTML.append(String.join("\n",
                        "        <TR  BGCOLOR=\"#2460d8\">",
                        String.format("            <th><FONT COLOR=White FACE=\"Geneva, Arial\" SIZE=3>%s</font></th>", testName),
                        "            <th BGCOLOR=\"#4CAF50\"><FONT COLOR=White FACE=\"Geneva, Arial\" SIZE=3>Passed</font></th>",
                        "            <th BGCOLOR=\"#f44336\"><FONT COLOR=White FACE=\"Geneva, Arial\" SIZE=3>Failed</font></th>",
                        "            <th BGCOLOR=\"#607D8B\"><FONT COLOR=White FACE=\"Geneva, Arial\" SIZE=3>Skipped*</font></th>",
                        "        </TR>\n"));
                for (String test : MappingReports.passedTests.get(testName)) {
                    bodyHTML.append(String.join("\n",
                            "        <TR>",
                            String.format("            <td><FONT class=\"test\">%s</font></td>", test),
                            "            <td><p class=\"verdict\">&#9989;</p></td>",
                            "            <td></td>",
                            "            <td></td>",
                            "        </TR>\n"));
                }
                for (String test : MappingReports.failedTests.get(testName)) {
                    bodyHTML.append(String.join("\n",
                            "        <TR>",
                            String.format("            <td><FONT class=\"test\">%s</font></td>", test),
                            "            <td></td>",
                            "            <td><p class=\"verdict\">&#x274C;</p></td>",
                            "            <td></td>",
                            "        </TR>\n"));
                }

                suiteResult = suiteResults.get(testName);
                testContext = suiteResult.getTestContext();
                skippedResult = testContext.getSkippedTests();
                testsSkipped = skippedResult.getAllResults();
                for (ITestResult testResult : testsSkipped) {
                    bodyHTML.append(String.join("\n",
                            "        <TR>",
                            String.format("            <td><FONT class=\"test\">%s</font></td>\n", testResult.getName()),
                            "            <td></td>",
                            "            <td></td>",
                            "            <td><p class=\"verdict\">&#10134;</p></td>",
                            "        </TR>\n"));
                }
            }
        }
        writeReport(createHTMLReport());
    }

    private String createHTMLReport() {
        StringBuilder repHTML = new StringBuilder();

        repHTML.append(String.join("\n",
                "<!DOCTYPE html>",
                "<html>",
                "<head>",
                "    <style>",
                "        .test {",
                "            padding: 20px;",
                "            font-family: 'Times New Roman';",
                "            font-style: normal;",
                "            font-weight: normal;",
                "            font-size: 18px;",
                "            color: black;",
                "        }",
                "        .verdict {",
                "            text-align: center;",
                "        }",
                "    </style>",
                "</head>",
                "<body>",
                "<p><font color=\"#696969\" FACE=\"Geneva, Arial\" SIZE=2>Date:   " + getCurrentDate() + "</font></p>",
                "    <TABLE BORDER=0 CELLSPACING=2 CELLPADDING=2 Width=\"100%\">\n"));

        repHTML.append(bodyHTML);

        repHTML.append(String.join("\n",
                "   </TABLE>",
                "<p><font color=\"#696969\" FACE=\"Geneva, Arial\" SIZE=2>Tests, that had been skipped due to failed previous test</font></p>",
                "</body>",
                "</html>"));
        return repHTML.toString();
    }

    private static void writeReport(String report) {
        try {
            FileUtils.writeStringToFile(new File("reports/reportForMapStudio.html"), report, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

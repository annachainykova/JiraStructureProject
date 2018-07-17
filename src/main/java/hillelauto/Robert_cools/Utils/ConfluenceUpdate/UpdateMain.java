package hillelauto.Robert_cools.Utils.ConfluenceUpdate;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;


public class UpdateMain {
    public static final HashSet<Services> services = new HashSet();
    final private static Logger log = Logger.getLogger(UpdateMain.class);

    public static void updateTestResult(Map<String, Boolean> testresult) {
        Request request = new Request();

        for (Services servise : services) {
// Update testresult in Table
            try {
                request.getPage(servise);
                if (request.getResponseResult().contains("200")) {
                    String value = request.getValue();
                    String updatedValue = inputTestResult(value, testresult);
                    request.updatePage(updatedValue, servise);
                    System.out.println(servise.getPageName() + " ::: " + request.getResponseResult());
                }
            } catch (IOException e) {
                log.error(servise.name() + " no connection");
            }
        }
    }


    private static String inputTestResult(String value, Map<String, Boolean> result)   //add test result to table //
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();     // get local data

        //Parse HTML with Jsoup
        Document doc = Jsoup.parse(value, StringUtils.EMPTY, Parser.xmlParser());
        doc.outputSettings().escapeMode(Entities.EscapeMode.base);

        doc.outputSettings().charset(CharEncoding.US_ASCII);
        doc.outputSettings().prettyPrint(false);


        Elements table = doc.select("table");
        Elements rows;
        if (table.size() == 1)                // check if page already has a table with Mappings covered autotests;
            rows = table.get(0).select("tr");
        else rows = table.get(1).select("tr");

        for (int i = 2; i < rows.size(); i++) { // skip the first rows because they are headers;
            Element row = rows.get(i);
            Elements cols = row.select("td");

            if (cols.size() > 5) {             // Check if it not Title
                result.forEach((mappingId, testResult) -> {
                    if (cols.get(0).text().equals(mappingId)) {
                        cols.get(6).text(dtf.format(localDate));
                        if (testResult) {
                            cols.get(7).html("<strong><center style=\"color: #339966;\">OK</center></strong>");
                        }
                        if (!testResult) {
                            cols.get(7).html("<strong><center style=\"color: #ff0000;\">Failed</center></strong>");
                        }
                    }
                });
            }
        }

        //Get Collection with tested mappings;
        Elements testedRow = new Elements();
        rows.forEach((row) -> {
            if (row.select("td").size() == 8) {
                if (!row.select("td").get(6).text().equals("")) {
                    testedRow.add(row);
                }
            }
        });

        // if table Mappings covered by autotests doesn't exist , we add it and fill test result to this table
        if (table.size() == 1) {

            doc.select("table").before("<table class=\"wrapped\"><tr><th colspan=\"9\" width=\"\">Mappings covered by autotests</th></tr>" +
                    rows.get(1).toString() +
                    testedRow.toString() +
                    "</table><p> <br/></p>");
            return doc.toString();
        }
        // if table Mappings covered by autotests already exist , we add test result to this table
        else {
            doc.select("table").get(0).html(
                    "<tr><th colspan=\"9\" width=\"\">Mappings covered by autotests</th></tr>" +
                            rows.get(1).toString() +
                            testedRow.toString());
            return doc.toString();
        }
    }

    //this method clear the Table from extra tags after manual Edit through UI
    public static String clearTable(String table) {
        return table.replaceAll("<colgroup><col /><col /><col /><col /><col /><col /><col /><col /><col /></colgroup>", "")
                .replaceAll("<td><br></td>", "<td></td>")
                .replaceAll("<br />", "");
    }
}
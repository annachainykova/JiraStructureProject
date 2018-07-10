package hillelauto.Facebook;

import hillelauto.Tools;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacebookTests{

    private static CloseableHttpClient httpclient = HttpClients.createDefault();
    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).setConnectionRequestTimeout(5000).build();


    public static String sendGet(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        request.setConfig(requestConfig);

        HttpResponse response = httpclient.execute(request);

        HttpEntity entity = response.getEntity();

        return entity != null ? EntityUtils.toString(entity) : "No response data.";

    }


    public String getID(String path) throws IOException {
        String response = sendGet(path);
        String patternString = ".*fb://profile/(\\d+)\".*";
        System.out.println(response);
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(response);
        if(matcher.find()){
            String ID = matcher.group(1);
            return ID;
        } else {
            return "No such user";
        }
    }

    @Test
    public void namesWithID() throws IOException {
        List <String> lines = Tools.lineOfFile("new.csv");
        ArrayList <String> newLines = new ArrayList<>();
        String patternString = ".*profile.php\\?id=(\\d+)&.*";
        Pattern pattern = Pattern.compile(patternString);
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                newLines.add(line.split(",")[0] + "," + matcher.group(1));
            } else {
                newLines.add(line.split(",")[0] + getID(line.split(",")[1]));
            }
        }
        Files.write(Paths.get("newfile.txt"), newLines);
    }
}


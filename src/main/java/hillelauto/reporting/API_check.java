package hillelauto.reporting;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class API_check {
    public static void main(String[] args) throws Exception{

        //System.out.println(getTitle());
        getProject();

    }

    public static String getTitle() throws IOException, APIException {
        APIClient client = new APIClient("https://hillelmanold.testrail.io/");
        client.setUser("rvalek@intersog.com");
        client.setPassword("hillel");
        JSONObject c = (JSONObject) client.sendGet("get_case/12");
        return (String) c.get("title");
    }

    public static void assResultToOneCase() throws IOException, APIException {
        APIClient client = new APIClient("https://hillelmanold.testrail.io/");
        client.setUser("rvalek@intersog.com");
        client.setPassword("hillel");
        Map data = new HashMap();
        data.put("status_id", new Integer(1));
        data.put("comment", "This test worked fine!");
        JSONObject r = (JSONObject) client.sendPost("add_result_for_case/45/1", data);
    }

    public static void createRun() throws IOException, APIException {
        APIClient client = new APIClient("https://hillelmanold.testrail.io/");
        client.setUser("rvalek@intersog.com");
        client.setPassword("hillel");
        Map data = new HashMap();
        data.put("include_all", true);
        data.put("name", "This is a new test run");
        JSONObject c = (JSONObject) client.sendPost("add_run/7", data);
        System.out.println(c.get("name"));

    }

    public static void getProject() throws IOException, APIException {
        APIClient client = new APIClient("https://hillelmanold.testrail.io/");
        client.setUser("rvalek@intersog.com");
        client.setPassword("hillel");
        JSONObject c = (JSONObject) client.sendGet("get_project/7");
        System.out.println(c.get("name"));
    }


}
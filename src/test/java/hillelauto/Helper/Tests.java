package hillelauto.Helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hillelauto.API_http_components.Requests;
import hillelauto.API_http_components.Vars;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class Tests {

    @Test
    public void testmy() throws JsonProcessingException {
        String json =
                "{"
                        + "'id' : 1,"
                        + "'name' : 'James Logan Howlett',"
                        + "'phone' : '93032903223',"
                        + "'role' : 'Administrator'"
                        + "}";

        // Now do the magic.
        User data = new Gson().fromJson(json, User.class);
        System.out.println(data.getName());

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String yui = ow.writeValueAsString(data);
        System.out.println(yui);

        JsonObject js = new JsonParser().parse(yui).getAsJsonObject();
        System.out.println(js);

        // Show it.
        System.out.println(data.toString());
    }




    @Test
    void testList() throws IOException {
        String json = "[{\"id\":\"1\",\"name\":\"James Logan Howlett\",\"phone\":\"+380670000003\",\"role\":\"Administrator\"},{\"id\":\"2\",\"name\":\"Charles Xavier\",\"phone\":\"+380670000002\",\"role\":\"Student\",\"strikes\":1},{\"id\":\"3\",\"name\":\"Erik Lehnsherr\",\"phone\":\"+380670000001\",\"role\":\"Support\",\"location\":\"Kiev\"}]";

        ObjectMapper objectMapper = new ObjectMapper();
        List<User> items = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructParametricType(List.class, User.class));
        for(User user : items) {
        if(user.getStrikes() != null) {
            System.out.println(new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(user));
            }
        }

    }
    /*
    List<Object> items = objectMapper.readValue(
    jsonString,
    objectMapper.getTypeFactory().constructParametricType(List.class, Object.class)
);
     */

    @Test
    void restPlusList() throws IOException {
        String[] responseData = Requests.sendGet(Vars.base_url + Vars.users_path, null);
        String json = responseData[1];
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> items = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructParametricType(List.class, User.class));
        for(User user : items) {
                System.out.println(new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(user));

        }

    }
}

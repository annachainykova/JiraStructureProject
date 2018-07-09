package hillelauto.API_restassured;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class APIjsonObjects {

    public static JSONObject createStudent() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Ivanov Ivan");
        jsonObject.put("phone", "+380939998800");
        return jsonObject;
    }

    public static JSONObject createNotStudent() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Ivanova Ivanna");
        jsonObject.put("phone", "+380939998801");
        jsonObject.put("role", "Administrator");
        return jsonObject;
    }

    public static JSONObject createUserInvalidRole() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Ivanov Ivan");
        jsonObject.put("phone", "+380939998802");
        jsonObject.put("role", "Teacher");
        return jsonObject;
    }

    public static JSONObject editUsers() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Chainykova Anna");
        return jsonObject;
    }


    public static JSONArray startServer() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id", "1");
        jsonObject1.put("name", "James Logan Howlett");
        jsonObject1.put("phone", "+380670000003");
        jsonObject1.put("role", "+Administrator");
        jsonArray.add(jsonObject1);
        JSONObject jsonObject2 = new JSONObject();
        jsonObject1.put("id", "2");
        jsonObject1.put("name", "Charles Xavier");
        jsonObject1.put("phone", "+380670000002");
        jsonObject1.put("role", "+Student");
        jsonObject2.put("strikes", 1);
        jsonArray.add(jsonObject2);
        JSONObject jsonObject3 = new JSONObject();
        jsonObject1.put("id", "3");
        jsonObject1.put("name", "Erik Lehnsherr");
        jsonObject1.put("phone", "+380670000001");
        jsonObject1.put("role", "Support");
        jsonObject1.put("location", "Kiev");
        jsonArray.add(jsonObject3);
        return jsonArray;


    }



}

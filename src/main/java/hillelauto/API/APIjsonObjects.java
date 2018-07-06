package hillelauto.API;

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



}

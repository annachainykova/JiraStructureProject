package hillelauto.API;

public class Vars {
    public static final String host = "http://localhost";
    public static final Integer port = 20007;
    public static final String basePath = "/api/users";

    public static final String response = "";

    public static final String CRUD = "";
    public static final String admins = host + "/refreshAdmins";

    public static final String baseBody = "[{" +
            "\"id\":\"1\"," +
            "\"name\":\"James Logan Howlett\"," +
            "\"phone\":\"+380670000003\"," +
            "\"role\":\"Administrator\"}," +
            "{\"id\":\"2\"," +
            "\"name\":\"Charles Xavier\"," +
            "\"phone\":\"+380670000002\"," +
            "\"role\":\"Student\"," +
            "\"strikes\":1}," +
            "{\"id\":\"3\"," +
            "\"name\":\"Erik Lehnsherr\"," +
            "\"phone\":\"+380670000001\"," +
            "\"role\":\"Support\"," +
            "\"location\":\"Kiev\"}]";

    public static final String createUser = "{" +
                    "\"name\":\"James Logan Howlett\"," +
                    "\"phone\":\"+380670000003\"}";






}

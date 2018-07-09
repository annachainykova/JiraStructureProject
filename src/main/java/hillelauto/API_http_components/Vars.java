package hillelauto.API_http_components;

public interface Vars {
        String base_url = "http://localhost:20007";
        String users_path = "/api/users";
        String refresh_admins_path = "/refresh/admins";

        String serverStart = "[{\"id\":\"1\",\"name\":\"James Logan Howlett\",\"phone\":\"+380670000003\",\"role\":\"Administrator\"}," +
                "{\"id\":\"2\",\"name\":\"Charles Xavier\",\"phone\":\"+380670000002\",\"role\":\"Student\",\"strikes\":1}," +
                "{\"id\":\"3\",\"name\":\"Erik Lehnsherr\",\"phone\":\"+380670000001\",\"role\":\"Support\",\"location\":\"Kiev\"}]";

        String addUserStudent = "{\"name\":\"StudentName\",\"phone\":\"+380670000004\"}";
        String editUserStudent = "{\"name\":\"NewName\"}";
        String addUserAdmin = "";
        String editUserAdmin = "";

        String addInvalidRoleUser = "";


}

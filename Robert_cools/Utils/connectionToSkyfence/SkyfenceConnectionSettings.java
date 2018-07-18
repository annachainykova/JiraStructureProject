package hillelauto.Robert_cools.Utils.connectionToSkyfence;

public class SkyfenceConnectionSettings {

    private static String PROXY;
    private static String userNameUI = "admin@skyfence.com";
    private static String passwordUI = "Barbapapa1@";
    private static String hostName;
    private static String userNameConsole = "root";
    private static String passwordConsole;
    private static String SKYFENCE_URL;

    public static String getPROXY() {
        return PROXY;
    }

    public static String getSkyfenceUrl() {
        return SKYFENCE_URL;
    }

    public static void setUpSkyfenceConnection(String proxyName) {
        boolean isRealProxy = false;

        switch (proxyName) {
            case "elena":
                hostName = "192.168.3.131";
                isRealProxy = true;
                passwordConsole = "skyfence";
                break;
            case "aobertas":
                hostName = "192.168.3.170";
                isRealProxy = true;
                passwordConsole = "Barbapapa1@";
                break;
            default:
                System.out.println("Wrong name of proxy :: " + proxyName);
        }
        if (isRealProxy) {
            PROXY = "." + proxyName + ".isogwork.org";
            SKYFENCE_URL = "https://" + proxyName + ".isogwork.org:4433";
        } else System.out.println("Wrong name of proxy :: " + proxyName);
    }

    public static String getUserNameUI() {
        return userNameUI;
    }

    public static String getPasswordUI() {
        return passwordUI;
    }

    public static String getHostName() {
        return hostName;
    }

    public static String getUserNameConsole() {
        return userNameConsole;
    }

    public static String getPasswordConsole() {
        return passwordConsole;
    }

}

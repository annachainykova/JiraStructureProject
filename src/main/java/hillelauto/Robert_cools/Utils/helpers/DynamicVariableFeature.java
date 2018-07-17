package hillelauto.Robert_cools.Utils.helpers;

final class DynamicVariableFeature {

    private static String dynamicUserName;
    private static String dynamicUserSelector;

    public static String getDynamicUserSelector() {
        return dynamicUserSelector;
    }

    public static void setDynamicUserSelector(String dynamicUserSelector) {
        DynamicVariableFeature.dynamicUserSelector = dynamicUserSelector;
    }

    public static String getDynamicUserName() {
        return dynamicUserName;
    }

    public static void setDynamicUserName(String val) {
        dynamicUserName = val;
    }
}

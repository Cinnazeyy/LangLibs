package li.cinnazeyy.langlibs.util;

public class Utils {
    private static final String messagePrefix = "";

    public static String getInfoMessageFormat(String info) {
        return messagePrefix + "§a" + info;
    }

    public static String getErrorMessageFormat(String error) {
        return messagePrefix + "§c" + error;
    }
}

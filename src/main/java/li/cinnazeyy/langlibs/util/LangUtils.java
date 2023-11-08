package li.cinnazeyy.langlibs.util;

import com.alpsbte.alpslib.utils.head.AlpsHeadUtils;
import li.cinnazeyy.langlibs.core.Language;

public class LangUtils {
    private static final String messagePrefix = "";

    public static String getInfoMessageFormat(String info) {
        return messagePrefix + "§a" + info;
    }

    public static String getErrorMessageFormat(String error) {
        return messagePrefix + "§c" + error;
    }

    public static void registerCustomHeads() {
        for (Language lang : Language.values()) AlpsHeadUtils.registerCustomHead(lang.getHeadId());
    }
}

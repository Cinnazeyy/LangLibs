package li.cinnazeyy.langlibs.util;

import com.alpsbte.alpslib.utils.heads.CustomHead;
import li.cinnazeyy.langlibs.LangLibs;
import li.cinnazeyy.langlibs.core.Language;
import org.bukkit.Bukkit;

import java.util.HashMap;

public class HeadUtils {
    public static HashMap<Language, CustomHead> LANGUAGE_HEADS;
    public static void loadHeadsAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(LangLibs.getPlugin(),() -> {
            for (Language lang:Language.values()) {
                LANGUAGE_HEADS.put(lang,new CustomHead(lang.HeadId));
            }
        });
    }
}
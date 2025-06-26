package li.cinnazeyy.langui.util;

import li.cinnazeyy.langlibs.core.LangLibAPI;
import li.cinnazeyy.langlibs.core.file.LanguageFile;
import li.cinnazeyy.langlibs.core.language.Language;
import li.cinnazeyy.langlibs.core.language.LanguageUtil;
import li.cinnazeyy.langui.LangUI;
import org.bukkit.plugin.Plugin;

public class LangUtil extends LanguageUtil {
    private static LangUtil langUtilInstance;

    public static void init() {
        if (langUtilInstance != null) return;
        Plugin plugin = LangUI.getPlugin();
        LangLibAPI.register(plugin, new LanguageFile[]{
                new LanguageFile(plugin, 1.0, Language.en_GB),
                new LanguageFile(plugin, 1.0, Language.de_DE, "de_AT", "de_CH"),
                new LanguageFile(plugin, 1.0, Language.fr_FR),
                new LanguageFile(plugin, 1.0, Language.he_IL)
        });
        langUtilInstance = new LangUtil();
    }

    public LangUtil() {
        super(LangUI.getPlugin());
    }

    public static LangUtil getInstance() {
        return langUtilInstance;
    }
}

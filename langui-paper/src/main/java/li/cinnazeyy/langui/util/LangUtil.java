package li.cinnazeyy.langui.util;

import li.cinnazeyy.langlibs.core.LangLibAPI;

import li.cinnazeyy.langlibs.core.language.Language;
import li.cinnazeyy.langlibs.core.file.LanguageFile;
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
                new LanguageFile(plugin, 1.0, Language.fr_FR, "fr_CA"),
                new LanguageFile(plugin, 1.0, Language.pt_PT, "pt_BR"),
                new LanguageFile(plugin, 1.0, Language.ko_KR),
                new LanguageFile(plugin, 1.0, Language.ru_RU, "ba_RU", "tt_RU"),
                new LanguageFile(plugin, 1.0, Language.zh_CN),
                new LanguageFile(plugin, 1.0, Language.zh_TW, "zh_HK"),
                new LanguageFile(plugin, 1.0, Language.he_IL),
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

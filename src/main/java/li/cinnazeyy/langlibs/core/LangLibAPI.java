package li.cinnazeyy.langlibs.core;

import com.destroystokyo.paper.ClientOption;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LangLibAPI {

    private static HashMap<String,LanguageFile[]> pluginLangFiles = new HashMap<>();
    public static void register(String pluginName, LanguageFile[] langFiles) {
        pluginLangFiles.put(pluginName,langFiles);
    }

    public static String get(CommandSender sender, String plugin, String key) {
        LanguageFile[] languageFiles = pluginLangFiles.get(plugin);
        return getLanguageFileByLocale(sender instanceof Player ? getLocaleTagByPlayer((Player) sender) : languageFiles[0].getTag(), languageFiles).getTranslation(key);
    }

    public String get(CommandSender sender, String plugin, String key, String... args) {
        LanguageFile[] languageFiles = pluginLangFiles.get(plugin);
        return getLanguageFileByLocale(sender instanceof Player ? getLocaleTagByPlayer((Player) sender) : languageFiles[0].getTag(),languageFiles).getTranslation(key, args);
    }

    public List<String> getList(CommandSender sender, String plugin, String key) {
        LanguageFile[] languageFiles = pluginLangFiles.get(plugin);
        return getLanguageFileByLocale(sender instanceof Player ? getLocaleTagByPlayer((Player) sender) : languageFiles[0].getTag(), languageFiles).getTranslations(key);
    }

    public List<String> getList(CommandSender sender, String plugin, String key, String... args) {
        LanguageFile[] languageFiles = pluginLangFiles.get(plugin);
        return getLanguageFileByLocale(sender instanceof Player ? getLocaleTagByPlayer((Player) sender) : languageFiles[0].getTag(),languageFiles).getTranslations(key, args);
    }

    private static LanguageFile getLanguageFileByLocale(String locale, LanguageFile[] languageFiles) {
        return Arrays.stream(languageFiles)
                .filter(lang -> lang.getTag().equalsIgnoreCase(locale))
                .findFirst()
                .orElseGet(() -> Arrays.stream(languageFiles)
                        .filter(lang -> lang.additionalLang != null && Arrays.stream(lang.additionalLang).anyMatch(l -> l.equalsIgnoreCase(locale)))
                        .findFirst()
                        .orElse(languageFiles[0]));
    }

    private static String getLocaleTagByPlayer(Player player) {
        return player != null ? player.getClientOption(ClientOption.LOCALE) : null;
    }
}

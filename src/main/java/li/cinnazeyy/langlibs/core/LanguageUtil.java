package li.cinnazeyy.langlibs.core;

import com.destroystokyo.paper.ClientOption;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class LanguageUtil extends YamlFileFactory {
    private static final String CONFIG_VERSION_PATH = "config-version";
    private final static String LANG_HEAD_ID_PATH = "lang.head-id";
    public LanguageFile[] languageFiles;

    public LanguageUtil(LanguageFile[] langFiles) {
        super(langFiles);
        languageFiles = langFiles;

        Arrays.stream(langFiles).forEach(lang -> {
            if (!lang.getFile().exists()) {
                createFile(lang);
            } else if (reloadFile(lang) && lang.getDouble(CONFIG_VERSION_PATH) != lang.getVersion()) {
                updateFile(lang);
            }
            reloadFile(lang);
        });
    }

    public String get(CommandSender sender, String key) {
        return getLanguageFileByLocale(sender instanceof Player ? getLocaleTagByPlayer((Player) sender) : languageFiles[0].getTag()).getTranslation(key);
    }

    public String get(CommandSender sender, String key, String... args) {
        return getLanguageFileByLocale(sender instanceof Player ? getLocaleTagByPlayer((Player) sender) : languageFiles[0].getTag()).getTranslation(key, args);
    }

    public List<String> getList(CommandSender sender, String key) {
        return getLanguageFileByLocale(sender instanceof Player ? getLocaleTagByPlayer((Player) sender) : languageFiles[0].getTag()).getTranslations(key);
    }

    public List<String> getList(CommandSender sender, String key, String... args) {
        return getLanguageFileByLocale(sender instanceof Player ? getLocaleTagByPlayer((Player) sender) : languageFiles[0].getTag()).getTranslations(key, args);
    }

    public LanguageFile getLanguageFileByLocale(String locale) {
        return Arrays.stream(languageFiles)
                .filter(lang -> lang.getTag().equalsIgnoreCase(locale))
                .findFirst()
                .orElseGet(() -> Arrays.stream(languageFiles)
                        .filter(lang -> lang.additionalLang != null && Arrays.stream(lang.additionalLang).anyMatch(l -> l.equalsIgnoreCase(locale)))
                        .findFirst()
                        .orElse(languageFiles[0]));
    }

    public String getLocaleTagByPlayer(Player player) {
        return player != null ? player.getClientOption(ClientOption.LOCALE) : null;
    }
}

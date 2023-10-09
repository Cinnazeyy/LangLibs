package li.cinnazeyy.langlibs.core.language;

import com.destroystokyo.paper.ClientOption;
import li.cinnazeyy.langlibs.core.file.LanguageFile;
import li.cinnazeyy.langlibs.core.file.YamlFileFactory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class LanguageUtil extends YamlFileFactory {
    private static final String CONFIG_VERSION_PATH = "config-version";
    private final static String LANG_HEAD_ID_PATH = "lang.head-id";
    public LanguageFile[] languageFiles;

    public LanguageUtil(Plugin plugin) {
        super(LangLibAPI.getLanguageFiles(plugin));
        LanguageFile[] langFiles = LangLibAPI.getLanguageFiles(plugin);
        languageFiles = langFiles;

        Arrays.stream(langFiles).forEach(lang -> {
            if (!lang.getFile().exists()) createFile(lang);
            else if (reloadFile(lang) && lang.getDouble(CONFIG_VERSION_PATH) != lang.getVersion()) updateFile(lang);
            reloadFile(lang);
        });
    }

    public String get(CommandSender sender, String key) {
        return (sender instanceof Player ? getLanguageFileByPlayer((Player) sender) : languageFiles[0]).getTranslation(key);
    }

    public String get(CommandSender sender, String key, String... args) {
        return (sender instanceof Player ? getLanguageFileByPlayer((Player) sender) : languageFiles[0]).getTranslation(key, args);
    }

    public List<String> getList(CommandSender sender, String key) {
        return (sender instanceof Player ? getLanguageFileByPlayer((Player) sender) : languageFiles[0]).getTranslations(key);
    }

    public List<String> getList(CommandSender sender, String key, String... args) {
        return (sender instanceof Player ? getLanguageFileByPlayer((Player) sender) : languageFiles[0]).getTranslations(key, args);
    }

    public LanguageFile getLanguageFileByPlayer(Player player) {
        String locale = LangLibAPI.getPlayerLang(player);

        return Arrays.stream(languageFiles)
                .filter(lang -> lang.getLanguage().toString().equalsIgnoreCase(locale))
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

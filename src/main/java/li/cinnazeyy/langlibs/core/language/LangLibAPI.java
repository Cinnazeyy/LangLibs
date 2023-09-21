package li.cinnazeyy.langlibs.core.language;

import li.cinnazeyy.langlibs.core.file.LanguageFile;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class LangLibAPI {

    private static HashMap<Plugin, LanguageFile[]> pluginLangFiles = new HashMap<>();
    private static HashMap<UUID, String> playerLocale = new HashMap<>();

    public static void register(Plugin plugin, LanguageFile[] langFiles) {
        pluginLangFiles.put(plugin,langFiles);
        Bukkit.getConsoleSender().sendMessage(Component.text("Registered plugin " + plugin.getName() + " to the language system"));
    }

    public static String getPlayerLang(Player player) {
        return playerLocale.get(player.getUniqueId());
    }

    public static void setPlayerLang(Player player, String lang) {

    }

    public static LanguageFile[] getLanguageFiles(Plugin plugin) {
        LanguageFile[] languageFiles = pluginLangFiles.get(plugin);
        if (languageFiles == null) throw new RuntimeException("LanguageAPI has not been registered yet!");
        return languageFiles;
    }


}

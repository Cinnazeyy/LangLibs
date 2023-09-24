package li.cinnazeyy.langlibs.core.language;

import li.cinnazeyy.langlibs.core.DatabaseConnection;
import li.cinnazeyy.langlibs.core.file.LanguageFile;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class LangLibAPI {

    private static HashMap<Plugin, LanguageFile[]> pluginLangFiles = new HashMap<>();
    private static HashMap<UUID, String> playerLocale = new HashMap<>();

    public static void register(Plugin plugin, LanguageFile[] langFiles) {
        pluginLangFiles.put(plugin,langFiles);
        Bukkit.getConsoleSender().sendMessage(Component.text("Registered plugin " + plugin.getName() + " to the language system"));
    }

    public static String getPlayerLang(Player player) {
        String lang = playerLocale.get(player.getUniqueId());
        if (lang != null) return lang;
        else {
            try (ResultSet rsUser = DatabaseConnection.createStatement("SELECT uuid, lang FROM langUsers WHERE uuid = ?").setValue(player.getUniqueId()).executeQuery()) {
                if (rsUser.next()) playerLocale.put(UUID.fromString(rsUser.getString(0)),rsUser.getString(1));
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.SEVERE, "A SQL error occurred!", e);
            }
        }
        return playerLocale.get(player.getUniqueId());
    }

    public static void setPlayerLang(Player player, String lang) {
        playerLocale.put(player.getUniqueId(), lang);
        try {
            DatabaseConnection.createStatement("INSERT INTO langUsers (uuid, lang) VALUES (?, ?) ON DUPLICATE KEY UPDATE uuid=? lang=?")
                    .setValue(player)
                    .setValue(lang)
                    .setValue(player)
                    .setValue(lang).executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "A SQL error occurred!", e);
        }
    }

    public static void removePlayerLang(Player player) {
        playerLocale.remove(player.getUniqueId());
    }

    public static LanguageFile[] getLanguageFiles(Plugin plugin) {
        LanguageFile[] languageFiles = pluginLangFiles.get(plugin);
        if (languageFiles == null) throw new RuntimeException("LanguageAPI has not been registered yet!");
        return languageFiles;
    }


}

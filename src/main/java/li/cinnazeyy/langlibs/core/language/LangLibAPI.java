package li.cinnazeyy.langlibs.core.language;

import li.cinnazeyy.langlibs.LangLibs;
import li.cinnazeyy.langlibs.core.DatabaseConnection;
import li.cinnazeyy.langlibs.core.file.LanguageFile;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;

public class LangLibAPI {

    private static final HashMap<Plugin, LanguageFile[]> pluginLangFiles = new HashMap<>();
    private static final HashMap<UUID, String> playerLocale = new HashMap<>();

    @SuppressWarnings("unused")
    public static void register(Plugin plugin, LanguageFile[] langFiles) {
        pluginLangFiles.put(plugin,langFiles);
        Bukkit.getConsoleSender().sendMessage(text("Registered plugin " + plugin.getName() + " to the language system", NamedTextColor.GREEN));
    }

    public static String getPlayerLang(UUID playerUUID) {
        String lang = playerLocale.get(playerUUID);
        if (lang != null) return lang;
        else {
            try (ResultSet rsUser = DatabaseConnection.createStatement("SELECT uuid, lang FROM langUsers WHERE uuid = ?").setValue(playerUUID.toString()).executeQuery()) {
                if (rsUser.next()) playerLocale.put(UUID.fromString(rsUser.getString(1)),rsUser.getString(2));
                DatabaseConnection.closeResultSet(rsUser);
            } catch (SQLException e) {
                LangLibs.getPlugin().getComponentLogger().error(text("A SQL error occurred!"), e);
            }
        }
        return playerLocale.get(playerUUID);
    }

    public static void setPlayerLang(Player player, String lang) {
        playerLocale.put(player.getUniqueId(), lang);
        String uuid = player.getUniqueId().toString();

        //TODO: Fix SQL Library
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO langUsers (uuid, lang) VALUES (?, ?) ON DUPLICATE KEY UPDATE uuid = ?, lang = ?";

            // Create a PreparedStatement
            try {
                assert connection != null;
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    // Set the parameter values
                    preparedStatement.setString(1, uuid);
                    preparedStatement.setString(2, lang);
                    preparedStatement.setString(3, uuid);
                    preparedStatement.setString(4, lang);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                LangLibs.getPlugin().getComponentLogger().error(text("A SQL error occurred!"), e);
            }

            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            /*player.sendMessage(Utils.ChatUtils.getInfoMessageFormat(
                    LangUtil.getInstance().get(getMenuPlayer(), LangPaths.Message.Info.CHANGED_LANGUAGE,
                            LangUtil.getInstance().getLanguageFileByPlayer(getMenuPlayer()).getLanguage().toString())));*/
        } catch (SQLException e) {
            LangLibs.getPlugin().getComponentLogger().error(text("A SQL error occurred!"), e);
        }

        /*try {
            DatabaseConnection.createStatement("INSERT INTO langUsers (uuid, lang) VALUES (?, ?) ON DUPLICATE KEY UPDATE uuid = ? lang = `?`")
                    .setValue(player.getUniqueId().toString())
                    .setValue(lang)
                    .setValue(player.getUniqueId().toString())
                    .setValue(lang).executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "A SQL error occurred!", e);
        }*/
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

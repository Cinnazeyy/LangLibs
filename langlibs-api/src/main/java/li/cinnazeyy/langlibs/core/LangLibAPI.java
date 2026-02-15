package li.cinnazeyy.langlibs.core;

import com.alpsbte.alpslib.io.database.SqlHelper;
import li.cinnazeyy.langlibs.core.file.LanguageFile;
import li.cinnazeyy.langlibs.core.language.LanguageUtil;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;

@SuppressWarnings("unused")
public class LangLibAPI {

    private static final HashMap<Plugin, LanguageFile[]> pluginLangFiles = new HashMap<>();
    private static final HashMap<UUID, String> playerLocale = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(LangLibAPI.class);

    @SuppressWarnings("unused")
    public static void register(Plugin plugin, LanguageFile[] langFiles) {
        pluginLangFiles.put(plugin,langFiles);
        Bukkit.getConsoleSender().sendMessage(text("Registered plugin " + plugin.getName() + " to the language system", NamedTextColor.GREEN));
    }

    public static String getPlayerLang(UUID playerUUID) {
        String lang = playerLocale.get(playerUUID);
        if (lang != null) return lang;
        else {
            try {
                SqlHelper.runQuery("SELECT uuid, lang FROM langUsers WHERE uuid = ?", ps -> {
                    ps.setString(1, playerUUID.toString());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        playerLocale.put(UUID.fromString(rs.getString(1)), rs.getString(2));
                } else {
                    Player p = Bukkit.getPlayer(playerUUID);
                    if (p != null) {
                        playerLocale.put(playerUUID, LanguageUtil.getLocaleTagByPlayer(p));
                    } else {
                        playerLocale.put(playerUUID, "en_US");
                    }
                }
                });
            } catch (SQLException e) {
                logger.error("A SQL error occurred!", e);
            }
        }
        return playerLocale.get(playerUUID);
    }

    public static void setPlayerLang(@NonNull Player player, String lang) {
        playerLocale.put(player.getUniqueId(), lang);
        String uuid = player.getUniqueId().toString();

        try {
            SqlHelper.runQuery("INSERT INTO langUsers (uuid, lang) VALUES (?, ?) ON DUPLICATE KEY UPDATE uuid = ?, lang = ?", ps -> {
                ps.setString(1, uuid);
                ps.setString(2, lang);
                ps.setString(3, uuid);
                ps.setString(4, lang);
                ps.executeUpdate();
            });
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        } catch (SQLException e) {
            logger.error("A SQL error occurred!", e);
        }
    }

    public static void removePlayerLang(@NonNull Player player) {
        playerLocale.remove(player.getUniqueId());
    }

    public static LanguageFile @NonNull [] getLanguageFiles(Plugin plugin) {
        LanguageFile[] languageFiles = pluginLangFiles.get(plugin);
        if (languageFiles == null) throw new RuntimeException("LanguageAPI has not been registered yet!");
        return languageFiles;
    }
}

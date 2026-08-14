package li.cinnazeyy.langlibs.core.data;

import li.cinnazeyy.langlibs.core.database.DatabaseConnection;
import li.cinnazeyy.langlibs.core.database.DatabaseCredentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class MysqlDataProvider implements DataProvider {
    private static final Logger logger = LoggerFactory.getLogger(MysqlDataProvider.class);

    private static final String SELECT_LANG = "SELECT uuid, lang FROM langUsers WHERE uuid = ?";
    private static final String UPSERT_LANG = "INSERT INTO langUsers (uuid, lang) VALUES (?, ?) ON DUPLICATE KEY UPDATE lang = ?";

    private final Map<UUID, String> cache = new HashMap<>();
    private final DatabaseCredentials credentials;

    public MysqlDataProvider(DatabaseCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public void init(Plugin plugin) {
        try {
            DatabaseConnection.InitializeDatabase(credentials);
            Bukkit.getConsoleSender().sendMessage(text("Successfully initialized database connection.", GREEN));
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize database connection", e);
        }
    }

    @Override
    public void close() {
        cache.clear();
    }

    @Override
    public String getPlayerLang(UUID playerUUID) {
        return cache.get(playerUUID);
    }

    @Override
    public boolean setPlayerLang(Player player, String lang) {
        cache.put(player.getUniqueId(), lang);
        try {
            DatabaseConnection.createStatement(UPSERT_LANG)
                    .setValue(player.getUniqueId().toString())
                    .setValue(lang)
                    .setValue(lang)
                    .executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error("A SQL error occurred while saving player language!", e);
            return false;
        }
    }

    @Override
    public boolean loadPlayerLang(Player player) {
        final UUID uuid = player.getUniqueId();
        try (ResultSet rs = DatabaseConnection
                .createStatement(SELECT_LANG)
                .setValue(uuid.toString())
                .executeQuery()) {
            if (rs.next()) {
                String lang = rs.getString(2);
                cache.put(uuid, lang);
                return true;
            }
            DatabaseConnection.closeResultSet(rs);
        } catch (SQLException e) {
            logger.error("A SQL error occurred while loading player language!", e);
        }
        return false;
    }

    @Override
    public void removePlayerLang(UUID playerUUID) {
        cache.remove(playerUUID);
    }
}
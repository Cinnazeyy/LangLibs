package li.cinnazeyy.langlibs.core;

import li.cinnazeyy.langlibs.LangLibs;
import li.cinnazeyy.langlibs.core.database.DatabaseConnection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

import static net.kyori.adventure.text.Component.text;

public class EventListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(LangLibs.getPlugin(), () -> {
            String uuid = player.getUniqueId().toString();
            try (ResultSet rsUser = DatabaseConnection.createStatement("SELECT uuid, lang FROM langUsers WHERE uuid = ?").setValue(uuid).executeQuery()) {
                if (rsUser.next()) LangLibAPI.setPlayerLang(player,rsUser.getString(2));
                DatabaseConnection.closeResultSet(rsUser);
            } catch (SQLException e) {
                LangLibs.getPlugin().getComponentLogger().error(text("A SQL error occurred!"), e);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDisconnect(PlayerQuitEvent event) {
        LangLibAPI.removePlayerLang(event.getPlayer());
    }
}

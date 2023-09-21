package li.cinnazeyy.langlibs.core;

import li.cinnazeyy.langlibs.LangLibs;
import li.cinnazeyy.langlibs.core.language.LangLibAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(LangLibs.getPlugin(), () -> {
            try (ResultSet rsUser = DatabaseConnection.createStatement("SELECT uuid, lang FROM langUsers WHERE uuid = ?").setValue(event.getPlayer().getUniqueId()).executeQuery()) {
                if (!rsUser.next()) return; // User Doesnt have any database entry




            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }
}

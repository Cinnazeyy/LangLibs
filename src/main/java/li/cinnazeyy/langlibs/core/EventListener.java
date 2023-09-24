package li.cinnazeyy.langlibs.core;

import com.destroystokyo.paper.ClientOption;
import li.cinnazeyy.langlibs.LangLibs;
import li.cinnazeyy.langlibs.core.language.LangLibAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class EventListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(LangLibs.getPlugin(), () -> {
            try (ResultSet rsUser = DatabaseConnection.createStatement("SELECT uuid, lang FROM langUsers WHERE uuid = ?").setValue(player.getUniqueId()).executeQuery()) {
                if (!rsUser.next()) LangLibAPI.setPlayerLang(player,player.getClientOption(ClientOption.LOCALE));
                else LangLibAPI.setPlayerLang(player,rsUser.getString(1));
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.SEVERE, "A SQL error occurred!", e);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        LangLibAPI.removePlayerLang(event.getPlayer());
    }
}

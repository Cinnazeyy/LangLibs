package li.cinnazeyy.langlibs.core;

import com.alpsbte.alpslib.io.database.SqlHelper;
import com.destroystokyo.paper.event.player.PlayerClientOptionsChangeEvent;
import li.cinnazeyy.langlibs.LangLibs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

import static net.kyori.adventure.text.Component.text;

public class EventListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(LangLibs.getPlugin(), () -> {
            String uuid = player.getUniqueId().toString();
            try {
                SqlHelper.runQuery("SELECT uuid, lang FROM langUsers WHERE uuid = ?", ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) LangLibAPI.setPlayerLang(player, rs.getString(2));
                });
            } catch (SQLException e) {
                LangLibs.getPlugin().getComponentLogger().error(text("A SQL error occurred!"), e);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDisconnect(@NonNull PlayerQuitEvent event) {
        LangLibAPI.removePlayerLang(event.getPlayer());
    }

    @EventHandler
    public void onPlayerClientOptionsChange(@NotNull PlayerClientOptionsChangeEvent e) {
        LangLibAPI.removePlayerLang(e.getPlayer());
    }
}

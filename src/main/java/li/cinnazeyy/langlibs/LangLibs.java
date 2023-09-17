package li.cinnazeyy.langlibs;

import li.cinnazeyy.langlibs.core.LangLibAPI;
import net.kyori.adventure.Adventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class LangLibs extends JavaPlugin {
    private static final String VERSION = "1.0";
    private static LangLibs plugin;
    @Override
    public void onEnable() {
        plugin = this;

        LangLibAPI.register("PLOTSYSTEM",null);
        Bukkit.getConsoleSender().sendMessage(Component.text(LangLibAPI.get(null,"PLOTSYSTEM","plot.abandon")));

        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage(Component.text("---[", NamedTextColor.DARK_GRAY)
                .append(Component.text("LangLibs", NamedTextColor.AQUA))
                .append(Component.text("v" + VERSION,NamedTextColor.GOLD))
                .append(Component.text("]---")));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

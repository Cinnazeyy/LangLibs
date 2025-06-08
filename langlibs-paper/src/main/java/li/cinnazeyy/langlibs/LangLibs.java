package li.cinnazeyy.langlibs;

import li.cinnazeyy.langlibs.core.EventListener;
import li.cinnazeyy.langlibs.core.config.ConfigUtil;
import li.cinnazeyy.langlibs.core.database.DatabaseConnection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public final class LangLibs extends JavaPlugin {
    private static final String VERSION = "1.4.2";
    private static LangLibs plugin;
    @Override
    public void onEnable() {
        plugin = this;

        // Initialize configs
        try {
            ConfigUtil.init(this);
        } catch (ConfigurateException e) {
            this.getComponentLogger().warn(text("Could not load configuration files!"));
            Bukkit.getConsoleSender().sendMessage(text("The config files must be configured!", YELLOW));
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize database connection
        try {
            DatabaseConnection.InitializeDatabase(ConfigUtil.getConfig().getCredentials());
            Bukkit.getConsoleSender().sendMessage("Successfully initialized database connection.");
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage("Could not initialize database connection!");
            LangLibs.getPlugin().getComponentLogger().error(text(ex.getMessage()), ex);
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register Events
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // Startup graphic
        Bukkit.getConsoleSender().sendMessage(empty());
        Bukkit.getConsoleSender().sendMessage(text(" ▄▄▄    ▄▀", AQUA));
        Bukkit.getConsoleSender().sendMessage(text("█   █  ▀█▀▀▀▀▄▄", AQUA));
        Bukkit.getConsoleSender().sendMessage(text("█▀▀▀█    ▀     ▀▀▄", AQUA));
        Bukkit.getConsoleSender().sendMessage(text("█   █  ▄▄█▀▀▀▄▄   ▀▄", AQUA));
        Bukkit.getConsoleSender().sendMessage(text("▄    ▄█▀  ▄▄▄███▄  ▀▄", AQUA));
        Bukkit.getConsoleSender().sendMessage(text("█   ▄█▀ ▄▄ ██▀▀██▄  ▀▄", AQUA));
        Bukkit.getConsoleSender().sendMessage(text("█   █▄  ▀▀█▀  █▀██   █", AQUA));
        Bukkit.getConsoleSender().sendMessage(text("▀▄  ▀█   ▄▄██▄ ▀▄▀   █", AQUA));
        Bukkit.getConsoleSender().sendMessage(text(" ▀▄  ▀█ ▄█████ ▄▀    ▀", AQUA));
        Bukkit.getConsoleSender().sendMessage(text("  ▀▄   ▀▀████▀▀  ▄▄█▄▄▄", AQUA));
        Bukkit.getConsoleSender().sendMessage(text("    ▀▄▄     ▄     ▄█▄▄▄", AQUA));
        Bukkit.getConsoleSender().sendMessage(text("       ▀▀▄▄▄▄█▄  █ █▄▀ █", AQUA));
        Bukkit.getConsoleSender().sendMessage(text("            ▄▀   ▀▀▀  ▀", AQUA));
        Bukkit.getConsoleSender().sendMessage(empty());

        Bukkit.getConsoleSender().sendMessage(text("[", DARK_GRAY)
                .append(text("LangLibs", AQUA))
                .append(text(" v" + VERSION, GOLD))
                .append(text("] Loaded successfully!")));
    }

    @Override
    public void reloadConfig() {
        try {
            ConfigUtil.reloadAllConfigs();
        } catch (ConfigurateException e) {
            this.getComponentLogger().warn(text("Could not load configuration files!"));
            Bukkit.getConsoleSender().sendMessage(text("The config files must be configured!", YELLOW));
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

    public static LangLibs getPlugin() { return plugin; }
}

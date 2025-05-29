package li.cinnazeyy.langlibs;

import com.alpsbte.alpslib.io.config.ConfigNotImplementedException;
import li.cinnazeyy.langlibs.core.DatabaseConnection;
import li.cinnazeyy.langlibs.core.EventListener;
import li.cinnazeyy.langlibs.core.file.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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
            ConfigUtil.init();
        } catch (ConfigNotImplementedException e) {
            this.getComponentLogger().warn(text("Could not load configuration file."));
            Bukkit.getConsoleSender().sendMessage(text("The config file must be configured!", YELLOW));
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        reloadConfig();

        // Initialize database connection
        try {
            DatabaseConnection.InitializeDatabase();
            Bukkit.getConsoleSender().sendMessage("Successfully initialized database connection.");
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage("Could not initialize database connection.");
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
        ConfigUtil.getInstance().reloadFiles();
        ConfigUtil.getInstance().saveFiles();
    }

    public static LangLibs getPlugin() { return plugin; }

    public @NotNull YamlConfiguration getConfig() {
        return ConfigUtil.getInstance().configs[0];
    }

    public @NotNull YamlConfiguration getLanguageConfig() {
        return ConfigUtil.getInstance().configs[1];
    }
}

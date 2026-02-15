package li.cinnazeyy.langlibs;

import com.alpsbte.alpslib.io.database.DatabaseConnection;
import com.google.common.io.CharStreams;
import li.cinnazeyy.langlibs.core.EventListener;
import li.cinnazeyy.langlibs.core.config.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public final class LangLibs extends JavaPlugin {
    private static final String VERSION = "1.5.1";
    private static LangLibs plugin;

    @Override
    public void onEnable() {
        plugin = this;

        // Initialize configs
        createConfig("config.yml");
        createConfig("languages.yml");
        try {
            ConfigUtil.init(this);
        } catch (ConfigurateException e) {
            this.getComponentLogger().warn(text("Could not load configuration files!"), e);
            Bukkit.getConsoleSender().sendMessage(text("The config files must be configured!", YELLOW));
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize database connection
        try {
            initDatabase();
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

    public void createConfig(String configFileName) {
        File file = getDataPath().resolve(configFileName).toFile();
        if (!file.exists()) saveResource(configFileName, false);
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

    public static LangLibs getPlugin() {return plugin;}

    public void initDatabase() throws IOException, SQLException, ClassNotFoundException {
        DatabaseConnection.initializeDatabase(ConfigUtil.getConfig().getCredentials(), true);
        var initScript = CharStreams.toString(Objects.requireNonNull(getTextResource("DATABASE.sql")));
        try (var con = DatabaseConnection.getConnection(); var s = con.createStatement()) {
            s.execute(initScript);
        }
    }
}

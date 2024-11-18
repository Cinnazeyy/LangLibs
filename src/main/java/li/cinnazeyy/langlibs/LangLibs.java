package li.cinnazeyy.langlibs;

import li.cinnazeyy.langlibs.commands.CMD_Language;
import li.cinnazeyy.langlibs.core.DatabaseConnection;
import li.cinnazeyy.langlibs.core.EventListener;
import li.cinnazeyy.langlibs.util.LangUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;

public final class LangLibs extends JavaPlugin {
    private static final String VERSION = "1.3";
    private static LangLibs plugin;
    private YamlConfiguration config;
    @Override
    public void onEnable() {
        plugin = this;

        // Create configs
        createConfig();

        // Register language heads
        LangUtils.registerCustomHeads();

        // Initialize database connection
        try {
            DatabaseConnection.InitializeDatabase();
            Bukkit.getConsoleSender().sendMessage("Successfully initialized database connection.");
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage("Could not initialize database connection.");
            Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);

            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register Events
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // Register Commands
        final CommandMap commandMap = Bukkit.getServer().getCommandMap();
        if (config.getBoolean("languageSelection.enabled")) commandMap.register("language", new CMD_Language("language"));

        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage(Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("LangLibs", NamedTextColor.AQUA))
                .append(Component.text("v" + VERSION,NamedTextColor.GOLD))
                .append(Component.text("] Loaded successfully!")));
    }

    public static LangLibs getPlugin() { return plugin; }

    public @NotNull YamlConfiguration getConfig() {
        return config;
    }

    private void createConfig() {
        File createConfig = new File(getDataFolder(), "config.yml");
        if (!createConfig.exists()) {
            createConfig.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        config = new YamlConfiguration();
        try {
            config.load(createConfig);
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "An error occurred!", e);
        }
    }
}

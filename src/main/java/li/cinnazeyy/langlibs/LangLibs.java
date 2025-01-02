package li.cinnazeyy.langlibs;

import li.cinnazeyy.langlibs.core.DatabaseConnection;
import li.cinnazeyy.langlibs.core.EventListener;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public final class LangLibs extends JavaPlugin {
    private static final String VERSION = "1.4.2";
    private static LangLibs plugin;
    private YamlConfiguration config;
    private YamlConfiguration languageConfig;
    @Override
    public void onEnable() {
        plugin = this;

        // Create configs
        createConfigs();

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

        // Plugin startup logic

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

    public static LangLibs getPlugin() { return plugin; }

    public @NotNull YamlConfiguration getConfig() {
        return config;
    }

    public @NotNull YamlConfiguration getLanguageConfig() { return languageConfig; }

    private void createConfigs() {
        File createConfig = new File(getDataFolder(), "config.yml");
        File createLangConfig = new File(getDataFolder(), "languages.yml");

        if (!createConfig.exists()) {
            createConfig.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        if (!createLangConfig.exists()) {
            createLangConfig.getParentFile().mkdirs();
            saveResource("languages.yml", false);
        }

        config = new YamlConfiguration();
        languageConfig = new YamlConfiguration();

        try {
            config.load(createConfig);
            languageConfig.load(createLangConfig);
        } catch (Exception e) {
            LangLibs.getPlugin().getComponentLogger().error(text("An error occurred!"), e);
        }
    }
}

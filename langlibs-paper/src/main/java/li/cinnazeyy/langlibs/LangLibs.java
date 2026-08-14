package li.cinnazeyy.langlibs;

import li.cinnazeyy.langlibs.core.EventListener;
import li.cinnazeyy.langlibs.core.YamlConfigDataProvider;
import li.cinnazeyy.langlibs.core.config.ConfigUtil;
import li.cinnazeyy.langlibs.core.data.DataProvider;
import li.cinnazeyy.langlibs.core.data.MysqlDataProvider;
import li.cinnazeyy.langlibs.core.data.VelocityDataProvider;
import li.cinnazeyy.langlibs.core.LangLibAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;

import java.nio.file.Files;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public final class LangLibs extends JavaPlugin {
    private static final String VERSION = "1.5.1";
    private static LangLibs plugin;
    private static YamlConfigDataProvider configProvider;
    private static DataProvider dataProvider;

    @Override
    public void onEnable() {
        plugin = this;

        // Initialize configs
        createConfig("config.yml");
        createConfig("languages.yml");
        try {
            configProvider = new YamlConfigDataProvider();
            ConfigUtil.init(configProvider);
        } catch (ConfigurateException e) {
            this.getComponentLogger().warn(text("Could not load configuration files!"), e);
            Bukkit.getConsoleSender().sendMessage(text("The config files must be configured!", YELLOW));
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize data provider
        if (!initDataProvider()) {
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
        if (Files.exists(getDataPath().resolve(configFileName))) return;
        saveResource(configFileName, false);
    }

    private boolean initDataProvider() {
        String source = ConfigUtil.getConfig().getDataSource();
        switch (source) {
            case "mysql","velocity" -> {}
            default -> {
                Bukkit.getConsoleSender().sendMessage(text("Unknown 'data-source' value: " + source + ". Falling back to mysql.", YELLOW));
                source = "mysql";
            }
        }

        // suppressed for now, since there will likely be more types in the future
        //noinspection SwitchStatementWithTooFewBranches
        dataProvider = switch (source) {
            case "velocity" -> new VelocityDataProvider();
            default -> new MysqlDataProvider(ConfigUtil.getConfig().getCredentials());
        };

        try {
            LangLibAPI.setDataProvider(dataProvider, this);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(text("Could not initialize data provider!", RED));
            LangLibs.getPlugin().getComponentLogger().error(text(e.getMessage()), e);
            return false;
        }

        Bukkit.getConsoleSender().sendMessage(text("Using '" + source + "' data provider.", AQUA));
        return true;
    }

    @Override
    public void reloadConfig() {
        configProvider.reloadAllConfigs();
    }

    @Override
    public void onDisable() {
        if (dataProvider != null) dataProvider.close();
    }

    public static LangLibs getPlugin() {return plugin;}
}

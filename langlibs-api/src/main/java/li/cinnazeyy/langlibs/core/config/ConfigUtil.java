package li.cinnazeyy.langlibs.core.config;

import org.bukkit.plugin.Plugin;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.util.NamingSchemes;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public class ConfigUtil {
    private ConfigUtil() {}

    private static MainConfig mainConfig = null;
    private static LanguageConfig languageConfig = null;

    private static CommentedConfigurationNode configRoot = null;
    private static CommentedConfigurationNode languageRoot = null;

    private static YamlConfigurationLoader configLoader = null;
    private static YamlConfigurationLoader languageLoader = null;

    @SuppressWarnings("unused")
    public static void init(Plugin plugin) throws ConfigurateException {
        final ObjectMapper.Factory customFactory = ObjectMapper.factoryBuilder()
                .defaultNamingScheme(NamingSchemes.PASSTHROUGH)
                .build();

        configLoader = YamlConfigurationLoader.builder()
                .path(plugin.getDataPath().resolve("config.yml"))
                .defaultOptions(opts -> opts.serializers(build -> build.registerAnnotatedObjects(customFactory)))
                .build();
        languageLoader = YamlConfigurationLoader.builder()
                .path(plugin.getDataPath().resolve("languages.yml"))
                .defaultOptions(opts -> opts.serializers(build -> build.registerAnnotatedObjects(customFactory)))
                .build();

        loadConfigFiles();
    }

    @SuppressWarnings("unused")
    public static void reloadAllConfigs() throws ConfigurateException {
        configLoader.save(configRoot);
        languageLoader.save(languageRoot);

        loadConfigFiles();
    }

    @SuppressWarnings("unused")
    public static MainConfig getConfig() {
        return mainConfig;
    }

    public static LanguageConfig getLanguageConfig() {
        return languageConfig;
    }

    private static void loadConfigFiles() throws ConfigurateException {
        configRoot = configLoader.load();
        mainConfig = configRoot.get(MainConfig.class);

        languageRoot = languageLoader.load();
        languageConfig = languageRoot.get(LanguageConfig.class);
    }
}

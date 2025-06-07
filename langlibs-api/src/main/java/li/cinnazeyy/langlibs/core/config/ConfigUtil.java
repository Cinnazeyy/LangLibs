package li.cinnazeyy.langlibs.core.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;

public class ConfigUtil {
    private static MainConfig mainConfig = null;
    private static LanguageConfig languageConfig = null;

    private static CommentedConfigurationNode configRoot = null;
    private static CommentedConfigurationNode languageRoot = null;

    private static YamlConfigurationLoader configLoader = null;
    private static YamlConfigurationLoader languageLoader = null;

    @SuppressWarnings("unused")
    public static void init() throws ConfigurateException {
        configLoader = YamlConfigurationLoader.builder()
                .path(Path.of("config.yml"))
                .build();
        languageLoader = YamlConfigurationLoader.builder()
                .path(Path.of("languages.yml"))
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

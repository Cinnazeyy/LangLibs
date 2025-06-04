package li.cinnazeyy.langlibs.core.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;

public class ConfigUtil {
    private static MainConfig mainConfig = null;

    private static CommentedConfigurationNode configRoot = null;
    private static CommentedConfigurationNode languageRoot = null;

    @SuppressWarnings("unused")
    public static void init() {
        YamlConfigurationLoader configLoader = YamlConfigurationLoader.builder()
                .path(Path.of("config.yml"))
                .build();
        YamlConfigurationLoader languageLoader = YamlConfigurationLoader.builder()
                .path(Path.of("languages.yml"))
                .build();


        try {
            configRoot = configLoader.load();
            mainConfig = configRoot.get(MainConfig.class);

            languageRoot = languageLoader.load();
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    public static MainConfig getConfig() {
        return mainConfig;
    }

    public static CommentedConfigurationNode getLanguageConfig() {
        return languageRoot;
    }
}

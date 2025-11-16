package li.cinnazeyy.langlibs.core.file;

import li.cinnazeyy.langlibs.core.language.Language;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class LanguageFile {
    private final Plugin plugin;
    private final Language language;
    private final Path filePath;
    private final double version;
    private final YamlConfigurationLoader languageLoader;

    public String[] additionalLang;
    private CommentedConfigurationNode root = null;
    private boolean lastTranslationNotSuccessful = false;

    public LanguageFile(@NotNull Plugin plugin, double version, Language lang) {
        this.plugin = plugin;
        this.language = lang;
        this.version = version;
        this.filePath = plugin.getDataPath().toAbsolutePath().resolve(Paths.get("lang", lang + ".yml"));

        languageLoader = YamlConfigurationLoader.builder()
                .path(filePath)
                .build();

        loadFile();
    }

    public LanguageFile(Plugin plugin, double version, Language lang, String... additionalLang) {
        this(plugin, version, lang);
        this.additionalLang = additionalLang;
    }

    public Language getLanguage() {return language;}

    public double getVersion() {return version;}

    public double getConfiguredVersion() {return root.node("config-version").getDouble();}

    public String getFileName() {return filePath.toFile().getName();}

    public String getTranslation(@NotNull String key) {
        String translation = root.node((Object[]) key.split("\\.")).getString();
        lastTranslationNotSuccessful = translation == null;
        return translation != null ? translation : "undefined (" + getFileName() + ": " + key + ")";
    }

    public String getTranslation(String key, String @NotNull ... args) {
        String translation = getTranslation(key);
        for (int i = 0; i < args.length; i++) translation = translation.replace("{" + i + "}", args[i]);
        return translation;
    }

    public List<String> getTranslations(@NotNull String key) {
        try {
            var list = root.node((Object[]) key.split("\\.")).getList(String.class);
            lastTranslationNotSuccessful = false;
            return list;
        } catch (SerializationException e) {
            plugin.getComponentLogger().error("Could not parse string list in language file {} with key {}!", filePath.toString(), key);
        }
        lastTranslationNotSuccessful = true;
        return List.of("undefined (" + getFileName() + ": " + key + ")");
    }

    public List<String> getTranslations(String key, String @NotNull ... args) {
        String[] translations = getTranslations(key).toArray(new String[0]);
        for (int i = 0; i < args.length; i++) {
            for (int k = 0; k < translations.length; k++) {
                translations[k] = translations[k].replace("{" + i + "}", args[i]);
            }
        }
        return Arrays.asList(translations);
    }

    public InputStream getDefaultFileStream() {
        return plugin.getResource("lang/" + filePath.toFile().getName());
    }

    public List<String> readDefaultFile() {
        try (InputStream in = getDefaultFileStream()) {
            BufferedReader input = new BufferedReader(new InputStreamReader(in));
            List<String> lines = input.lines().collect(Collectors.toList()); // NOSONAR Need to be a modifiable list
            input.close();
            return lines;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public File getFile() {return filePath.toFile();}

    public void saveFile() throws ConfigurateException {
        languageLoader.save(root);
    }

    public void loadFile() {
        try {
            root = languageLoader.load();
        } catch (ConfigurateException e) {
            plugin.getComponentLogger().error(Component.text("Could not properly load language file" + filePath), e);
        }
    }

    public boolean lastTranslationWasNotSuccessful() {return lastTranslationNotSuccessful;}
}
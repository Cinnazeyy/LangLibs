package li.cinnazeyy.langlibs.core.file;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.ConfigurateException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public abstract class LanguageFileFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageFileFactory.class);


    private final LanguageFile[] langFiles;

    protected LanguageFileFactory(@NotNull LanguageFile[] langFiles) {
        this.langFiles = langFiles;
    }

    /**
     * Saves YAML files
     *
     * @return true if file saved successfully
     */
    @SuppressWarnings("unused")
    public boolean saveFiles() {
        final boolean[] successful = {true};
        Arrays.stream(langFiles).forEach(langFile -> {
            try {
                langFile.saveFile();
            } catch (ConfigurateException e) {
                successful[0] = false;
                LOGGER.error("An error occurred while saving yaml file", e);
            }
        });
        return successful[0];
    }

    /**
     * Reloads YAML files
     */
    public void reloadFiles() {
        Arrays.stream(langFiles).forEach(LanguageFile::loadFile);
    }

    /**
     * Creates a new YAML file with default values
     *
     * @param langFile YAML file
     * @return true if the file created successfully
     */
    public boolean createFile(LanguageFile langFile) {
        try {
            if (!langFile.getFile().getParentFile().exists()) {
                if (!langFile.getFile().getParentFile().mkdirs()) return false;
            }

            if (langFile.getFile().createNewFile()) {
                FileUtils.copyInputStreamToFile(langFile.getDefaultFileStream(), langFile.getFile());
                return true;
            }
        } catch (IOException ex) {
            LOGGER.error("An error occurred while creating yaml file", ex);
        }
        return false;
    }

    /**
     * Update YAML file from default config
     *
     * @param langFile YAML file
     * @return true if file updated successfully
     */
    protected boolean updateFile(LanguageFile langFile) {
        // Create Backup of YAML file
        try {
            FileUtils.copyFile(langFile.getFile(), Paths.get(langFile.getFile().getParentFile().getAbsolutePath(), "old_" + langFile.getFileName()).toFile());
        } catch (IOException ignored) {
            LOGGER.warn("Could not create backup of language file!");
        }

        // Update file
        try {
            List<String> currentFileLines = FileUtils.readLines(langFile.getFile(), StandardCharsets.UTF_8);
            List<String> defaultFileLines = langFile.readDefaultFile();

            currentFileLines.removeIf(s -> s.trim().isEmpty() || s.trim().startsWith("#") || s.split(":").length == 1);
            currentFileLines.forEach(s -> {
                String[] a = s.split(":");
                String newS = String.join(":", Arrays.copyOfRange(a, 1, a.length));
                int index = getIndex(a[0], defaultFileLines);
                if (index > -1)
                    defaultFileLines.set(index, defaultFileLines.get(index).split(":")[0] + ":" + newS);
            });

            defaultFileLines.set(getIndex("config-version", defaultFileLines), "config-version: " + langFile.getVersion());
            Files.write(langFile.getFile().toPath(), defaultFileLines);
            LOGGER.info("Updated {} to version {}.", langFile.getFileName(), langFile.getVersion());
            return true;
        } catch (IOException ex) {
            LOGGER.error("An error occurred while updating config file", ex);
        }
        return false;
    }

    /**
     * @return line index in list
     */
    public int getIndex(String line, List<String> list) {
        for (String s : list)
            if (s.startsWith(line) || s.equalsIgnoreCase(line))
                return list.indexOf(s);
        return -1;
    }
}
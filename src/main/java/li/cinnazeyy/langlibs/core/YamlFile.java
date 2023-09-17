package li.cinnazeyy.langlibs.core;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class YamlFile extends YamlConfiguration {
    private final Path filePath;
    private final double version;
    private final File file;
    private final String fileName;

    protected YamlFile(Path fileName, double version) {
        this.filePath = fileName;
        this.version = version;
        this.file = new File(YamlFileFactory.yamlPlugin.getDataFolder().getAbsolutePath() + File.separator + filePath.toString());
        this.fileName = file.getName();
    }

    @Override
    public @NotNull String saveToString() {
        try {
            // Increase config width to avoid line breaks
            Field op;
            try {
                // Legacy support for older versions of SnakeYAML
                op = YamlConfiguration.class.getDeclaredField("yamlOptions");
            } catch (NoSuchFieldException e) {
                op = YamlConfiguration.class.getDeclaredField("yamlDumperOptions");
            }
            op.setAccessible(true);
            final DumperOptions options = (DumperOptions) op.get(this);
            options.setWidth(getMaxConfigWidth());
            options.setSplitLines(false); // throws NoSuchMethodError on Legacy versions of SnakeYAML
        } catch (final Exception ignored) {}

        return super.saveToString();
    }

    public List<String> readDefaultFile() {
        try (InputStream in = getDefaultFileStream()) {
            BufferedReader input = new BufferedReader(new InputStreamReader(in));
            List<String> lines = input.lines().collect(Collectors.toList());
            input.close();
            return lines;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public InputStream getDefaultFileStream() {
        return YamlFileFactory.yamlPlugin.getResource(filePath.toString());
    }

    public double getVersion() {
        return version;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }

    public int getMaxConfigWidth() {
        return 250;
    }
}

package li.cinnazeyy.langlibs.core.file;

import com.alpsbte.alpslib.io.config.ConfigNotImplementedException;
import com.alpsbte.alpslib.io.config.ConfigurationUtil;

import java.nio.file.Paths;

public class ConfigUtil {
    private ConfigUtil() {}

    private static ConfigurationUtil configUtilInstance;

    public static void init() throws ConfigNotImplementedException {
        if (configUtilInstance != null) return;
        configUtilInstance = new ConfigurationUtil(new ConfigurationUtil.ConfigFile[]{
                new ConfigurationUtil.ConfigFile(Paths.get("config.yml"), 1.0, true),
                new ConfigurationUtil.ConfigFile(Paths.get("languages.yml"), 1.0, true)
        });
    }

    public static ConfigurationUtil getInstance() {
        return configUtilInstance;
    }
}

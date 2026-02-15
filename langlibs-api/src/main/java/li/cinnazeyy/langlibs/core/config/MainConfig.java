package li.cinnazeyy.langlibs.core.config;

import com.alpsbte.alpslib.io.database.DatabaseSection;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@SuppressWarnings("unused")
@ConfigSerializable
public class MainConfig {
    public MainConfig() {}

    @Setting("database")
    private DatabaseSection credentials;

    @Comment("NOTE: Do not change!")
    @Setting("version")
    private int version;

    public DatabaseSection getCredentials() {
        return credentials;
    }

    public int getVersion() {
        return version;
    }
}

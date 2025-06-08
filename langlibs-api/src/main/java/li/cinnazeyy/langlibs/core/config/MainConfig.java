package li.cinnazeyy.langlibs.core.config;

import li.cinnazeyy.langlibs.core.database.DatabaseCredentials;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@SuppressWarnings("unused")
@ConfigSerializable
public class MainConfig {
    public MainConfig() {}

    @Setting("database")
    private DatabaseCredentials credentials;

    @Comment("NOTE: Do not change!")
    @Setting("version")
    private int version;

    public DatabaseCredentials getCredentials() {
        return credentials;
    }

    public int getVersion() {
        return version;
    }
}

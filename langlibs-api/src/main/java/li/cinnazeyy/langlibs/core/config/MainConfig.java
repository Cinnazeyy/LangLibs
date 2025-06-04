package li.cinnazeyy.langlibs.core.config;

import li.cinnazeyy.langlibs.core.database.DatabaseCredentials;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class MainConfig {
    @Setting("database")
    private DatabaseCredentials credentials;

    @Setting("config-version")
    @Comment("NOTE: Do not change!")
    private String configVersion;

    public DatabaseCredentials getCredentials() {
        return credentials;
    }
}

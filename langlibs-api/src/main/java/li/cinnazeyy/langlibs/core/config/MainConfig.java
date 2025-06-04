package li.cinnazeyy.langlibs.core.config;

import li.cinnazeyy.langlibs.core.database.DatabaseCredentials;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@SuppressWarnings("unused")
@ConfigSerializable
public class MainConfig {
    @Setting("database")
    private DatabaseCredentials credentials;

    @Comment("NOTE: Do not change!")
    private int version;

    public DatabaseCredentials getCredentials() {
        return credentials;
    }
}

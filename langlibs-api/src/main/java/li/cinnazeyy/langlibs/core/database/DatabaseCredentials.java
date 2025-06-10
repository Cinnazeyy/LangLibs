package li.cinnazeyy.langlibs.core.database;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public record DatabaseCredentials(
        String url,
        String name,
        String username,
        String password
) {}

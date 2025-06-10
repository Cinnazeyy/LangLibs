package li.cinnazeyy.langlibs.core.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public record LanguageSection(
        @Setting("name")
        String name,

        @Setting("region")
        String region,

        @Setting("headId")
        String headId,

        @Setting("itemModel")
        String itemModel
) {}

package li.cinnazeyy.langlibs.core.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@SuppressWarnings("unused")
@ConfigSerializable
public record LanguageConfig(
        LanguageSection en_GB,
        LanguageSection de_DE,
        LanguageSection fr_FR,
        LanguageSection ko_KR,
        LanguageSection pt_PT,
        LanguageSection ru_RU,
        LanguageSection zh_CN,
        LanguageSection zh_TW,
        LanguageSection he_IL,
        LanguageSection custom_1,
        LanguageSection custom_2,
        LanguageSection custom_3,
        LanguageSection custom_4,
        LanguageSection custom_5,

        @Comment("NOTE: Do not change!")
        int version
) {}

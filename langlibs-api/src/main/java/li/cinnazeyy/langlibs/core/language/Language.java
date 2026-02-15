package li.cinnazeyy.langlibs.core.language;

import li.cinnazeyy.langlibs.core.config.ConfigUtil;
import li.cinnazeyy.langlibs.core.config.LanguageConfig;
import li.cinnazeyy.langlibs.core.config.LanguageSection;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unused")
public enum Language {
    en_GB,
    de_DE,
    fr_FR,
    ko_KR,
    pt_PT,
    ru_RU,
    zh_CN,
    zh_TW,
    he_IL,
    da_DK,
    es_ES,
    hu_HU,
    it_IT,
    nl_NL,
    ro_RO,
    custom_1,
    custom_2,
    custom_3,
    custom_4,
    custom_5;

    private final String name, region, headId, itemModel;

    Language() {
        LanguageConfig config = ConfigUtil.getLanguageConfig();
        String name, region, headId, itemModel;

        try {
            LanguageSection section = (LanguageSection) config.getClass().getMethod(this.name()).invoke(config);
            name = section.name();
            region = section.region();
            headId = section.headId();
            itemModel = section.itemModel();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            name = "undefined";
            region = "undefined";
            headId = "36076";
            itemModel = "";
        }

        this.name = name;
        this.region = region;
        this.headId = headId;
        this.itemModel = itemModel;
    }

    public String getRegion() {
        return region;
    }

    public String getName() {
        return name;
    }

    public String getHeadId() {
        return headId;
    }

    public String getItemModel() {
        return itemModel;
    }
}



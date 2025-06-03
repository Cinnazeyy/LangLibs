package li.cinnazeyy.langlibs.core;

import li.cinnazeyy.langlibs.LangLibs;
import org.bukkit.configuration.file.FileConfiguration;

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
    he.IL,
    custom_2,
    custom_3,
    custom_4,
    custom_5;

    private final String name, region, headId, itemModel;

    Language() {
        FileConfiguration langConfig = LangLibs.getPlugin().getLanguageConfig();
        this.name = langConfig.getString(this.name() + ".name");
        this.region = langConfig.getString(this.name() + ".region");
        this.headId = langConfig.getString(this.name() + ".headId");
        this.itemModel = langConfig.getString(this.name() + ".itemModel");
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



package li.cinnazeyy.langlibs.core;

import li.cinnazeyy.langlibs.core.config.ConfigUtil;

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
    custom_1,
    custom_2,
    custom_3,
    custom_4,
    custom_5;

    private final String name, region, headId, itemModel;

    Language() {
        this.name = ConfigUtil.getLanguageConfig().node(this.name(), "name").getString();
        this.region = ConfigUtil.getLanguageConfig().node(this.name(), "region").getString();
        this.headId = ConfigUtil.getLanguageConfig().node(this.name(), "headId").getString();
        this.itemModel = ConfigUtil.getLanguageConfig().node(this.name(), "modelId").getString();
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



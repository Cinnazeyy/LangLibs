package li.cinnazeyy.langlibs.core;
@SuppressWarnings("unused")
public enum Language {
    en_GB("English", "UK", "27577", "item_flag_uk"),
    de_DE("Deutsch", "Deutschland", "522", "item_flag_de"),
    fr_FR("Français", "France", "21905","item_flag_fr"),
    ko_KR("한국어", "한국","27583","item_flag_kr"),
    pt_PT("Português", "Portugal","22022","item_flag_pt"),
    ru_RU("Русский", "Россия","4406", "item_flag_ru"),
    zh_CN("简体中文", "中国大陆","23238","item_flag_cn"),
    zh_TW("繁體中文", "台灣","11627", "item_flag_tw");

    private final String name, region, headId, itemModel;

    Language(String name, String region, String headId, String itemModel) {
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



package li.cinnazeyy.langlibs.core;

public enum Language {
    en_GB("English", "UK", "27577", 2000),
    de_DE("Deutsch", "Deutschland", "522", 2001),
    fr_FR("Français", "France", "21905",2002),
    ko_KR("한국어", "한국","27583",2003),
    pt_PT("Português", "Portugal","22022",2004),
    ru_RU("Русский", "Россия","4406", 2005),
    zh_CN("简体中文", "中国大陆","23238",2006),
    zh_TW("繁體中文", "台灣","11627", 2007);

    private final String name, region, headId;
    private final int itemModel;

    Language(String name, String region, String headId, int itemModel) {
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

    public int getItemModel() {
        return itemModel;
    }
}



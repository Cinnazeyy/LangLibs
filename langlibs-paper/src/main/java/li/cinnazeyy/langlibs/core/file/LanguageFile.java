package li.cinnazeyy.langlibs.core.file;

import li.cinnazeyy.langlibs.core.Language;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class LanguageFile extends YamlFile {
    private final Language language;
    public String[] additionalLang;

    public LanguageFile(Language lang, double version) {
        super(Paths.get("lang", lang + ".yml"), version);
        this.language = lang;
    }

    public LanguageFile(Language lang, double version, String... additionalLang) {
        this(lang, version);
        this.additionalLang = additionalLang;
    }

    public String getTranslation(String key) {
        String translation = getString(key);
        return translation != null ? translation : "undefined";
    }

    public String getTranslation(String key, String... args) {
        String translation = getTranslation(key);
        for (int i = 0; i < args.length; i++) translation = translation.replace("{" + i + "}", args[i]);
        return translation;
    }

    public List<String> getTranslations(String key) {return getStringList(key);}

    public List<String> getTranslations(String key, String... args) {
        String[] translations = getTranslations(key).toArray(new String[0]);
        for (int i = 0; i < args.length; i++) {
            for (int k = 0; k < translations.length; k++) {
                translations[k] = translations[k].replace("{" + i + "}", args[i]);
            }
        }
        return Arrays.asList(translations);
    }

    public Language getLanguage() {return language;}

    @Override
    public InputStream getDefaultFileStream() {
        return YamlFileFactory.yamlPlugin.getResource("lang/" + getFile().getName());
    }

    @Override
    public int getMaxConfigWidth() {return 400;}
}
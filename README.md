<p align="center">
    <img src="LangLibsLogo.png" alt="logo" width="250" height="250">
</p>

<h1 align="center">Lang Libs</h1>
<p align="center">
A Minecraft Paper language library to implement and sync localisations between plugins
</p>

## About the Project
Lang Libs aims to make adding support for multiple languages to your plugins as easy and as seamless as possible, by having one central language library that all other plugins can depend on.

## Installation

### 1. Maven

<p>To use the latest version of Lang Libs, you need to add the following dependency to your pom.xml. You can find a list of all versions here:</p>
<a href="https://mvn.alps-bte.com/#browse/browse:lang-libs">https://mvn.alps-bte.com/#browse/browse:lang-libs</a>

```xml
<!-- Alps Repo -->
<repository>
    <id>alpsbte-repo</id>
    <url>https://mvn.alps-bte.com/repository/alps-bte/</url>
</repository>
```

```xml
<!-- LangLibs -->
<dependency>
    <groupId>li.cinnazeyy</groupId>
    <artifactId>LangLibs</artifactId>
    <version>{latest version}</version>
    <scope>provided</scope>
</dependency>
```

### 2. Register Plugin

In your main plugin class you need to first register the yaml file factory of the lang libs dependency in your onEnable
like so

```java
@Override
public void onEnable(){
        // Plugin startup logic
        YamlFileFactory.registerPlugin(this);
        }
```

Make sure this is the YamlFileFactory class from LangLibs!

### 3. Create LangUtil

Next create a new class called LangUtil which extends LanguageUtil like so, and create a new LanguageFile for each
language that you want to support for this plugin.

```java
public class LangUtil extends LanguageUtil {
    private static LangUtil langUtilInstance;

    public static void init() {
        if (langUtilInstance != null) return;
        LangLibAPI.register(ExamplePlugin.getPlugin(), new LanguageFile[]{
                new LanguageFile(Language.en_GB, 1.0),
                new LanguageFile(Language.de_DE, 1.0, "de_AT", "de_CH"),
        });
        langUtilInstance = new LangUtil();
    }

    public LangUtil() {
        super(ExamplePlugin.getPlugin());
    }

    public static LangUtil getInstance() {
        return langUtilInstance;
    }

    @Override
    public String get(CommandSender sender, String key) {
        return super.get(sender, key);
    }
}
```

you can also specify alternative language codes that should also point to the same languageFile like with "de_AT" and "
de_CH" in this example.

### 4. Create Language Files

Next you need to actually create those language files.

For each LanguageFile instance you created in the init method of the LangUtil class, you need to create a yml file with
the same name as the enum value under resources/lang. This path matters!

For example, I now have a de_DE.yml and as well as an en_GB.yml under resources/lang

**en_GB.yml:**

```
test-message: 'This is a test message'

config-version: 1.0
```

**de_DE.yml:**

```
test-message: 'Dies ist eine Test-Nachricht'

config-version: 1.0
```

Make sure to add the`config-version: 1.0` at the bottom.
This version needs to be updated whenever you update the structure of this file.
This is so that the plugin will automatically add missing translations when updating your plugin and you do not need to
manually update every language file everytime.

When updating this version do not forget to also update the version Langutil class!

### 5. Load Language Files

Next, you need to load the language files in your onEnable like so

```java
// Load language files
try{
        LangUtil.init();
        Bukkit.getConsoleSender().sendMessage(Component.text("Successfully loaded language files.",NamedTextColor.GREEN));
        }catch(Exception ex){
        Bukkit.getLogger().log(Level.SEVERE,ex.getMessage(),ex);
        getServer().getPluginManager().disablePlugin(this);
        return;
        }
```

And now you are completely set up!

## Getting Translations

You can get translations with the following:

```java
String testMessage=LangUtil.getInstance().get(player,"test-message");
```

## Language Change Event

If you want to execute some code whenever a player's language has changed, you can listen to the `LanguageChangeEvent`
like so:

```java
public class EventListener implements Listener {
    @EventHandler
    public void onLanguageChange(LanguageChangeEvent event) {
        event.getPlayer().sendMessage(text("Your language has changed to " + event.getLanguage().getName()));
    }
}
```
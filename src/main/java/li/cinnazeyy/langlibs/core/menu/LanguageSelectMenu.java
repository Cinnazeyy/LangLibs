package li.cinnazeyy.langlibs.core.menu;

import com.alpsbte.alpslib.utils.item.ItemBuilder;
import li.cinnazeyy.langlibs.LangLibs;
import li.cinnazeyy.langlibs.core.Language;
import li.cinnazeyy.langlibs.core.event.LanguageChangeEvent;
import li.cinnazeyy.langlibs.core.language.LangLibAPI;
import li.cinnazeyy.langlibs.util.HeadUtils;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class LanguageSelectMenu extends Gui {
    public LanguageSelectMenu(Player player) { super(player, "language-menu", "Select Language", 3); }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        for (int i = 0; i < Language.values().length; i++) {
            addItem(i,getLanguageIcon(Language.values()[i]));
        }
    }

    private Icon getLanguageIcon(Language lang) {
        boolean useHeads = LangLibs.getPlugin().getConfig().getBoolean("languageSelection.useHeads");
        Icon icon = useHeads ?
                new Icon(new ItemBuilder(HeadUtils.LANGUAGE_HEADS.get(lang).getAsItemStack()).build())
                : new Icon(new ItemBuilder(Material.PAPER).setItemModel(lang.ItemModel).build());
        icon.setName("§6§l" + lang.Name + " &8(&7" + lang.Region + "&8)"); //TODO: Use Components instead
        icon.onClick((event) -> clickLanguageIcon((Player) event.getWhoClicked(), lang));
        return icon;
    }

    private void clickLanguageIcon(Player player, Language lang) {
        LangLibAPI.setPlayerLang(player,lang.toString());
        LanguageChangeEvent event = new LanguageChangeEvent(player,lang);
        Bukkit.getPluginManager().callEvent(event);
    }
}

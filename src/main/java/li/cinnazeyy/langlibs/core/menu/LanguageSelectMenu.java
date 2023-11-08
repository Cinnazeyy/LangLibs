package li.cinnazeyy.langlibs.core.menu;

import com.alpsbte.alpslib.utils.head.AlpsHeadUtils;
import com.alpsbte.alpslib.utils.item.ItemBuilder;
import li.cinnazeyy.langlibs.LangLibs;
import li.cinnazeyy.langlibs.core.Language;
import li.cinnazeyy.langlibs.core.event.LanguageChangeEvent;
import li.cinnazeyy.langlibs.core.language.LangLibAPI;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.Objects;

public class LanguageSelectMenu extends Gui {
    public LanguageSelectMenu(Player player) { super(player, "language-menu", "Select Language", 3); }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        for (int i = 0; i < Language.values().length; i++) {
            addItem(i,getLanguageIcon(Language.values()[i]));
        }
        for (int i = 18; i < 27; i++) {
            addItem(i,new Icon(Material.GRAY_STAINED_GLASS_PANE));
        }
        addItem(22, new Icon(Material.BARRIER).setName("§l§cClose").onClick((e) -> Objects.requireNonNull(e.getClickedInventory()).close()));
        addItem(25, new Icon(Material.SLIME_BALL));
    }

    private Icon getLanguageIcon(Language lang) {
        boolean useHeads = LangLibs.getPlugin().getConfig().getBoolean("languageSelection.useHeads");
        Icon icon = useHeads ?
                new Icon(new ItemBuilder(AlpsHeadUtils.getCustomHead(lang.getHeadId())).build())
                : new Icon(new ItemBuilder(Material.PAPER).setItemModel(lang.getItemModel()).build());
        icon.setName("§6§l" + lang.getName() + " §r§8(§7" + lang.getRegion() + "§8)"); //TODO: Use Components instead
        icon.onClick((event) -> clickLanguageIcon(event, lang));
        return icon;
    }

    private void clickLanguageIcon(InventoryClickEvent event, Language lang) {
        LangLibAPI.setPlayerLang(player,lang.toString());
        LanguageChangeEvent langEvent = new LanguageChangeEvent(player,lang);
        Bukkit.getPluginManager().callEvent(langEvent);
        Objects.requireNonNull(event.getClickedInventory()).close();
    }
}

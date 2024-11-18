package li.cinnazeyy.langlibs.core.menu;

import com.alpsbte.alpslib.utils.head.AlpsHeadUtils;
import com.alpsbte.alpslib.utils.item.ItemBuilder;
import li.cinnazeyy.langlibs.LangLibs;
import li.cinnazeyy.langlibs.core.Language;
import li.cinnazeyy.langlibs.core.event.LanguageChangeEvent;
import li.cinnazeyy.langlibs.core.language.LangLibAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.mask.BinaryMask;
import org.ipvp.canvas.type.ChestMenu;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class LanguageSelectMenu {
    private final Menu menu;
    private final Player menuPlayer;

    public LanguageSelectMenu(Player player) {
        menuPlayer = player;
        menu = ChestMenu.builder(3).title(text("Select Language")).redraw(true).build();
    }

    public void open() {
        BinaryMask.builder(menu)
                .item(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(empty()).build())
                .pattern("000000000")
                .pattern("000000000")
                .pattern("111101111")
                .build()
                .apply(menu);

        for (int i = 0; i < Language.values().length; i++) {
            Language lang = Language.values()[i];
            menu.getSlot(i).setItem(getLanguageItem(lang));
            menu.getSlot(i).setClickHandler((p, info) -> clickLanguageItem(lang));
        }

        menu.getSlot(22).setItem(new ItemBuilder(Material.BARRIER)
                .setName(text("Close", RED).decoration(TextDecoration.BOLD, true))
                .build());
        menu.getSlot(22).setClickHandler((p, info) -> menu.close());
    }

    private ItemStack getLanguageItem(Language lang) {
        boolean useHeads = LangLibs.getPlugin().getConfig().getBoolean("languageSelection.useHeads");

        Component itemName = text(lang.getName(), GOLD)
                .decoration(TextDecoration.BOLD, true)
                .append(text(" (", DARK_GRAY))
                .append(text(lang.getRegion(), GRAY))
                .append(text(")", DARK_GRAY));

        return useHeads ?
                new ItemBuilder(AlpsHeadUtils.getCustomHead(lang.getHeadId())).setName(itemName).build()
                : new ItemBuilder(Material.PAPER).setItemModel(lang.getItemModel()).setName(itemName).build();
    }

    private void clickLanguageItem(Language lang) {
        LangLibAPI.setPlayerLang(menuPlayer, lang.toString());
        LanguageChangeEvent langEvent = new LanguageChangeEvent(menuPlayer, lang);
        Bukkit.getPluginManager().callEvent(langEvent);
        menu.close();
    }
}

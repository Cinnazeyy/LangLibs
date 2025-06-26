package li.cinnazeyy.langui.commands;

import li.cinnazeyy.langui.LanguageSelectMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMD_Language extends BukkitCommand {
    public CMD_Language(@NotNull String name) {
        super(name);
        this.description = "Opens the language picker";
        this.usageMessage = "/language";
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(sender instanceof Player p)) return true;
        new LanguageSelectMenu(p).open();
        return true;
    }
}
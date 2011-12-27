package net.krinsoft.teleportsuite.commands;

import com.pneumaticraft.commandhandler.Command;
import net.krinsoft.teleportsuite.Localization;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author krinsdeath
 */
public abstract class TeleportCommand extends Command {

    protected TeleportSuite plugin;

    public TeleportCommand(TeleportSuite plugin) {
        super(plugin);
        this.plugin = (TeleportSuite) plugin;
    }

    public Player[] check(CommandSender sender, String target) {
        Player[] checked = new Player[2];
        if (!(sender instanceof Player) || plugin.getServer().getPlayer(target) == null) {
            Localization.error("error.target", sender);
            return null;
        }
        checked[0] = (Player) sender;
        checked[1] = plugin.getServer().getPlayer(target);
        if (checked[0].equals(checked[1])) {
            Localization.error("error.target", sender);
            return null;
        }
        return checked;
    }

    public int verifyWallet(CommandSender sender) {
        if (plugin.economy) {
            return 0;
        } else {
            return -1;
        }
    }
}

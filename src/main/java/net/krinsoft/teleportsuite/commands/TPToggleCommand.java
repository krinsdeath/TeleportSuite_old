package net.krinsoft.teleportsuite.commands;

import java.util.List;
import net.krinsoft.teleportsuite.Localization;
import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

/**
 *
 * @author krinsdeath
 */
public class TPToggleCommand extends TeleportCommand {

    public TPToggleCommand(TeleportSuite plugin) {
        super(plugin);
        this.setName("TeleportSuite Toggle");
        this.setCommandUsage("/tptoggle");
        this.setArgRange(0, 0);
        this.addKey("teleport toggle");
        this.addKey("tps toggle");
        this.addKey("tptoggle");
        this.setPermission("teleport.toggle", "Allows this user to toggle their request accessibility.", PermissionDefault.TRUE);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player)) {
            return;
        }
        TeleportPlayer.toggle((Player) sender);
        String msg = (!TeleportPlayer.getPlayer((Player) sender).isToggled()) ? Localization.getString("teleport.toggle.allowed", "") : Localization.getString("teleport.toggle.denied", "");
        Localization.message("teleport.toggle.message", msg, (Player) sender);
    }
}

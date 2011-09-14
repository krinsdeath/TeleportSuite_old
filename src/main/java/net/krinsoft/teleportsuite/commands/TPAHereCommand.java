package net.krinsoft.teleportsuite.commands;

import java.util.List;
import net.krinsoft.teleportsuite.Localization;
import net.krinsoft.teleportsuite.TeleportSuite;
import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportPlayer.Priority;
import net.krinsoft.teleportsuite.TeleportPlayer.Teleport;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

/**
 *
 * @author krinsdeath
 */
public class TPAHereCommand extends TeleportCommand {

    public TPAHereCommand(TeleportSuite plugin) {
        super(plugin);
        this.setName("TeleportSuite TPAHere");
        this.setCommandUsage("/tpahere [player]");
        this.addCommandExample("/tpahere Player");
        this.setArgRange(1, 1);
        this.addKey("teleport here");
        this.addKey("tps here");
        this.addKey("tpahere");
        this.setPermission("teleport.tpahere", "Allows this user to request another user to teleport to him.", PermissionDefault.TRUE);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player[] checked = check(sender, args.get(0));
        if (checked == null || checked[0] == checked[1]) {
            Localization.error("error.target", (Player) sender);
            return;
        }
        TeleportPlayer.request(checked[0], checked[1], Teleport.HERE, Priority.REQUEST);
    }
}

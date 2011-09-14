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
public class TPHereCommand extends TeleportCommand {

    public TPHereCommand(TeleportSuite plugin) {
        super(plugin);
        this.setName("TeleportSuite TPHere");
        this.setCommandUsage("/tphere [player]");
        this.addCommandExample("/tphere Player");
        this.setArgRange(1, 1);
        this.addKey("teleport tphere");
        this.addKey("tps tphere");
        this.addKey("tphere");
        this.setPermission("teleport.tphere", "Allows this user to teleport users to him immediately.", PermissionDefault.OP);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        Player[] checked = check(sender, args.get(0));
        if (checked == null) {
            return;
        }
        TeleportPlayer.request(checked[0], checked[1], Teleport.HERE, Priority.COMMAND);
    }
}

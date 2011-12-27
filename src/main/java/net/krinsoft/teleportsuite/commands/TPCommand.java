package net.krinsoft.teleportsuite.commands;

import java.util.List;
import net.krinsoft.teleportsuite.TeleportSuite;
import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportPlayer.Priority;
import net.krinsoft.teleportsuite.TeleportPlayer.Teleport;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.entity.Player;

/**
 *
 * @author krinsdeath
 */
public class TPCommand extends TeleportCommand {

    public TPCommand(TeleportSuite plugin) {
        super(plugin);
        this.setName("TeleportSuite TP");
        this.setCommandUsage("/tp [player] [player]");
        this.addCommandExample("/tp Player -- Teleport to Player");
        this.addCommandExample("/tp Player1 Player2 -- Teleport Player1 to Player2");
        this.setArgRange(1, 2);
        this.addKey("teleport tp");
        this.addKey("tps tp");
        this.addKey("tp");
        this.setPermission("teleport.tp", "Allows this user to teleport immediately.", PermissionDefault.OP);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        Player[] checked = new Player[2];
        if (args.size() == 2) {
            checked = check(plugin.getServer().getPlayer(args.get(0)), args.get(1));
        } else {
            checked = check(sender, args.get(0));
        }
        if (checked == null) {
            return;
        }
        TeleportPlayer.request(checked[0], checked[1], Teleport.TO, Priority.COMMAND);
    }
}

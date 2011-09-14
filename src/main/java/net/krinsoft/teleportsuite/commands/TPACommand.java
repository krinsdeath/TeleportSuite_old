package net.krinsoft.teleportsuite.commands;

import java.util.List;
import net.krinsoft.teleportsuite.Localization;
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
public class TPACommand extends TeleportCommand {

    public TPACommand(TeleportSuite plugin) {
        super(plugin);
        this.setName("TeleportSuite TPA");
        this.setCommandUsage("/tpa [player]");
        this.addCommandExample("/tpa Player");
        this.setArgRange(1, 1);
        this.addKey("teleport to");
        this.addKey("tps to");
        this.addKey("tpa");
        this.setPermission("teleport.tpa", "Allows this user to request a teleport.", PermissionDefault.TRUE);
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
        TeleportPlayer.request(checked[0], checked[1], Teleport.TO, Priority.REQUEST);
    }
}

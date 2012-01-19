package net.krinsoft.teleportsuite.commands;

import java.util.List;
import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

/**
 *
 * @author krinsdeath
 */
public class TPAcceptCommand extends TeleportCommand {

    public TPAcceptCommand(TeleportSuite plugin) {
        super(plugin);
        this.setName("TeleportSuite Accept");
        this.setCommandUsage("/tpaccept [player]");
        this.addCommandExample("/tpaccept - Accepts the first request in your list.");
        this.addCommandExample("/tpaccept Player - Accepts the request from 'Player'");
        this.addCommandExample("/tpaccept --all - Accepts all open requests.");
        this.setArgRange(0, 1);
        this.addKey("teleport accept");
        this.addKey("tps accept");
        this.addKey("tpaccept");
        this.addKey("tpacc");
        this.setPermission("teleport.accept", "Allows this user to accept requests.", PermissionDefault.TRUE);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player)) {
            return;
        }
        if (args.isEmpty()) {
            TeleportPlayer.acceptFirst((Player) sender);
        } else {
            if (args.get(0).startsWith("--a")) {
                TeleportPlayer.acceptAll((Player) sender);
                return;
            }
            TeleportPlayer.accept((Player) sender, plugin.getServer().getPlayer(args.get(0)));
        }
    }
}

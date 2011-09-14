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
public class TPRejectCommand extends TeleportCommand {

	public TPRejectCommand(TeleportSuite plugin) {
		super(plugin);
		this.setName("TeleportSuite Reject");
		this.setCommandUsage("/tpreject [player]");
		this.addCommandExample("/tpreject - Rejects your first open request.");
		this.addCommandExample("/tpreject Player - Rejects a request from 'Player'");
		this.addCommandExample("/tpreject --all - Rejects all open requests.");
		this.setArgRange(0, 1);
		this.addKey("teleport reject");
		this.addKey("tps reject");
		this.addKey("tpreject");
		this.setPermission("teleport.reject", "Allows this user to reject requests.", PermissionDefault.TRUE);
	}

	@Override
	public void runCommand(CommandSender sender, List<String> args) {
		if (!(sender instanceof Player)) { return; }
		if (args.isEmpty()) {
			TeleportPlayer.rejectFirst((Player) sender);
			return;
		} else {
			if (args.get(0).startsWith("--a")) {
				TeleportPlayer.rejectAll((Player) sender);
				return;
			}
			TeleportPlayer.reject((Player) sender, plugin.getServer().getPlayer(args.get(0)));
			return;
		}
	}

}

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
public class TPBackCommand extends TeleportCommand {

	public TPBackCommand(TeleportSuite plugin) {
		super(plugin);
		this.setName("TeleportSuite Back");
		this.setCommandUsage("/tpback");
		this.setArgRange(0, 0);
		this.addKey("teleport back");
		this.addKey("tps back");
		this.addKey("tpback");
		this.addKey("back");
		this.setPermission("teleport.back", "Allows this user to teleport to their last known location.", PermissionDefault.TRUE);
	}

	@Override
	public void runCommand(CommandSender sender, List<String> args) {
		if (!(sender instanceof Player)) { return; }
		TeleportPlayer tp = TeleportPlayer.getPlayer((Player) sender);
		if (tp.getLastKnown() != null) {
			((Player)sender).teleport(tp.getLastKnown());
			return;
		} else {
			Localization.error("error.no_location", (Player) sender);
		}
	}

}

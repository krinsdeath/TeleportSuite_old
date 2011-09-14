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
public class TPCancelCommand extends TeleportCommand {

	public TPCancelCommand(TeleportSuite plugin) {
		super(plugin);
		this.setName("TeleportSuite Cancel");
		this.setCommandUsage("/tpcancel");
		this.setArgRange(0, 0);
		this.addKey("teleport cancel");
		this.addKey("tps cancel");
		this.addKey("tpcancel");
		this.setPermission("teleport.cancel", "Allows this user to cancel any currently active request.", PermissionDefault.TRUE);
	}

	@Override
	public void runCommand(CommandSender sender, List<String> args) {
		if (!(sender instanceof Player)) { return; }
		Player player = (Player) sender;
		if (TeleportPlayer.active.containsKey(player.getName())) {
			Localization.message("request.cancel", TeleportPlayer.active.get(player.getName()).getName(), player);
			Localization.message("request.cancelled", player.getName(), plugin.getServer().getPlayer(TeleportPlayer.active.get(player.getName()).getName()));
			TeleportPlayer.players.get(TeleportPlayer.active.get(player.getName()).getName()).finish(player.getName());
			TeleportPlayer.cancel(player.getName());
		}
	}

}

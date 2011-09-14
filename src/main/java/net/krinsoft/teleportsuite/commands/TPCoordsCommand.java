package net.krinsoft.teleportsuite.commands;

import java.util.List;
import net.krinsoft.teleportsuite.Localization;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

/**
 *
 * @author krinsdeath
 */
public class TPCoordsCommand extends TeleportCommand {

	public TPCoordsCommand(TeleportSuite plugin) {
		super(plugin);
		this.setName("TeleportSuite Coords");
		this.setCommandUsage("/tpcoords");
		this.setArgRange(0, 0);
		this.addKey("teleport coords");
		this.addKey("tps coords");
		this.addKey("tpcoords");
		this.setPermission("teleport.coords", "Allows this user to check their current location.", PermissionDefault.TRUE);
	}

	@Override
	public void runCommand(CommandSender sender, List<String> args) {
		if (!(sender instanceof Player)) { return; }
		Player player = (Player) sender;
		Location l = player.getLocation();
		Localization.message("message.location", l.getWorld().getName(), l.getX(), l.getY(), l.getZ(), player);
	}

}

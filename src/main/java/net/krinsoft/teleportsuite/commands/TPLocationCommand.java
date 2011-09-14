package net.krinsoft.teleportsuite.commands;

import java.util.List;
import net.krinsoft.teleportsuite.Localization;
import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

/**
 *
 * @author krinsdeath
 */
public class TPLocationCommand extends TeleportCommand {

	public TPLocationCommand(TeleportSuite plugin) {
		super(plugin);
		this.setName("TeleportSuite Location");
		this.setCommandUsage("/tploc [x y z] [world]");
		this.addCommandExample("/tploc 31 76 -91 world_nether");
		this.addCommandExample("/tploc -19 67 13");
		this.setArgRange(3, 4);
		this.addKey("teleport location");
		this.addKey("tps location");
		this.addKey("tps loc");
		this.addKey("tploc");
		this.setPermission("teleport.tploc", "Allows this user to teleport to a location.", PermissionDefault.OP);
	}

	@Override
	public void runCommand(CommandSender sender, List<String> args) {
		if (!(sender instanceof Player)) { return; }
		Player player = (Player) sender;
		World w = player.getWorld();
		if (args.size() > 3) {
			w = player.getServer().getWorld(args.get(3));
			if (w != null) {
				if (plugin.getPermissionHandler().canEnterWorld(sender, w.getName())) {
				} else {
					Localization.error("error.permission", player);
					return;
				}
			} else {
				Localization.error("error.invalid_world", player);
				return;
			}
		}
		double x = 0, y = 0, z = 0;
		float yaw = player.getLocation().getYaw(), pitch = player.getLocation().getPitch();
		try {
			x = Integer.parseInt(args.get(0));
			y = Integer.parseInt(args.get(1));
			z = Integer.parseInt(args.get(2));
		} catch (NumberFormatException e) {
			Localization.error("error.params", player);
			return;
		}
		Location loc = new Location(w, x, y, z, yaw, pitch);
		if (w.getBlockAt(loc).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
			TeleportPlayer.getPlayer(player).setLastKnown(player.getLocation());
			player.teleport(loc);
		} else {
			Localization.error("error.destination", player);
		}
	}

}

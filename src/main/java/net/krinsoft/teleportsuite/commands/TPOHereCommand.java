package net.krinsoft.teleportsuite.commands;


import java.util.List;
import net.krinsoft.teleportsuite.Localization;
import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportPlayer.Priority;
import net.krinsoft.teleportsuite.TeleportPlayer.Teleport;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
/**
 *
 * @author krinsdeath
 */
public class TPOHereCommand extends TeleportCommand {

	public TPOHereCommand(TeleportSuite plugin) {
		super(plugin);
		this.setName("TeleportSuite TPOHere");
		this.setCommandUsage("/tpohere [player]");
		this.addCommandExample("/tpohere Player");
		this.setArgRange(1, 1);
		this.addKey("teleport forcehere");
		this.addKey("tps forcehere");
		this.addKey("tpohere");
		this.setPermission("teleport.tpohere", "Allows this user to force other users to his location.", PermissionDefault.FALSE);
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
		TeleportPlayer.request(checked[0], checked[1], Teleport.HERE, Priority.OVERRIDE);
	}
}

package net.krinsoft.teleportsuite.commands;

import com.pneumaticraft.commandhandler.Command;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
/**
 *
 * @author krinsdeath
 */
public abstract class TeleportCommand extends Command {

	protected TeleportSuite plugin;

	public TeleportCommand(TeleportSuite plugin) {
		super(plugin);
		this.plugin = (TeleportSuite) plugin;
	}

	public Player[] check(CommandSender sender, String target) {
		Player[] checked = new Player[2];
		if (!(sender instanceof Player) || plugin.getServer().getPlayer(target) == null) {
			return null;
		}
		checked[0] = (Player) sender;
		checked[1] = plugin.getServer().getPlayer(target);
		return checked;
	}

}

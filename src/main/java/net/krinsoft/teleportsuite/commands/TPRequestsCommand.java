package net.krinsoft.teleportsuite.commands;

import java.util.List;
import net.krinsoft.teleportsuite.Localization;
import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author krinsdeath
 */
public class TPRequestsCommand extends TeleportCommand {

	public TPRequestsCommand(TeleportSuite plugin) {
		super(plugin);
		
	}

	@Override
	public void runCommand(CommandSender sender, List<String> args) {
		if (!(sender instanceof Player)) { return; }
		Player player = (Player) sender;
		TeleportPlayer tp = TeleportPlayer.getPlayer(player);
		Localization.message("request.open.header", player.getName(), player);
		if (tp.requests().isEmpty()) {
			Localization.message("request.open.none", "", player);
		} else {
			for (String line : tp.requests()) {
				Localization.message("request.open.entry", line, player);
			}
		}
		if (TeleportPlayer.active.containsKey(player.getName())) {
			Localization.message("request.open.self", TeleportPlayer.active.get(player.getName()).getName(), player);
		}
	}



}

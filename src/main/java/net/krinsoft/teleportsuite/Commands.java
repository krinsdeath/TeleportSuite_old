package net.krinsoft.teleportsuite;

import net.krinsoft.teleportsuite.TeleportPlayer.Priority;
import net.krinsoft.teleportsuite.TeleportPlayer.Teleport;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author krinsdeath
 * @version ${project.version}
 */

class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) { return false; }

		boolean check = false;
		Player player = (Player) sender, target = null;
		Server server = player.getServer();
		
		if (TeleportPlayer.getPlayer(player) == null) {
			TeleportPlayer.addPlayer(player);
		}

		if (args.length > 0 && args.length != 3) {
			target = server.getPlayer(args[0]);
			if (target != null && target != player) {
				if (TeleportPlayer.getPlayer(target) == null) {
					TeleportPlayer.addPlayer(target);
				}
				check = true;
			} else {
				Localization.error("error.target", player);
				return true;
			}
		}
		// handler for '/tp'
		if (cmd.getName().equalsIgnoreCase("tp")) {
			if (sender.hasPermission("commandsuite.teleport.tp")) {
				if (args.length < 1) {
					Localization.error("error.arguments", player);
					return false;
				} else {
					if (check) {
						TeleportPlayer.request(player, target, Teleport.TO, Priority.COMMAND);
						return true;
					}
				}
			} else {
				Localization.error("error.permission", player);
			}
		}

		// handler for '/tpa'
		if (cmd.getName().equalsIgnoreCase("tpa")) {
			if (sender.hasPermission("commandsuite.teleport.tpa")) {
				if (args.length < 1) {
					Localization.error("error.arguments", player);
					return false;
				} else {
					if (check) {
						TeleportPlayer.request(player, target, Teleport.TO, Priority.REQUEST);
						return true;
					}
				}
			} else {
				Localization.error("error.permission", player);
			}
		}

		// handler for '/tpo'
		if (cmd.getName().equalsIgnoreCase("tpo")) {
			if (sender.hasPermission("commandsuite.teleport.tpo")) {
				if (args.length < 1) {
					Localization.error("error.arguments", player);
					return false;
				} else {
					if (check) {
						TeleportPlayer.request(player, target, Teleport.TO, Priority.OVERRIDE);
						return true;
					}
				}
			} else {
				Localization.error("error.permission", player);
			}
		}

		// handler for '/tphere'
		if (cmd.getName().equalsIgnoreCase("tphere")) {
			if (sender.hasPermission("commandsuite.teleport.tphere")) {
				if (args.length < 1) {
					Localization.error("error.arguments", player);
					return false;
				} else {
					if (check) {
						TeleportPlayer.request(player, target, Teleport.HERE, Priority.COMMAND);
						return true;
					}
				}
			} else {
				Localization.error("error.permission", player);
			}
		}

		// handler for '/tpahere'
		if (cmd.getName().equalsIgnoreCase("tpahere")) {
			if (sender.hasPermission("commandsuite.teleport.tpahere")) {
				if (args.length < 1) {
					Localization.error("error.arguments", player);
					return false;
				} else {
					if (check) {
						TeleportPlayer.request(player, target, Teleport.HERE, Priority.REQUEST);
						return true;
					}
				}
			} else {
				Localization.error("error.permission", player);
			}
		}

		// handler for '/tpohere'
		if (cmd.getName().equalsIgnoreCase("tpohere")) {
			if (sender.hasPermission("commandsuite.teleport.tpohere")) {
				if (args.length < 1) {
					player.sendMessage("Not enough arguments.");
					return false;
				} else {
					if (check) {
						TeleportPlayer.request(player, target, Teleport.HERE, Priority.OVERRIDE);
						return true;
					}
				}
			} else {
				Localization.error("error.permission", player);
			}
		}

		// handler for '/tpaccept'
		if (cmd.getName().equalsIgnoreCase("tpaccept")) {
			if (sender.hasPermission("commandsuite.teleport.tpaccept")) {
				if (check) {
					TeleportPlayer.accept(player, target);
					return true;
				} else {
					if (args.length == 0) {
						TeleportPlayer.acceptFirst(player);
					}
				}
			} else {
				Localization.error("error.permission", player);
			}
		}

		// handler for '/tpreject'
		if (cmd.getName().equalsIgnoreCase("tpreject")) {
			if (sender.hasPermission("commandsuite.teleport.tpreject")) {
				if (check) {
					TeleportPlayer.reject(player, target);
					return true;
				} else {
					if (args.length == 0) {
						TeleportPlayer.rejectFirst(player);
					}
				}
			} else {
				Localization.error("error.permission", player);
			}
		}

		// handler for '/back'
		if (cmd.getName().equalsIgnoreCase("back")) {
			if (sender.hasPermission("commandsuite.teleport.back")) {
				TeleportPlayer tp = TeleportPlayer.getPlayer(player);
				if (tp.getLastKnown() != null) {
					player.teleport(tp.getLastKnown());
					return true;
				} else {
					Localization.error("error.no_location", player);
				}
			} else {
				Localization.error("error.permission", player);
			}
		}

		// handler for '/tptoggle'
		if (cmd.getName().equalsIgnoreCase("tptoggle")) {
			if (sender.hasPermission("commandsuite.teleport.tptoggle")) {
				TeleportPlayer.toggle(player);
				String msg = (!TeleportPlayer.getPlayer(player).isToggled()) ? Localization.getString("teleport.toggle.allowed", "") : Localization.getString("teleport.toggle.denied", "");
				player.sendMessage(Localization.getString("teleport.toggle.message", msg));
				return true;
			} else {
				Localization.error("error.permission", player);
			}
		}

		// handler for '/requests'
		if (cmd.getName().equalsIgnoreCase("requests")) {
			if (sender.hasPermission("commandsuite.teleport.requests")) {
				TeleportPlayer tp = TeleportPlayer.getPlayer(player);
				if (tp.requests().isEmpty()) {
					player.sendMessage(Localization.getString("request.open.none", ""));
				} else {
					player.sendMessage(Localization.getString("request.open.header", player.getName()));
					for (String line : tp.requests()) {
						player.sendMessage(Localization.getString("request.open.entry", line));
					}
					if (TeleportPlayer.active.containsKey(player.getName())) {
						player.sendMessage(Localization.getString("request.open.self", TeleportPlayer.active.get(player.getName()).getName()));
					}
				}
			} else {
				Localization.error("error.permission", player);
			}
		}

		// handler for '/cancel'
		if (cmd.getName().equalsIgnoreCase("cancel")) {
			if (sender.hasPermission("commandsuite.teleport.tpcancel")) {
				if (TeleportPlayer.active.containsKey(player.getName())) {
					player.sendMessage(Localization.getString("request.cancel", TeleportPlayer.active.get(player.getName()).getName()));
					server.getPlayer(TeleportPlayer.active.get(player.getName()).getName()).sendMessage(Localization.getString("request.cancelled", player.getName()));
					TeleportPlayer.players.get(TeleportPlayer.active.get(player.getName()).getName()).finish(player);
					TeleportPlayer.active.remove(player.getName());
				}
			} else {
				Localization.error("error.permission", player);
			}
		}

		// handler for '/tploc'
		if (cmd.getName().equalsIgnoreCase("tploc") && args.length == 3) {
			if (sender.hasPermission("commandsuite.teleport.tploc")) {
				double x = 0, y = 0, z = 0;
				float yaw = player.getLocation().getYaw(), pitch = player.getLocation().getPitch();
				try {
					x = Integer.parseInt(args[0]);
					y = Integer.parseInt(args[1]);
					z = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					Localization.error("error.params", player);
					return false;
				}
				Location loc = new Location(player.getWorld(), x, y, z, yaw, pitch);
				if (player.getWorld().getBlockAt(loc).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
					TeleportPlayer.getPlayer(player).setLastKnown(player.getLocation());
					player.teleport(loc);
				} else {
					Localization.error("error.destination", player);
				}
			} else {
				Localization.error("error.permission", player);
			}
		}

		return true;
	}
}

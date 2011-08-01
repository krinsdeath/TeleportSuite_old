package net.krinsoft.teleportsuite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

/**
 *
 * @author krinsdeath
 */

public class TeleportPlayer implements Serializable {
	public enum Teleport {
		TO,
		HERE;
	}

	public enum Priority {
		REQUEST,
		COMMAND,
		OVERRIDE;
	}

	private final static long serialVersionUID = 991L;

	protected static HashMap<String, TeleportPlayer> players = new HashMap<String, TeleportPlayer>();
	protected static HashMap<String, Request> active = new HashMap<String, Request>();
	protected static List<String> requesting = new ArrayList<String>();
	protected static Server server;

	public static void init(TeleportSuite inst) {
		server = inst.getServer();

	}

	public static void addPlayer(Player player) {
		players.put(player.getName(), new TeleportPlayer(player));
	}

	public static void removePlayer(Player player) {
		players.remove(player.getName());
	}

	public static TeleportPlayer getPlayer(Player player) {
		return players.get(player.getName());
	}

	public static void toggle(Player player) {
		players.get(player.getName()).toggle();
	}

	public static void accept(Player from, Player to) {
		// check if the accepting player's list has this player
		TeleportPlayer accept = getPlayer(from);
		if (accept.hasRequest(to)) {
			// there's a request! let's check how it should be handled
			if (active.containsKey(to.getName())) {
				Request t = active.get(to.getName());
				if (t.getType() == Teleport.HERE) {
					accept.setLastKnown(from.getLocation());
					from.sendMessage(Localization.getString("teleport.message", to.getName()));
					to.sendMessage(Localization.getString("teleport.notice", from.getName()));
					from.teleport(to.getLocation());
					accept.finish(to);
					active.remove(to.getName());
				} else if (t.getType() == Teleport.TO) {
					getPlayer(to).setLastKnown(to.getLocation());
					to.sendMessage(Localization.getString("teleport.message", from.getName()));
					from.sendMessage(Localization.getString("teleport.notice", to.getName()));
					to.teleport(from.getLocation());
					getPlayer(to).finish(from);
					active.remove(to.getName());
				}
				requesting.remove(to.getName());
			} else {
				// weird, there was a request without an active request
				// let's remove it and cancel
				requesting.remove(to.getName());
				accept.finish(to);
			}
		} else {
			// tell the accepter that he doesn't have a request from that player
			from.sendMessage(Localization.getString("request.none", to.getName()));
		}
		players.put(from.getName(), accept);
	}

	public static void acceptFirst(Player player) {
		if (TeleportPlayer.getPlayer(player).requests().isEmpty()) {
			return;
		} else {
			accept(player, server.getPlayer(TeleportPlayer.getPlayer(player).requests().get(0)));
		}
	}

	public static void reject(Player from, Player to) {
		if (getPlayer(from).requests().contains(to.getName())) {
			active.remove(to.getName());
			from.sendMessage(Localization.getString("request.deny", to.getName()));
			to.sendMessage(Localization.getString("request.denied", from.getName()));
			getPlayer(from).finish(to);
			requesting.remove(to.getName());
			return;
		} else {
			to.sendMessage(Localization.getString("request.none", from.getName()));
		}
	}

	public static void rejectFirst(Player player) {
		if (TeleportPlayer.getPlayer(player).requests().isEmpty()) {
			return;
		} else {
			reject(player, server.getPlayer(TeleportPlayer.getPlayer(player).requests().get(0)));
		}
	}

	public static void request(Player from, Player to, Teleport type, Priority priority) {
		if (requesting.contains(from.getName())) {
			from.sendMessage(Localization.getString("request.only_one", ""));
		} else {
			if (getPlayer(to).isToggled()) {
				if (priority == Priority.OVERRIDE) {
					active.put(from.getName(), new Request(to.getName(), type));
					getPlayer(to).request(from);
					accept(to, from);
				} else {
					from.sendMessage(Localization.getString("request.ignored", to.getName()));
				}
			} else {
				if (priority == Priority.COMMAND) {
					active.put(from.getName(), new Request(to.getName(), type));
					getPlayer(to).request(from);
					accept(to, from);
				} else {
					active.put(from.getName(), new Request(to.getName(), type));
					getPlayer(to).request(from);
					from.sendMessage(Localization.getString("teleport.request.to", to.getName()));
					to.sendMessage(Localization.getString("teleport.request.from", from.getName()));
					requesting.add(from.getName());
				}
			}
		}
	}

	public static class Request implements Serializable {
		private final static long serialVersionUID = 33144L;

		private String name;
		private Teleport type;

		public Request(String name, Teleport type) {
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return this.name;
		}

		public Teleport getType() {
			return this.type;
		}
	}

	private String name;
	private String world;
	private boolean toggle;

	private List<String> requests = new ArrayList<String>();

	// last known location, used for '/back'
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;

	public TeleportPlayer(Player player) {
		this.name = player.getName();
		this.world = player.getWorld().getName();
		this.toggle = false;
		setLastKnown(player.getLocation());
	}

	private void setLastKnown(Location location) {
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
		this.world = location.getWorld().getName();
	}

	public Location getLastKnown() {
		return new Location(server.getWorld(world), x, y, z, yaw, pitch);
	}

	public boolean isToggled() {
		return this.toggle;
	}

	public void toggle() {
		if (toggle) {
			this.toggle = false;
			requests.clear();
			return;
		} else {
			this.toggle = true;
			return;
		}
	}

	public List<String> requests() {
		return this.requests;
	}

	public boolean hasRequest(Player p) {
		return this.requests.contains(p.getName());
	}

	public void request(Player p) {
		this.requests.add(p.getName());
	}

	public void finish(Player to) {
		this.requests.remove(to.getName());
	}
}

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

	/**
	 * Adds a player to the Map, so that he can issue requests
	 * @param player
	 * the handle of the player
	 */
	public static void addPlayer(Player player) {
		players.put(player.getName(), new TeleportPlayer(player));
	}

	/**
	 * Removes a player from the Map, mostly for cleanup/optimization
	 * @param player
	 * the handle of the player
	 */
	public static void removePlayer(Player player) {
		players.remove(player.getName());
	}

	/**
	 * Fetches the specified player from the map
	 * @param player
	 * the handle of the player
	 * @return
	 * A TeleportPlayer object related to the handle provided
	 */
	public static TeleportPlayer getPlayer(Player player) {
		return players.get(player.getName());
	}

	/**
	 * Toggles the requests feature for the specified player
	 * @param player
	 * the handle of the player
	 */
	public static void toggle(Player player) {
		players.get(player.getName()).toggle();
	}

	/**
	 * Accepts a teleport request
	 * @param from
	 * the player accepting the request
	 * @param to
	 * the player who initiated the request
	 */
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

	/**
	 * Same as accept, just accepts the first request in the list
	 * @see #accept(org.bukkit.entity.Player, org.bukkit.entity.Player)
	 * @param player
	 * the handle of the player who is accepting the request
	 */
	public static void acceptFirst(Player player) {
		if (TeleportPlayer.getPlayer(player).requests().isEmpty()) {
			return;
		} else {
			accept(player, server.getPlayer(TeleportPlayer.getPlayer(player).requests().get(0)));
		}
	}

	/**
	 * Rejects a teleport quest
	 * @param from
	 * the handle of the player who received the request
	 * @param to
	 * the handle of the player who initiated the request
	 */
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

	/**
	 * Same as reject
	 * @see #reject(org.bukkit.entity.Player, org.bukkit.entity.Player)
	 * @param player
	 * the handle of the player who is rejecting the request
	 */
	public static void rejectFirst(Player player) {
		if (TeleportPlayer.getPlayer(player).requests().isEmpty()) {
			return;
		} else {
			reject(player, server.getPlayer(TeleportPlayer.getPlayer(player).requests().get(0)));
		}
	}

	/**
	 * Initiates a request from the specified player to the the specified player
	 * @param from
	 * The handle of the player initiating the request
	 * @param to
	 * The handle of the player receiving the request
	 * @param type
	 * The request type
	 * @see Teleport
	 * @param priority
	 * The priority of the request
	 * @see Priority
	 */
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

	/**
	 * Stores a request relating to the target
	 */
	public static class Request implements Serializable {
		/**
		 * Serializable Version ID of this class
		 */
		private final static long serialVersionUID = 33144L;

		/**
		 * The name of the player who initiated the request
		 */
		private String name;
		/**
		 * The type of Teleport
		 * @see Teleport
		 */
		private Teleport type;

		/**
		 * Constructs a Request object
		 * @param name
		 * The name of the player who initiated the request
		 * @param type
		 * The type of request
		 * @see Teleport
		 */
		public Request(String name, Teleport type) {
			this.name = name;
			this.type = type;
		}

		/**
		 * Gets the name of the player who initiated this request
		 * @return
		 * the name of the player
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * Gets the type of request
		 * @return
		 * the type
		 * @see Teleport
		 */
		public Teleport getType() {
			return this.type;
		}
	}

	/**
	 * The name of the player to which this TeleportPlayer object is mapped
	 * @deprecated
	 */
	private String name;
	/**
	 * A string containing the world name of the player's last known location
	 */
	private String world;
	/**
	 * The player's toggle status;
	 * true = player is ignoring requests
	 * false = player is accepting requests
	 */
	private boolean toggle;

	/**
	 * A list of requests (by player name) this player has queued
	 */
	private List<String> requests = new ArrayList<String>();

	// last known location, used for '/back'
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;

	/**
	 * Constructs a TeleportPlayer object mapped to the specified player
	 * @param player
	 * the handle of the player to which this object belongs
	 */
	public TeleportPlayer(Player player) {
		this.name = player.getName();
		this.world = player.getWorld().getName();
		this.toggle = false;
		setLastKnown(player.getLocation());
	}

	/**
	 * Sets this player's last known location, used for '/back'
	 * @param location
	 * the location to set
	 */
	public final void setLastKnown(Location location) {
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
		this.world = location.getWorld().getName();
	}

	/**
	 * Gets this player's last known location, used for '/back'
	 * @return
	 * the player's location prior to their last teleport, or their location when they logged in
	 * (whichever is newer)
	 */
	public Location getLastKnown() {
		return new Location(server.getWorld(world), x, y, z, yaw, pitch);
	}

	/**
	 * Returns the player's toggle status
	 * @return true
	 * the player is rejecting requests
	 * @return false
	 * the player is accepting requests
	 */
	public boolean isToggled() {
		return this.toggle;
	}

	/**
	 * Toggles this player's accept/reject status
	 */
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

	/**
	 * Fetches a list of player names that are queued for requests
	 * @return
	 * the player's names
	 */
	public List<String> requests() {
		return this.requests;
	}

	/**
	 * Checks whether this player has a queued request from the specified player
	 * @param p
	 * The player handle to check
	 * @return true
	 * the player has a request from the provided player
	 * @return false
	 * the player has no such request
	 */
	public boolean hasRequest(Player p) {
		return this.requests.contains(p.getName());
	}

	/**
	 * Adds a request from the specified player to this player's request queue
	 * @param p
	 * The player to add to the request queue
	 */
	public void request(Player p) {
		this.requests.add(p.getName());
	}

	/**
	 * Removes a player from this player's request list
	 * @param to
	 * the player handle to remove from the list
	 */
	public void finish(Player to) {
		this.requests.remove(to.getName());
	}
}

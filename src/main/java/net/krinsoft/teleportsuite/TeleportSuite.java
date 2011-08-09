package net.krinsoft.teleportsuite;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

/**
 *
 * @author krinsdeath
 * @version 1.0.2
 */

public class TeleportSuite extends JavaPlugin {
	protected static PluginDescriptionFile pdf;
	protected static PluginManager pm;
	protected static Configuration config;

	private final Commands cmds = new Commands();
	private final Players pListener = new Players();

	@Override
	public void onEnable() {
		pdf = getDescription();
		config = getConfiguration();
		pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, pListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, pListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_KICK, pListener, Event.Priority.Normal, this);
		TeleportPlayer.init(this);
		setup();
		Localization.setConfig(config);
		System.out.println(pdf.getFullName() + " (by " + pdf.getAuthors().toString().replaceAll("([\\[\\]])", "") + ") is enabled.");
	}

	@Override
	public void onDisable() {
		TeleportPlayer.clean();
		System.out.println(pdf.getFullName() + " (by " + pdf.getAuthors().toString().replaceAll("([\\[\\]])", "") + ") is disabled.");
		pm = null;
		pdf = null;
		config = null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return cmds.onCommand(sender, cmd, label, args);
	}

	private void setup() {
		if (!versionMatch() || config.getBoolean("plugin.rebuild", false)) {
			if (config.getProperty("plugin.rebuild") == null) {
				config.setProperty("plugin.rebuild", true);
			}
			if (config.getBoolean("plugin.rebuild", false)) {
				System.out.println(pdf.getFullName() + " detected first run.");
				System.out.println("Building configuration file...");
				config.getString("teleport.message", "Teleporting to &a<player>&f...");
				config.getString("teleport.notice", "&a<player>&f is teleporting to you.");
				config.getString("teleport.request.from", "Teleport request from &a<player>&f. (Accept with &a/tpaccept <player>&f)");
				config.getString("teleport.request.to", "Awaiting response from &a<player>&f...");
				config.getString("teleport.toggle.allowed", "allowed");
				config.getString("teleport.toggle.denied", "ignored");
				config.getString("teleport.toggle.message", "Teleport requests will be <flag>.");
				config.getString("request.none", "You have no request from &a<player>&f!");
				config.getString("request.deny", "You denied &a<player>&f's teleport request.");
				config.getString("request.denied", "&a<player>&f denied your teleport request.");
				config.getString("request.only_one", "You can only request one teleport at a time.");
				config.getString("request.ignored", "&a<player>&f is not allowing teleport requests.");
				config.getString("request.open.header", "Open requests...");
				config.getString("request.open.self", "Requesting teleport to: &a<player>&f");
				config.getString("request.open.entry", "&a<player>&f");
				config.getString("request.open.none", "You have no requests.");
				config.getString("error.arguments", "&CNot enough arguments.");
				config.getString("error.target", "&CInvalid target.");
				config.getString("error.permission", "&CYou do not have permission for that.");
				config.getString("error.no_location", "&CYou haven't teleported yet.");
				config.getString("error.params", "&CInvalid parameters.");
				config.getString("error.destination", "&CInvalid destination.");
				config.setProperty("plugin.rebuild", false);
				config.getString("plugin.version", "1.0.0");
				config.save();
				System.out.println("... done.");
			}
			if (!versionMatch()) {
				try {
					int cf = Integer.parseInt(config.getString("plugin.version").split("\\.")[2]);
					int cr = Integer.parseInt(config.getString("plugin.version").split("\\.")[1]);
					int cv = Integer.parseInt(config.getString("plugin.version").split("\\.")[0]);
					update(cf, cr, cv);
				} catch (NumberFormatException e) {
					System.out.println("You changed the version number. Some features may not be added.");
				}
			}
			config.setProperty("plugin.version", pdf.getVersion());
			config.save();
		}
		config.load();
	}

	private boolean versionMatch() {
		if (pdf.getVersion().equals(config.getString("plugin.version"))) {
			return true;
		} else {
			return false;
		}
	}

	private void update(int f, int r, int v) {
		System.out.println("Updating " + pdf.getFullName() + " to latest version.");
		int fix = Integer.parseInt(pdf.getVersion().split("\\.")[2]);
		int rev = Integer.parseInt(pdf.getVersion().split("\\.")[1]);
		int ver = Integer.parseInt(pdf.getVersion().split("\\.")[0]);
		if (ver == 1 && v <= ver) {
			if (rev == 0 && r <= rev) {
				if (fix >= 1 && f < fix) {
					config.getString("request.cancel", "You cancelled your teleport to &a<player>&f.");
					config.getString("request.cancelled", "&a<player>&f cancelled their teleport request.");
				}
				if (fix >= 3 && f < fix) {
					config.getString("error.invalid_world", "&CInvalid world");
					config.getString("message.location", "You are currently at (&b<x>&f, &b<y>&f, &b<z>&f) in &a<world>&f.");
				}
				if (fix >= 4 && f < fix) {
					config.setProperty("teleport.toggle.default", false);
					System.out.println("Config updated to " + pdf.getVersion() + ". Finalizing...");
				}
			}
		}
	}

}
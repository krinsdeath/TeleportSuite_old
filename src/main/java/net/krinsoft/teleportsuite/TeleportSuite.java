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
 * @version 1.0.1
 */

public class TeleportSuite extends JavaPlugin {
	protected static PluginDescriptionFile pdf;
	protected static PluginManager pm;
	protected static Configuration config;

	private final Commands cmds = new Commands(this);
	private final Players pListener = new Players(this);

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
		System.out.println(pdf.getFullName() + " (by " + pdf.getAuthors().toString() + ") is enabled.");
	}

	@Override
	public void onDisable() {
		System.out.println(pdf.getFullName() + " (by " + pdf.getAuthors().toString() + ") is disabled.");
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
				System.out.println("Building " + pdf.getFullName() + " configuration file...");
				config.setProperty("teleport.message", "Teleporting to &a<player>&f...");
				config.setProperty("teleport.notice", "&a<player>&f is teleporting to you.");
				config.setProperty("teleport.request.from", "Teleport request from &a<player>&f. (Accept with &a/tpaccept <player>&f)");
				config.setProperty("teleport.request.to", "Awaiting response from &a<player>&f...");
				config.setProperty("teleport.toggle.allowed", "allowed");
				config.setProperty("teleport.toggle.denied", "ignored");
				config.setProperty("teleport.toggle.message", "Teleport requests will be <flag>.");
				config.setProperty("request.none", "You have no request from &a<player>&f!");
				config.setProperty("request.deny", "You denied &a<player>&f's teleport request.");
				config.setProperty("request.denied", "&a<player>&f denied your teleport request.");
				config.setProperty("request.only_one", "You can only request one teleport at a time.");
				config.setProperty("request.ignored", "&a<player>&f is not allowing teleport requests.");
				config.setProperty("request.open.header", "Open requests...");
				config.setProperty("request.open.self", "Requesting teleport to: &a<player>&f");
				config.setProperty("request.open.entry", "&a<player>&f");
				config.setProperty("request.open.none", "You have no requests.");
				config.setProperty("request.cancel", "You cancelled your teleport to &a<player>&f.");
				config.setProperty("request.cancelled", "&a<player>&f cancelled their teleport request.");
				config.setProperty("error.arguments", "&CNot enough arguments.");
				config.setProperty("error.target", "&CInvalid target.");
				config.setProperty("error.permission", "&CYou do not have permission for that.");
				config.setProperty("error.no_location", "&CYou haven't teleported yet.");
				config.setProperty("error.params", "&CInvalid parameters.");
				config.setProperty("error.destination", "&CInvalid destination.");
				config.setProperty("plugin.rebuild", false);
				config.setProperty("plugin.version", pdf.getVersion());
				config.save();
				System.out.println("... done.");
			}
			if (!config.getString("plugin.version").equalsIgnoreCase(pdf.getVersion())) {
				try {
					int cf = Integer.parseInt(config.getString("plugin.version").split("\\.")[2]);
					int cr = Integer.parseInt(config.getString("plugin.version").split("\\.")[1]);
					int cv = Integer.parseInt(config.getString("plugin.version").split("\\.")[0]);
					update(cf, cr, cv);
				} catch (NumberFormatException e) {
					System.out.println("You changed the version number..");
					System.out.println("Some features may not be added.");
				}
			}
			config.setProperty("plugin.version", pdf.getVersion());
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
		int fix = Integer.parseInt(pdf.getVersion().split("\\.")[2]);
		int rev = Integer.parseInt(pdf.getVersion().split("\\.")[1]);
		int ver = Integer.parseInt(pdf.getVersion().split("\\.")[0]);
		if (ver == 1 && v <= ver) {
			if (rev == 0 && r <= rev) {
				if (fix >= 1 && f < fix) {
					config.setProperty("request.cancel", "You cancelled your teleport to &a<player>&f.");
					config.setProperty("request.cancelled", "&a<player>&f cancelled their teleport request.");
				}
				if (fix >= 2 && f < fix) {
					
				}
			}
		}

	}

}

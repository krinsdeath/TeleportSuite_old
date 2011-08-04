package net.krinsoft.teleportsuite;

import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

/**
 *
 * @author krinsdeath
 */

class Localization {
	private static Configuration config;

	public static void setConfig(Configuration conf) {
		config = conf;
	}

	public static String getString(String path, String rep) {
		String msg = config.getString(path, "not null");
		msg = msg.replaceAll("(<flag>|<player>)", rep);
		msg = msg.replaceAll("&([a-fA-F0-9])", "\u00A7$1");
		return msg;
	}

	public static void error(String path, Player player) {
		String msg = config.getString(path, "not null");
		msg = msg.replaceAll("<player>", player.getName());
		msg = msg.replaceAll("&([a-fA-F0-9])", "\u00A7$1");
		player.sendMessage(msg);
	}

}

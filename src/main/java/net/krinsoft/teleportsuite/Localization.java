package net.krinsoft.teleportsuite;

import java.util.regex.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

/**
 *
 * @author krinsdeath
 */

class Localization {
	private static Configuration config;
	private final static Pattern COLOR = Pattern.compile("&([a-fA-f0-9])");

	public static void setConfig(Configuration conf) {
		config = conf;
	}

	public static String getString(String path, String rep) {
		String msg = config.getString(path, "not null");
		msg = msg.replaceAll("(<flag>|<player>)", rep);
		msg = color(msg);
		return msg;
	}

	public static void error(String path, Player player) {
		String msg = config.getString(path, "not null");
		msg = msg.replaceAll("<player>", player.getName());
		msg = color(msg);
		player.sendMessage(msg);
	}

	private static String color(String msg) {
		return COLOR.matcher(msg).replaceAll("\u00A7$1");
	}

}

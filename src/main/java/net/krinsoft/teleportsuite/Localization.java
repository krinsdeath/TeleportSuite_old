package net.krinsoft.teleportsuite;

import java.util.List;
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
        String msg = config.getString(path);
        if (msg != null) {
            msg = msg.replaceAll("(<flag>|<player>)", rep);
            msg = color(msg);
            return msg;
        }
        return null;
    }

    public static void error(String path, Player player) {
        String msg = config.getString(path, "not null");
        if (msg != null) {
            msg = msg.replaceAll("<player>", player.getName());
            msg = color(msg);
            player.sendMessage(msg);
        }
    }

    private static String color(String msg) {
        return COLOR.matcher(msg).replaceAll("\u00A7$1");
    }

    public static void message(String path, String rep, Player p) {
        Object obj = config.getProperty(path);
        if (obj instanceof List) {
            List<String> list = (List<String>) obj;
            for (String line : list) {
                line = line.replaceAll("(<flag>|<player>)", rep);
                line = color(line);
                p.sendMessage(line);
            }
        } else if (obj instanceof String) {
            String msg = (String) obj;
            msg = msg.replaceAll("(<flag>|<player>)", rep);
            msg = color(msg);
            p.sendMessage(msg);
        } else if (obj == null) {
            return;
        }
    }

    public static void message(String path, String name, double x, double y, double z, Player p) {
        String msg = config.getString(path, "not null");
        if (msg != null) {
            msg = msg.replaceAll("<world>", name);
            msg = msg.replaceAll("<x>", "" + (int) x);
            msg = msg.replaceAll("<y>", "" + (int) y);
            msg = msg.replaceAll("<z>", "" + (int) z);
            msg = color(msg);
            p.sendMessage(msg);
        }
    }

    public static boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }
}

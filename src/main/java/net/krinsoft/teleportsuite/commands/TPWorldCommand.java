package net.krinsoft.teleportsuite.commands;

import java.util.List;
import net.krinsoft.teleportsuite.Localization;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

/**
 *
 * @author krinsdeath
 */
public class TPWorldCommand extends TeleportCommand {

    public TPWorldCommand(TeleportSuite plugin) {
        super(plugin);
        this.setName("TeleportSuite World");
        this.setCommandUsage("/tpworld [world]");
        this.addCommandExample("/tpworld sky_world");
        this.setArgRange(0, 1);
        this.addKey("teleport world");
        this.addKey("tps world");
        this.addKey("tpworld");
        this.setPermission("teleport.tpworld", "Allows this user to teleport to world spawns.", PermissionDefault.OP);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        World world = player.getWorld();
        if (args.size() == 1) {
            world = plugin.getServer().getWorld(args.get(0));
            if (world == null) {
                Localization.error("error.invalid_world", player);
                return;
            }
        }
        if (plugin.getPermissionHandler().canEnterWorld(sender, world.getName())) {
            setLastKnown(player, player.getLocation());
            Location loc = getLastKnown(player, world.getName());
            if (loc == null) {
                loc = world.getSpawnLocation();
                setLastKnown(player, loc);
            }
            plugin.getUsers().save();
            player.teleport(loc);
            return;
        }
    }

    public void setLastKnown(Player p, Location l) {
        String location = (int)l.getX() + ":" + (int)l.getY() + ":" + (int)l.getZ();
        System.out.println(l.getWorld().getName() + ": " + location);
        plugin.getUsers().setProperty(p.getName() + "." + l.getWorld().getName(), location);
    }

    public Location getLastKnown(Player p, String world) {
        String loc = plugin.getUsers().getString(p.getName() + "." + world);
        System.out.println(world + ": " + loc);
        int[] place = new int[3];
        try {
            place[0] = Integer.parseInt(loc.split(":")[0]);
            place[1] = Integer.parseInt(loc.split(":")[1]);
            place[2] = Integer.parseInt(loc.split(":")[2]);
        } catch (NumberFormatException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        } catch (NullPointerException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
        return new Location(plugin.getServer().getWorld(world), place[0], place[1], place[2]);
    }
}

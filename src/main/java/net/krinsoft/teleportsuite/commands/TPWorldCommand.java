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
            plugin.saveUsers();
            player.teleport(loc);
            return;
        }
    }

    public void setLastKnown(Player p, Location l) {
        String location = l.getX() + ":" + l.getY() + ":" + l.getZ();
        plugin.getUsers().set(p.getName() + "." + l.getWorld().getName(), location);
    }

    public Location getLastKnown(Player p, String world) {
        String loc = plugin.getUsers().getString(p.getName() + "." + world);
        double[] place = new double[3];
        try {
            String[] locs = loc.split(":");
            place[0] = Double.parseDouble(locs[0]);
            place[1] = Double.parseDouble(locs[1]);
            place[2] = Double.parseDouble(locs[2]);
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

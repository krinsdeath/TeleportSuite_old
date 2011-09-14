package net.krinsoft.teleportsuite.commands;

import java.util.List;
import net.krinsoft.teleportsuite.Localization;
import net.krinsoft.teleportsuite.TeleportSuite;
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
            player.teleport(world.getSpawnLocation());
            return;
        }
    }
}

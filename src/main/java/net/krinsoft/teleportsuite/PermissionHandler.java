package net.krinsoft.teleportsuite;

import com.pneumaticraft.commandhandler.PermissionsInterface;
import java.util.List;
import org.bukkit.command.CommandSender;

/**
 *
 * @author krinsdeath
 */
public class PermissionHandler implements PermissionsInterface {

    private boolean opFallBack;

    public PermissionHandler(boolean opFallBack) {
        this.opFallBack = opFallBack;
    }

    public boolean canEnterWorld(CommandSender sender, String world) {
        String worldNode = "teleport.world." + world;
        if (hasPermission(sender, worldNode, false) || hasPermission(sender, "multiverse.access." + world, false)) {
            return true;
        }
        return false;
    }

    public boolean hasPermission(CommandSender sender, String node, boolean isOpRequired) {
        if (opFallBack && sender.isOp()) {
            return true;
        }
        if (isOpRequired && opFallBack && !sender.isOp()) {
            return false;
        }
        if (sender.hasPermission(node)) {
            return true;
        } else if (sender.isPermissionSet(node) && !sender.hasPermission(node)) {
            return false;
        } else if (!sender.isPermissionSet(node) && sender.hasPermission("teleport.*")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasAnyPermission(CommandSender sender, List<String> allPermissionStrings, boolean opRequired) {
        for (String node : allPermissionStrings) {
            if (hasPermission(sender, node, opRequired)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAllPermission(CommandSender sender, List<String> allPermissionStrings, boolean opRequired) {
        for (String node : allPermissionStrings) {
            if (!hasPermission(sender, node, opRequired)) {
                return false;
            }
        }
        return true;
    }
}

package net.krinsoft.teleportsuite;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import com.pneumaticraft.commandhandler.CommandHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import net.krinsoft.teleportsuite.commands.TPACommand;
import net.krinsoft.teleportsuite.commands.TPAHereCommand;
import net.krinsoft.teleportsuite.commands.TPAcceptCommand;
import net.krinsoft.teleportsuite.commands.TPBackCommand;
import net.krinsoft.teleportsuite.commands.TPCancelCommand;
import net.krinsoft.teleportsuite.commands.TPCommand;
import net.krinsoft.teleportsuite.commands.TPCoordsCommand;
import net.krinsoft.teleportsuite.commands.TPHereCommand;
import net.krinsoft.teleportsuite.commands.TPLocationCommand;
import net.krinsoft.teleportsuite.commands.TPOCommand;
import net.krinsoft.teleportsuite.commands.TPOHereCommand;
import net.krinsoft.teleportsuite.commands.TPRejectCommand;
import net.krinsoft.teleportsuite.commands.TPRequestsCommand;
import net.krinsoft.teleportsuite.commands.TPToggleCommand;
import net.krinsoft.teleportsuite.commands.TPWorldCommand;
import org.bukkit.World;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 *
 * @author krinsdeath
 * @version 1.1.0
 */
public class TeleportSuite extends JavaPlugin {

    protected static PluginDescriptionFile pdf;
    protected static PluginManager pm;
    protected static Configuration config;
    protected static Configuration users;
    private final Players pListener = new Players();
    private final Entities eListener = new Entities();
    private double chVersion = 1;
    private CommandHandler commandHandler;
    private PermissionHandler permissionHandler;

    @Override
    public void onEnable() {
        if (!validateCommandHandler()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        registerConfiguration();
        registerCommands();
        Localization.setConfig(config);
        TeleportPlayer.init(this);
        setup();
        pm.registerEvent(Event.Type.PLAYER_JOIN, pListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, pListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_KICK, pListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_DEATH, eListener, Event.Priority.Normal, this);
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
        List<String> arguments = new ArrayList<String>(Arrays.asList(args));
        arguments.add(0, label);
        return commandHandler.locateAndRunCommand(sender, arguments);
    }

    private boolean validateCommandHandler() {
        Logger log = Logger.getLogger("TeleportSuite");
        try {
            commandHandler = new CommandHandler(this, null);
            if (this.commandHandler.getVersion() >= chVersion) {
                return true;
            } else {
                log.warning("A plugin with an outdated version of CommandHandler initialized before " + this + ".");
                log.warning(this + " needs CommandHandler v" + chVersion + " or higher, but CommandHandler v" + commandHandler.getVersion() + " was detected.");
                log.warning("Nag the authors of the following plugins: ");
                return false;
            }
        } catch (Throwable t) {
        }
        log.warning("A plugin with an outdated version of CommandHandler initialized before " + this + ".");
        log.warning(this + " needs CommandHandler v" + chVersion + " or higher, but CommandHandler v" + commandHandler.getVersion() + " was detected.");
        log.warning("Nag the authors of the following plugins: ");
        return false;
    }

    private void registerConfiguration() {
        pm = getServer().getPluginManager();
        pdf = getDescription();
        config = getConfiguration();
        users = new Configuration(new File(getDataFolder(), "users.yml"));
        users.load();
        Permission worlds = new Permission("teleport.world.*");
        worlds.setDefault(PermissionDefault.TRUE);
        if (pm.getPermission(worlds.getName()) == null) {
            pm.addPermission(worlds);
        }
        for (World world : getServer().getWorlds()) {
            Permission perm = new Permission("teleport.world." + world.getName());
            perm.setDefault(PermissionDefault.TRUE);
            if (pm.getPermission(perm.getName()) == null) {
                pm.addPermission(perm);
            }
            worlds.getChildren().put(perm.getName(), true);
        }
        worlds.recalculatePermissibles();
    }

    private void registerCommands() {
        permissionHandler = new PermissionHandler(config.getBoolean("plugin.opfallback", false));
        commandHandler = new CommandHandler(this, permissionHandler);
        commandHandler.registerCommand(new TPACommand(this));
        commandHandler.registerCommand(new TPAHereCommand(this));
        commandHandler.registerCommand(new TPAcceptCommand(this));
        commandHandler.registerCommand(new TPBackCommand(this));
        commandHandler.registerCommand(new TPCancelCommand(this));
        commandHandler.registerCommand(new TPCommand(this));
        commandHandler.registerCommand(new TPHereCommand(this));
        commandHandler.registerCommand(new TPOCommand(this));
        commandHandler.registerCommand(new TPOHereCommand(this));
        commandHandler.registerCommand(new TPRejectCommand(this));
        commandHandler.registerCommand(new TPRequestsCommand(this));
        commandHandler.registerCommand(new TPToggleCommand(this));
        commandHandler.registerCommand(new TPWorldCommand(this));
        commandHandler.registerCommand(new TPLocationCommand(this));
        commandHandler.registerCommand(new TPCoordsCommand(this));
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
            if (rev >= 0 && r <= rev) {
                if (r == 0 && rev > 0) {
                    fix = 10;
                }
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
                }
            }
            if (rev == 1 && r == 0) {
                config.setProperty("plugin.opfallback", false);
                config.setProperty("plugin.version", pdf.getVersion());
                System.out.println("Config updated to " + pdf.getVersion() + ". Finalizing...");
            }
        }
    }

    public Configuration getUsers() {
        return users;
    }

    public PermissionHandler getPermissionHandler() {
        return this.permissionHandler;
    }
}

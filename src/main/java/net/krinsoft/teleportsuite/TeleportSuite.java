package net.krinsoft.teleportsuite;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.pneumaticraft.commandhandler.CommandHandler;
import java.io.File;
import java.io.IOException;
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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 *
 * @author krinsdeath
 * @version 1.1.0
 */
public class TeleportSuite extends JavaPlugin {
    private final static Logger LOGGER = Logger.getLogger("TeleportSuite");

    protected static PluginDescriptionFile pdf;
    protected static PluginManager pm;
    protected static FileConfiguration config;
    protected static FileConfiguration users;
    private final Players pListener = new Players();
    private final Entities eListener = new Entities();
    private double chVersion = 1;
    private CommandHandler commandHandler;
    private PermissionHandler permissionHandler;

    // economy
    public boolean economy = false;
    //private AllPay allpay = null;

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
        log("TeleportSuite by " + pdf.getAuthors().toString().replaceAll("([\\[\\]])", "") + ") is enabled.");
    }

    @Override
    public void onDisable() {
        TeleportPlayer.clean();
        log("TeleportSuite by " + pdf.getAuthors().toString().replaceAll("([\\[\\]])", "") + ") is disabled.");
        pm = null;
        pdf = null;
        config = null;
    }

    public void log(String message) {
        message = "[" + this + "] " + message;
        LOGGER.info(message);
    }

    public void warn(String message) {
        message = "[" + this + "] " + message;
        LOGGER.warning(message);
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
                warn("A plugin with an outdated version of CommandHandler initialized before " + this + ".");
                warn(this + " needs CommandHandler v" + chVersion + " or higher, but CommandHandler v" + commandHandler.getVersion() + " was detected.");
                warn("Nag the authors of the following plugins: ");
                return false;
            }
        } catch (Throwable t) {
        }
        warn("A plugin with an outdated version of CommandHandler initialized before " + this + ".");
        warn(this + " needs CommandHandler v" + chVersion + " or higher, but CommandHandler v" + commandHandler.getVersion() + " was detected.");
        warn("Nag the authors of the following plugins: ");
        return false;
    }

    private void registerConfiguration() {
        pm = getServer().getPluginManager();
        pdf = getDescription();
        config = getConfig();
        config.setDefaults(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml")));
        users = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "users.yml"));
        users.setDefaults(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "users.yml")));
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
        // header
        config.options().header(
                "Messages:\n" +
                "  Each message can be customized to your liking.\n" +
                "  Any message left blank will not be relayed.\n" +
                "  I recommend leaving error messages as they are.\n" +
                "---\n" +
                "Economy:\n" +
                "  Type: An item ID, or -1 for a currency plugin (default: red rose)\n" +
                "  Amount: The amount of [type] required (default: 1)\n" +
                "---"
                );
        if (!versionMatch() || config.getBoolean("plugin.rebuild", false)) {
            if (config.get("plugin.rebuild") == null) {
                config.set("plugin.rebuild", true);
            }
            if (config.getBoolean("plugin.rebuild", false)) {
                if (config.getString("plugin.version") == null) {
                    log(pdf.getFullName() + " detected first run.");
                    log("Building configuration file...");
                } else {
                    log("Rebuilding configuration file...");
                }
                // teleport messages
                config.set("teleport.message", "Teleporting to &a<player>&f...");
                config.set("teleport.notice", "&a<player>&f is teleporting to you.");
                config.set("teleport.request.from", "Teleport request from &a<player>&f. (Accept with &a/tpaccept <player>&f)");
                config.set("teleport.request.to", "Awaiting response from &a<player>&f...");
                config.set("teleport.toggle.allowed", "allowed");
                config.set("teleport.toggle.denied", "ignored");
                config.set("teleport.toggle.message", "Teleport requests will be <flag>.");
                config.set("teleport.toggle.default", false);
                // request messages
                config.set("request.none", "You have no request from &a<player>&f!");
                config.set("request.deny", "You denied &a<player>&f's teleport request.");
                config.set("request.denied", "&a<player>&f denied your teleport request.");
                config.set("request.only_one", "You can only request one teleport at a time.");
                config.set("request.ignored", "&a<player>&f is not allowing teleport requests.");
                config.set("request.open.header", "Open requests...");
                config.set("request.open.self", "Requesting teleport to: &a<player>&f");
                config.set("request.open.entry", "&a<player>&f");
                config.set("request.open.none", "You have no requests.");
                config.set("request.cancel", "You cancelled your teleport to &a<player>&f.");
                config.set("request.cancelled", "&a<player>&f cancelled their teleport request.");
                // error messages
                config.set("error.arguments", "&CNot enough arguments.");
                config.set("error.target", "&CInvalid target.");
                config.set("error.permission", "&CYou do not have permission for that.");
                config.set("error.no_location", "&CYou haven't teleported yet.");
                config.set("error.params", "&CInvalid parameters.");
                config.set("error.destination", "&CInvalid destination.");
                config.set("error.invalid_world", "&CInvalid world");
                // informational messages
                config.set("message.location", "You are currently at (&b<x>&f, &b<y>&f, &b<z>&f) in &a<world>&f.");
                config.set("plugin.rebuild", false);
                config.set("plugin.version", pdf.getVersion());
                log("... done.");
            }
        }
        if (!economyMatch()) {
            config.set("economy.use", false);
            config.set("economy.type", "38");
            config.set("economy.amount", 1);
        }
        saveConfig();
        economy = config.getBoolean("economy.use");
    }

    private boolean versionMatch() {
        if (config.getString("plugin.version") == null) { return false; }
        if (pdf.getVersion().equals(config.getString("plugin.version"))) {
            return true;
        }
        return false;
    }

    private boolean economyMatch() {
        return (config.get("economy.use") == null);
    }

    public FileConfiguration getUsers() {
        return users;
    }

    public void saveUsers() {
        try {
            users.save(new File(this.getDataFolder(), "users.yml"));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public PermissionHandler getPermissionHandler() {
        return this.permissionHandler;
    }
}

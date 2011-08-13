package net.krinsoft.teleportsuite;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author krinsdeath
 */
class Players extends PlayerListener {

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (TeleportPlayer.getPlayer(event.getPlayer()) == null) {
            TeleportPlayer.addPlayer(event.getPlayer());
        }
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        TeleportPlayer.removePlayer(event.getPlayer());
    }

    @Override
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        TeleportPlayer.removePlayer(event.getPlayer());
    }
}

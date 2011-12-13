package net.krinsoft.teleportsuite;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

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

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) { return; }
        if (event.getTo() != null && event.getFrom() != null && !event.getTo().equals(event.getFrom())) {
            TeleportPlayer.getPlayer(event.getPlayer()).setLastKnown(event.getFrom());
        }
    }
}

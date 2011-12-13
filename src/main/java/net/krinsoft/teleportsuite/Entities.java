package net.krinsoft.teleportsuite;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

/**
 *
 * @author krinsdeath
 */
class Entities extends EntityListener {

    @Override
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            TeleportPlayer.getPlayer(((Player) event.getEntity())).setLastKnown(((Player) event.getEntity()).getLocation());
        }
    }
}

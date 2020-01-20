package pl.natusiek.grouptp.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import pl.natusiek.grouptp.basic.arena.GameArena;
import pl.natusiek.grouptp.basic.arena.GameArenaManager;
import pl.natusiek.grouptp.basic.spectate.GameSpectate;
import pl.natusiek.grouptp.config.MessagesConfig;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class PlayerProtectGameListener implements Listener {

    private final GameArenaManager arenaManager;
    private final GameSpectate gameSpectate;

    public PlayerProtectGameListener(GameArenaManager arenaManager, GameSpectate gameSpectate) {
        this.arenaManager = arenaManager;
        this.gameSpectate = gameSpectate;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        final Player player = (Player) event.getEntity();
        final GameArena arena = this.arenaManager.findArenaByPlayer(player.getUniqueId());

        if (arena == null) {
            event.setCancelled(true);
        } else if (this.gameSpectate.isPlaying(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;

        final Entity victim = event.getEntity();
        final Entity attacker = event.getDamager();

        if (victim instanceof Player && attacker instanceof Player) {
            final Player playerAttacker = (Player) attacker;
            final GameArena arena = this.arenaManager.findArenaByPlayer(playerAttacker.getUniqueId());

            if (arena == null) {
                event.setCancelled(true);
            } else if (this.gameSpectate.isPlaying(playerAttacker.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemDrop(PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        final GameArena arena = this.arenaManager.findArenaByPlayer(player.getUniqueId());

        if (arena == null) {
            event.setCancelled(true);
        } else if (this.gameSpectate.isPlaying(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemPickup(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        final GameArena arena = this.arenaManager.findArenaByPlayer(player.getUniqueId());

        if (arena == null) {
            event.setCancelled(true);
        } else if (this.gameSpectate.isPlaying(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

}

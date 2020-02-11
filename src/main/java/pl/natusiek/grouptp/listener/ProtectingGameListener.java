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
import pl.natusiek.grouptp.game.arena.Arena;
import pl.natusiek.grouptp.game.arena.ArenaManager;
import pl.natusiek.grouptp.game.spectate.ArenaSpectate;


public class ProtectingGameListener implements Listener {

    private final ArenaManager arenaManager;

    public ProtectingGameListener(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        final Player player = (Player) event.getEntity();
        final Arena arena = this.arenaManager.findArenaByPlayer(player.getUniqueId());

        if (arena == null) event.setCancelled(true);
        else if (arena.isSpectators(player.getUniqueId())) event.setCancelled(true);

    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;

        final Entity victim = event.getEntity();
        final Entity attacker = event.getDamager();

        if (victim instanceof Player && attacker instanceof Player) {
            final Player playerAttacker = (Player) attacker;
            final Arena arena = this.arenaManager.findArenaByPlayer(playerAttacker.getUniqueId());

            if (arena == null) event.setCancelled(true);
            else if (arena.isSpectators(playerAttacker.getUniqueId())) event.setCancelled(true);

        }
    }

    @EventHandler
    public void onItemDrop(PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        final Arena arena = this.arenaManager.findArenaByPlayer(player.getUniqueId());

        if (arena == null) event.setCancelled(true);
        else if (arena.isSpectators(player.getUniqueId())) event.setCancelled(true);

    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemPickup(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        final Arena arena = this.arenaManager.findArenaByPlayer(player.getUniqueId());

        if (arena == null) event.setCancelled(true);
        else if (arena.isSpectators(player.getUniqueId())) event.setCancelled(true);

    }

}
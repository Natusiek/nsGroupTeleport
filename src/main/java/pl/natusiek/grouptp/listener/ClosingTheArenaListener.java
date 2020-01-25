package pl.natusiek.grouptp.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.game.arena.Arena;
import pl.natusiek.grouptp.game.arena.ArenaManager;
import pl.natusiek.grouptp.game.kit.KitManager;
import pl.natusiek.grouptp.helper.BorderHelper;
import pl.natusiek.grouptp.helper.LocationHelper;
import pl.natusiek.grouptp.helper.PlayerHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class ClosingTheArenaListener implements Listener {

    private final KitManager kitManager;
    private final ArenaManager arenaManager;

    private final Map<UUID, Long> cooldown = new HashMap<>();

    public ClosingTheArenaListener(KitManager kitManager, ArenaManager arenaManager) {
        this.kitManager = kitManager;
        this.arenaManager = arenaManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final Arena arena = this.arenaManager.findArenaByPlayer(player.getUniqueId());

        if (arena == null) return;
        arena.removePlayer(player.getUniqueId());

        if (arena.getPlayers().size() <= 1) {
            if (arena.getPlayers().size() > 0) {
                final UUID winnerId = arena.getPlayers().get(0);
                final Player winner = Bukkit.getPlayer(winnerId);
                if (winner != null) {
                    closeArena(winner, player);
                    Bukkit.broadcastMessage(colored(MessagesConfig.ARENA$END_ARENA.replace("{WINNER}", winner.getName()).replace("{ARENA}", arena.getName())));
                }
                arena.removePlayer(winnerId);
            }
            arena.restart();
        }
    }

    @EventHandler
    public void onQuit(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final Arena arena = this.arenaManager.findArenaByPlayer(player.getUniqueId());
        final GroupTeleportPlugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);
        if (arena == null) return;
        arena.removePlayer(player.getUniqueId());

        if (arena.getPlayers().size() <= 1) {
            if (arena.getPlayers().size() > 0) {
                final UUID winnerId = arena.getPlayers().get(0);
                final Player winner = Bukkit.getPlayer(winnerId);
                if (winner != null) {
                    closeArena(winner, player);
                    Bukkit.broadcastMessage(colored(MessagesConfig.ARENA$END_ARENA.replace("{WINNER}", winner.getName()).replace("{ARENA}", arena.getName())));
                }
                arena.removePlayer(winnerId);
            }
            arena.restart();
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();

        PlayerHelper.addItemFromLobby(player);
        player.teleport(LocationHelper.fromString("world, 200.0, 80.0, 200.0, 0.0f, 0.0f").toLocation());
        BorderHelper.setBorder(player, player.getLocation(), 1000000);
    }

    private void closeArena(Player winner, Player lost) {
        PlayerHelper.spawnFirework(winner.getLocation());
        Bukkit.getScheduler().runTaskLater(GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class), () -> {
            clearEffect(winner);
            PlayerHelper.addItemFromLobby(winner);
            winner.setGameMode(GameMode.ADVENTURE);
            winner.teleport(LocationHelper.fromString("world, 200.0, 80.0, 200.0, 0.0f, 0.0f").toLocation());

            this.kitManager.setCurrentKit(winner.getUniqueId(), null);
            this.kitManager.setCurrentKit(lost.getUniqueId(), null);

            BorderHelper.setBorder(winner, winner.getLocation(),1000000);
            winner.sendTitle(" ", colored(" &aWygrales!"));
        }, 80L);
    }

    private void clearEffect(Player player) {
        player.getActivePotionEffects()
                .stream()
                .map(PotionEffect::getType)
                .forEach(player::removePotionEffect);
    }

}

package pl.natusiek.grouptp.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.basic.arena.GameArena;
import pl.natusiek.grouptp.basic.arena.GameArenaManager;
import pl.natusiek.grouptp.basic.kit.KitManager;
import pl.natusiek.grouptp.basic.spectate.GameSpectate;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.helper.BorderHelper;
import pl.natusiek.grouptp.helper.MessageHelper;
import pl.natusiek.grouptp.helper.PlayerHelper;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class ClosingTheArenaListener implements Listener {

    private final GroupTeleportPlugin plugin;
    private final GameArenaManager arenaManager;
    private final KitManager kitManager;
    private final GameSpectate gameSpectate;

    public ClosingTheArenaListener(GroupTeleportPlugin plugin,GameArenaManager arenaManager, KitManager kitManager, GameSpectate gameSpectate) {
        this.plugin = plugin;
        this.arenaManager = arenaManager;
        this.kitManager = kitManager;
        this.gameSpectate = gameSpectate;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final GameArena gameArena = this.arenaManager.findArenaByPlayer(player.getUniqueId());

        if (gameArena == null) return;
        gameArena.removePlayer(player.getUniqueId());

        if (gameArena.getPlayers().size() <= 1) {
            if (gameArena.getPlayers().size() > 0) {
                final UUID winnerId = gameArena.getPlayers().get(0);
                final Player winner = Bukkit.getPlayer(winnerId);
                if (winner != null) {
                    closeArena(winner, player, gameArena);
                }
                gameArena.removePlayer(winnerId);
            }
            gameArena.restart();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final GameArena gameArena = this.arenaManager.findArenaByPlayer(player.getUniqueId());

        if (gameArena == null) return;
        gameArena.removePlayer(player.getUniqueId());

        if (gameArena.getPlayers().size() <= 1) {
            if (gameArena.getPlayers().size() > 0) {
                final UUID winnerId = gameArena.getPlayers().get(0);
                final Player winner = Bukkit.getPlayer(winnerId);
                if (winner != null) {
                    closeArena(winner, player, gameArena);
                }
                gameArena.removePlayer(winnerId);
            }
            gameArena.restart();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRespawn(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();

        PlayerHelper.addItemFromLobby(player);
        player.teleport(plugin.getConfigManager().getSpawnLocation());
        BorderHelper.setBorder(player, player.getLocation(), 1000000);
    }

    private void closeArena(Player winner, Player lost, GameArena gameArena) {
        PlayerHelper.spawnFirework(winner.getLocation());
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            clearEffect(winner);
            PlayerHelper.addItemFromLobby(winner);
            winner.setGameMode(GameMode.ADVENTURE);
            winner.teleport(plugin.getConfigManager().getSpawnLocation());

            this.kitManager.setCurrentKit(winner.getUniqueId(), null);
            this.kitManager.setCurrentKit(lost.getUniqueId(), null);

            gameArena.getSpectetors()
                    .stream()
                    .map(Bukkit::getPlayer)
                    .forEach(player -> {
                        clearEffect(player);
                        PlayerHelper.addItemFromLobby(player);
                        this.gameSpectate.leaveSpectate(player);
                        this.gameSpectate.removeSpectetor(player.getUniqueId());
                        player.teleport(plugin.getConfigManager().getSpawnLocation());
                        player.sendMessage(colored(MessagesConfig.SPECTATOR$ARENA_CLOSE));
                    });

            BorderHelper.setBorder(winner, winner.getLocation(), 100000);
            MessageHelper.sendTitle(winner, " ", " &aWygrales!");
            Bukkit.broadcastMessage(colored(MessagesConfig.ARENA$END_ARENA.replace("{WINNER}", winner.getName()).replace("{ARENA}", gameArena.getName())));
        }, 80L);
    }

    private void clearEffect(Player player) {
        player.getActivePotionEffects()
                .stream()
                .map(PotionEffect::getType)
                .forEach(player::removePotionEffect);
    }
}

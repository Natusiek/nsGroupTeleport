package pl.natusiek.grouptp.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import pl.natusiek.grouptp.arena.GameArena;
import pl.natusiek.grouptp.arena.GameArenaManager;
import pl.natusiek.grouptp.arena.GameSpectate;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.helper.BorderHelper;
import pl.natusiek.grouptp.helper.ItemBuilder;
import pl.natusiek.grouptp.helper.MessageHelper;
import pl.natusiek.grouptp.kit.KitManager;

import java.util.UUID;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class PlayerCloseArenaListener implements Listener {

    private final GameArenaManager arenaManager;
    private final KitManager kitManager;
    private final GameSpectate gameSpectate;

    public PlayerCloseArenaListener(GameArenaManager arenaManager, KitManager kitManager, GameSpectate gameSpectate) {
        this.arenaManager = arenaManager;
        this.kitManager = kitManager;
        this.gameSpectate = gameSpectate;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRespawn(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();

        PlayerKitsListener.giveMenuItem(player);
        this.kitManager.setCurrentKit(player.getUniqueId(), null);
        BorderHelper.setBorder(player, player.getLocation(), 1000000);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final GameArena gameArena = this.arenaManager.findArenaByPlayer(player.getUniqueId());
        if (gameArena == null) {
            return;
        }
        gameArena.removePlayer(player.getUniqueId());
        if (gameArena.getPlayers().size() <= 1) {
            if (gameArena.getPlayers().size() > 0) {
                final UUID winnerId = gameArena.getPlayers().get(0);
                final Player winner = Bukkit.getPlayer(gameArena.getPlayers().get(0));
                if (winner != null) {
                    BorderHelper.setBorder(winner, winner.getLocation(), 1000000);
                    MessageHelper.sendTitle(winner, " ", "&aWygrales!");
                    PlayerKitsListener.giveMenuItem(winner);
                    winner.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(winner::removePotionEffect);
                    winner.teleport(Bukkit.getWorld("world").getSpawnLocation());
                    winner.getInventory().setArmorContents(null);
                    kitManager.setCurrentKit(winner.getUniqueId(), null);
                    kitManager.setCurrentKit(player.getUniqueId(), null);
                    winner.setGameMode(GameMode.ADVENTURE);
                    player.setGameMode(GameMode.ADVENTURE);
                    gameSpectate.getPlayer().stream().forEach(players -> gameSpectate.leaveSpectate(players));
                }
                gameArena.removePlayer(winnerId);
                Bukkit.broadcastMessage(colored(MessagesConfig.ARENA$END_ARENA.replace("{WINNER}", winner.getName()).replace("{ARENA}", gameArena.getNames(arenaManager.getArenas()))));
            }
            gameArena.restart();
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final GameArena gameArena = this.arenaManager.findArenaByPlayer(player.getUniqueId());
        if (gameArena == null) {
            return;
        }
        gameArena.removePlayer(player.getUniqueId());
        if (gameArena.getPlayers().size() <= 1) {
            if (gameArena.getPlayers().size() > 0) {
                final UUID winnerId = gameArena.getPlayers().get(0);
                final Player winner = Bukkit.getPlayer(gameArena.getPlayers().get(0));
                if (winner != null) {
                    BorderHelper.setBorder(winner, winner.getLocation(), 1000000);
                    MessageHelper.sendTitle(winner, " ", "&aWygrales!");
                    PlayerKitsListener.giveMenuItem(winner);
                    winner.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(winner::removePotionEffect);
                    winner.teleport(Bukkit.getWorld("world").getSpawnLocation());
                    winner.getInventory().setArmorContents(null);
                    kitManager.setCurrentKit(winner.getUniqueId(), null);
                    gameSpectate.getPlayer().stream().forEach(players -> gameSpectate.leaveSpectate(players));
                    winner.setGameMode(GameMode.ADVENTURE);
                    player.setGameMode(GameMode.ADVENTURE);
                }
                gameArena.removePlayer(winnerId);
                Bukkit.broadcastMessage(colored(MessagesConfig.ARENA$END_ARENA.replace("{WINNER}", winner.getName()).replace("{ARENA}", gameArena.getNames(arenaManager.getArenas()))));
            }
            gameArena.restart();
        }
    }
}

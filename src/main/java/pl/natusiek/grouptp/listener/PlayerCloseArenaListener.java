package pl.natusiek.grouptp.listener;

import java.util.UUID;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.basic.arena.GameArena;
import pl.natusiek.grouptp.basic.arena.GameArenaManager;
import pl.natusiek.grouptp.basic.spectate.GameSpectate;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.helper.BorderHelper;
import pl.natusiek.grouptp.helper.MessageHelper;
import pl.natusiek.grouptp.basic.kit.KitManager;

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
        player.setGameMode(GameMode.ADVENTURE);
        PlayerHubListener.giveMenuItem(player);
        BorderHelper.setBorder(player, player.getLocation(), 1000000);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final Plugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);
        final GameArena gameArena = this.arenaManager.findArenaByPlayer(player.getUniqueId());

        if (gameArena == null) return;
        gameArena.removePlayer(player.getUniqueId());
        if (gameArena.getPlayers().size() <= 1) {
            if (gameArena.getPlayers().size() > 0) {
                final UUID winnerId = gameArena.getPlayers().get(0);
                final Player winner = Bukkit.getPlayer(gameArena.getPlayers().get(0));
                if (winner != null) {
                    firework(winner.getLocation());
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        winner.setGameMode(GameMode.ADVENTURE);
                        winner.teleport(Bukkit.getWorld("world").getSpawnLocation());
                        winner.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(winner::removePotionEffect);

                        kitManager.setCurrentKit(winner.getUniqueId(), null);
                        kitManager.setCurrentKit(player.getUniqueId(), null);

                        gameSpectate.getPlayers().stream().map(Bukkit::getPlayer).forEach(players -> {

                            gameSpectate.leaveSpectate(players);
                            players.sendMessage(colored(MessagesConfig.SPECTATOR$ARENA_CLOSE));
                        });

                        PlayerHubListener.giveMenuItem(winner);
                        BorderHelper.setBorder(winner, winner.getLocation(), 1000000);
                        MessageHelper.sendTitle(winner, " ", "&aWygrales!");
                    }, 80L);
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
        final Plugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);
        final GameArena gameArena = this.arenaManager.findArenaByPlayer(player.getUniqueId());
        if (gameArena == null) return;

        gameArena.removePlayer(player.getUniqueId());
        if (gameArena.getPlayers().size() <= 1) {
            if (gameArena.getPlayers().size() > 0) {
                final UUID winnerId = gameArena.getPlayers().get(0);
                final Player winner = Bukkit.getPlayer(gameArena.getPlayers().get(0));
                if (winner != null) {
                    firework(winner.getLocation());
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        winner.setGameMode(GameMode.ADVENTURE);
                        winner.teleport(Bukkit.getWorld("world").getSpawnLocation());
                        winner.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(winner::removePotionEffect);

                        kitManager.setCurrentKit(winner.getUniqueId(), null);
                        kitManager.setCurrentKit(player.getUniqueId(), null);

                        gameSpectate.getPlayers().stream().map(Bukkit::getPlayer).forEach(players -> {
                            gameSpectate.leaveSpectate(players);
                            players.sendMessage(colored(MessagesConfig.SPECTATOR$ARENA_CLOSE));
                        });

                        PlayerHubListener.giveMenuItem(winner);
                        BorderHelper.setBorder(winner, winner.getLocation(), 1000000);
                        MessageHelper.sendTitle(winner, " ", "&aWygrales!");
                    }, 80L);
                }
                gameArena.removePlayer(winnerId);
                Bukkit.broadcastMessage(colored(MessagesConfig.ARENA$END_ARENA.replace("{WINNER}", winner.getName()).replace("{ARENA}", gameArena.getNames(arenaManager.getArenas()))));
            }
            gameArena.restart();
        }
        event.setDeathMessage(null);
    }

    private static void firework(Location location) {
        Firework firework = (Firework)location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder().flicker(false).trail(true).withColor(Color.YELLOW).withColor(Color.WHITE).with(FireworkEffect.Type.BALL).build();
        meta.setPower(1);
        meta.addEffect(effect);
        firework.setFireworkMeta(meta);
        firework.setFireworkMeta(meta);
        firework.setFireworkMeta(meta);
    }

}

package pl.natusiek.grouptp.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.basic.gui.GameInfoInventoryProvider;
import pl.natusiek.grouptp.basic.spectate.GameSpectate;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.helper.MessageHelper;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class WatchingTheArenaListener implements Listener {

    private final GroupTeleportPlugin plugin;
    private final GameSpectate gameSpectate;

    private final Map<UUID, Long> cooldown = new HashMap<>();

    public WatchingTheArenaListener(GroupTeleportPlugin plugin, GameSpectate gameSpectate) {
        this.plugin = plugin;
        this.gameSpectate = gameSpectate;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (item == null) return;

        final Player player = event.getPlayer();
        if (plugin.getConfigManager().getItemSpectator().isSimilar(item)) {
            GameInfoInventoryProvider.INVENTORY.open(player);
        } else if (plugin.getConfigManager().getItemLeaveSpectator().isSimilar(item)) {
            gameSpectate.leaveSpectate(player);
            player.teleport(plugin.getConfigManager().getSpawnLocation());
        }

        if (plugin.getConfigManager().getItemCompass().isSimilar(item)) {
            if (!gameSpectate.isPlaying(player.getUniqueId())) return;
            if (cooldown.containsKey(player.getUniqueId())) {
                long time = cooldown.get(player.getUniqueId());
                if (time > System.currentTimeMillis()) {
                    player.sendMessage(colored(MessagesConfig.ARENA$COMPASS$COOLDOWN
                            .replace("{TIME}", TimeUnit.MILLISECONDS.toSeconds(time - System.currentTimeMillis()) + toString())));
                    return;
                }
            }
            cooldown.put(player.getUniqueId(), System.currentTimeMillis() + (MessagesConfig.ARENA$COMPASS$TIME_COOLDOWND * 1000));

            Player result = null;
            double lastDistance = Double.MAX_VALUE;
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (player.getName().equalsIgnoreCase(players.getName()) || !player.canSee(players)) {
                    continue;
                }
                if (player.getLocation().distance(players.getLocation()) <= 100 && !player.getName().equalsIgnoreCase(players.getName())) {
                    double distance = player.getLocation().distance(players.getLocation());

                    if (distance < lastDistance) {
                        lastDistance = distance;
                        result = players;
                    }
                }
                if(result != null) {
                    int distance = ((int) lastDistance);
                    MessageHelper.sendActionbar(player, MessagesConfig.ARENA$COMPASS$NEAREST_PLAYER
                            .replace("{RESULT}", result.getName())
                            .replace("{DISTANCE}", distance+""));
                    player.setCompassTarget(result.getLocation());
                } else {
                    MessageHelper.sendActionbar(player, MessagesConfig.ARENA$COMPASS$NO_FOUND_PLAYER);
                    player.setCompassTarget(player.getLocation());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        this.gameSpectate.leaveSpectate(player);
        this.cooldown.remove(player.getUniqueId());
    }

}

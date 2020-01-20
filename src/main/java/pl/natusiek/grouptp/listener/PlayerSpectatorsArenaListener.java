package pl.natusiek.grouptp.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import pl.natusiek.grouptp.basic.spectate.GameSpectate;
import pl.natusiek.grouptp.basic.gui.GameInfoInventoryProvider;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.helper.BorderHelper;
import pl.natusiek.grouptp.helper.ItemBuilder;
import pl.natusiek.grouptp.helper.MessageHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class PlayerSpectatorsArenaListener implements Listener {

    private GameSpectate gameSpectate;

    private final Map<UUID, Long> cooldown = new HashMap<>();

    private final static ItemStack spectatorsOpenGui = new ItemBuilder(Material.PAPER).withName("&aOBSERWUJ GRACZY").build();
    private final static ItemStack leaveItem = new ItemBuilder(Material.NAME_TAG).withName("&4Opusc").addEnchantment(Enchantment.DURABILITY, 10).build();
    private final static ItemStack compass = new ItemBuilder(Material.COMPASS).withName("&8(&fPPM, aby odswiezyc&8)").build();

    public PlayerSpectatorsArenaListener(GameSpectate gameSpectate) {
        this.gameSpectate = gameSpectate;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();

        if(item == null) return;
        if(this.spectatorsOpenGui.isSimilar(item)) {
            GameInfoInventoryProvider.INVENTORY.open(player);
        } else if(this.leaveItem.isSimilar(item)) {
            this.gameSpectate.leaveSpectate(player);
        }
    }

    @EventHandler
    public void magicCompass(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();

        if (event.getItem() == null) return;
        if (!this.gameSpectate.isPlaying(player.getUniqueId())) return;
        if (item.isSimilar(this.compass)) {
            if (Bukkit.getOnlinePlayers().size() <= 1) {
                player.sendMessage(colored(MessagesConfig.ARENA$COMPASS$FEW_PEOPLE));
                return;
            }
            if (this.cooldown.containsKey(player.getUniqueId())) {
                long time = this.cooldown.get(player.getUniqueId()).longValue();
                if (time > System.currentTimeMillis()) {
                    player.sendMessage(colored(MessagesConfig.ARENA$COMPASS$COOLDOWN
                            .replace("{TIME}", TimeUnit.MILLISECONDS.toSeconds(time - System.currentTimeMillis()) + toString())));
                    return;
                }
            }
            this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + (MessagesConfig.ARENA$COMPASS$TIME_COOLDOWND * 1000));

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
                int distance = ((int) lastDistance);

                if (result != null) {
                    MessageHelper.sendActionbar(player, MessagesConfig.ARENA$COMPASS$NEAREST_PLAYER
                            .replace("{RESULT}", result.getName())
                            .replace("{DISTANCE}", distance + toString()));
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKick(PlayerKickEvent event) {
        final Player player = event.getPlayer();

        this.gameSpectate.leaveSpectate(player);
        this.cooldown.remove(player.getUniqueId());
    }

}

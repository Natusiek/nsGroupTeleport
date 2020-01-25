package pl.natusiek.grouptp.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.game.arena.Arena;
import pl.natusiek.grouptp.game.arena.ArenaManager;
import pl.natusiek.grouptp.game.gui.ArenaInfoInventoryProvider;
import pl.natusiek.grouptp.game.spectate.ArenaSpectate;
import pl.natusiek.grouptp.helper.ItemBuilder;
import pl.natusiek.grouptp.helper.MessageHelper;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class SpectetorsArenaListener implements Listener {

    private final ArenaManager arenaManager;
    private final ArenaSpectate arenaSpectate;

    private final Map<UUID, Long> cooldown = new HashMap<>();

    private final static ItemStack spectatorsOpenGui = new ItemBuilder(Material.COMPASS).withName("&8* &aObserwuj innych graczy &8*").build();
    private final static ItemStack leaveItem = new ItemBuilder(Material.NAME_TAG).withName("&8* &4Opusc obserwowanie &8*").addEnchantment(Enchantment.DURABILITY, 10).build();
    private final static ItemStack compass = new ItemBuilder(Material.COMPASS).withName("&8* &eZnajdz pobliskiego gracza &8*").build();

    public SpectetorsArenaListener(ArenaManager arenaManager, ArenaSpectate arenaSpectate) {
        this.arenaManager = arenaManager;
        this.arenaSpectate = arenaSpectate; }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (item == null) return;

        final Player player = event.getPlayer();
        if (spectatorsOpenGui.isSimilar(item)) {
            ArenaInfoInventoryProvider.INVENTORY.open(player);
        } else if (leaveItem.isSimilar(item)) {
            Arena arena = this.arenaManager.findArenaByPlayer(player.getUniqueId());
            arenaSpectate.leaveSpectate(player);
            arena.removeSpectators(player.getUniqueId());
        }

        else if (compass.isSimilar(item)) {
            if (cooldown.containsKey(player.getUniqueId())) {
                long time = cooldown.get(player.getUniqueId());
                if (time > System.currentTimeMillis()) {
                    player.sendMessage(colored(MessagesConfig.ARENA$COMPASS$COOLDOWN
                            .replace("{TIME}", TimeUnit.MILLISECONDS.toSeconds(time - System.currentTimeMillis()) +"")));
                    return;
                }
            }
            cooldown.put(player.getUniqueId(), System.currentTimeMillis() + (MessagesConfig.ARENA$COMPASS$TIME_COOLDOWND * 1000));

            for (Player players : Bukkit.getOnlinePlayers()) {
                Player result = null;
                double lastDistance = Double.MAX_VALUE;
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
                            .replace("{DISTANCE}", "" + distance));
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
        this.arenaSpectate.leaveSpectate(player);
        this.cooldown.remove(player.getUniqueId());
    }
}

package pl.natusiek.grouptp.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;

import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.basic.arena.GameArena;
import pl.natusiek.grouptp.basic.arena.GameArenaManager;
import pl.natusiek.grouptp.basic.gui.KitInventoryProvider;
import pl.natusiek.grouptp.basic.kit.Kit;
import pl.natusiek.grouptp.basic.kit.KitManager;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.helper.BorderHelper;
import pl.natusiek.grouptp.helper.PlayerHelper;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class ChoosingAnArenaListener implements Listener {

    private final GroupTeleportPlugin plugin;
    private final KitManager kitManager;
    private final GameArenaManager arenaManager;

    private final Map<UUID, Long> cooldown = new HashMap<>();

    public ChoosingAnArenaListener(GroupTeleportPlugin plugin, GameArenaManager arenaManager, KitManager kitManager) {
        this.plugin = plugin;
        this.arenaManager = arenaManager;
        this.kitManager = kitManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        PlayerHelper.addItemFromLobby(player);
        player.setFireTicks(0);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setFallDistance(0);
        player.teleport(plugin.getConfigManager().getSpawnLocation());
        player.setGameMode(GameMode.ADVENTURE);
        BorderHelper.setBorder(player, player.getLocation(), 1000000);
    }

    @EventHandler
    public void leaveServer(PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (item == null) return;

        final Player player = event.getPlayer();
        if (plugin.getConfigManager().getItemLeaveServer().isSimilar(item)) {
            PlayerHelper.TeleportPlayerToServer(player, MessagesConfig.BUNGEE$SERVER);
        } else if (plugin.getConfigManager().getItemKits().isSimilar(item)) {
            KitInventoryProvider.INVENTORY.open(player);
        }
    }
    @EventHandler
    public void choosingArena(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        final Block clicked = event.getClickedBlock();
        if (clicked.getType() == plugin.getConfigManager().getButton()) {

            final Button btn = (Button) clicked.getState().getData();
            final Block base = clicked.getRelative(btn.getAttachedFace());
            if (base.getType() != plugin.getConfigManager().getBase()) return;

            final Player player = event.getPlayer();
            if (this.cooldown.containsKey(player.getUniqueId())) {
                long time = this.cooldown.get(player.getUniqueId());
                if (time > System.currentTimeMillis()) {
                    player.sendMessage(colored(MessagesConfig.ARENA$CANNOT_JOIN$COOLDOWN
                    .replace("{TIME}", ""+TimeUnit.MILLISECONDS.toSeconds(time - System.currentTimeMillis()))));
                    return;
                }
            }
            this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + (MessagesConfig.ARENA$SEARCHING$COOLDOWN * 1000));
            final Kit kit = this.kitManager.getCurrentKit(player.getUniqueId());
            if (kit == null) {
                player.sendMessage(colored(MessagesConfig.KIT$DONT_HAVE_KIT));
                return;
            }
            final GameArena availableArena = this.arenaManager.getArenas()
                    .stream()
                    .filter(gameArena -> gameArena.getState() == GameArena.ArenaStates.AVAILABLE)
                    .findFirst()
                    .orElse(null);
            if (availableArena == null) {
                player.sendMessage(colored(MessagesConfig.ARENA$CANNOT_JOIN$NO_AVAILABLE_ARENAS));
                return;
            }
            final List<Player> inRadius = PlayerHelper.findPlayersInRadius(player,
                    MessagesConfig.ARENA$SEARCHING$RADIUS, MessagesConfig.ARENA$SEARCHING$MAX_PLAYERS)
                    .stream()
                    .filter(playerInRadius -> {
                        final Kit playerInRadiusKit = this.kitManager.getCurrentKit(playerInRadius.getUniqueId());
                        return playerInRadiusKit != null && kit.getName().equals(playerInRadiusKit.getName());
                    }).collect(Collectors.toList());
            if (inRadius.size() < MessagesConfig.ARENA$SEARCHING$MIN_PLAYERS_IN_RADIUS) {
                player.sendMessage(colored(MessagesConfig.ARENA$CANNOT_JOIN$NO_PLAYERS_IN_RADIUS));
                return;
            }
            inRadius.add(player);
            inRadius.stream().map(Player::getUniqueId).forEach(availableArena::addPlayer);
            availableArena.start();
        }
    }

    @EventHandler
    public void WeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        this.cooldown.remove(event.getPlayer().getUniqueId());
    }

}

package pl.natusiek.grouptp.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pl.natusiek.grouptp.basic.arena.GameArena;
import pl.natusiek.grouptp.basic.arena.GameArenaManager;
import pl.natusiek.grouptp.basic.gui.KitInventoryProvider;
import pl.natusiek.grouptp.basic.kit.Kit;
import pl.natusiek.grouptp.basic.kit.KitManager;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.helper.BorderHelper;
import pl.natusiek.grouptp.helper.ItemBuilder;
import pl.natusiek.grouptp.helper.MessageHelper;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class PlayerHubListener implements Listener {

    private final GameArenaManager arenaManager;

    private final static ItemStack leave = new ItemBuilder(Material.WOOD_BUTTON).withName("&cWYJSCIE").build();
    private final static ItemStack kitsOpenItem = new ItemBuilder(Material.BOOK).withName("&6WYBOR ZESTAWU").build();
    private final static ItemStack leaveWithServer = new ItemBuilder(Material.WOOD_BUTTON).withName("&cWYJSCIE").build();
    private final static ItemStack spectatorsOpenGui = new ItemBuilder(Material.PAPER).withName("&aOBSERWUJ GRACZY").build();

    public PlayerHubListener(GameArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        giveMenuItem(player);
        player.setFireTicks(0);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setFallDistance(0);
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(Bukkit.getWorld("World").getSpawnLocation());
        BorderHelper.setBorder(player, player.getLocation(), 1000000);
    }

    @EventHandler
    public void leaveServer(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();

        if (item == null) return;
        if (leave.isSimilar(item)) {
            MessageHelper.TeleportPlayerToServer(player, MessagesConfig.BUNGEE$SERVER);
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();

        if (item == null)  return;
        if (kitsOpenItem.isSimilar(item)) {
            KitInventoryProvider.INVENTORY.open(player);
        }
    }

    @EventHandler
    public void WeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }

    public static void giveMenuItem(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setItem(0, kitsOpenItem);
        player.getInventory().setItem(4, spectatorsOpenGui);
        player.getInventory().setItem(8, leaveWithServer);
    }

}

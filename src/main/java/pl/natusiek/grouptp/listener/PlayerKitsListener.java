package pl.natusiek.grouptp.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.natusiek.grouptp.basic.gui.kitInventory;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.helper.BorderHelper;
import pl.natusiek.grouptp.helper.ItemBuilder;
import pl.natusiek.grouptp.kit.Kit;
import pl.natusiek.grouptp.kit.KitManager;

import java.util.Objects;
import java.util.function.Consumer;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class PlayerKitsListener implements Listener {

	private final KitManager kitManager;

	private final static ItemStack kitsOpenItem = new ItemBuilder(Material.BOOK).withName("&6WYBOR ZESTAWU").build();
	private final static ItemStack leaveWithServer = new ItemBuilder(Material.WOOD_BUTTON).withName("&cWYJSCIE").build();
	private final static ItemStack spectatorsOpenGui = new ItemBuilder(Material.COMPASS).withName("&aOBSERWUJ GRACZY").build();

	public PlayerKitsListener(KitManager kitManager) {
		this.kitManager = kitManager;
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
		player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
		BorderHelper.setBorder(player, player.getLocation(), 1000000);

	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final ItemStack item = event.getItem();

		if (item == null)  return;
		if (kitsOpenItem.isSimilar(item)) {
			kitInventory.INVENTORY_KIT.open(player);
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		final Inventory inventory = event.getInventory();
		final Player player = (Player) event.getWhoClicked();

		if (!(event.getWhoClicked() instanceof Player)) return;
		if (event.getInventory().getTitle().equalsIgnoreCase(kitInventory.INVENTORY_KIT.getTitle())) {
			event.setCancelled(true);
		}
		if (inventory == null || !inventory.getTitle().equalsIgnoreCase(kitInventory.INVENTORY_KIT.getTitle())) {
			return;
		}
		event.setCancelled(true);

		if(event.getCurrentItem().getType() == Material.HOPPER) {
			PlayerKitsListener.giveMenuItem(player);
			this.kitManager.setCurrentKit(player.getUniqueId(), null);
			event.setCancelled(true);
		}

		final ItemStack clickedIcon = event.getCurrentItem();
		if (clickedIcon == null) return;
		
		final Kit clickedKit = this.kitManager.findByIcon(clickedIcon);
		if (clickedKit == null) return;

		final Kit currentKit = this.kitManager.getCurrentKit(player.getUniqueId());
		if (currentKit != null && clickedKit.getName().equals(currentKit.getName())) {
			player.sendMessage(colored(MessagesConfig.KIT$USE));
			return;
		}
		this.giveKit(player, clickedKit);
		this.kitManager.setCurrentKit(player.getUniqueId(), clickedKit);
		player.sendMessage(colored(MessagesConfig.KIT$TAKE.replace("{KIT}", clickedKit.getName())));
	}
	
	public static void giveMenuItem(Player player) {
		clearInventory(player);
		player.getInventory().setItem(0, kitsOpenItem);
		player.getInventory().setItem(4, spectatorsOpenGui);
		player.getInventory().setItem(8, leaveWithServer);
	}

	private void giveKit(Player player, Kit kit) {
		clearInventory(player);
		this.kitManager.fillInventoryByKit(player.getInventory(), kit);
	}
	
	private static void clearInventory(Player player) {
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
	}
}

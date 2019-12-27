package pl.natusiek.grouptp.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class PlayerHelper {

	public static boolean addItem(Inventory inventory, ItemStack itemStack) {
		if(canPickup(inventory, itemStack)) {
			inventory.addItem(itemStack);
			return true;
		}
		return false;
	}
	
	public static boolean canPickup(Inventory inventory, ItemStack itemStack) {
		return inventory.firstEmpty() != -1 ||
			StreamSupport.stream(inventory.spliterator(), false)
				.filter(itemStack::isSimilar)
				.anyMatch(pickupedItemStack -> pickupedItemStack.getAmount() + itemStack.getAmount() < pickupedItemStack.getMaxStackSize());
		}
	
	public static void addItemOrDrop(Player player, ItemStack itemStack) {
		if (!addItem(player.getInventory(), itemStack)) {
			player.getLocation().getBlock().getWorld().dropItemNaturally(player.getLocation(), itemStack);
		}
	}
	
	public static void addItemsOrDrop(Player player, ItemStack... itemStacks) {
		Arrays.stream(itemStacks).forEach(itemStack -> addItemOrDrop(player, itemStack));
	}
	
	public static void addItemsOrDrop(Player player, List<ItemStack> items) {
		items.forEach(item -> addItemOrDrop(player, item));
	}

	public static List<UUID> getPlayer(List<UUID> uuid) { return uuid; }

	public static List<Player> findPlayersInRadius(Player base, int radius, int limit) {
    /*return Bukkit.getOnlinePlayers().stream()
        .filter(player -> player.getLocation().distance(base.getLocation()) <= radius
            && !player.getUniqueId().equals(base.getUniqueId()))
        .collect(Collectors.toList());*/
    final List<Player> players = new ArrayList<>(limit);
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (player.getLocation().distance(base.getLocation()) <= radius
          && !player.getUniqueId().equals(base.getUniqueId())) {
        players.add(player);
        if (players.size() >= limit) {
          break;
        }
      }
    }
    return players;
  }

}

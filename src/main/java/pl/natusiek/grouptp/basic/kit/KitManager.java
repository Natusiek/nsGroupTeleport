package pl.natusiek.grouptp.basic.kit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public interface KitManager {

	Kit findByIcon(ItemStack icon);

	Kit findByName(String name);

	List<Kit> getKits();

	void addKit(Kit kit);

	void giveKit(Player player, Kit kit);

	void fillInventoryByKit(Inventory inventory, Kit kit);

	Kit getCurrentKit(UUID uuid);

	void setCurrentKit(UUID uuid, Kit kit);

}
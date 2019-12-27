package pl.natusiek.grouptp.kit;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public interface KitManager {

	Kit findByIcon(ItemStack icon);

	Kit findByName(String name);

	List<Kit> getKits();

	void addKit(Kit kit);

	void fillInventoryByKit(Inventory inventory, Kit kit);

	Kit getCurrentKit(UUID uuid);

	void setCurrentKit(UUID uuid, Kit kit);
}
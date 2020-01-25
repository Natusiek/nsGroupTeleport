package pl.natusiek.grouptp.game.kit;

import java.util.List;
import java.util.UUID;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface KitManager {

    Kit findByIcon(ItemStack icon);

    Kit findByName(String name);

    List<Kit> getKits();

    Kit addKit(Kit kit);

    void deleteKit(Kit kit);

    void fillInventoryByKit(Inventory inventory, Kit kit);

    Kit getCurrentKit(UUID uuid);

    void setCurrentKit(UUID uuid, Kit kit);

}

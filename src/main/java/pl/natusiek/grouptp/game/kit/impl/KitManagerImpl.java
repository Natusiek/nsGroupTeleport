package pl.natusiek.grouptp.game.kit.impl;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pl.natusiek.grouptp.game.kit.Kit;
import pl.natusiek.grouptp.game.kit.KitManager;

import java.util.*;

public class KitManagerImpl implements KitManager {

    private final Set<Kit> kits = new HashSet<>();
    private final Map<UUID, Kit> currentKits = new HashMap<>();

    @Override
    public Kit findByIcon(ItemStack icon) {
        return this.kits.stream()
                .filter(kit -> kit.getIcon().isSimilar(icon))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Kit findByName(String name) {
        return this.kits.stream()
                .filter(kit -> kit.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addKit(Kit kit) {
        this.kits.add(kit);
    }

    @Override
    public void removeKit(Kit kit) {
        this.kits.remove(kit);
    }

    @Override
    public List<Kit> getKits() { return new ArrayList<>(kits); }

    @Override
    public void fillInventoryByKit(Inventory inventory, Kit kit) {
        if (inventory instanceof PlayerInventory) {
            inventory.clear();
            inventory.setContents(kit.getContent());
            ((PlayerInventory) inventory).setArmorContents(new ItemStack[4]);
            ((PlayerInventory) inventory).setArmorContents(kit.getArmorContent());
        }
    }

    @Override
    public void setCurrentKit(UUID uuid, Kit kit) { this.currentKits.put(uuid, kit); }

    @Override
    public Kit getCurrentKit(UUID uuid) { return this.currentKits.get(uuid); }

}

package pl.natusiek.grouptp.kit.impl;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pl.natusiek.grouptp.kit.Kit;
import pl.natusiek.grouptp.kit.KitManager;

import java.util.*;
import java.util.stream.Stream;

public class KitManagerImpl  implements KitManager {

    private final List<Kit> kits = new ArrayList<>();
    private final Map<UUID, Kit> currentKits = new HashMap<>();

    @Override
    public Kit findByName(String name) {
        return this.kits.stream()
                .filter(kit -> kit.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Kit findByIcon(ItemStack icon) {
        return this.kits.stream()
                .filter(kit -> kit.getIcon().isSimilar(icon))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addKit(Kit kit) {
        this.kits.add(kit);
    }

    @Override
    public List<Kit> getKits() {
        return new ArrayList<>(kits);
    }

    @Override
    public void fillInventoryByKit(Inventory inventory, Kit kit) {
        kit.getContent().forEach(inventory::addItem);
        if (inventory instanceof PlayerInventory) {
            ((PlayerInventory) inventory).setArmorContents(kit.getArmorContent());
        }
    }

    @Override
    public void setCurrentKit(UUID uuid, Kit kit) {
        this.currentKits.put(uuid, kit);
    }

    @Override
    public Kit getCurrentKit(UUID uuid) {
        return this.currentKits.get(uuid);
    }
}
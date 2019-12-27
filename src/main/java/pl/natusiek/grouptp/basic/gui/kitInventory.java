package pl.natusiek.grouptp.basic.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.helper.ItemBuilder;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class kitInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY_KIT = SmartInventory.builder()
            .id("allKit")
            .provider(new kitInventory())
            .size(5, 9)
            .title(colored("&6Lista kitow &8- &fWybierz swoj kit."))
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE)));

        GroupTeleportPlugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);
        plugin.getKitManager().getKits()
                .forEach(kit -> {
                    contents.set(kit.getRows(), kit.getColumn(), ClickableItem.empty(kit.getIcon()));
                    contents.set(3, 4, ClickableItem.empty(new ItemBuilder(Material.HOPPER).withName("&7Zresetuj swoj kit.").withLore("").build()));
                });

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}

package pl.natusiek.grouptp.basic.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.basic.kit.KitManager;
import pl.natusiek.grouptp.helper.ItemBuilder;
import pl.natusiek.grouptp.listener.PlayerHubListener;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class KitInventoryProvider implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("kit")
            .provider(new KitInventoryProvider())
            .size(4, 9)
            .title(colored("&fWybierz swoj kit."))
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE)));

        final GroupTeleportPlugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);
        plugin.getKitManager().getKits().forEach(kit ->
                contents.set(kit.getRows(), kit.getColumn(), ClickableItem.empty(kit.getIcon())));

        final ItemStack hopper = new ItemBuilder(Material.HOPPER).withName("&7Zresetuj swoj kit.").build();
        contents.set(2, 4, ClickableItem.of(new ItemStack(hopper), e -> {
            PlayerHubListener.giveMenuItem(player);
            plugin.getKitManager().setCurrentKit(player.getUniqueId(), null);
            player.sendMessage(colored("&7Zresetowales swoj zestaw."));
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) { }

}

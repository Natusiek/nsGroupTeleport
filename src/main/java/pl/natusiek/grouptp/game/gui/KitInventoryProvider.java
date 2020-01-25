package pl.natusiek.grouptp.game.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.game.kit.Kit;
import pl.natusiek.grouptp.helper.ItemBuilder;
import pl.natusiek.grouptp.helper.PlayerHelper;

import java.io.File;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class KitInventoryProvider implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("kit")
            .provider(new KitInventoryProvider())
            .size(4, 9)
            .title(colored("&7Wybierz swoj kit."))
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE)));

        final GroupTeleportPlugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);
        plugin.getKitManager().getKits().forEach(kit -> {
            contents.set(kit.getRows(), kit.getColumn(), ClickableItem.of(new ItemStack(kit.getIcon()), event -> {
                final ItemStack clickedIcon = event.getCurrentItem();
                if(clickedIcon == null) return;

                final Kit clickedKit = plugin.getKitManager().findByIcon(clickedIcon);
                if (clickedKit == null) return;

                final Kit currentKit = plugin.getKitManager().getCurrentKit(player.getUniqueId());
                if (currentKit != null && clickedKit.getName().equals(clickedKit.getName())) {
                    player.sendMessage(colored(MessagesConfig.KIT$USE));
                    return;
                }
                plugin.getKitManager().fillInventoryByKit(player.getInventory(), clickedKit);
                plugin.getKitManager().setCurrentKit(player.getUniqueId(), clickedKit);
                player.sendMessage(colored(MessagesConfig.KIT$TAKE.replace("{KIT}", clickedKit.getName())));
            }));
        });
        final ItemStack hopper = new ItemBuilder(Material.HOPPER).withName("&7Zresetuj swoj kit.").build();
        contents.set(2, 4, ClickableItem.of(new ItemStack(hopper), event -> {
            plugin.getKitManager().setCurrentKit(player.getUniqueId(), null);
            PlayerHelper.addItemFromLobby(player);
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}

package pl.natusiek.grouptp.basic.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.basic.kit.Kit;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.helper.ItemBuilder;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class KitInventoryProvider implements InventoryProvider {


    private static GroupTeleportPlugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("kit")
            .provider(new KitInventoryProvider())
            .size(plugin.getConfigManager().getRowsKit(), plugin.getConfigManager().getColumnKit())
            .title(colored(plugin.getConfigManager().getNameKit()))
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE)));

        final GroupTeleportPlugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);
        plugin.getKitManager().getKits().forEach(kit ->
                contents.set(kit.getRows(), kit.getColumn(), ClickableItem.of(new ItemStack(kit.getIcon()), event -> {
                    final ItemStack clickedIcon = event.getCurrentItem();
                    if (clickedIcon == null) return;

                    final Kit clickedKit = plugin.getKitManager().findByIcon(clickedIcon);
                    if (clickedKit == null) return;

                    final Kit currentKit = plugin.getKitManager().getCurrentKit(player.getUniqueId());
                    if (currentKit != null && clickedKit.getName().equals(currentKit.getName())) {
                        player.sendMessage(colored(MessagesConfig.KIT$USE));
                        return;
                    }
                    plugin.getKitManager().giveKit(player, clickedKit);
                    plugin.getKitManager().setCurrentKit(player.getUniqueId(), clickedKit);
                    player.sendMessage(colored(MessagesConfig.KIT$TAKE.replace("{KIT}", clickedKit.getName())));
                })));

        final ItemStack hopper = new ItemBuilder(Material.HOPPER).withName("&7Zresetuj swoj kit.").build();
        contents.set(2, 4, ClickableItem.of(new ItemStack(hopper), e -> {
            addItemFromLobby(player);
            plugin.getKitManager().setCurrentKit(player.getUniqueId(), null);
            player.sendMessage(colored("&7Zresetowales swoj zestaw."));
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) { }

    private void addItemFromLobby(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setItem(0, plugin.getConfigManager().getItemKits());
        player.getInventory().setItem(3, plugin.getConfigManager().getItemSpectator());
        player.getInventory().setItem(8, plugin.getConfigManager().getItemLeaveServer());
    }
}

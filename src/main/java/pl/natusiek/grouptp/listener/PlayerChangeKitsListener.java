package pl.natusiek.grouptp.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pl.natusiek.grouptp.basic.gui.KitInventoryProvider;
import pl.natusiek.grouptp.basic.kit.Kit;
import pl.natusiek.grouptp.basic.kit.KitManager;
import pl.natusiek.grouptp.config.MessagesConfig;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class PlayerChangeKitsListener implements Listener {

    private final KitManager kitManager;

    public PlayerChangeKitsListener(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();
        final Player player = (Player) event.getWhoClicked();

        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getInventory().getTitle().equalsIgnoreCase(KitInventoryProvider.INVENTORY.getTitle())) {
            event.setCancelled(true);
        }
        if (inventory == null || !inventory.getTitle().equalsIgnoreCase(KitInventoryProvider.INVENTORY.getTitle())) {
            return;
        }
        event.setCancelled(true);

        final ItemStack clickedIcon = event.getCurrentItem();
        if (clickedIcon == null) return;

        final Kit clickedKit = this.kitManager.findByIcon(clickedIcon);
        if (clickedKit == null) return;

        final Kit currentKit = this.kitManager.getCurrentKit(player.getUniqueId());
        if (currentKit != null && clickedKit.getName().equals(currentKit.getName())) {
            player.sendMessage(colored(MessagesConfig.KIT$USE));
            return;
        }
        this.kitManager.giveKit(player, clickedKit);
        this.kitManager.setCurrentKit(player.getUniqueId(), clickedKit);
        player.sendMessage(colored(MessagesConfig.KIT$TAKE.replace("{KIT}", clickedKit.getName())));
    }

}
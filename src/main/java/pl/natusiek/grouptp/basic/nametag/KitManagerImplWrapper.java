package pl.natusiek.grouptp.basic.nametag;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pl.natusiek.grouptp.basic.kit.Kit;
import pl.natusiek.grouptp.basic.kit.impl.KitManagerImpl;

public class KitManagerImplWrapper extends KitManagerImpl {

    @Override
    public void fillInventoryByKit(Inventory inventory, Kit kit) {
        super.fillInventoryByKit(inventory, kit);
        if (inventory.getHolder() != null && inventory.getHolder() instanceof Player) {
            final Player player = ((Player) inventory.getHolder());
            //NametagEdit.getApi().setPrefix(player, kit.getName().toUpperCase() + " &f");
        }
    }
}

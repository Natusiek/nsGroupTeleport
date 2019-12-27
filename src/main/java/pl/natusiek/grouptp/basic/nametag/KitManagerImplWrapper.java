package pl.natusiek.grouptp.basic.nametag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import pl.natusiek.grouptp.kit.Kit;
import pl.natusiek.grouptp.kit.impl.KitManagerImpl;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class KitManagerImplWrapper extends KitManagerImpl {

    /* @Override
    public void fillInventoryByKit(Inventory inventory, Kit kit) {
        super.fillInventoryByKit(inventory, kit);
        if (inventory.getHolder() != null && inventory.getHolder() instanceof Player) {
            final Player player = ((Player) inventory.getHolder());
            NametagEdit.getApi().setPrefix(player, kit.getName().toUpperCase() + " &f");
        }
    } */
}
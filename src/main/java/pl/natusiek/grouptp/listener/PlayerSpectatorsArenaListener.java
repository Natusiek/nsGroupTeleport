package pl.natusiek.grouptp.listener;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.natusiek.grouptp.arena.GameSpectate;
import pl.natusiek.grouptp.basic.gui.allGameInventory;
import pl.natusiek.grouptp.helper.ItemBuilder;

public class PlayerSpectatorsArenaListener implements Listener {

    private GameSpectate gameSpectate;

    private final static ItemStack spectatorsOpenGui = new ItemBuilder(Material.COMPASS).withName("&aOBSERWUJ GRACZY").build();
    private static final ItemStack leaveItem = new ItemBuilder(Material.NAME_TAG).withName("&4Opusc").addEnchantment(Enchantment.DURABILITY, 10).build();

    public PlayerSpectatorsArenaListener(GameSpectate gameSpectate) {
        this.gameSpectate = gameSpectate;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();

        if(item == null) return;
        if(spectatorsOpenGui.isSimilar(item)) {
            allGameInventory.INVENTORY.open(player);
        }
        if(leaveItem.isSimilar(item)) {
            gameSpectate.leaveSpectate(player);
        }
    }


}

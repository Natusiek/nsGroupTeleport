package pl.natusiek.grouptp.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.helper.ItemBuilder;
import pl.natusiek.grouptp.helper.MessageHelper;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class PlayerLeaveServerListener implements Listener {

    private final static ItemStack leave = new ItemBuilder(Material.WOOD_BUTTON).withName("&cWYJSCIE").build();

    @EventHandler
    public void OpenLeave(PlayerInteractEvent event) {
        final Plugin plugin = GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class);
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();

        if (item == null) return;
        if (leave.isSimilar(item)) {
            player.sendMessage(colored(MessagesConfig.BUNGEE$MESSAGE_LEAVE));
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    MessageHelper.TeleportPlayerToServer(player, MessagesConfig.BUNGEE$SERVER);
                }
            }, 10L);
        }
    }

}

package pl.natusiek.grouptp.listener;

import net.minecraft.server.v1_8_R3.WorldBorder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.game.arena.Arena;
import pl.natusiek.grouptp.game.arena.ArenaManager;


import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class DuringGamePlayerListener implements Listener {

    private final ArenaManager arenaManager;

    public DuringGamePlayerListener(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    @EventHandler
    public void onCraftJukeBox(CraftItemEvent event) {
        final Recipe recipe = event.getRecipe();
        if (recipe == null)
            return;

        final ItemStack result = recipe.getResult();
        if (result == null)
            return;

        if (result.getType() == Material.JUKEBOX) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            if (event.getWhoClicked() instanceof Player) {
                final Player player = ((Player) event.getWhoClicked());
                player.sendMessage(colored("&4Nie mozesz craftowac jukeboxa!"));
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Arena arena = this.arenaManager.findArenaByPlayer(player.getUniqueId());

        final Block block = event.getBlock();
        if (block.getLocation().getBlockY() > MessagesConfig.BUILD$MAX_Y) {
            if (!MessagesConfig.BUILD$ABOVE_CLOUDS || arena == null) {
                event.setCancelled(true);
                return;
            }
            event.setCancelled(true);
            player.sendMessage(colored(MessagesConfig.BUILD$ABOVE_CLOUD));
        }
    }

    @EventHandler
    public void onTeleportOutOfBorder(PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        final Arena arena = this.arenaManager.findArenaByPlayer(player.getUniqueId());

        if (arena == null || event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;

        WorldBorder border = arena.getBorder(player.getUniqueId());
        if (border == null)
            border = arena.setBorder(player.getUniqueId(), arena.getCenter(), arena.getSize());

        final Location location = event.getTo();
        double size = border.getSize() / 2;
        double x = location.getX() - border.getCenterX(), z = location.getZ() - border.getCenterZ();
        if ((x > size || (-x) > size) || (z > size || (-z) > size)) {
            event.setCancelled(true);
            player.sendMessage(colored(MessagesConfig.ARENA$CANNOT_USE_PEARL_OUTSIDE_BORDER));
            player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
        }
    }

}
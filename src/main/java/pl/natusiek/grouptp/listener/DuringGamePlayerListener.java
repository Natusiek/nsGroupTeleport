package pl.natusiek.grouptp.listener;

import java.text.DecimalFormat;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import pl.natusiek.grouptp.basic.arena.GameArena;
import pl.natusiek.grouptp.basic.arena.GameArenaManager;
import pl.natusiek.grouptp.basic.spectate.GameSpectate;
import pl.natusiek.grouptp.config.MessagesConfig;

import net.minecraft.server.v1_8_R3.WorldBorder;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class DuringGamePlayerListener implements Listener {

    private final GameArenaManager arenaManager;
    private final DecimalFormat decimalFormat;

    public DuringGamePlayerListener(GameArenaManager arenaManager) {
        this.arenaManager = arenaManager;
        this.decimalFormat = new DecimalFormat("##.#");
    }

    @EventHandler
    public void onShootBow(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Projectile) {

            final Player player = ((Player) event.getEntity());
            if (player instanceof Player) {

                final Projectile projectile = ((Projectile) event.getDamager());
                if (projectile instanceof Arrow) {

                    final Player shooter = ((Player) projectile.getShooter());
                    if (shooter instanceof Player) {
                        shooter.sendMessage(colored(MessagesConfig.ARENA$HP_OPPONENT
                        .replace("{OPPONENT}", player.getName())
                        .replace("{HP}", this.decimalFormat.format(player.getHealth() / 2))));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCraftJukeBox(CraftItemEvent event) {
        final Recipe recipe = event.getRecipe();
        if (recipe == null) return;

        final ItemStack result = recipe.getResult();
        if (result == null) return;

        if (result.getType() == Material.JUKEBOX) {
            event.setCancelled(true);
            event.setResult(Result.DENY);
            if (event.getWhoClicked() instanceof Player) {
                final Player player = ((Player) event.getWhoClicked());
                player.sendMessage(colored("&4Nie mozesz craftowac jukeboxa!"));
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final GameArena gameArena = this.arenaManager.findArenaByPlayer(player.getUniqueId());

        final Block block = event.getBlock();
        if (block.getLocation().getBlockY() > MessagesConfig.BUILD$MAX_Y) {
            if (!MessagesConfig.BUILD$ABOVE_CLOUDS || gameArena == null) {
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
        final GameArena gameArena = this.arenaManager.findArenaByPlayer(player.getUniqueId());

        if (gameArena == null || event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            return;
        }

        WorldBorder border = gameArena.getBorder(player.getUniqueId());
        if (border == null) {
            border = gameArena.setBorder(player.getUniqueId(), gameArena.getCenter(), gameArena.getSize());
        }
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

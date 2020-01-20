package pl.natusiek.grouptp.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import net.minecraft.server.v1_8_R3.WorldBorder;

import pl.natusiek.grouptp.basic.arena.GameArena;
import pl.natusiek.grouptp.basic.arena.GameArenaManager;
import pl.natusiek.grouptp.basic.kit.Kit;
import pl.natusiek.grouptp.basic.spectate.GameSpectate;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.helper.ItemBuilder;
import pl.natusiek.grouptp.helper.MessageHelper;
import pl.natusiek.grouptp.helper.PlayerHelper;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class PlayerGameArenaListener implements Listener {

	private final DecimalFormat decimalFormat;
	private final GameSpectate gameSpectate;
	private final GameArenaManager arenaManager;

	public PlayerGameArenaListener(GameArenaManager arenaManager, GameSpectate gameSpectate) {
		this.gameSpectate = gameSpectate;
		this.arenaManager = arenaManager;
		this.decimalFormat = new DecimalFormat("##.#");
	}

	@EventHandler
	public void onCraftJukeBox(CraftItemEvent event) {
		final Recipe recipe = event.getRecipe();
		final ItemStack result = recipe.getResult();
		if (recipe == null) return;
		if (result == null) return;
		if (result.getType() == Material.JUKEBOX) {
			event.setCancelled(true);
			event.setResult(Result.DENY);
			if (event.getWhoClicked() instanceof Player) {
				final Player player = ((Player) event.getWhoClicked());
				player.sendMessage(colored("Nie mozesz craftowac jukeboxa!"));
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onShootBow(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Projectile) {
			final Projectile projectile = ((Projectile) event.getDamager());

			if (event.getEntity() instanceof Player) {
				final Player player = ((Player) event.getEntity());

				if (projectile instanceof Arrow) {
					if (projectile.getShooter() instanceof Player) {
						//final Object object = projectile;
						final Player players = (Player) projectile.getShooter();

						players.sendMessage(colored(MessagesConfig.ARENA$HP_OPPONENT
								.replace("{OPPONENT}", player.getName())
								.replace("{HP}", this.decimalFormat.format(player.getHealth() / 2))));

					}
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlace(BlockPlaceEvent event) {
		final Player player = event.getPlayer();
		final Block block = event.getBlock();
		final GameArena arena = this.arenaManager.findArenaByPlayer(player.getUniqueId());
		if (block.getLocation().getBlockY() > MessagesConfig.BUILD$MAX_Y) {
			if (!MessagesConfig.BUILD$ABOVE_CLOUDS || arena == null) {
				return;
			}
			player.sendMessage(colored(MessagesConfig.BUILD$ABOVE_CLOUD));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onTeleportOutOfBorder(PlayerTeleportEvent event) {
		final Player player = event.getPlayer();
		final GameArena arena = this.arenaManager.findArenaByPlayer(player.getUniqueId());
		
		if (arena == null || event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
			return;
		}
		WorldBorder border = arena.getBorder(player.getUniqueId());
		if (border == null) {
			border = arena.setBorder(player.getUniqueId(), arena.getCenter(), arena.getSize());
		}
		Location loc = event.getTo();
		double size = border.getSize() / 2;
		double x = loc.getX() - border.getCenterX(), z = loc.getZ() - border.getCenterZ();
		if ((x > size || (-x) > size) || (z > size || (-z) > size)) {
			event.setCancelled(true);
			player.sendMessage(colored(MessagesConfig.ARENA$CANNOT_USE_PEARL_OUTSIDE_BORDER));
			player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
		}
	}

}

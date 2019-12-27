package pl.natusiek.grouptp.listener;

import net.minecraft.server.v1_8_R3.WorldBorder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.arena.GameArena;
import pl.natusiek.grouptp.arena.GameArenaManager;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.helper.ItemBuilder;

import java.text.DecimalFormat;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class PlayerGameArenaListener implements Listener {

	private final DecimalFormat decimalFormat;
	private final GameArenaManager arenaManager;

	private final static ItemStack apple = new ItemBuilder(Material.GOLDEN_APPLE).withName("&e&lZmutowane Jablko").build();

	public PlayerGameArenaListener(GameArenaManager arenaManager) {
		this.arenaManager = arenaManager;
		this.decimalFormat = new DecimalFormat("##.#");
	}

	@EventHandler
	public void WeatherChange(WeatherChangeEvent event) {
		if (event.toWeatherState()) {
			event.setCancelled(true);
		}
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

	@EventHandler
	public void onShootBow(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Projectile && event.getEntity() instanceof Player) {
			final ProjectileSource shooter = ((Arrow) event.getDamager()).getShooter();
			if (shooter instanceof Player) {
				final Player attacker = (Player) shooter;
				final Player victim = (Player) event.getEntity();
				attacker.sendMessage(colored(MessagesConfig.ARENA$HP_OPPONENT
						.replace("{OPPONENT}", victim.getName())
						.replace("{HP}", this.decimalFormat.format(victim.getHealth() / 2))));
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlace(BlockPlaceEvent event) {
		final Player player = event.getPlayer();
		final Block block = event.getBlock();
		final GameArena arena = this.arenaManager.findArenaByPlayer(player.getUniqueId());
		if (!player.hasPermission("nsGroupTeleport.build")) {
			if (arena == null) {
				player.sendMessage(colored(MessagesConfig.BUILD$IN_SPAWN));
				event.setCancelled(true);
				return;
			}
		}
		if (block.getLocation().getBlockY() > MessagesConfig.BUILD$MAX_Y) {
			if (!MessagesConfig.BUILD$ABOVE_CLOUDS || arena == null) {
				return;
			}
			player.sendMessage(colored(MessagesConfig.BUILD$ABOVE_CLOUD));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEat(PlayerItemConsumeEvent event) {
		final Player player = event.getPlayer();
		final ItemStack item = event.getItem();
		if (item == null) { return; }
		if (apple.isSimilar(item)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1));
			player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 7200, 3));
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 3));
			player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2700, 1));
			player.getPlayer().getInventory().removeItem(new ItemStack[]{ this.apple });
			event.setCancelled(true);
		}
	}


	@EventHandler
	public void onTeleportOutOfBorder(final PlayerTeleportEvent event) {
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

package pl.natusiek.grouptp.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.material.Button;
import org.bukkit.scheduler.BukkitRunnable;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.arena.GameArena;
import pl.natusiek.grouptp.arena.GameArena.ArenaStates;
import pl.natusiek.grouptp.arena.GameArenaManager;
import pl.natusiek.grouptp.arena.impl.GameArenaImpl;
import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.helper.MessageHelper;
import pl.natusiek.grouptp.helper.PlayerHelper;
import pl.natusiek.grouptp.kit.Kit;
import pl.natusiek.grouptp.kit.KitManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class PlayerChangeArenaListener implements Listener {

	private final GameArenaManager arenaManager;
	private final Material button, base;
	private final KitManager kitManager;
	
	private final Map<UUID, Long> cooldown = new HashMap<>();

	public PlayerChangeArenaListener(GameArenaManager arenaManager, KitManager kitManager, Material button, Material base) {
		this.arenaManager = arenaManager;
		this.kitManager = kitManager;
		this.button = button;
		this.base = base;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (event.getClickedBlock() == null) return;

		final Block clicked = event.getClickedBlock();
		if (clicked.getType() == this.button) {
			final Button btn = (Button) clicked.getState().getData();
			final Block base = clicked.getRelative(btn.getAttachedFace());

			if (base.getType() != this.base) return;

			final Player player = event.getPlayer();
			if (this.cooldown.containsKey(player.getUniqueId())
					&& this.cooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
				player.sendMessage(colored(MessagesConfig.ARENA$CANNOT_JOIN$COOLDOWN));
				return;
			}
			this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + (MessagesConfig.ARENA$SEARCHING$COOLDOWN * 1000));
			final Kit kit = this.kitManager.getCurrentKit(player.getUniqueId());
			if (kit == null) {
				player.sendMessage(colored(MessagesConfig.KIT$DONT_USE));
				return;
			}

			final GameArena availableArena = this.arenaManager.getArenas()
					.stream()
					.filter(arena -> arena.getState() == ArenaStates.AVAILABLE)
					.findFirst()
					.orElse(null);
			if (availableArena == null) {
				player.sendMessage(colored(MessagesConfig.ARENA$CANNOT_JOIN$NO_AVAILABLE_ARENAS));
				return;
			}

			final List<Player> inRadius = PlayerHelper.findPlayersInRadius(player,
					MessagesConfig.ARENA$SEARCHING$RADIUS, MessagesConfig.ARENA$SEARCHING$MAX_PLAYERS)
					.stream()
					.filter(playerInRadius -> {
						final Kit playerInRadiusKit = this.kitManager.getCurrentKit(playerInRadius.getUniqueId());
						return playerInRadiusKit != null && kit.getName().equals(playerInRadiusKit.getName());
					}).collect(Collectors.toList());
			if (inRadius.size() < MessagesConfig.ARENA$SEARCHING$MIN_PLAYERS_IN_RADIUS) {
				player.sendMessage(colored(MessagesConfig.ARENA$CANNOT_JOIN$NO_PLAYERS_IN_RADIUS));
				return;
			}
			inRadius.add(player);
			inRadius.stream().map(Player::getUniqueId).forEach(availableArena::addPlayer);
			availableArena.start();
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		final Player player = event.getPlayer();
		final GameArena arena = this.arenaManager.findArenaByPlayer(player.getUniqueId());
		
		if (arena == null) {
			event.setCancelled(true);
			player.sendMessage(colored(MessagesConfig.ARENA$NO_DROP));
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) { 
		final Player player = event.getPlayer();
		this.cooldown.remove(player.getUniqueId());
	}
}

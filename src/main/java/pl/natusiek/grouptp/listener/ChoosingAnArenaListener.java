package pl.natusiek.grouptp.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;

import pl.natusiek.grouptp.config.MessagesConfig;
import pl.natusiek.grouptp.game.arena.Arena;
import pl.natusiek.grouptp.game.arena.ArenaManager;
import pl.natusiek.grouptp.game.gui.ArenaInfoInventoryProvider;
import pl.natusiek.grouptp.game.gui.KitInventoryProvider;
import pl.natusiek.grouptp.game.kit.Kit;
import pl.natusiek.grouptp.game.kit.KitManager;
import pl.natusiek.grouptp.helper.BorderHelper;
import pl.natusiek.grouptp.helper.ItemBuilder;
import pl.natusiek.grouptp.helper.LocationHelper;
import pl.natusiek.grouptp.helper.PlayerHelper;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class ChoosingAnArenaListener implements Listener {

    private final KitManager kitManager;
    private final ArenaManager arenaManager;

    private final Map<UUID, Long> cooldown = new HashMap<>();

    private final static ItemStack leaveServer = new ItemBuilder(Material.FENCE_GATE).withName("&8* &cWyjscie z serwer'a &8*").build();
    private final static ItemStack kitsOpenItem = new ItemBuilder(Material.BOOK).withName("&8* &6Wybor zestaw'u &8*").build();

    public ChoosingAnArenaListener(KitManager kitManager, ArenaManager arenaManager) {
        this.kitManager = kitManager;
        this.arenaManager = arenaManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        PlayerHelper.addItemFromLobby(player);
        this.kitManager.setCurrentKit(player.getUniqueId(), null);
        BorderHelper.setBorder(player, player.getLocation(), 1000000);
    }

    @EventHandler
    public void leaveServer(PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (item == null) return;

        final Player player = event.getPlayer();
        if (this.leaveServer.isSimilar(item)) {
            PlayerHelper.TeleportPlayerToServer(player, MessagesConfig.BUNGEE$SERVER);
        } else if (this.kitsOpenItem.isSimilar(item)) {
            KitInventoryProvider.INVENTORY.open(player);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        final Block clicked = event.getClickedBlock();
        if (clicked.getType() == Material.WOOD_BUTTON) {
            final Button btn = (Button) clicked.getState().getData();
            final Block base = clicked.getRelative(btn.getAttachedFace());

            if (base.getType() != Material.JUKEBOX) return;

            final Player player = event.getPlayer();
            if (this.cooldown.containsKey(player.getUniqueId())
                    && this.cooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
                player.sendMessage(colored(MessagesConfig.ARENA$CANNOT_JOIN$COOLDOWN));
                return;
            }
            this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + (MessagesConfig.ARENA$SEARCHING$COOLDOWN * 1000));
            final Kit kit = this.kitManager.getCurrentKit(player.getUniqueId());
            if (kit == null) {
                player.sendMessage(colored(MessagesConfig.KIT$DONT_HAVE_KIT));
                return;
            }

            final Arena availableArena = this.arenaManager.getArenas()
                    .stream()
                    .filter(arena -> arena.getState() == Arena.ArenaStates.AVAILABLE)
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

}

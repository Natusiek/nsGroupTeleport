package pl.natusiek.grouptp.arena.impl;

import net.minecraft.server.v1_8_R3.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.natusiek.grouptp.arena.GameArena;
import pl.natusiek.grouptp.arena.GameArenaManager;
import pl.natusiek.grouptp.arena.GameSpectate;
import pl.natusiek.grouptp.helper.BorderHelper;
import pl.natusiek.grouptp.helper.ItemBuilder;
import pl.natusiek.grouptp.helper.MessageHelper;
import pl.natusiek.grouptp.listener.PlayerKitsListener;

import java.util.*;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class GameSpectateImpl implements GameSpectate {

    private final GameArena gameArena;
    private final GameArenaManager gameArenaManager;
    private final List<Player> spectators = new ArrayList<>();

    private final Map<Player, GameArena> spectatorsArena = new HashMap<>();
    private final Map<Player, WorldBorder> worldBorders = new HashMap<>();

    private static final ItemStack leaveItem = new ItemBuilder(Material.NAME_TAG).withName("&4Opusc").addEnchantment(Enchantment.DURABILITY, 10).build();

    public GameSpectateImpl(GameArenaManager gameArenaManager, GameArena gameArena) {
        this.gameArena = gameArena;
        this.gameArenaManager = gameArenaManager;
    }

    @Override
    public void joinSpectate(Player player, GameArena gameArena) {
        final GameArena availableArena = gameArenaManager.getArenas()
                .stream()
                .filter(arena -> arena.getState() == GameArena.ArenaStates.IN_GAME)
                .findFirst()
                .orElse(null);
        if (gameArena == availableArena) {
            spectators.add(player);
            hidePlayer(player);
            player.teleport(gameArena.getCenter());
            player.setAllowFlight(true);
            player.setFlying(true);
            Bukkit.broadcastMessage("elo");
            MessageHelper.sendTitle(player, "", colored("&7Obserwujesz teraz arene: &6" + gameArena.getNames(gameArenaManager.getArenas())));
            player.getInventory().setArmorContents(new ItemStack[4]);
            player.getInventory().clear();
            player.getInventory().setItem(4, this.leaveItem);
            BorderHelper.setBorder(gameArena, player, gameArena.getCenter(), gameArena.getSize());
        } else {
            player.sendMessage(colored("&7Arena nie jest taka ciekawa. "));
        }
    }


    @Override
    public void leaveSpectate(Player player) {
        this.spectators.remove(player);
        showPlayer(player);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setFallDistance(0);
        player.setFireTicks(0);
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        PlayerKitsListener.giveMenuItem(player);
        BorderHelper.setBorder(player, player.getLocation(), 1000000);
    }

    @Override
    public List<Player> getPlayer() { return new ArrayList<>(spectators); }

    @Override
    public GameArena getGameArena(Player player) { return this.spectatorsArena.get(player); }

    @Override
    public GameArena setGameArena(Player player, GameArena gameArena) {
        final GameArena gameArenas = this.spectatorsArena.get(player);

        if(gameArenas == null) {
            this.spectatorsArena.put(player, gameArena);
        }
        gameArenas.getName();
        return gameArenas;
    }

    @Override
    public WorldBorder getBorder(Player player) {
        return this.worldBorders.get(player);
    }

    @Override
    public WorldBorder setBorder(Player player, Location center, int size) {
        WorldBorder border = this.worldBorders.get(player);
        if (border == null) {
            this.worldBorders.put(player, border = new WorldBorder());
        }
        border.setCenter(center.getX(), center.getZ());
        border.setSize(size);
        return border;
    }

    @Override
    public void hidePlayer(Player player) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.hidePlayer(player);
        }
    }

    @Override
    public void showPlayer(Player player) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.showPlayer(player);
        }
    }

}

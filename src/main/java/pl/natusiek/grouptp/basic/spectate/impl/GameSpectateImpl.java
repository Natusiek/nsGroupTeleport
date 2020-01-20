package pl.natusiek.grouptp.basic.spectate.impl;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.WorldBorder;

import org.bukkit.potion.PotionEffect;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.basic.arena.GameArena;
import pl.natusiek.grouptp.basic.spectate.GameSpectate;
import pl.natusiek.grouptp.helper.BorderHelper;
import pl.natusiek.grouptp.helper.ItemBuilder;
import pl.natusiek.grouptp.listener.PlayerHubListener;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class GameSpectateImpl implements GameSpectate {

    private final List<UUID> spectatorsArena = new ArrayList<>();
    private final Map<UUID, WorldBorder> worldBorders = new HashMap<>();

    @Override
    public void joinSpectate(Player player, GameArena gameArena) {
        this.spectatorsArena.add(player.getUniqueId());
        player.teleport(gameArena.getCenter());
        Bukkit.broadcastMessage("1");
        Bukkit.getScheduler().runTaskLater(GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class), () -> {
            player.setAllowFlight(true);
            player.setFlying(true);
            player.getInventory().clear();
            player.getInventory().setArmorContents(new ItemStack[4]);
            BorderHelper.setBorder(gameArena, player, gameArena.getCenter(), gameArena.getSize());
            player.getInventory().setItem(4, new ItemBuilder(Material.NAME_TAG).withName("&4Opusc").addEnchantment(Enchantment.DURABILITY, 10).build());
            player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS).withName("&8(&fPPM, aby odswiezyc&8)").build());
            Bukkit.broadcastMessage("2");
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.hidePlayer(player);
            }
        }, 20L);
    }

    @Override
    public void leaveSpectate(Player player) {
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        Bukkit.broadcastMessage("1 - leave");
        Bukkit.getScheduler().runTaskLater(GroupTeleportPlugin.getPlugin(GroupTeleportPlugin.class), () -> {
            player.setFireTicks(0);
            player.setFoodLevel(20);
            player.setHealth(20.0D);
            player.setFallDistance(0);
            player.setFlying(false);
            player.setAllowFlight(false);
            player.getInventory().clear();
            PlayerHubListener.giveMenuItem(player);
            player.getInventory().setArmorContents(new ItemStack[4]);
            BorderHelper.setBorder(player, player.getLocation(), 1000000);
            player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
            player.sendMessage(colored("&8(&4BETA&8) &7OBSERWOWANIA AREN!"));
            player.sendMessage(colored("    &8&l* &fJezeli masz blad to wbij na dc.pvpcloud.pl"));
            for (Player players : Bukkit.getOnlinePlayers()) players.showPlayer(player);
            Bukkit.broadcastMessage("2 - leave");
        }, 20L);
        this.spectatorsArena.remove(player.getUniqueId());
        Bukkit.broadcastMessage("3 - leave");
    }

    @Override
    public List<UUID> getPlayers() { return new ArrayList<>(spectatorsArena); }

    @Override
    public boolean isPlaying(UUID uuid) { return this.spectatorsArena.contains(uuid); }

    @Override
    public WorldBorder setBorder(UUID uuid, Location center, int size) {
        WorldBorder border = this.worldBorders.get(uuid);
        if (border == null) {
            this.worldBorders.put(uuid, border = new WorldBorder());
        }
        border.setCenter(center.getX(), center.getZ());
        border.setSize(size);
        return border;
    }

}

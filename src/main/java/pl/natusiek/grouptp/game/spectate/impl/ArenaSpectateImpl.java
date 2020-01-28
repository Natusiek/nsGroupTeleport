package pl.natusiek.grouptp.game.spectate.impl;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import pl.natusiek.grouptp.GroupTeleportPlugin;
import pl.natusiek.grouptp.game.arena.Arena;
import pl.natusiek.grouptp.game.arena.ArenaManager;
import pl.natusiek.grouptp.game.spectate.ArenaSpectate;
import pl.natusiek.grouptp.helper.BorderHelper;
import pl.natusiek.grouptp.helper.ItemBuilder;
import pl.natusiek.grouptp.helper.LocationHelper;
import pl.natusiek.grouptp.helper.PlayerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class ArenaSpectateImpl implements ArenaSpectate {

    @Override
    public void joinSpectate(Player player, Arena arena) {
        player.setFlying(true);
        player.setAllowFlight(true);
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS).withName("&8* &eZnajdz pobliskiego gracza &8*").build());
        player.getInventory().setItem(4, new ItemBuilder(Material.NAME_TAG).withName("&8* &4Opusc obserwowanie &8*").addEnchantment(Enchantment.DURABILITY, 10).build());
        BorderHelper.setBorder(arena, player, arena.getCenter(), arena.getSize());
        Bukkit.getOnlinePlayers().forEach(players -> players.hidePlayer(player));
        player.teleport(arena.getCenter().toLocation());
        arena.addSpectators(player.getUniqueId());
    }

    @Override
    public void leaveSpectate(Player player) {
        player.setFlying(false);
        player.setAllowFlight(false);
        player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
        PlayerHelper.addItemFromLobby(player);
        BorderHelper.setBorder(player, player.getLocation(), 1000000);
        player.teleport(LocationHelper.fromString("world, 200.0, 80.0, 200.0, 0.0f, 0.0f").toLocation());

        Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(player));
    }
}

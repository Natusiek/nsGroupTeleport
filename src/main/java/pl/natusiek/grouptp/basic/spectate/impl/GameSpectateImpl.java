package pl.natusiek.grouptp.basic.spectate.impl;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import pl.natusiek.grouptp.basic.arena.GameArena;
import pl.natusiek.grouptp.basic.spectate.GameSpectate;
import pl.natusiek.grouptp.helper.BorderHelper;

import static pl.natusiek.grouptp.helper.MessageHelper.colored;

public class GameSpectateImpl implements GameSpectate {

    private GameArena gameArena;

    @Override
    public void joinSpectate(Player player, GameArena gameArena) {
        player.setAllowFlight(true);
        player.setFlying(true);
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        BorderHelper.setBorder(gameArena, player, gameArena.getCenter(), gameArena.getSize());
        Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(player));
        player.teleport(gameArena.getCenter().toLocation());
    }

    @Override
    public void leaveSpectate(Player player) {
        player.setFlying(false);
        player.setAllowFlight(false);
        player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
        BorderHelper.setBorder(player, player.getLocation(), 1000000);
        player.sendMessage(colored("&8(&4BETA&8) &7OBSERWOWANIA AREN!"));
        player.sendMessage(colored("    &8&l* &fJezeli masz blad to wbij na dc.pvpcloud.pl"));
        Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(player));
    }

    @Override
    public void addSpectetor(UUID uuid) { gameArena.getSpectetors().add(uuid); }

    @Override
    public void removeSpectetor(UUID uuid) { gameArena.getSpectetors().remove(uuid); }

    @Override
    public boolean isPlaying(UUID uuid) { return gameArena.getSpectetors().contains(uuid); }


}

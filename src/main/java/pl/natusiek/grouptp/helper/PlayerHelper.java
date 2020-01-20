package pl.natusiek.grouptp.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class PlayerHelper {

    public static List<Player> findPlayersInRadius(Player base, int radius, int limit) {
        final List<Player> players = new ArrayList<>(limit);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().distance(base.getLocation()) <= radius && !player.getUniqueId().equals(base.getUniqueId())) {
                players.add(player);
                if (players.size() >= limit) {
                    break;
                }
            }
        }
        return players;
    }

}
package pl.natusiek.grouptp.arena;

import net.minecraft.server.v1_8_R3.WorldBorder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface GameSpectate {

    void joinSpectate(Player player, GameArena gameArena);

    void leaveSpectate(Player player);

    List<Player> getPlayer();

    GameArena getGameArena(Player player);

    GameArena setGameArena(Player player, GameArena gameArena);

    WorldBorder getBorder(Player player);

    WorldBorder setBorder(Player player, Location center, int size);

    void hidePlayer(Player player);

    void showPlayer(Player player);

}
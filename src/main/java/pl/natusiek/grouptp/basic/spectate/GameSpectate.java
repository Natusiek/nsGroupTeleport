package pl.natusiek.grouptp.basic.spectate;

import net.minecraft.server.v1_8_R3.WorldBorder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.natusiek.grouptp.basic.arena.GameArena;

import java.util.List;
import java.util.UUID;

public interface GameSpectate {

    void joinSpectate(Player player, GameArena gameArena);

    void leaveSpectate(Player player);

    List<UUID> getPlayers();

    boolean isPlaying(UUID uuid);

    WorldBorder setBorder(UUID uuid, Location center, int size);

}
package pl.natusiek.grouptp.arena;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;

import net.minecraft.server.v1_8_R3.WorldBorder;

public interface GameArena {

  List<UUID> getPlayers();

  void addPlayer(UUID uuid);

  void removePlayer(UUID uuid);

  boolean isPlaying(UUID uuid);

  String getName();

  String getNames(List<GameArena> arenas);

  Location getCenter();

  int getSize();

  int getState();

  void setState(int state);

  void start();

  void restart();

  WorldBorder getBorder(UUID uuid);

  WorldBorder setBorder(UUID uuid, Location center, int size);

  final class ArenaStates {

    public static final int
            IN_GAME = 1,
            AVAILABLE = 2;
  }

}
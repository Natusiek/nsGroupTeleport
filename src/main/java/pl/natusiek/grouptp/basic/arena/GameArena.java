package pl.natusiek.grouptp.basic.arena;

import java.util.List;
import java.util.UUID;

import net.minecraft.server.v1_8_R3.WorldBorder;
import org.bukkit.Location;
import pl.natusiek.grouptp.helper.LocationHelper;


public interface GameArena {

  List<UUID> getPlayers();

  List<UUID> getSpectetors();

  void addPlayer(UUID uuid);

  void removePlayer(UUID uuid);

  boolean isPlaying(UUID uuid);

  String getName();

  LocationHelper getCenter();

  int getSize();

  int getState();

  void setState(int state);

  void start();

  void restart();

  WorldBorder getBorder(UUID uuid);

  WorldBorder setBorder(UUID uuid, LocationHelper center, int size);

  final class ArenaStates {

    public static final int
            IN_GAME = 1,
            AVAILABLE = 2;
  }

}